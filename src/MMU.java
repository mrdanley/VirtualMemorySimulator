//uses FIFO for replacement algorithm
//handles virtual page table
import java.util.Scanner;
public class MMU{
  private LinkedList tlbIndices;

  public MMU(){
    tlbIndices = new LinkedList();
  }
  public int checkTLB(TLBCache tlb,String vpn){
    return tlb.getTLBEntryIndex(vpn);
  }
  public String pageTableEntryClear(VirtualPageTable pt,int ramIndex){
    String pfn = Integer.toHexString(ramIndex).toUpperCase();
    if(pfn.length()==1) pfn = "0"+pfn;
    int ptIndex = pt.getPTEntryIndex(pfn);
    pt.getPTEntry(ptIndex).setValid(0);
    pt.getPTEntry(ptIndex).setPageFrame("");

    String evictedPage = Integer.toHexString(ptIndex).toUpperCase();
    if(evictedPage.length()==1) evictedPage="0"+evictedPage;
    return evictedPage;
  }
  public void setRef(TLBCache tlb,VirtualPageTable pt,int tlbIndex){
    tlb.getTLBEntry(tlbIndex).setRef(1);
    int ptIndex = Integer.parseInt(tlb.getTLBEntry(tlbIndex).getVirtualPage(),16);
    pt.getPTEntry(ptIndex).setRef(1);
  }
  public void setDirty(TLBCache tlb,VirtualPageTable pt,int index){
    tlb.getTLBEntry(index).setDirty(1);
    pt.getPTEntry(index).setDirty(1);
  }
  public int checkPT(VirtualPageTable pt,String vpn){
    int ptIndex = Integer.parseInt(vpn,16);
    int valid = pt.getPTEntry(ptIndex).getValid();
    if(valid==1){
      return ptIndex;
    }else{
      return -1;
    }
  }
  public void setRef(VirtualPageTable pt,int index){
    pt.getPTEntry(index).setRef(1);
  }
  public int fifo(){
    return tlbIndices.removeHead();
  }
  public void updateTLBandPT(VirtualPageTable pt,int ramIndex,TLBCache tlb,int tlbIndex,String vpn){
    String pageFrame = Integer.toHexString(ramIndex).toUpperCase();
    if(pageFrame.length()==1) pageFrame = "0"+pageFrame;
    int ptIndex = Integer.parseInt(vpn,16);
    pt.setPTEntry(ptIndex,1,0,0,pageFrame);
    //add new entry in page table to tlb
    tlb.setTLBEntry(tlbIndex,vpn,1,0,0,pageFrame);
    tlbIndices.add(tlbIndex);
  }
  public void copyFromPTtoTLB(TLBCache tlb,VirtualPageTable pt,int tlbIndex,int ptIndex){
    String vpn = Integer.toHexString(ptIndex).toUpperCase();
    if(vpn.length()==1) vpn = "0"+vpn;
    tlb.setTLBEntry(tlbIndex,vpn,pt.getPTEntry(ptIndex).getValid(),
                                pt.getPTEntry(ptIndex).getRef(),
                                pt.getPTEntry(ptIndex).getDirty(),
                                pt.getPTEntry(ptIndex).getPageFrame());
    tlbIndices.add(tlbIndex);
  }
  public boolean checkDirty(VirtualPageTable pt,int ramIndex){
    String pfn = Integer.toHexString(ramIndex).toUpperCase();
    if(pfn.length()==1) pfn = "0"+pfn;
    int ptIndex = pt.getPTEntryIndex(pfn);
    if(pt.getPTEntry(ptIndex).getDirty()==1){
      return true;
    }else{
      return false;
    }
  }
}
