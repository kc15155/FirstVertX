package com.howtodoinjava.demo.jsonsimple;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TreeNode implements Iterable<TreeNode> {

	  private String value;
	  private TreeNode parent;
	  private Set<TreeNode> children;

	  public TreeNode() {
	    children = new HashSet<TreeNode>();
	  }
	  
	  public TreeNode(String value, TreeNode parent)
	  {
		  this.value=value;
		  this.parent=parent;
		  children = new HashSet<TreeNode>();
	  }

	  public boolean addChild(TreeNode n) {
	    return children.add(n);
	  }

	  public boolean removeChild(TreeNode n) {
	    return children.remove(n);
	  }
	  
	  public String getValue()
	  {
		  return this.value;
	  }
	  
	  public TreeNode getParent()
	  {
		  return this.parent;
	  }
	  
	  public Set<TreeNode> getChildren()
	  {
		  return this.children;
	  }

	  public Iterator<TreeNode> iterator() {
	    return children.iterator();
	  }
	  
}