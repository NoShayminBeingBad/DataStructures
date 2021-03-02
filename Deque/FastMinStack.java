import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;

public class FastMinStack<T> implements MinStack<T> {

  protected Comparator<? super T> comp;
  
  protected List<T> l;
  protected LinkedList<T> minL;

  public FastMinStack() {
    this(new DefaultComparator<T>());
  }

  public FastMinStack(Comparator<? super T> comp) {
    this.comp = comp;
	this.l = new ArrayList<T>();
	this.minL = new LinkedList<T>();
  }

  public void push(T x) {
	if (minL.isEmpty() || comp.compare(x, minL.getLast()) <= 0){
		minL.add(x);
	}
	l.add(x);
  }

  public T pop() {
	if (l.isEmpty()) return null;
	T temp = l.remove(l.size() - 1);
	if (comp.compare(temp, minL.getLast()) == 0){
		minL.pollLast();
	}
	return temp;
  }

  public T min() {
	if (minL.isEmpty()) return null;
	return minL.getLast();
  }

  public int size() {
    return l.size();
  }

  public Iterator<T> iterator() {
    return l.iterator();
  }

  public Comparator<? super T> comparator() {
    return comp;
  }
}
