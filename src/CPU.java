//address width is 16 bits
//the page offset is 8 bits
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CPU{
  private MMU mmu;
  private OS os;
  private PhysicalMemory pm;
  private VirtualPageTable pt;
  private TLBCache tlb;

  public CPU(){
    //instantiate all private members
    mmu = new MMU();
    tlb = new TLBCache();
    pt = new VirtualPageTable();
    pm = new PhysicalMemory();
    os = new OS(pm,pt);
  }
  //read instructions and pass them to mmu in correct format
  public void readInstructions(String filename){
    try {
      FileReader fileReader = new FileReader(new File(filename));
      BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();

			String instruction,virtualAddress,dataString,offset,vpn,pfn;
      int rBitCounter = 0,tlbIndex,ptIndex,ramIndex;
      //read in all contents of testfile
			while ((instruction = bufferedReader.readLine()) != null) {
        //take in next line as vpn
        virtualAddress = bufferedReader.readLine();
        vpn = virtualAddress.substring(0,2);
        offset = virtualAddress.substring(2);
        // tlbIndex = tlb.getTLBEntryIndex(vpn);
        if(instruction.equals("0")){
          //read from memory location
          do{
            tlbIndex = mmu.checkTLB(tlb,vpn);
            if(tlbIndex != -1){//hit
              mmu.setRef(tlb,pt,tlbIndex);
              System.out.println("address is "+tlb.getTLBEntry(tlbIndex).getPageFrame()+offset);
              break;
            }else{//soft miss
              ptIndex = mmu.checkPT(pt,vpn);
              if(ptIndex != -1){//hit
                mmu.setRef(pt,ptIndex);
                if(tlb.isFull()){//fifo to get tlb index to replace
                  tlbIndex = mmu.fifo();
                }else{
                  tlbIndex = tlb.getStackPtr();
                  tlb.incrStackPtr();
                }
                mmu.copyFromPTtoTLB(tlb,pt,tlbIndex,ptIndex);
                System.out.println("address is "+tlb.getTLBEntry(tlbIndex).getPageFrame()+offset);
                break;
              }else{//hard miss
                ramIndex = os.clockAlgorithm(pt);
                if(mmu.checkDirty(pt,ramIndex)){
                  os.writeRamPageToDisk(pm,ramIndex,vpn);
                }
                //set PT[index] to null
                os.writeDiskPageToRam(pm,pt,ramIndex,vpn);
              }
            }
          }while(true);
        }else{//"1" encountered
          //get dataString from file
          dataString = bufferedReader.readLine();
          //write to memory location
          do{
            tlbIndex = mmu.checkTLB(tlb,vpn);
            if(tlbIndex != -1){//hit
              mmu.setRef(tlb,pt,tlbIndex);
              //OS write data to ram[PT[vpn]][offset]
              pfn = tlb.getTLBEntry(Integer.parseInt(vpn,16)).getPageFrame();
              ramIndex = Integer.parseInt(pfn,16);
              os.writeToRamPage(pm,ramIndex,dataString,offset);
              //set dirty bit
              mmu.setDirty(tlb,pt,tlbIndex);
              System.out.println("address is "+tlb.getTLBEntry(tlbIndex).getPageFrame()+offset);
              break;
            }else{//soft miss
              ptIndex = mmu.checkPT(pt,vpn);
              if(ptIndex != -1){//hit
                mmu.setRef(pt,ptIndex);
                if(tlb.isFull()){//fifo to get tlb index to replace
                  tlbIndex = mmu.fifo();
                }else{
                  tlbIndex = tlb.getStackPtr();
                  tlb.incrStackPtr();
                }
                mmu.copyFromPTtoTLB(tlb,pt,tlbIndex,ptIndex);
                pfn = tlb.getTLBEntry(tlbIndex).getPageFrame();
                ramIndex = Integer.parseInt(pfn,16);
                os.writeToRamPage(pm,ramIndex,dataString,offset);
                //set dirty bit
                mmu.setDirty(tlb,pt,tlbIndex);
                System.out.println("address is "+tlb.getTLBEntry(tlbIndex).getPageFrame()+offset);
                break;
              }else{//hard miss
                ramIndex = os.clockAlgorithm(pt);
                if(mmu.checkDirty(pt,ramIndex)){
                  os.writeRamPageToDisk(pm,ramIndex,vpn);
                }
                //set PT[index] to null
                os.writeDiskPageToRam(pm,pt,ramIndex,vpn);
              }
            }
          }while(true);
        }

        rBitCounter++;
        if(rBitCounter == 20){
          os.resetRefBit(tlb,pt);
          rBitCounter = 0;
        }
			}
			fileReader.close();
    }catch (IOException e) {
		  e.printStackTrace();
		}
  }
}
