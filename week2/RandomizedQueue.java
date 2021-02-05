
package week2;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

// the item removed is chosen uniformly at random among items in the data structure.
public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] queue;
    private int size;

    // construct an empty randomized queue
    public RandomizedQueue() {
        // 不允许直接创建泛型数组，只能强制类型转换
        queue = (Item[]) new Object[1];
        size = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // 数组实现queue，需要考虑扩容和缩容
    private void resize(int capacity) {
        Item[] newQueue = (Item[]) new Object[capacity];
        for (int i = 0; i < size; ++i) newQueue[i] = queue[i];
        queue = newQueue;

    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        // 考虑扩容
        if (queue.length == size) resize(queue.length * 2);
        queue[size++] = item;
    }

    // remove and return a random item
    // 实现思路：随机选一个元素，和最后一个元素交换位置，再将最后一个元素出队，避免整体移动
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        // random()返回[0, 1)的浮点数，随机选取一个目标被尾元素替换
        int index = (int) Math.random() * size;
        Item item = queue[index];
        queue[index] = queue[size-1];
        queue[--size] = null;
        // 考虑缩容，并且避免抖动（上课讲的缩容方式）
        if (size > 0 && size == queue.length / 4) resize(queue.length / 2);
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        int index = (int) Math.random() * size;
        Item item = queue[index];
        return item;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new queueIterator();
    }

    // 注意：不要在内部类中声明泛型！哪怕名称相同编译器都会理解为不同类型
    private class queueIterator implements Iterator<Item> {
        // 需要random order返回，考虑到不改变原数组，创个新的，然后shuffle打乱
        private Item[] randomQ;
        private int index = 0;

        public queueIterator() {
            randomQ = (Item[]) new Object[size]; // 无法初始化Generics数组，所以用到类型转换
            for (int i = 0; i < size; ++i) randomQ[i] = queue[i];
            StdRandom.shuffle(randomQ);
        }

        public boolean hasNext() {
            return index < randomQ.length;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return randomQ[index++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<String> deque = new RandomizedQueue<>();
        while(!StdIn.isEmpty()) {
            String s = StdIn.readString();
            if(!s.equals("-")) {
                StdOut.println("1->deque.size()=" +deque.size());
                deque.enqueue(s);
                StdOut.println("2->deque.size()=" +deque.size());
            }
            else if(!deque.isEmpty()) {
                StdOut.println(deque.dequeue() + " ");
                StdOut.println("3->deque.size()=" +deque.size());
            }
        }
        StdOut.println("(" + deque.size() +" left on the deque)");
    }

}
