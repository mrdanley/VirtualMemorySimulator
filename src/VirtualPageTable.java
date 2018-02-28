public class VirtualPageTable{
  private int[] PageTableEntries; //VirtualPage# as indices
  //table entry - V | R | D | PageFrame#
  
  public VirtualPageTable(){
    PageTableEntries = new int[256];
  }
}
