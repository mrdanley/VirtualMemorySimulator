//resets the r-bit every 20 instructions
//uses the clock algorithm for page replacement (must implement as a circular linked list)
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class OS{
  public OS(){
  }
  public OS(PhysicalMemory pm,VirtualPageTable pt){
    String vpn,pfn;
    for(int i=0;i<16;i++){
      pfn = (Integer.toHexString(i)).toUpperCase();
      vpn = "0" + pfn;
      pt.getPTEntries()[i] = new PTEntry(1,0,0,pfn);
      loadPage(i,vpn,pm);
    }
    clockPtr = 0;
  }
  private void pageReplacement(VirtualPageTable pt,PhysicalMemory pm,String diskReadVPN){
    //write dirty page to disk
    String ramWriteOutVPN = "0"+Integer.toHexString(pm.getClockPtr());
    try {
      FileWriter fileWriter = new FileWriter(new File("../page_files/"+ramWriteOutVPN+".pg"),false);
      BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
      StringBuffer dataString = new StringBuffer("");
      for(int i=0;i<pm.getRam()[pm.getClockPtr()].length;i++){
        dataString.append(pm.getRam()[pm.getClockPtr()][i]+"\n");
      }
      bufferedWriter.write(dataString.toString());
      bufferedWriter.close()
      fileWriter.close();
    }catch (IOException e) {
      e.printStackTrace();
    }
    //load new page to ram
    loadPage(pm.getClockPtr(),diskReadVPN,pm);
    pt.getPTEntries()[Integer.parseInt(diskReadVPN,16)].setValid(1);
    pt.getPTEntries()[Integer.parseInt(diskReadVPN,16)].setRef(0);
    pt.getPTEntries()[Integer.parseInt(diskReadVPN,16)].setDirty(0);
    pt.getPTEntries()[Integer.parseInt(diskReadVPN,16)].setPageFrame(Integer.toHexString(pm.getClockPtr()));

    //move pointer for ram
    int newClockPtr;
    if(pm.getClockPtr()==15){
      newClockPtr = 0;
    }else{
      newClockPtr = pm.getClockPtr() + 1;
    }
    pt.setClockPtr(newClockPtr);
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
        pm.getRam()[ramIndex][pageLineIndex++] = Integer.parseInt(dataString);
      }
      bufferedReader.close();
      fileReader.close();
    }catch (IOException e) {
      e.printStackTrace();
    }
  }
}
