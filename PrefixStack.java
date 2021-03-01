package comp2402a4;

import java.util.Iterator;

public interface PrefixStack extends Iterable<Integer> {
  public void push(int x);
  public int pop();
  public int get(int i);
  public int set(int i, int x);
  public long prefixSum(int i);
  public int size();
  public Iterator<Integer> iterator();
}
