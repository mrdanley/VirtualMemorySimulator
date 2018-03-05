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
    copyOriginalDiskPages();
    System.out.println("Original disk pages copied to memory");
  }
  public void copyOriginalDiskPages(){
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
  //read rws and pass them to mmu in correct format
  public void readInstructions(String filename){
    StringBuffer csv = new StringBuffer("address,r/w,value,soft,hard,hit,evicted_pg#,dirty_evicted_page\n");
    String virtualAddress,offset,vpn,pfn;
    String physicalAddress,rw,value,soft="0",hard="0",hit="0",evictedPage,dirtyPage="0";
    int tlbIndex,ptIndex,ramIndex,offsetIndex,rwCounter = 0;
    // Scanner kb = new Scanner(System.in);
    try {
      FileReader fileReader = new FileReader(new File("../test_files/"+filename));
      BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
      //read in all contents of testfile
			while ((rw = bufferedReader.readLine()) != null) {
        System.out.println("rw "+(++rwCounter));
        //take in next line as vpn
        virtualAddress = bufferedReader.readLine();
        System.out.println("Virtual address "+virtualAddress);
        vpn = virtualAddress.substring(0,2);
        offset = virtualAddress.substring(2);

        soft="0";
        hard="0";
        hit="0";
        if(!dirtyPage.equals("1")) dirtyPage="0";//if hasnt been set to 1, default value is 0
        evictedPage = "None";
        if(rw.equals("0")){
          System.out.println("Read");
          //read from memory location
          do{
            // printTLBandPageTables();

            tlbIndex = mmu.checkTLB(tlb,vpn);
            if(tlbIndex != -1){//hit
              System.out.println("Hit");

              if(soft.equals("1") || hard.equals("1")){
                hit="0";
              }else{
                hit="1";
              }

              mmu.setRef(tlb,pt,tlbIndex);

              //get value from ram page
              pfn = tlb.getTLBEntry(tlbIndex).getPageFrame();
              ramIndex = Integer.parseInt(pfn,16);
              offsetIndex = Integer.parseInt(offset,16);
              value = Integer.toString(pm.getRamPage(ramIndex)[offsetIndex]);
              System.out.println("Value "+value);

              physicalAddress = pfn+offset;
              System.out.println("Physical address is "+physicalAddress);
              break;
            }else{//not hit
              ptIndex = mmu.checkPT(pt,vpn);

              if(tlb.isFull()){//fifo to get tlb index to replace
                tlbIndex = mmu.fifo();
              }else{
                tlbIndex = tlb.getStackPtr();
                tlb.incrStackPtr();
              }

              if(ptIndex != -1){//hit
                System.out.println("Soft miss");

                soft="1";
                hard="0";
                hit="0";

                mmu.setRef(pt,ptIndex);
                mmu.copyFromPTtoTLB(tlb,pt,tlbIndex,ptIndex);

                pfn = tlb.getTLBEntry(tlbIndex).getPageFrame();
                ramIndex = Integer.parseInt(pfn,16);
                //get value from ram page
                offsetIndex = Integer.parseInt(offset,16);
                value = Integer.toString(pm.getRamPage(ramIndex)[offsetIndex]);

                physicalAddress = pfn+offset;
                System.out.println("Physical address is "+physicalAddress);
                break;
              }else{//hard miss
                System.out.println("Hard miss");

                soft="0";
                hard="1";
                hit="0";

                ramIndex = os.clockAlgorithm(pt);
                if(mmu.checkDirty(pt,ramIndex)){
                  os.writeRamPageToDisk(pm,pt,ramIndex);
                  dirtyPage = "1";
                }
                evictedPage = mmu.pageTableEntryClear(pt,ramIndex);
                System.out.println("Evicted page "+evictedPage);

                os.writeDiskPageToRam(pm,ramIndex,vpn);
                mmu.updateTLBandPT(pt,ramIndex,tlb,tlbIndex,vpn);
              }
            }
          }while(true);
        }else{//"1" encountered
          System.out.println("Write");
          //get value write from file
          value = bufferedReader.readLine();
          System.out.println("Value "+value);

          //write to memory location
          do{
            // printTLBandPageTables();

            tlbIndex = mmu.checkTLB(tlb,vpn);
            if(tlbIndex != -1){//hit
              System.out.println("Hit");

              if(soft.equals("1") || hard.equals("1")){
                hit="0";
              }else{
                hit="1";
              }

              mmu.setRef(tlb,pt,tlbIndex);

              //OS write data to ram[PT[vpn]][offset]
              pfn = tlb.getTLBEntry(tlbIndex).getPageFrame();
              ramIndex = Integer.parseInt(pfn,16);
              //write specific code
              os.writeToRamPage(pm,ramIndex,value,offset);
              mmu.setDirty(tlb,pt,tlbIndex);

              physicalAddress = pfn+offset;
              System.out.println("Physical address is "+physicalAddress);
              break;
            }else{//not hit
              ptIndex = mmu.checkPT(pt,vpn);

              if(tlb.isFull()){//fifo to get tlb index to replace
                tlbIndex = mmu.fifo();
              }else{
                tlbIndex = tlb.getStackPtr();
                tlb.incrStackPtr();
              }

              if(ptIndex != -1){//hit
                System.out.println("Soft miss");

                soft="1";
                hard="0";
                hit="0";

                mmu.setRef(pt,ptIndex);
                mmu.copyFromPTtoTLB(tlb,pt,tlbIndex,ptIndex);

                pfn = tlb.getTLBEntry(tlbIndex).getPageFrame();
                ramIndex = Integer.parseInt(pfn,16);
                //write specific code
                os.writeToRamPage(pm,ramIndex,value,offset);
                mmu.setDirty(tlb,pt,tlbIndex);

                physicalAddress = pfn+offset;
                System.out.println("Physical address is "+physicalAddress);
                break;
              }else{//hard miss
                System.out.println("Hard miss");

                soft="0";
                hard="1";
                hit="0";

                ramIndex = os.clockAlgorithm(pt);
                if(mmu.checkDirty(pt,ramIndex)){
                  os.writeRamPageToDisk(pm,pt,ramIndex);
                  dirtyPage = "1";
                }
                evictedPage = mmu.pageTableEntryClear(pt,ramIndex);
                System.out.println("Evicted page "+evictedPage);

                os.writeDiskPageToRam(pm,ramIndex,vpn);
                mmu.updateTLBandPT(pt,ramIndex,tlb,tlbIndex,vpn);
              }
            }
          }while(true);
        }
        if(rwCounter % 5 == 0){
          // System.out.println("Reset R bits");
          // System.out.println("Before reset:");
          // printTLBandPageTables();
          os.resetRefBit(tlb,pt);
          // System.out.println("After reset:");
          // printTLBandPageTables();
        }
        // kb.next();
        csv.append(physicalAddress+","+rw+","+value+","+soft+","+hard+","+
                  hit+","+evictedPage+","+dirtyPage+System.lineSeparator());
			}
			fileReader.close();
    }catch (IOException e) {
		  e.printStackTrace();
		}
    copyOrigPagesBack();
    System.out.println("Original disk pages copied back");

    // System.out.print(csv.toString());

    //create csv file
    try {

      String writeFilename = filename.substring(0,filename.indexOf(".txt"))+"_results";
      File f = new File("../test_results/"+writeFilename+".csv");
      f.getParentFile().mkdirs();
      f.createNewFile();
      FileWriter fileWriter = new FileWriter(f);
      fileWriter.write(csv.toString());
      fileWriter.close();
      System.out.println("'"+writeFilename+".csv' written to 'test_results' directory in main directory");
    }catch (IOException e) {
      e.printStackTrace();
    }
  }
  public void copyOrigPagesBack(){
    //rewrite original disk pages
    String filename;
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
  public void printTLBandPageTables(){
    System.out.println("TLB");
    tlb.printTable();
    System.out.println("Page Table");
    pt.printTable();
  }
}
