package main;

import java.util.ArrayList;

public class Node {

	public Node() {
		next = null;
		pare = null;
	}
	
	public Node(String lab) {
		label = lab;
		next = null;
		pare = null;
	}
	
	public Node(String lab, Node pare) {
		label = lab;
		next = null;
		this.pare = pare;
	}
	
	public Node(Node node) {
		this.label = node.label;
		this.input = node.input;
		this.output = node.output;
		this.depth = node.depth;
		this.pare = null;
		if (node.next != null) {
			this.next = new Node(node.next);
			this.next.setPare(this);
		} else {
			this.next = null;
		}
	}
	
	public String getInput() {
		return input;
	}
	
	public void setInput(String input) {
		this.input = input;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getOutput() {
		return output;
	}
	
	public void setOutput(String output) {
		this.output = output;
	}
	
	public Node getNext() {
		return next;
	}
	
	public void setNext(Node next) {
		this.next = next;
		this.next.setPare(this);
		updateDepth(next.getDepth() + 1);
	}
	
	public Node getPare() {
		return pare;
	}
	
	public void setPare(Node pare) {
		this.pare = pare;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	public void setVals(String vals) {
	
		boolean first = false;
		boolean second = false;
		String nextLabel = "";
		for (int i = 0; i < vals.length(); i++) {
			if (first && second) {
				nextLabel += Character.toString(vals.charAt(i));
			} else if (first && vals.charAt(i) == '|') {
				second = true;
			} else if (first) {
				output += Character.toString(vals.charAt(i));
			} else if (vals.charAt(i) == '|') {
				first = true;
			} else {
				input += Character.toString(vals.charAt(i));
			}
		}
	
		if (this.next == null) {
			next = new Node(nextLabel, this);
		}
		updateDepth(1);
	}

	public void copy(Node other) {
		this.label = other.label;
		this.input = other.input;
		this.output = other.output;
		this.depth = other.depth;
		if (other.next != null) {
			this.next = new Node(" ", this);
			this.next = other.next;
		} else {
			this.next = null;
		}
	}
	
	public boolean equals(Node other) {
		boolean result = false;
	
		if (this.depth == other.depth) {
			if (this.label == other.label) {
				if (this.input == other.input) {
					if (this.output == other.output) {
						if (this.next != null && other.next != null) {
							result = this.next.equals(other.next);
						} else if (this.next == null && other.next == null) {
							result = true;
						}
					}
				}
			}
		}
	
		return result;
	}
	
	public void updateDepth(int depth) {
		this.depth = depth;
		if (pare != null) {
			this.pare.updateDepth(depth+1);
		}
	}

	public ArrayList<IOPair> to_IOPairList() {
		
		ArrayList<IOPair> list = new ArrayList<IOPair>();
		IOPair pair = null;
		Node T = this;
		while (T != null && T.getDepth() > 0) {
			pair = new IOPair(T.getInput(), T.getOutput());
			list.add(pair);
			T = T.getNext();
		}
		
		return list;
	}
	
	private String input = "";
	private String output = "";
	private String label = "";
	private Node next;
	private Node pare;
	private int depth = 0;
}
