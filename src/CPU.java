//address width is 16 bits
//the page offset is 8 bits
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class CPU{
  private MMU mmu;
  private OS os;
  private PhysicalMemory pm;
  private VirtualPageTable pt;
  private TLBCache tlb;
  private int[][] originalDiskPages;

  public CPU(){
    //instantiate all private members
    mmu = new MMU();
    tlb = new TLBCache();
    pt = new VirtualPageTable();
    pm = new PhysicalMemory();
    os = new OS(pm,pt);

    //copy page
    originalDiskPages = new int[256][256];
    String filename;
    for(int page=0;page<256;page++){
      filename = Integer.toHexString(page).toUpperCase();
      if(filename.length()==1) filename="0"+filename;
      int pageIndex=0;
      try {
        FileReader fileReader = new FileReader(new File("../page_files/"+filename+".pg"));
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuffer stringBuffer = new StringBuffer();

        String dataString;
        int pageLineIndex = 0;
        //read in all contents of testfile
        while ((dataString = bufferedReader.readLine()) != null) {
          originalDiskPages[page][pageIndex++] = Integer.parseInt(dataString);
        }

        bufferedReader.close();
        fileReader.close();
      }catch(IOException e){
        e.printStackTrace();
      }
    }
  }
  //read instructions and pass them to mmu in correct format
  public void readInstructions(String filename){
    Scanner kb = new Scanner(System.in);
    try {
      FileReader fileReader = new FileReader(new File(filename));
      BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();

			String instruction,virtualAddress,dataString,offset,vpn,pfn;
      int rBitCounter = 0,tlbIndex,ptIndex,ramIndex, instructionCounter = 0;
      //read in all contents of testfile
			while ((instruction = bufferedReader.readLine()) != null) {
        System.out.println("Instruction "+(++instructionCounter));
        //take in next line as vpn
        virtualAddress = bufferedReader.readLine();
        System.out.println("Virtual address "+virtualAddress);
        vpn = virtualAddress.substring(0,2);
        offset = virtualAddress.substring(2);
        // tlbIndex = tlb.getTLBEntryIndex(vpn);
        if(instruction.equals("0")){
          System.out.println("Read");
          //read from memory location
          do{
            System.out.println("TLB");
            tlb.printTable();
            System.out.println("Page Table");
            pt.printTable();

            tlbIndex = mmu.checkTLB(tlb,vpn);
            if(tlbIndex != -1){//hit
              System.out.println("Hit");
              mmu.setRef(tlb,pt,tlbIndex);
              System.out.println("Physical address is "+tlb.getTLBEntry(tlbIndex).getPageFrame()+offset);
              break;
            }else{//soft miss
              System.out.println("Soft miss");
              ptIndex = mmu.checkPT(pt,vpn);

              if(tlb.isFull()){//fifo to get tlb index to replace
                tlbIndex = mmu.fifo();
              }else{
                tlbIndex = tlb.getStackPtr();
                tlb.incrStackPtr();
              }

              if(ptIndex != -1){//hit
                System.out.println("Hit");
                mmu.setRef(pt,ptIndex);
                mmu.copyFromPTtoTLB(tlb,pt,tlbIndex,ptIndex);
                System.out.println("Physical address is "+tlb.getTLBEntry(tlbIndex).getPageFrame()+offset);
                break;
              }else{//hard miss
                System.out.println("Hard miss");
                ramIndex = os.clockAlgorithm(pt);
                if(mmu.checkDirty(pt,ramIndex)){
                  os.writeRamPageToDisk(pm,pt,ramIndex);
                  System.out.println("Dirty Page to be evicted");
                }
                String evictedPage = mmu.pageTableEntryClear(pt,ramIndex);
                System.out.println("Evicted page "+evictedPage);

                os.writeDiskPageToRam(pm,ramIndex,vpn);
                mmu.updateTLBandPT(pt,ramIndex,tlb,tlbIndex,vpn);
              }
            }
          }while(true);
        }else{//"1" encountered
          System.out.println("Write");
          //get dataString from file
          dataString = bufferedReader.readLine();
          System.out.println("Value "+dataString);
          //write to memory location
          do{
            System.out.println("TLB");
            tlb.printTable();
            System.out.println("Page Table");
            pt.printTable();

            tlbIndex = mmu.checkTLB(tlb,vpn);
            if(tlbIndex != -1){//hit
              System.out.println("Hit");
              mmu.setRef(tlb,pt,tlbIndex);

              //write specific code
              //OS write data to ram[PT[vpn]][offset]
              pfn = tlb.getTLBEntry(tlbIndex).getPageFrame();
              ramIndex = Integer.parseInt(pfn,16);
              os.writeToRamPage(pm,ramIndex,dataString,offset);
              //set dirty bit
              mmu.setDirty(tlb,pt,tlbIndex);

              System.out.println("Physical address is "+tlb.getTLBEntry(tlbIndex).getPageFrame()+offset);
              break;
            }else{//soft miss
              System.out.println("Soft miss");
              ptIndex = mmu.checkPT(pt,vpn);

              if(tlb.isFull()){//fifo to get tlb index to replace
                tlbIndex = mmu.fifo();
              }else{
                tlbIndex = tlb.getStackPtr();
                tlb.incrStackPtr();
              }

              if(ptIndex != -1){//hit
                System.out.println("Hit");
                mmu.setRef(pt,ptIndex);
                mmu.copyFromPTtoTLB(tlb,pt,tlbIndex,ptIndex);

                //write specific code
                pfn = tlb.getTLBEntry(tlbIndex).getPageFrame();
                ramIndex = Integer.parseInt(pfn,16);
                os.writeToRamPage(pm,ramIndex,dataString,offset);
                //set dirty bit
                mmu.setDirty(tlb,pt,tlbIndex);

                System.out.println("Physical address is "+tlb.getTLBEntry(tlbIndex).getPageFrame()+offset);
                break;
              }else{//hard miss
                System.out.println("Hard miss");
                ramIndex = os.clockAlgorithm(pt);
                if(mmu.checkDirty(pt,ramIndex)){
                  os.writeRamPageToDisk(pm,pt,ramIndex);
                  System.out.println("Dirty Page to be evicted");
                }
                String evictedPage = mmu.pageTableEntryClear(pt,ramIndex);
                System.out.println("Evicted page "+evictedPage);

                os.writeDiskPageToRam(pm,ramIndex,vpn);
                mmu.updateTLBandPT(pt,ramIndex,tlb,tlbIndex,vpn);
              }
            }
          }while(true);
        }

        rBitCounter++;
        if(rBitCounter == 5){
          System.out.println("Reset R bits");
          os.resetRefBit(tlb,pt);
          rBitCounter = 0;
        }
			}
			fileReader.close();
    }catch (IOException e) {
		  e.printStackTrace();
		}

    //rewrite original disk pages
    for(int page=0;page<256;page++){
      filename = Integer.toHexString(page).toUpperCase();
      if(filename.length()==1) filename="0"+filename;
      try {
        FileWriter fileWriter = new FileWriter(new File("../page_files/"+filename+".pg"),false);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        StringBuffer dataString = new StringBuffer("");

        //read in all contents of testfile
        for(int pageIndex=0;pageIndex<256;pageIndex++){
          dataString.append(originalDiskPages[page][pageIndex]+"\n");
        }
        bufferedWriter.write(dataString.toString());

        bufferedWriter.close();
        fileWriter.close();
      }catch(IOException e){
        e.printStackTrace();
      }
    }

  }
}
