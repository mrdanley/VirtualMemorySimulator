//uses FIFO for replacement algorithm
//handles virtual page table
public class MMU{
  public String read(TLBCache tlb,VirtualPageTable pt,String vpn){
    int ptIndex,tlbIndex = tlb.getTLBEntryIndex(vpn);
    String pageFrame = tlb.getTLBEntry(tlbIndex);
    if(pageFrame==null){
      ptIndex = Integer.parseInt(vpn,16);
      pageFrame = pt.getPTEntry(ptIndex).getPageFrame();
      if(pageFrame==null){//hard miss
        System.out.println("Hard miss");
        //os will do clock page replacement algorithm
      }else{//soft miss
        System.out.println("Soft miss");
        System.out.println("Hit");
        if(tlb.isFull()){
          //fifo replacement algorithm
        }else{
          //set page table entry reference bit to 1
          pt.getPTEntry(ptIndex).setRef(1);
          //add entry from page table to tlb
          tlb.getTLBEntries()[tlb.getStackPtr()] = new TLBEntry(vpn,1,1,0,pageFrame);
          tlb.incrStackPtr();
        }
      }
    }else{
      System.out.println("Hit");
      tlb.getTLBEntry(tlbIndex).setRef(1);
    }
    return pageFrame;
  }
  public String write(TLBCache tlb,VirtualPageTable pt,String vpn,int data){
    return "";
  }
}
