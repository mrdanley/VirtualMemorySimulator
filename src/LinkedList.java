public class LinkedList{
  private Node head;
  private static int counter;

  public LinkedList(){
    head = new Node(null);
    counter = 0;
  }
  public void add(int index){
    if(counter==0){
      head = new Node(data);
      incrCounter();
      return;
    }

    Node temp = new Node(index);
    Node current = head;

    while(current.getNext() != null){
      current = current.getNext();
    }

    current.setNext(temp);
    incrCounter();
  }
  public int removeHead(){
    Node oldHead = head;
    this.head = head.getNext();
    return oldHead.getData();
  }
  private static int getCounter(){
    return counter;
  }
  private static void incrCounter(){
    counter++;
  }
}
