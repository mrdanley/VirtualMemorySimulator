//resets the r-bit every 20 instructions
//uses the clock algorithm for page replacement (must implement as a circular linked list)
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class OS{
  private CircularLinkedList ptIndices;

  public OS(){
  }
  public OS(PhysicalMemory pm,VirtualPageTable pt){
    clock = new LinkedList();//initialize linked list
    String vpn,pfn;
    for(int i=0;i<16;i++){
      pfn = (Integer.toHexString(i)).toUpperCase();
      pt.getPTEntries(i) = new PTEntry(1,0,0,pfn);
      //add 0 because hex 0-15 is 0-F, need 4 more bits for 8 bit vpn
      vpn = "0" + pfn;
      loadPage(i,vpn,pm);
      //add vpn index to clock
      ptIndices.add(i);
    }
  }
  public int clockAlgorithm(VirtualPageTable pt){
    //choose page to replace using clock algorithm
    boolean notFound = true;
    int currentIndex;
    do{
      currentIndex = ptIndices.getNext();
      if(pt.getPTEntry(currentIndex).getRef()==1){
        pt.getPTEntry(currentIndex).setRef(0);
      }else{
        return ptIndices.remove();
      }
    }while(true);
  }
  public void writeRamPageToDisk(PhysicalMemory pm,int ramIndex){
    String diskPageName = Integer.toHexString(ramIndex);
    if(ramWriteOut.length()==1) ramWriteOut = "0"+ ramWriteOut;
    try {
      FileWriter fileWriter = new FileWriter(new File("../page_files/"+diskPageName+".pg"),false);
      BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
      StringBuffer dataString = new StringBuffer("");
      for(int i=0;i<pm.getRam(ramIndex).length;i++){
        dataString.append(pm.getRam(ramIndex)[i]+"\n");
      }
      bufferedWriter.write(dataString.toString());
      bufferedWriter.close()
      fileWriter.close();
    }catch (IOException e) {
      e.printStackTrace();
    }
  }
  public void writeDiskPageToRam(PhysicalMemory pm,VirtualPageTable pt,int ramIndex,String vpn){
    try {
      FileReader fileReader = new FileReader(new File("../page_files/"+vpn+".pg"));
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      StringBuffer stringBuffer = new StringBuffer();

      String dataString;
      int pageLineIndex = 0;
      //read in all contents of testfile
      while ((dataString = bufferedReader.readLine()) != null) {
        pm.getRam(ramIndex)[pageLineIndex++] = Integer.parseInt(dataString);
      }
      bufferedReader.close();
      fileReader.close();
    }catch (IOException e) {
      e.printStackTrace();
    }
    String pageFrame = Integer.toHexString(ramIndex);
    if(pageFrame.length()==1) pageFrame = "0"+pageFrame;
    int ptIndex = Integer.parseInt(vpn,16);
    pt.getPTEntry(ptIndex).setPageFrame(pageFrame);
    pt.getPTEntry(ptIndex).setDirty(0);
  }
  private void loadPage(int ramIndex,String vpn,PhysicalMemory pm){
    try {
      FileReader fileReader = new FileReader(new File("../page_files/"+vpn+".pg"));
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      StringBuffer stringBuffer = new StringBuffer();

      String dataString;
      int pageLineIndex = 0;
      //read in all contents of testfile
      while ((dataString = bufferedReader.readLine()) != null) {
        pm.getRam(ramIndex)[pageLineIndex++] = Integer.parseInt(dataString);
      }
      bufferedReader.close();
      fileReader.close();
    }catch (IOException e) {
      e.printStackTrace();
    }
  }
  public void writeToRamPage(PhysicalMemory pm,int ramIndex,String dataString,String offset){
    pm.getRam(ramIndex)[offset] = Integer.parseInt(dataString);
  }
  public void resetRefBit(TLBCache tlb,VirtualPageTable pt){
    for(int i=0;i<tlb.getStackPtr();i++){
      tlb.getTLBEntry(i).setRef(0);
    }
    for(int i=0;i<ptIndices.getCounter();i++){
      pt.getPTEntry(ptIndices.getData()).setRef(0);
      ptIndices = ptIndices.getNext();
    }
  }
}
