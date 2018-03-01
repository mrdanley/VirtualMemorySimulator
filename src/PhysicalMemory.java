//ram[#ofPageFiles][pageSize]
//ex. ram[16][1024] for 16 pages of data, 1kB/page
//address width is 12 bits

public class PhysicalMemory{
  private String [][]  ram;

  public PhysicalMemory(){
    ram = new String[64][];
    //load 64 page files
    //with first index as page names
    //and second index as integer lines within
    
  }
}
