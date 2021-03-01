package comp2402a3;

import java.util.LinkedList;
import java.util.Queue;
import java.io.PrintWriter;
import java.util.Random;
import java.util.ArrayList;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * An implementation of binary trees
 * @author morin
 *
 * @param <Node>
 */
public class BinaryTree<Node extends BinaryTree.BTNode<Node>> {

	public static class BTNode<Node extends BTNode<Node>> {
		public Node left;
		public Node right;
		public Node parent;
	}

	/**
	 * An extension of BTNode that you can actually instantiate.
	 */
	protected static class EndNode extends BTNode<EndNode> {
			public EndNode() {
				this.parent = this.left = this.right = null;
			}
	}

	/**
	 * Used to make a mini-factory
	 */
	protected Node sampleNode;

	/**
	 * The root of this tree
	 */
	protected Node r;

	/**
	 * This tree's "null" node
	 */
	protected Node nil;

	/**
	 * Create a new instance of this class
	 * @param sampleNode - a sample of a node that can be used
	 * to create a new node in newNode()
	 * @param nil - a node that will be used in place of null
	 */
	public BinaryTree(Node sampleNode, Node nil) {
		this.sampleNode = sampleNode;
		this.nil = nil;
		r = nil;
	}

	/**
	 * Create a new instance of this class
	 * @param sampleNode - a sample of a node that can be used
	 * to create a new node in newNode()
	 */
	public BinaryTree(Node sampleNode) {
		this.sampleNode = sampleNode;
	}

