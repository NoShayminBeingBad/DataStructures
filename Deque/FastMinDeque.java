import java.util.Comparator;
import java.util.List;
import java.util.LinkedList;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;

public class FastMinDeque<T> implements MinDeque<T> {

  protected Comparator<? super T> comp;
  
  protected Deque<T> l;
  protected Deque<T> r;
  protected Deque<T> minL;
  protected Deque<T> minR;

  public FastMinDeque() {
    this(new DefaultComparator<T>());
  }

	/*
	Its the same as FastMinStack but 2 back to back, remember to rebalance
	*/
  public FastMinDeque(Comparator<? super T> comp) {
    this.comp = comp;
    this.l = new ArrayDeque<T>();
	this.r = new ArrayDeque<T>();
	this.minL = new ArrayDeque<T>();
	this.minR = new ArrayDeque<T>();
  }
  

  public void addFirst(T x) {
    if (minL.isEmpty()||comp.compare(x, minL.getFirst()) <= 0){
		minL.addFirst(x);
	}
	l.addFirst(x);
	rebalance();
	reMin();
  }

  public void addLast(T x) {
    if (minR.isEmpty()||comp.compare(x, minR.getLast()) <= 0){
		minR.addLast(x);
	}
	r.addLast(x);
	rebalance();
	reMin();
  }

  public T removeFirst() {
	if (l.isEmpty()){
		if (r.size() == 1){
			return removeLast();
		}
		return null;
	}
	T temp = l.pollFirst();
	if (comp.compare(temp, minL.getFirst()) == 0){
		minL.pollFirst();
	}
	rebalance();
	reMin();
	return temp;
  }

  public T removeLast() {
	if (r.isEmpty()){
		if (l.size() == 1){
			return removeFirst();
		}
		return null;
	}
	T temp = r.pollLast();
	if (comp.compare(temp, minR.getLast()) == 0){
		minR.pollLast();
	}
	rebalance();
	reMin();
	return temp;
  }

  public T min() {
	if (minL.isEmpty() && !minR.isEmpty()){
		return minR.getLast();
	}
	if (!minL.isEmpty() && minR.isEmpty()){
		return minL.getFirst();
	}
	T temp = minL.getFirst();
	if (comp.compare(minR.getLast(), temp) < 0){
		temp = minR.getLast();
	}
    return temp;
  }
  
  public T slowMin(){
	Iterator it = this.iterator();
	T minNum = null;
	while (it.hasNext()){
		T temp = (T)it.next();
		if (minNum == null || comp.compare(temp, minNum) < 0){
			minNum = temp;
		}
	}
	return minNum;
  }
  
  public void rebalance(){
	int right = r.size();
	int left = l.size();
	int n = right + left;
	if (right > 3*left){
		int s = n/2 - left;
		ArrayDeque<T> l1 = new ArrayDeque<T>();
		ArrayDeque<T> l2 = new ArrayDeque<T>();
		l1.addAll(l);
		ArrayList<T> temp = new ArrayList<T>(r);
		l1.addAll(temp.subList(0, s));
		l2.addAll(temp.subList(s, r.size()));
		l = l1;
		r = l2;
		reMinL();
		reMinR();
	}else if(left > 3*right){
		int s = left - n/2;
		ArrayDeque<T> l1 = new ArrayDeque<T>();
		ArrayDeque<T> l2 = new ArrayDeque<T>();
		ArrayList<T> temp = new ArrayList<T>(l);
		l1.addAll(temp.subList(0, s));
		l2.addAll(temp.subList(s, temp.size()));
		l2.addAll(r);
		l = l1;
		r = l2;
		reMinL();
		reMinR();
	}
  }
  
  public void reMin(){
	if (!r.isEmpty() && comp.compare(minR.getFirst(), r.getFirst()) != 0){
		reMinL();
	}
	if (!l.isEmpty() && comp.compare(minL.getLast(), l.getLast()) != 0){
		reMinR();
	}
  }
  
  public void reMinL(){
	Iterator<T> il = l.descendingIterator();
	LinkedList<T> newMin = new LinkedList<T>();
	if (il.hasNext()){
		T temp = il.next();
		newMin.addFirst(temp);
		while (il.hasNext()){
			temp = il.next();
			if (comp.compare(temp, newMin.getFirst()) <= 0){
				newMin.addFirst(temp);
			}
		}
	}
	minL = newMin;
  }
  
  public void reMinR(){
	Iterator<T> ir = r.iterator();
	LinkedList<T> newMin = new LinkedList<T>();
	if (ir.hasNext()){
		T temp = ir.next();
		newMin.addLast(temp);
		while (ir.hasNext()){
			temp = ir.next();
			if (comp.compare(temp, newMin.getLast()) <= 0){
				newMin.addLast(temp);
			}
		}
	}
	minR = newMin;
  }

  public int size() {
    return l.size() + r.size();
  }
  
  public void printL(){
    System.out.print("[");
    Iterator<T> it = l.iterator();
    while (it.hasNext()) {
      System.out.print(it.next());
      if (it.hasNext()) {
        System.out.print(",");
      }
    }
    System.out.print("] ");
    System.out.print("[");
    it = r.iterator();
    while (it.hasNext()) {
      System.out.print(it.next());
      if (it.hasNext()) {
        System.out.print(",");
      }
    }
    System.out.println("]");
  }
  
  public void printMin(){
    System.out.print("[");
    Iterator<T> it = minL.iterator();
    while (it.hasNext()) {
      System.out.print(it.next());
      if (it.hasNext()) {
        System.out.print(",");
      }
    }
    System.out.print("] ");
    System.out.print("[");
    it = minR.iterator();
    while (it.hasNext()) {
      System.out.print(it.next());
      if (it.hasNext()) {
        System.out.print(",");
      }
    }
    System.out.println("]");
  }

  public Iterator<T> iterator() {
	return new DoubleDequeIterator<T>(l, r);
  }

  public Comparator<? super T> comparator() {
    return comp;
  }

}
