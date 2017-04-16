package utg.sp.thompson.tree;

/**
 * The class Node implements a node of a tree
 *
 * @author jeb
 *
 */
public class Node {
  /**
   * the node number
   */
  public int number;
  /**
   * the depth of the node in the tree
   */
  public int depth;

  /**
   * the list of children of this node.
   */
  public Node[] children;

  /**
   * the parent of the node
   */
  public Node parent;

  /**
   * Creates a Node with the specified number of children.
   *
   * @param numChildren
   *          The number of children of this node.
   */
  public Node(int numChildren) {
    children = new Node[numChildren];
  }

  /**
   * @return true if this is a leave
   */
  public boolean isLeave() {
    for (int i = 0; i < children.length; i++) {
      if (children[i] != null) {
        return false;
      }
    }
    return true;
  }

  /**
   * Gets the first child of this node.
   *
   * @return the left most child of this node.
   */
  public Node leftMostChild() {
    return children[0];
  }

  /**
   * Determines if this node is the left most child of it's parent node.
   *
   * @return true if this , is the left most child of it's parent.
   */
  public boolean isLeftMostChild() {
    return this.parent.children[0] == this;
  }

  /**
   * Determines if this node is the right most child of it's parent node.
   *
   * @return true if this , is the right most child of it's parent.
   */
  public boolean isRightMostChild() {
    return this.parent.children[parent.children.length - 1] == this;
  }

  /**
   * Gets the last child of this
   *
   * @return the right most child of this
   */
  public Node rightMostChild() {
    return children[children.length - 1];
  }

  /**
   * returns true if the two trees this and p are structurally equal. does not
   * take the values of number, depth and parent into account
   *
   * @param n
   *          the node
   * @return equality
   */
  public boolean equals(Node n) {
    if (this.isLeave() && n.isLeave()) {
      return true;
    } else if (this.isLeave()) {
      return false;
    } else if (n.isLeave()) {
      return false;
    } else {
      for (int i = 0; i < children.length; i++) {
        if (!children[i].equals(n.children[i])) {
          return false;
        }
      }
      return true;// all nodes are the same.
    }
  }

  /**
   * @return a structural identical copy of the tree with root node
   */
  public Node copy() {
    if (this.isLeave()) {
      return new Node(0);
    } else {
      Node node = new Node(children.length);
      for (int i = 0; i < children.length; i++) {
        node.children[i] = this.children[i].copy();
      }
      return node;
    }
  }
}