	/**
	 * Allocate a new node for use in this tree
	 * @return
	 */
	@SuppressWarnings({"unchecked"})
	protected Node newNode() {
		try {
			Node u = (Node)sampleNode.getClass().getDeclaredConstructor().newInstance();
			u.parent = u.left = u.right = nil;
			return u;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Compute the depth (distance to the root) of u
	 * @param u
	 * @return the distanct between u and the root, r
	 */
	public int depth(Node u) {
		int d = 0;
		while (u != r) {
			u = u.parent;
			d++;
		}
		return d;
	}

	/**
	 * Compute the size (number of nodes) of this tree
	 * @warning uses recursion so could cause a stack overflow
	 * @return the number of nodes in this tree
	 */
	public int size() {
		return size(r);
	}

	/**
	 * @return the size of the subtree rooted at u
	 */
	protected int size(Node u) {
		if (u == nil) return 0;
		return 1 + size(u.left) + size(u.right);
	}

	/**
	 * Compute the number of nodes in this tree without recursion
	 * @return
	 */
	public int size2() {
		Node u = r, prev = nil, next;
		int n = 0;
		while (u != nil) {
			if (prev == u.parent) {
				n++;
				if (u.left != nil) next = u.left;
				else if (u.right != nil) next = u.right;
				else next = u.parent;
			} else if (prev == u.left) {
				if (u.right != nil) next = u.right;
				else next = u.parent;
			} else {
				next = u.parent;
			}
			prev = u;
			u = next;
		}
		return n;
	}

	/**
	 * Compute the maximum depth of any node in this tree
	 * @return the maximum depth of any node in this tree
	 */
	public int height() {
		return height(r);
	}

	/**
	 * @return the height of the subtree rooted at u
	 */
	protected int height(Node u) {
		if (u == nil) return -1;
		return 1 + Math.max(height(u.left), height(u.right));
	}


	public String toString() {
		StringBuilder sb = new StringBuilder();
		toStringHelper(sb, r);
		return sb.toString();
	}

	protected void toStringHelper(StringBuilder sb, Node u) {
			if (u == null) {
				return;
			}
			sb.append('(');
			toStringHelper(sb, u.left);
			toStringHelper(sb, u.right);
			sb.append(')');
	}


	/**
	 * @ return an n-node BinaryTree that has the shape of a random
	 * binary search tree.
	 */
	public static BinaryTree<EndNode> randomBST(int n) {
		Random rand = new Random();
		EndNode sample = new EndNode();
		BinaryTree<EndNode> t = new BinaryTree<EndNode>(sample);
		t.r = randomBSTHelper(n, rand);
		return t;
	}

	protected static EndNode randomBSTHelper(int n, Random rand) {
		if (n == 0) {
			return null;
		}
		EndNode r = new EndNode();
		int ml = rand.nextInt(n);
		int mr = n - ml - 1;
		if (ml > 0) {
			r.left = randomBSTHelper(ml, rand);
			r.left.parent = r;
		}
		if (mr > 0) {
			r.right = randomBSTHelper(mr, rand);
			r.right.parent = r;
		}
		return r;
	}

	/**
	 * @return
	 */
	public boolean isEmpty() {
		return r == nil;
	}

	/**
	 * Make this tree into the empty tree
	 */
	public void clear() {
		r = nil;
	}

	/**
	 * Demonstration of a recursive traversal
	 * @param u
	 */
	public void traverse(Node u) {
		if (u == nil) return;
		traverse(u.left);
		traverse(u.right);
	}

	/**
	 * Demonstration of a non-recursive traversal
	 */
	public void traverse2() {
		Node u = r, prev = nil, next;
		while (u != nil) {
			if (prev == u.parent) {
				if (u.left != nil) {
					next = u.left;
				} else if (u.right != nil) {
					next = u.right;
				}	else {
					next = u.parent;
				}
			} else if (prev == u.left) {
				if (u.right != nil) {
					next = u.right;
				} else {
					next = u.parent;
				}
			} else {
				next = u.parent;
			}
			prev = u;
			u = next;
		}
	}

	/**
	 * Demonstration of a breadth-first traversal
	 */
	public void bfTraverse() {
		Queue<Node> q = new LinkedList<Node>();
		if (r != nil) q.add(r);
		while (!q.isEmpty()) {
			Node u = q.remove();
			if (u.left != nil) q.add(u.left);
			if (u.right != nil) q.add(u.right);
		}
	}

	/**
	 * Find the first node in an in-order traversal
	 * @return the first node reported in an in-order traversal
	 */
	public Node firstNode() {
		Node w = r;
		if (w == nil) return nil;
		while (w.left != nil)
			w = w.left;
		return w;
	}

	/**
	 * Find the node that follows w in an in-order traversal
	 * @param w
	 * @return the node that follows w in an in-order traversal
	 */
	public Node nextNode(Node w) {
		if (w.right != nil) {
			w = w.right;
			while (w.left != nil)
				w = w.left;
		} else {
			while (w.parent != nil && w.parent.left != w)
				w = w.parent;
			w = w.parent;
		}
		return w;
	}

	public int totalDepth() {
		Node u = r, prev = nil, next;
		int d = 0;
		int total = 0;
		while (u != nil){
			if (prev == u.parent){
				if (u.left != nil){
					next = u.left;
					d++;
				}else if (u.right != nil){
					next = u.right;
					d++;
				}else {
					next = u.parent;
					total += d;
					d--;
				}
			}else if (prev == u.left){
				if (u.right != nil){
					next = u.right;
					d++;
				}else {
					next = u.parent;
					total += d;
					d--;
				}
			}else {
				next = u.parent;
				total += d;
				d--;
			}
			prev = u;
			u = next;
		}
		return total;
	}

	public int totalLeafDepth() {
		Node u = r, prev = nil, next;
		int d = 0;
		int total = 0;
		while (u != nil){
			if (prev == u.parent){
				if (u.left != nil){
					next = u.left;
					d++;
				}else if (u.right != nil){
					next = u.right;
					d++;
				}else {
					next = u.parent;
					total += d;
					d--;
				}
			}else if (prev == u.left){
				if (u.right != nil){
					next = u.right;
					d++;
				}else {
					next = u.parent;
					d--;
				}
			}else if (prev == u.right){
				next = u.parent;
				d--;
			}else {
				next = u.parent;
				total += d;
				d--;
			}
			prev = u;
			u = next;
		}
		return total;
	}

	public String bracketSequence() {
		StringBuilder sb = new StringBuilder();
		Node u = r, prev = nil, next;
		if (r == nil) return ".";
		sb.append("(");
		while (u != nil){
			if (prev == u.parent){
				if (u.left != nil){
					next = u.left;
					sb.append("(");
				}else if (u.right != nil){
					next = u.right;
					sb.append(".(");
				}else {
					next = u.parent;
					sb.append("..)");
				}
			}else if (prev == u.left){
				if (u.right != nil){
					next = u.right;
					sb.append("(");
				}else {
					next = u.parent;
					sb.append(".)");
				}
			}else if (prev == u.right){
				next = u.parent;
				sb.append(")");
			}else {
				next = u.parent;
				sb.append(")");
			}
			prev = u;
			u = next;
		}
		return sb.toString();
	}

	public void prettyPrint(PrintWriter w) {
		if (r == nil)return;
		ArrayList<StringBuilder> arr = new ArrayList<StringBuilder>();
		Node u = r, prev = nil, next;
		int d = 0;
		int len = 0;
		arr.add(new StringBuilder());
		arr.get(d).append("*");
		while (u != nil){
			//System.out.println("arr: " + arr.get(0).length() + " len: " + d);
			if (prev == u.parent){
				if (u.left != nil){
					next = u.left;
					d++;
					if (d >= arr.size()){
						arr.add(new StringBuilder());
					}
					//System.out.println("arr: " + arr.get(d).length() + " len: " + len);
					while (arr.get(d).length() < len){
						arr.get(d).append(" ");
					}
					arr.get(d).append("*");
				}else if (u.right != nil){
					next = u.right;
					len += 2;
					//System.out.println("arr: " + arr.get(d).length() + " len: " + len);
					while (arr.get(d).length() < len){
						arr.get(d).append("-");
					}
					arr.get(d).append("*");
				}else {
					next = u.parent;
					if (d > 0 && u.parent.left == u)
						d--;
				}
			}else if (prev == u.left){
				if (u.right != nil){
					next = u.right;
					len += 2;
					//System.out.println("arr: " + arr.get(d).length() + " len: " + d);
					while (arr.get(d).length() < len){
						arr.get(d).append("-");
					}
					arr.get(d).append("*");
				}else {
					next = u.parent;
					if (d > 0 && u.parent.left == u)
						d--;
				}
			}else if (prev == u.right){
				next = u.parent;
				if (d > 0 && u.parent.left == u)
					d--;
			}else {
				next = u.parent;
				if (d > 0 && u.parent.left == u)
					d--;
			}
			prev = u;
			u = next;
		}
		for (int i = 0; i < arr.size() - 1; i++){
			w.println(arr.get(i));
			for (int j = 0; j < arr.get(i).length(); j++){
				if (arr.get(i+1).length() >= j &&arr.get(i).charAt(j) == '*' && arr.get(i+1).charAt(j) == '*'){
					w.print("|");
				}else {
					w.print(" ");
				}
			}
			w.println();
		}
		w.println(arr.get(arr.size() - 1));
	}

	public static void main(String[] args) {
		try {
			PrintWriter w;
			if (args.length == 0) {
				w = new PrintWriter(System.out);
			} else if (args.length == 1) {
				w = new PrintWriter(System.out);				
			} else {
				w = new PrintWriter(new FileWriter(args[1]));
			}
			BinaryTree<EndNode> a =randomBST(100);
			a.prettyPrint(w);
			//System.out.println(a.bracketSequence());
			w.flush();
		} catch (IOException e) {
			System.err.println(e);
			System.exit(-1);
		}
	}

}
