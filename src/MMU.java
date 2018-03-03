//uses FIFO for replacement algorithm
//handles virtual page table
public class MMU{
  private LinkedList tlbIndices;
  public MMU(){
    tlbIndices = new LinkedList();
  }
  public int checkTLB(TLBCache tlb,String vpn){
    return tlb.getTLBEntryIndex(vpn);
  }
  public void setRef(TLBCache tlb,VirtualPageTable pt,int index){
    tlb.getTLBEntry(index).setRef(1);
    pt.getPTEntry(index).setRef(1);
  }
  public void setDirty(TLBCache tlb,VirtualPageTable pt,int index){
    tlb.getTLBEntry(index).setDirty(1);
    pt.getPTEntry(index).setDirty(1);
  }
  public int checkPT(TLBCache tlb,String vpn){
    ptIndex = Integer.parseInt(vpn,16);
    int valid = pt.getPTEntry(ptIndex).getValid();
    if(valid==1){
      return ptIndex;
    }els{
      return -1;
    }
  }
  public void setRef(VirtualPageTable pt,int index){
    pt.getPTEntry(index).setRef(1);
  }
  public int fifo(){
    return tlbIndices.removeHead();
  }
  public void copyFromPTtoTLB(TLBCache tlb,VirtualPageTable pt,int tlbIndex,int ptIndex){
    String vpn = Integer.toHextString(ptIndex,16);
    if(vpn.length()==1) vpn = "0"+vpn;
    tlb.setTLBEntry(tlbIndex,vpn,pt.getPTEntry(ptIndex).getValid(),
                                pt.getPTEntry(ptIndex).getRef(),
                                pt.getPTEntry(ptIndex).getDirty(),
                                pt.getPTEntry(ptIndex).getPageFrame());
    ptIndices.add(tlbIndex);
  }
  public boolean checkDirty(VirtualPageTable pt,int ramIndex){
    if(pt.getPTEntry(Integer.parseInt(ramIndex,16)).getDirty()==1){
      return true;
    }else{
      return false;
    }
  }
}
