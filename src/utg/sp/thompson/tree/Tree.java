package utg.sp.thompson.tree;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

import de.uniwue.smooth.draw.IpeDraw;

/**
 * The class Tree implements a tree. A Tree object consists of a root node. A
 * Tree object knows his leaves.
 *
 * @author jeb
 *
 */
public class Tree implements Arity {
  /**
   * the root of the tree
   */
  public Node root;
  /**
   * the leaves of the tree
   */
  public Vector<Node> leaves = new Vector<Node>();

  /**
   * returns true if the two trees this and t are structurally equal. does not
   * take the values of leaves into account
   *
   * @param t
   *          the tree
   * @return equality
   */
  public boolean equals(Tree t) {
    return root.equals(t.root);
  }

  /**
   * @return a normalized identical structural copy of this
   */
  public Tree copy() {
    Tree tree = new Tree();
    tree.root = root.copy();
    tree.normalize();
    return tree;
  }

  /**
   * computes the node numbers, the depths of the nodes, the parent of each node
   * and the leaves of the tree
   */
  public void normalize() {
    leaves = new Vector<Node>();
    normalize(root, 0, 0);
  }

  private int normalize(Node node, int depth, int number) {
    node.depth = depth;
    if (node.isLeave()) {
      leaves.addElement(node);
    } else {
      for (int i = 0; i < node.children.length; i++) {
        node.children[i].parent = node;
        number = normalize(node.children[i], depth + 1, number);
      }
    }
    node.number = number++;
    return number;
  }

  /**
   * @param size
   *          one plus number of leaves
   * @return a random tree with size + 1 leaves
   */
  public static Tree randomTree(int size) {
    Random random = new Random();
    Tree tree = new Tree();
    tree.root = new Node(size);
    Node node = tree.root;
    for (int i = 1; i <= size; i++) {
      node = tree.root;
      while (true) {
        if (node.isLeave()) {
          node.children = new Node[ARY];
          for (int z = 0; z < ARY; z++) {// create new nodes for each node.
            node.children[z] = new Node(ARY);
          }
          break;

        } else {
          // move to a random child.
          int j = random.nextInt(node.children.length);
          node = node.children[j];
        }
      }
    }
    tree.normalize();
    return tree;
  }

