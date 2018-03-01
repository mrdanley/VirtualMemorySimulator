import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
//ex. ram[16][1024] for 16 pages of data, 1kB/page
//address width is 12 bits

public class PhysicalMemory{
  private int [][] ram;

  public PhysicalMemory(){
    ram = new int[16][256];
  }
  public PhysicalMemory(VirtualPageTable pt){
    ram = new int[16][256];
    //load first 16 page files
    //with first index as page names
    //and second index as integer lines within
    String vpn,pfn;
    for(int i=0;i<16;i++){
      vpn = ("0"+Integer.toHexString(i)).toUpperCase();
      pfn = vpn;
      pt.getPTEntries()[i] = new PTEntry(1,0,0,pfn);
      try {
        FileReader fileReader = new FileReader(new File("../page_files/"+vpn+".pg"));
        BufferedReader bufferedReader = new BufferedReader(fileReader);
  			StringBuffer stringBuffer = new StringBuffer();

  			String dataString;
        int pageIndex = 0;
        //read in all contents of testfile
  			while ((dataString = bufferedReader.readLine()) != null) {
          ram[i][pageIndex++] = Integer.parseInt(dataString);
  			}
  			fileReader.close();
      }catch (IOException e) {
  		  e.printStackTrace();
  		}
    }
  }
  public int[][] getRam(){
    return ram;
  }
}
