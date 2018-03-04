public class LinkedList{
  private Node head;
  private Node tail;
  private static int counter;

  public LinkedList(){
    head = new Node(-1);
    tail = head;
    counter = 0;
  }
  public void add(int index){
    if(counter==0){
      head = new Node(index);
      tail = head;
      incrCounter();
      return;
    }

    Node temp = new Node(index);
    tail.setNext(temp);
    tail = temp;
    if(counter<8) incrCounter();
  }
  public int removeHead(){
    Node oldHead = new Node(head.getData());
    tail.setNext(oldHead);
    tail = oldHead;
    head = head.getNext();
    return oldHead.getData();
  }
  public static int getCounter(){
    return counter;
  }
  private static void incrCounter(){
    counter++;
  }
}
