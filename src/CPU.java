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
    mmu=new MMU();
    os=new OS();
    pt=new VirtualPageTable();
    pm=new PhysicalMemory(pt);
    tlb=new TLBCache();
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
        if(instruction.equals("0")){
          physicalAddress = mmu.read(tlb,pt,virtualAddress);
          if(physicalAddress.equals("")){//clock page replacement algorithm
            os.pageReplacement(pm)
          }else{//pageframe returned, combine with offset and access ram
            offset = virtualAddress.substring(2,4);
            System.out.println("Address "+physicalAddress+offset);
          }
        }else{//"1" encountered
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
