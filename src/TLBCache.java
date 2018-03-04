import java.util.Map;
import java.util.HashMap;

public class TLBCache{
  private final int TLB_SIZE = 8;
  private TLBEntry[] tlbEntries;
  private int stackPtr;//keeps track of filled area

  public TLBCache(){
    tlbEntries = new TLBEntry[TLB_SIZE];
    for(int i=0;i<tlbEntries.length;i++){
      tlbEntries[i] = new TLBEntry();
    }
    stackPtr = 0;
  }
  public void printTable(){
    for(int i=0;i<TLB_SIZE;i++){
      System.out.print("Index "+i+" ");
      tlbEntries[i].printEntry();
    }
  }
  public int getTLBEntryIndex(String vpn){
    for(int i=0;i<stackPtr;i++){
      //check valid bit
      if(tlbEntries[i].getValid()!=0){
        //check virtual page #
        if(tlbEntries[i].getVirtualPage().equals(vpn)){
          return i;
        }
      }
    }
    return -1;
  }
  public TLBEntry getTLBEntry(int index){
    if(tlbEntries[index].getValid()==1)
      return tlbEntries[index];
    else{
      return null;
    }

  }
  public void setTLBEntry(int index,String vpn,int valid,int ref,int dirty,String pfn){
    tlbEntries[index] = new TLBEntry(vpn,valid,ref,dirty,pfn);
  }
  public int getStackPtr(){
    return stackPtr;
  }
  public void incrStackPtr(){
    stackPtr++;
  }
  public boolean isFull(){
    if(stackPtr==tlbEntries.length) return true;
    else return false;
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
  public TLBEntry(String vpn, int v, int r, int d, String pfn){
    virtualPageNum = vpn;
    validBit = v;
    refBit = r;
    dirtyBit = d;
    pageFrameNum = pfn;
  }
  public void printEntry(){
    System.out.println(virtualPageNum+" "+validBit+" "+refBit+" "+dirtyBit+" "+pageFrameNum);
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
