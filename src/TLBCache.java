public class TLBCache{
  private final int TLB_SIZE = 8;
  private TLBEntry[] tlb;

  public TLBCache(){
    tlb = new TLBEntry[TLB_SIZE];
  }
  public TLBEntry getTLBEntry(int index){
    return tlb[index];
  }
}

class TLBEntry{
  private String virtualPageNum;
  private int validBit;
  private int refBit;
  private int dirtyBit;
  private String pageFrameNum;

  public TLBEntry(){

  }
  public TLBEntry(String vpn, int v, int r, int d, String pfn){
    virtualPageNum = vpn;
    validBit = v;
    refBit = r;
    dirtyBit = d;
    pageFrameNum = pfn;
  }
  public void setVirtualPage(String v){
    virtualPageNum = v;
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
  public String getVirtualPage(){
    return virtualPageNum;
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