  /**
   * plots the partition associated with this
   *
   * @param file
   *          the name of the output file
   */
  public void plotPartitionIpe(String file) {
    try {
      FileWriter f = new FileWriter(file + ".ipe");
      f.write(IpeDraw.getIpePreamble());
      f.write(IpeDraw.getIpeConf());
      f.write(IpeDraw.drawIpeEdge(10, 10, 610, 10));
      f.write(IpeDraw.drawIpeEdge(10, 8, 10, 12));
      double pos = 10;
      for (Node leave : leaves) {
        pos += Math.pow(2, -leave.depth) * 600;
        f.write(IpeDraw.drawIpeEdge((int) pos, 8, (int) pos, 12));
      }
      f.write(IpeDraw.getIpeEnd());
      f.close();
      Process p = Runtime.getRuntime()
          .exec("ipetoipe -pdf -runlatex " + file + ".ipe " + file + ".pdf");
      p.waitFor();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * @param file
   *          the name of the file
   */
  public void plotTreeDot(String file) {
    try {
      FileWriter f = new FileWriter(file + ".dot");
      f.write("digraph test {\n");
      f.write("    edge [arrowhead=none]\n");
      f.write("    node [shape=point, label=\"\", width=\".05\"" +
          ", height=\".05\"]\n");
      f.write("    { rank=same; ");
      for (Node leave : leaves) {
        f.write("N" + leave.number + "; ");
      }
      f.write("}\n");
      f.write(plotNodesDot(root));
      for (int i = 0; i < leaves.size() - 1; i++) {
        f.write("    N" + leaves.get(i).number + " -> N" +
            leaves.get(i + 1).number + "[style=\"invis\"];\n");
      }
      f.write("}\n");
      f.close();
      Process p = Runtime.getRuntime()
          .exec("dot -Tpdf " + file + ".dot -o " + file + ".pdf");
      p.waitFor();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private String plotNodesDot(Node node) {
    return plotNodesDot(node, node.children.length);
  }

  /**
   * plots the dotes of this node.
   *
   * @param node
   *          the nodes to describe
   * @param numChildren
   *          the number of children of this node.
   * @return a string representation of this node and it's children.
   */
  public String plotNodesDot(Node node, int numChildren) {
    String s = "";
    if (!node.isLeave()) {
      for (int i = 0; i < node.children.length; i++) {
        s += "    N" + node.number + " -> N" + node.children[i].number + "\n";
        s += plotNodesDot(node.children[i], numChildren);
      }
    }
    return s;
  }

  /**
   * @return the leaf exponents of this
   */
  public Vector<Integer> leafExponents() {
    Vector<Integer> exponents = new Vector<Integer>();
    for (Node leaf : leaves) {
      exponents.add(leafExponent(leaf, root));
    }
    return exponents;
  }

  // today : just consider the leefmost child.
  private int leafExponent(Node leaf, Node r) {
    // TODO: verify if the methods leftMostChild and rightMostChild are working
    // correctly.
    int i = 0;
    Node node = leaf;
    if (!node.isLeftMostChild()) {
      return 0;
    }
    while (node.isLeftMostChild()) {
      i++;
      node = node.parent;
    }
    while (node.isRightMostChild()) {
      node = node.parent;
    }
    if (node == r) {
      return i - 1;// exclude root count.
    } else {
      return i;
    }
  }

  @Override
  public String toString() {
    return toString(root, "");
  }

  private String toString(Node n, String indent) {
    String s = indent + n.depth + " " + n.number + "\n";
    if (!n.isLeave()) {
      for (int i = 0; i < n.children.length; i++) {
        s += toString(n.children[i], indent + "  ");
      }
    }
    return s;
  }

  /**
   * computes the superposition of the trees t1 an t2
   *
   * @param t1
   *          the tree t1
   * @param t2
   *          the tree t2
   * @param n1
   *          the actual node of t1
   * @param n2
   *          the actual node of t2
   * @param addedToT1
   *          the list of nodes that have been added to t1
   * @param addedToT2
   *          the list of nodes that have been added to t2
   */
  public static void superpose(Tree t1, Tree t2, Node n1, Node n2,
      Vector<Node> addedToT1, Vector<Node> addedToT2) {
    if (n1.isLeave() && n2.isLeave()) {
    } else if (n1.isLeave()) {
      addedToT1.set(t1.leaves.indexOf(n1), n2.copy());
    } else if (n2.isLeave()) {
      addedToT2.set(t2.leaves.indexOf(n2), n1.copy());
    } else {
      for (int i = 0; i < n2.children.length; i++) {
        // check if i is define for t1.
        if (i < n1.children.length) {
          superpose(t1, t2, n1.children[i], n2.children[i], addedToT1,
              addedToT2);
        }
      }

    }
  }

  /**
   * @param size
   *          the number of carets
   * @return an all right tree with size carets
   */
  public static Tree allRightTree(int size) {
    return allRightGeneralTree(size);
  }

  /**
   * creates an all right random general tree with size carets.
   *
   * @param size
   *          the number of carets
   * @return an all right tree with size carets
   */
  public static Tree allRightGeneralTree(int size) {
    Tree result = new Tree();
    Node node = new Node(ARY);
    result.root = node;
    for (int i = 1; i <= size; i++) {

      for (int j = 0; j < node.children.length; j++) {
        node.children[j] = new Node(ARY);
      }

      if (node.children.length > 0) {
        node = node.children[node.children.length - 1];// point to the last one.
      }
    }
    result.normalize();
    return result;
  }

}
