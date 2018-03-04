public class CircularLinkedList{
  private Node head,tail;
  private static int counter;

  public CircularLinkedList(){
    head = null;
    tail = null;
    counter = 0;
  }
  public void add(int index){
    if(counter==0){
      head = new Node(index);
      tail = head;
      head.setNext(tail);
      incrCounter();
      return;
    }

    Node temp = new Node(index);
    tail.setNext(temp);
    temp.setNext(head);
    tail = temp;
    incrCounter();
  }
  public Node getHead(){
    return head;
  }
  public Node getTail(){
    return tail;
  }
  public int remove(){
    Node temp = head;
    head = head.getNext();
    tail.setNext(head);
    decrCounter();
    return temp.getData();
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
