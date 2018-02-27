//address width is 16 bits
//the page offset is 8 bits

public class CPU{
  private MMU mmu;
  private OS os;
  private PhysicalMemory pm;
  private TLBCache tlb;
  private VirtualPageTable pt;
}
