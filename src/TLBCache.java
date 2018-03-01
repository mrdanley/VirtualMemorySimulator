public class TLBCache{
  private TLBEntry[] TlbEntries;

  public TLBCache(){
    TlbEntries = new TLBEntry[8];
  }
}

class TLBEntry{
  private String virtualPageNum;
  private int validBit;
  private int refBit;
  private int dirtyBit;
  private String pageFrameNum;
  
  public TLBEntry(){
    virtualPageNum = "";
    validBit = 0;
    refBit = 0;
    dirtyBit = 0;
    pageFrameNum = "";
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
