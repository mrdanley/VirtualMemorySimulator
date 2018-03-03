//ex. ram[16][1024] for 16 pages of data, 1kB/page
//address width is 12 bits

public class PhysicalMemory{
  private int [][] ram;

  public PhysicalMemory(){
    ram = new int[16][256];
  }
  public int[] getRamPage(int index){
    return ram[index];
  }
}
