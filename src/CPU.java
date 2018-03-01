//address width is 16 bits
//the page offset is 8 bits

public class CPU{
  private MMU mmu;
  private OS os;
  private PhysicalMemory pm;
  private VirtualPageTable pt;

  public CPU(){
    //instantiate all private members

  }
  //read instructions and pass them to mmu in correct format
  public void readInstructions(String filename){
    
  }
}
