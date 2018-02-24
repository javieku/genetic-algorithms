package org.pe.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tree<T> implements Cloneable {

	protected T data;
    protected List<Tree<T>> children;
    protected int numNodes;
    protected int depth;

    public Tree() {
        children = new ArrayList<Tree<T>>();
    }

    public Tree(T data) {
    	children = new ArrayList<Tree<T>>();
        setData(data);
    }

    public List<Tree<T>> getChildren() {
        return this.children;
    }

    public int getNumberOfChildren() {
        return getChildren().size();
    }
    
	public int getNumberOfNodes() {
		return numNodes;
	}

    public boolean hasChildren() {
        return (getNumberOfChildren() > 0);
    }

    public void setChildren(List<Tree<T>> children) {
        this.children = children;
    }

    public void addChild(Tree<T> child) {
        children.add(child);
    }

    public void addChildAt(int index, Tree<T> child) {
        children.add(index, child);
    }

    public void removeChildren() {
        this.children = new ArrayList<Tree<T>>();
    }

    public void removeChildAt(int index) throws IndexOutOfBoundsException {
        children.remove(index);
    }

    public Tree<T> getChildAt(int index) throws IndexOutOfBoundsException {
        return children.get(index);
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String toString() {
    	return toString(new String(), 1);
    }
    
    private String toString(String res, int d) {
    	Tree<T> child;
    	int i = 0;
    	res += data.toString();
    	
    	if (children.size() > 0)
    		res += "(\n";
    	
        while (i < children.size()) {
        	for (int j = 0; j < d; j++)
    			res += "|   ";
        	
			child = children.get(i);
			res = child.toString(res, d+1);
			i++;
			if (i < children.size())
				res += "\n";
		}
        
        if (children.size() > 0) {
        	res += "\n";
        	for (int j = 0; j < d-1; j++)
        		res += "|   ";
        	res += ")";
        }
        
		return res;
    }
    
    public boolean equals(Tree<T> node) {
        return node.getData().equals(getData());
    }
    
    @SuppressWarnings("unchecked")
	public Object clone() {
    	Tree<T> t = new Tree<T>();
    	t.numNodes = numNodes;
    	t.depth = depth;
    	t.data = data;
    	for(int i = 0; i  < children.size(); i++) {
    		t.children.add((Tree<T>) children.get(i).clone());
    	}
    	return t;
    }
    
    public void printFile(String path) {
    	if(path == null)
    		path = "Tree" + this.hashCode() + ".txt";
    	File file = new File(path);
    	BufferedWriter buf;
		try {
			buf = new BufferedWriter(new FileWriter(file));
			buf.write(toString());
			buf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
    }

	public Tree<T> getNode(int n) {
		Tree<T> child;
		Tree<T> res = null;
		boolean found = false;
		int i  = 0;
		if (n == 0) {
			res = this;
		} else {
			n--;
			while (!found && i < children.size()) {
				child = children.get(i);
				if (child.getNumberOfNodes() > n) {
					res = child.getNode(n);
					found = (res != null);
				} else {
					n -= child.getNumberOfNodes();
				}
				i++;
			}
		}
		return res;
	}
	
	public void setNode(int n, Tree<T> subtree) {
		setSubtree(n, subtree);
		countNodes();
		setDepth();
	}

	private boolean setSubtree(int n, Tree<T> subtree) {
		Tree<T> child;
		int i  = 0;
		boolean done = false;
		n--;
		numNodes = 1;
		while (!done && i < children.size()) {
			child = children.get(i);
			if (child.getNumberOfNodes() > n) {
				if (n == 0) {
					children.set(i, subtree);
					done = true;
				} else
					done = child.setSubtree(n, subtree);
			} else {
				n -= child.getNumberOfNodes();
			}
			i++;
		}
		return done;
	}
	
	private void countNodes() {
		numNodes = 0;
		if (!hasChildren()) {
			numNodes = 1;
		} else {
			numNodes++;
			for(Tree<T> child : children) {
				child.countNodes();
				numNodes += child.getNumberOfNodes();
			}
		}
	}
	
	private int setDepth() {
		depth = 0;
		if(hasChildren())
			depth++;
		int max = 0;
		for (Tree<T> child: children) {
			max = Math.max(max, child.setDepth());
		}
		depth += max; 
		return depth;
	}
	
	public int getDepth() {
		return depth;
	}
}