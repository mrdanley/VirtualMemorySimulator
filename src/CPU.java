//address width is 16 bits
//the page offset is 8 bits
import java.util.Scanner;


public class CPU{
  private MMU mmu;
  private OS os;
  private PhysicalMemory pm;
  private VirtualPageTable pt;

  public CPU(){
    //instantiate all private members
    mmu=new MMU();
    os=new OS();
    pm=new PhysicalMemory();
    pt=new VirtualPageTable();

  }
  //read instructions and pass them to mmu in correct format
  public void readInstructions(String filename) throws FileNotFoundException{
  		File input=new File(filename);
  		Scanner sc=new Scanner(input);
  		while(sc.hasNextLine()){
  			String line=sc.nextLine();
  			System.out.println(line);
  			//read
  			if(line.equals("0")){
  				String destination=sc.nextLine();
  				mmu.read(destination);
  			}
  			//write
  			else if(line.equals("1")){
  				String destination=sc.nextLine();
  				int integer=Integer.parseInt(sc.nextLine());
  				mmu.write(destination,integer);
  			}
  		}


  	}
}
