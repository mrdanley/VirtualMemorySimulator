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

			String instruction,virtualAddress,offset,physicalAddress;
      int data;
      //read in all contents of testfile
			while ((instruction = bufferedReader.readLine()) != null) {
        //take in next line as vpn
        virtualAddress = bufferedReader.readLine();
        String vpn = virtualAddress.substring(0,2);
        int tlbIndex = tlb.getTLBEntryIndex(vpn);
        if(instruction.equals("0")){
          do{
            physicalAddress = mmu.read(tlb,pt,vpn);
            if(physicalAddress.equals("")){//clock page replacement algorithm
              os.pageReplacement(pt,pm,vpn);
            }else{//pageframe returned, combine with offset and access ram
              offset = virtualAddress.substring(2,4);
              System.out.println("Address "+physicalAddress+offset);
              break;
            }
          }while(true);
        }else{//"1" encountered
        //CONTINUE HEREEEEE
          data = Integer.parseInt(bufferedReader.readLine());
          physicalAddress = mmu.write(tlb,pt,virtualAddress,data);
        }
			}
			fileReader.close();
    }catch (IOException e) {
		  e.printStackTrace();
		}
  }
}
