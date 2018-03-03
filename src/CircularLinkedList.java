public class CircularLinkedList{
  private Node last;
  private static int counter;

  public CircularLinkedList(){
    last = new Node(-1);
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
    temp.setNext(last.getNext());
    last.setNext(temp);
    last = temp;
    incrCounter();
  }
  public Node getNext(){
    return last.getNext();
  }
  public int remove(){
    Node temp = last.getNext();
    last.setNext(temp.getNext());
    decrCounter();
  }
  public static int getCounter(){
    return counter;
  }
  public static void incrCounter(){
    counter++;
  }
  public static void decrCounter(){
    counter--;
  }
}
