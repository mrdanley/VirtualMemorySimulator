public class VirtualPageTable{
  private PTEntry[] ptEntries;
  private int stackPtr;

  public VirtualPageTable(){
    ptEntries = new PTEntry[256];
    for(int i=0;i<ptEntries.length;i++){
      ptEntries[i] = new PTEntry();
    }
    stackPtr = 0;
  }
  public void printTable(){
    for(int i=0;i<ptEntries.length;i++){
      if(ptEntries[i].getValid()==1){
        String vpn = Integer.toHexString(i).toUpperCase();
        if(vpn.length()==1) vpn="0"+vpn;
        System.out.print("Index "+vpn+" ");
        ptEntries[i].printEntry();
      }
    }
  }
  public PTEntry getPTEntry(int index){
    return ptEntries[index];
  }
  public int getPTEntryIndex(String pfn){
    for(int i=0;i<ptEntries.length;i++){
      if(ptEntries[i].getValid()==1 && ptEntries[i].getPageFrame().equals(pfn)){
        return i;
      }
    }
    return -1;
  }
  public void setPTEntry(int index,int valid,int ref,int dirty,String pfn){
    ptEntries[index] = new PTEntry(valid,ref,dirty,pfn);
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
  public void printEntry(){
    System.out.println(validBit+" "+refBit+" "+dirtyBit+" "+pageFrameNum);
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
