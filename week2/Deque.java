package week2;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {


    private int size;
    private Node first;
    private Node last;

    // 用链表实现双端队列
    private class Node {
        private Item val;
        private Node next;
        private Node past;
        public Node(Item val) {
            this.val = val;
        }
    }
    // construct an empty deque
    public Deque() {
        this.size = 0;
        this.first = null;
        this.last = null;
    }

    // is the deque empty?
    public boolean isEmpty(){
        return this.size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("you must add a valid item!");
        Node oldFirst = this.first;
        this.first = new Node(item);
        this.first.next = oldFirst;
        // 如果oldFirst不是null，才能往回指，否则这个新节点是唯一的节点，自然也是last。addLast同理。
        if (isEmpty()) this.last = this.first;
        else oldFirst.past = this.first;
        ++this.size;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("you must add a valid item!");
        Node oldLast = this.last;
        this.last = new Node(item);
        this.last.past = oldLast;
        if(isEmpty()) this.first = this.last;
        else oldLast.next = this.last;
        ++this.size;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("deque is empty!");
        Item item = this.first.val;
        this.first = this.first.next;
        --this.size;
        // 删除了以后，考虑当前deque为空的情况
        if (isEmpty()) this.last = null;
        else this.first.past = null;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("deque is empty!");
        Item item = this.last.val;
        this.last = this.last.past;
        --this.size;
        if (isEmpty()) this.first = null;
        else this.last.next = null;
        return item;
    }

    // return an iterator over items in order from front to back
    // 实现Iterable接口就要实现Iterator()
    @Override
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node cur = first;

        public boolean hasNext() {
            return this.cur != null;
        }
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("next is null");
            Item item = cur.val;
            cur = cur.next;
            return item;
        }
    }



    public static void main(String[] args) {
        Deque<String> deque = new Deque<String> ();
        while(!StdIn.isEmpty()) {
            String s = StdIn.readString();
            if(!s.equals("-")) {
                StdOut.println("1->deque.size()=" +deque.size());
                deque.addFirst(s);
                StdOut.println("2->deque.size()=" +deque.size());
            }
            else if(!deque.isEmpty()) {
                StdOut.println(deque.removeFirst() + " ");
                StdOut.println("3->deque.size()=" +deque.size());
            }
        }
        StdOut.println("(" + deque.size() +" left on the deque)");
    }


}
