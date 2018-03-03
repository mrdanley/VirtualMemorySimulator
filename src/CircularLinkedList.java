public class CircularLinkedList{
  private Node last;
  private static int counter;

  public LinkedList(){
    last = new Node(null);
    counter = 0;
  }
  public void add(int index){
    if(counter==0){
      last = new Node(index);
      last.setNext(last);
      incrCounter();
      return;
    }

    Node temp = new Node(index);
    temp.setNext() = last.getNext();
    last.setNext() = temp;
    last = temp;
    incrCounter();
  }
  public int getNext(){
    return last.getNext().getData();
  }
  public int remove(){
    Node temp = last.getNext();
    last.setNext() = temp.getNext();
    decrCounter();
  }
  private static int getCounter(){
    return counter;
  }
  private static void incrCounter(){
    counter++;
  }
  private static void decrCounter(){
    counter--;
  }
}
