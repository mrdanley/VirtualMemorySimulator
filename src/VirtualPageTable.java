public class VirtualPageTable{
  private PageTableEntry[] vpt; //VirtualPage# as indices
  //table entry - V | R | D | PageFrame#

  public VirtualPageTable(){

  }
  public PageTableEntry getPTE(int index){

  }
  public VirtualPageTable(){
    PageTableEntries = new int[16];
  }
}

class PageTableEntry{
  private int validBit;
  private int refBit;
  private int dirtyBit;
  private String pageFrameNum;

  public PageTableEntry(){

  }
  public PageTableEntry(int v, int r, int d, String pfn){

  }
  public void setValid(int v){

  }
  public void setRef(int r){

  }
  public void setDirty(int d){

  }
  public void setPageFrame(String p){

  }
  public int getValid(){

  }
  public int getRef(){

  }
  public int getDirty(){

  }
  public String getPageFrame(){

  }
}
