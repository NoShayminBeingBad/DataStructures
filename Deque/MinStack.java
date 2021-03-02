import java.util.Comparator;

public interface MinStack<T> extends Iterable<T> {
  public Comparator<? super T> comparator();

  public void push(T x);

  public T pop();

  public T min();

  public int size();
}
