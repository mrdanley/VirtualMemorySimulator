//resets the r-bit every 20 instructions
//uses the clock algorithm for page replacement (must implement as a circular linked list)
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class OS{
  private CircularLinkedList ptIndices;

  public OS(){
  }
  public OS(PhysicalMemory pm,VirtualPageTable pt){
    ptIndices = new CircularLinkedList();//initialize linked list
    String vpn,pfn;
    for(int i=0;i<16;i++){
      pfn = Integer.toHexString(i).toUpperCase();
      if(pfn.length()==1) pfn = "0"+pfn;
      pt.setPTEntry(i,1,0,0,pfn);
      //add 0 because hex 0-15 is 0-F, need 4 more bits for 8 bit vpn
      vpn = pfn;
      loadPage(i,vpn,pm);
      //add vpn index to clock
      ptIndices.add(i);
    }
  }
  public int clockAlgorithm(VirtualPageTable pt){
    //choose page to replace using clock algorithm
    boolean notFound = true;
    int currentIndex;
    Node currentNode = ptIndices.getHead();
    do{
      currentIndex = currentNode.getData();
      if(pt.getPTEntry(currentIndex).getRef()==1){
        pt.getPTEntry(currentIndex).setRef(0);
      }else{
        int clockNode = ptIndices.remove();
        ptIndices.add(clockNode);
        return clockNode;
      }
      currentNode = currentNode.getNext();
    }while(true);
  }
  public void writeRamPageToDisk(PhysicalMemory pm,VirtualPageTable pt,int ramIndex){
    String pfn = Integer.toHexString(ramIndex).toUpperCase();
    if(pfn.length()==1) pfn="0"+pfn;
    int ptIndex = pt.getPTEntryIndex(pfn);
    String vpn = Integer.toHexString(ptIndex).toUpperCase();
    if(vpn.length()==1) vpn="0"+vpn;
    try {
      FileWriter fileWriter = new FileWriter(new File("../page_files/"+vpn+".pg"),false);
      BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
      StringBuffer dataString = new StringBuffer("");
      for(int i=0;i<pm.getRamPage(ramIndex).length;i++){
        dataString.append(pm.getRamPage(ramIndex)[i]+"\n");
      }
      bufferedWriter.write(dataString.toString());
      bufferedWriter.close();
      fileWriter.close();
    }catch (IOException e) {
      e.printStackTrace();
    }
  }
  public void writeDiskPageToRam(PhysicalMemory pm,int ramIndex,String vpn){
    try {
      FileReader fileReader = new FileReader(new File("../page_files/"+vpn+".pg"));
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      StringBuffer stringBuffer = new StringBuffer();

      String dataString;
      int pageLineIndex = 0;
      //read in all contents of testfile
      while ((dataString = bufferedReader.readLine()) != null) {
        pm.getRamPage(ramIndex)[pageLineIndex++] = Integer.parseInt(dataString);
      }
      bufferedReader.close();
      fileReader.close();
    }catch (IOException e) {
      e.printStackTrace();
    }
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
        pm.getRamPage(ramIndex)[pageLineIndex++] = Integer.parseInt(dataString);
      }
      bufferedReader.close();
      fileReader.close();
    }catch (IOException e) {
      e.printStackTrace();
    }
  }
  public void writeToRamPage(PhysicalMemory pm,int ramIndex,String dataString,String offset){
    pm.getRamPage(ramIndex)[Integer.parseInt(offset,16)] = Integer.parseInt(dataString);
  }
  public void resetRefBit(TLBCache tlb,VirtualPageTable pt){
    for(int i=0;i<tlb.getStackPtr();i++){
      tlb.getTLBEntry(i).setRef(0);
    }
    System.out.println("Reset with "+ptIndices.getCounter());
    Node ptIndexCurrent = ptIndices.getHead();
    do{
      pt.getPTEntry(ptIndexCurrent.getData()).setRef(0);
      ptIndexCurrent = ptIndexCurrent.getNext();
    }while(ptIndexCurrent != ptIndices.getHead());
  }
}
