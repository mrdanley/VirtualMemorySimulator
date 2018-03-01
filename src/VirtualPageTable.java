public class VirtualPageTable{
  private PageTableEntry[] vpt; //VirtualPage# as indices
  //table entry - V | R | D | PageFrame#

  public VirtualPageTable(){
    vpt = new PageTableEntry[16];
  }
  public PageTableEntry getPTE(int index){
    return vpt[index];
  }
  
}

class PageTableEntry{
  private int validBit;
  private int refBit;
  private int dirtyBit;
  private String pageFrameNum;

  public PageTableEntry(){
    this(0,0,0,"");
  }
  public PageTableEntry(int v, int r, int d, String pfn){
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
