import java.util.Iterator;
import java.util.Arrays;
import java.util.Random;

public class RyabkoTree implements PrefixStack {
	
	Integer[] l;
	long[] tree;
	int size;

  public RyabkoTree() {
    l = new Integer[16];
	tree = new long[16];
	size = 1;
  }

  public void push(int x) {
	//System.out.println("Adding: " + x + " to " + size);
	if (size == 1){
		l[1] = x;
		tree[1] = x;
	}else {
		l[size] = x;
		int high = Integer.highestOneBit(size);
		int low = Integer.lowestOneBit(size);
		if (high == low){
			tree[size] = prefixSum(size - 2) + x;
		}else if (size % 2 == 1){
			tree[size] = x;
		}else {
			long sum = subPrefixSum(size - low, size - 2);
			tree[size] = sum + x;
		}
	}
	size++;
	resize();
	//System.out.println("\n" + Arrays.asList(l) + "\n" + Arrays.asList(tree));
  }

  public int pop() {
	  int save;
	  if (size == 1){
		  return 0;
	  }else {
		  save = l[--size];
	  }
	  return save;
  }


  public int get(int i) {
	i++;
    return l[i];
  }

  public int set(int i, int x) {
	if (i > size) return 0;
	i++;
	int save = l[i];
	//System.out.println("Changing " + save + " for " + x);
	l[i] = x;
	int dif = x - save;
    while (i < size){
		tree[i] += dif;
		i += i & (-i);
	}
	//System.out.println("\n" + Arrays.asList(l) + "\n" + Arrays.asList(tree));
	return save;
  }

  public long prefixSum(int i) {
	//if (i >= size) i = size - 1;
	i++;
    long sum = 0;
	while (i > 0){
		sum += tree[i];
		i -= i & (-i);
	}
	//System.out.println("\n" + Arrays.asList(l) + "\n" + Arrays.asList(tree));
	return sum;
  }
  
  public long subPrefixSum(int start, int i){
	i++;
    long sum = 0;
	while (i > start){
		sum += tree[i];
		i -= i & (-i);
	}
	//System.out.println("\n" + Arrays.asList(l) + "\n" + Arrays.asList(tree));
	return sum;
  }
  
  public void resize(){
	if (size + 2 >= l.length){
		Integer[] arr = new Integer[4 * size];
		long[] newTree = new long[4 * size];
		System.arraycopy(l, 0, arr, 0, l.length - 1);
		System.arraycopy(tree, 0, newTree, 0, tree.length - 1);
		l = arr;
		tree = newTree;
	}
  }
  
  public double log2(int n){
	return Math.log(n)/Math.log(2);
  }

  public int size(){
	return size - 1;
  }
  
  private class ArrIter<T> implements Iterator<T> {
	  int size;
	  int curr;
	  T[] arr;
	  
	  public ArrIter(T[] a, int s){
		this.arr = a;
		this.size = s;
		curr = 1;
	  }
	  
	  public boolean hasNext(){
		  return curr >= size;
	  }
	  
	  public T next(){
		  return arr[curr++];
	  }
	  
  }
  
  public void print(){
	System.out.println("\n" + Arrays.asList(l) + "\n" + Arrays.asList(tree));
  }
  
  public Iterator<Integer> iterator(){
	return new ArrIter(l, size);
  }
  
  public static void main(String[] args){
	RyabkoTree rt = new RyabkoTree();
	SlowPrefixStack sl = new SlowPrefixStack();
	Random rand = new java.util.Random();
	for (int i = 0; i < 25000; i++){
		int num = rand.nextInt(100) - 50;
		rt.push(num);
		sl.push(num);
		if (sl.prefixSum(i)!= rt.prefixSum(i)){
			System.out.println("Sum: " + rt.prefixSum(i) + " Acutal Sum: " + sl.prefixSum(i) + " Index: " + i);
		}
	}
	//rt.print();
	System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\");
	for (int i = 25000 - 1; i >= 0; i--){
		int r = rand.nextInt(100) - 50;
		rt.pop();
		sl.pop();
		if (sl.prefixSum(i)!= rt.prefixSum(i)){
			System.out.println("Sum: " + rt.prefixSum(i) + " Acutal Sum: " + sl.prefixSum(i) + " Index: " + i);
		}
	}
	//rt.print();
  }
  
}
