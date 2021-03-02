import java.util.Iterator;
import java.util.Collection;

public class DoubleDequeIterator<T> implements Iterator<T> {

    private Iterator<T> l;
	private Iterator<T> r;

    public DoubleDequeIterator(Collection<T> left, Collection<T> right) {
		this.l = left.iterator();
		this.r = right.iterator();
    }

    public boolean hasNext() {
		if (!l.hasNext()){
			return r.hasNext();
		}
		return true;
    }

    public T next() {
		if (l.hasNext()){
			return l.next();
		}else {
			return r.next();
		}
    }
}
