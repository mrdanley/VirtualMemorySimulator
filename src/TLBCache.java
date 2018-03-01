public class TLBCache{
  private TLBEntry[] tlb;

  public TLBCache(){

  }
  public TLBEntry getTLBEntry(int index){

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

  }
  public void setVirtualPage(String v){

  }
  public void setValid(int v){

  }
  public void setRef(int r){

  }
  public void setDirty(int d){

  }
  public void setPageFrame(String p){

  }
  public String getVirtualPage(){

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
