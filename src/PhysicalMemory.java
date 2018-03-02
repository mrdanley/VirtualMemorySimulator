//ex. ram[16][1024] for 16 pages of data, 1kB/page
//address width is 12 bits

public class PhysicalMemory{
  private int [][] ram;
  private int clockPtr;

  public PhysicalMemory(){
    ram = new int[16][256];
    clockPtr = 0;
  }
  public int[][] getRam(){
    return ram;
  }
  public int getClockPtr(){
    return clockPtr;
  }
  public void setClockPtr(int index){
    clockPtr = index;
  }
}
