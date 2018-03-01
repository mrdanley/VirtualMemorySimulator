public class VirtualPageTable{
  private PTEntry[] ptEntries;
  private int stackPtr, clockPtr;

  public VirtualPageTable(){
    ptEntries = new PTEntry[64];
    stackPtr = 0;
    clockPtr = 0;
  }
  public PTEntry[] getPTEntries(){
    return ptEntries;
  }
  public PTEntry getPTEntry(int index){
    if(ptEntries[index].getValid()==1)
      return ptEntries[index];
    else{
      return null;
    }
  }
  public int getClockPtr(){
    return clockPtr;
  }
  public void setClockPtr(int ptr){
    clockPtr = ptr;
  }
}

class PTEntry{
  private int validBit;
  private int refBit;
  private int dirtyBit;
  private String pageFrameNum;

  public PTEntry(){
    this(0,0,0,"");
  }
  public PTEntry(int v, int r, int d, String pfn){
    validBit = v;
    refBit = r;
    dirtyBit = d;
    pageFrameNum = pfn;
  }
  public void setValid(int v){
    validBit = v;
  }
  public void setRef(int r){
    refBit = r;
  }
  public void setDirty(int d){
    dirtyBit = d;
  }
  public void setPageFrame(String p){
    pageFrameNum = p;
  }
  public int getValid(){
    return validBit;
  }
  public int getRef(){
    return refBit;
  }
  public int getDirty(){
    return dirtyBit;
  }
  public String getPageFrame(){
    return pageFrameNum;
  }
}
