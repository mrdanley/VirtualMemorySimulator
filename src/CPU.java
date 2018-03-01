//address width is 16 bits
//the page offset is 8 bits
import java.util.Scanner;


public class CPU{
  private MMU mmu;
  private OS os;
  private PhysicalMemory pm;
  private VirtualPageTable pt;

  public CPU(){
    //instantiate all private members
    mmu=new MMU();
    os=new OS();
    pm=new PhysicalMemory();
    pt=new VirtualPageTable();

  }
  //read instructions and pass them to mmu in correct format
  public void readInstructions(String filename){
    File input=new File(filename);
		Scanner sc=new Scanner(input);
    While(sc.hasNextLine()){
      String line=sc.nextLine();
      banana


    }


  }
}
