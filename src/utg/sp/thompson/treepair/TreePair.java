package utg.sp.thompson.treepair;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

import de.uniwue.smooth.draw.IpeDraw;
import utg.sp.thompson.presentation.Element;
import utg.sp.thompson.presentation.Generator;
import utg.sp.thompson.tree.Arity;
import utg.sp.thompson.tree.Node;
import utg.sp.thompson.tree.Tree;

/**
 * This class implements an element of the Thompson group F represented as a
 * pair of trees
 *
 * @author jeb
 *
 */
public class TreePair implements Arity {
  /**
   * the domain tree
   */
  public Tree domain = null;
  /**
   * the codomain tree
   */
  public Tree codomain = null;

  /**
   * trivial constructor
   */
  public TreePair() {
    super();
    this.domain = null;
    this.codomain = null;
  }

  /**
   * constructor
   *
   * @param domain
   *          the domain
   * @param codomain
   *          the codomain
   */
  public TreePair(Tree domain, Tree codomain) {
    super();
    if (domain.leaves.size() != codomain.leaves.size()) {
      throw new RuntimeException("incompatible number of leaves\n d.size = " +
          domain.leaves.size() + " : c.size = " + codomain.leaves.size());
    }
    this.domain = domain;
    this.codomain = codomain;
  }

  /**
   * returns true if the two trees pairs this and p are structurally equal. does
   * not take the values of leaves into account, but uses reduction
   *
   * @param p
   *          the tree pair
   * @return equality
   */
  public boolean equals(TreePair p) {
    TreePair p1 = this.copy();
    p1.reduce();
    TreePair p2 = p.copy();
    p2.reduce();
    return p1.domain.equals(p2.domain) && p1.codomain.equals(p2.codomain);
  }

  /**
   * @return a normalized copy of the tree
   */
  public TreePair copy() {
    return new TreePair(domain.copy(), codomain.copy());
  }

  /**
   * @param size
   *          the number of leaves + one
   * @return a random tree pair
   */
  public static TreePair randomTreePair(int size) {
    return new TreePair(Tree.randomTree(size), Tree.randomTree(size));
  }

  /**
   * plots the graph of the piecewise linear function associated with the tree
   * pair this
   *
   * @param file
   *          the name of the file
   */
  public void plotGraphIpe(String file) {
    int xp[] = new int[domain.leaves.size() + 1];
    int yp[] = new int[codomain.leaves.size() + 1];
    xp[0] = 10;
    yp[0] = 10;
    String T = IpeDraw.drawIpeMark(xp[1], yp[1]);
    for (int i = 0; i < domain.leaves.size(); i++) {
      xp[i + 1] =
          (int) (xp[i] + 600.0 / Math.pow(2.0, domain.leaves.get(i).depth));
      yp[i + 1] =
          (int) (yp[i] + 600.0 / Math.pow(2.0, codomain.leaves.get(i).depth));
      T += IpeDraw.drawIpeMark(xp[i + 1], yp[i + 1]);
      T += IpeDraw.drawIpeMark(xp[i + 1], 10);
      T += IpeDraw.drawIpeMark(10, yp[i + 1]);
    }
    xp[domain.leaves.size()] = 610;
    yp[domain.leaves.size()] = 610;
    T += IpeDraw.drawIpeMark(610, 610);
    T += IpeDraw.drawIpeMark(610, 10);
    T += IpeDraw.drawIpeMark(10, 610);
    String S = IpeDraw.getIpePreamble() + IpeDraw.getIpeConf() + T;
    S += IpeDraw.drawIpeEdge(10, 10, 610, 10);
    S += IpeDraw.drawIpeEdge(610, 10, 610, 610);
    S += IpeDraw.drawIpeEdge(610, 610, 10, 610);
    S += IpeDraw.drawIpeEdge(10, 610, 10, 10);
    S += IpeDraw.drawIpePath(xp, yp) + IpeDraw.getIpeEnd();
    try {
      FileWriter f = new FileWriter(file + ".ipe");
      f.write(S);
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
   * plots the two trees of the tree pair this
   *
   * @param file
   *          the name of the file
   */
  public void plotTreePairDot(String file) {
    try {
      FileWriter f = new FileWriter(file + ".dot");
      f.write("digraph test {\n");
      f.write("    edge [arrowhead=none]\n");
      f.write("    node [shape=point, label=\"\", width=\".05\"" +
          ", height=\".05\"]\n");

      f.write("    { rank=same; ");
      for (Node leave : domain.leaves) {
        f.write("D" + leave.number + "; ");
      }
      for (Node leave : codomain.leaves) {
        f.write("C" + leave.number + "; ");
      }
      f.write("}\n");

      f.write(plotNodesDot(domain.root, "D"));
      for (int i = 0; i < domain.leaves.size() - 1; i++) {
        f.write("    D" + domain.leaves.get(i).number + " -> D" +
            domain.leaves.get(i + 1).number + "[style=\"invis\"];\n");
      }
      f.write(plotNodesDot(codomain.root, "C"));
      for (int i = 0; i < codomain.leaves.size() - 1; i++) {
        f.write("    C" + codomain.leaves.get(i).number + " -> C" +
            codomain.leaves.get(i + 1).number + "[style=\"invis\"];\n");
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

  private String plotNodesDot(Node node, String name) {
    String s = "";
    if (!node.isLeave()) {
      for (int i = 0; i < node.children.length; i++) {
        s += "    " + name + node.number + " -> " + name +
            node.children[i].number + "\n";
        s += plotNodesDot(node.children[i], name);
      }
    }
    return s;
  }

  public double value(double x) {
    // TODO Assignment 4
    // TODO: Prof Biollat , will be the one to solve it . please remind him on
    // piazza later.
    return 0;
  }

  /**
   * computes the product this * p
   *
   * @param p
   *          a tree pair
   * @return the product
   */
  public TreePair multiply(TreePair p) {
    TreePair p1 = this.copy();
    TreePair p2 = p.copy();
    Vector<Node> addedToC = new Vector<Node>();
    Vector<Node> addedToD = new Vector<Node>();
    for (int i = 0; i < p1.codomain.leaves.size(); i++) {
      addedToC.add(null);
    }
    for (int i = 0; i < p2.domain.leaves.size(); i++) {
      addedToD.add(null);
    }
    Tree.superpose(p1.codomain, p2.domain, p1.codomain.root, p2.domain.root,
        addedToC, addedToD);
    Tree d = p1.domain.copy();
    for (int i = 0; i < d.leaves.size(); i++) {
      Node n = addedToC.get(i);
      if (n != null) {
        // set all the children of d.
        d.leaves.get(i).children = n.children;
      }
    }
    Tree c = p2.codomain.copy();
    for (int i = 0; i < c.leaves.size(); i++) {
      Node n = addedToD.get(i);
      if (n != null) {
        c.leaves.get(i).children = n.children;
      }
    }
    d.normalize();
    c.normalize();
    TreePair product = new TreePair(d, c);
    return product;
  }

  /**
   * removes unnecessary carets pairs from the tree pair
   */
  public void reduce() {
    boolean modified = true;
    while (modified) {
      modified = false;
      for (int i = 0; i < domain.leaves.size() - 1; i++) {
        if (sameLeavesParent(domain, codomain)) {
          // reset / free the children.
          for (int z = 0; z < domain.leaves.size(); z++) {
            domain.leaves.get(i).parent.children[z] = null;
            codomain.leaves.get(i).parent.children[z] = null;
          }
          domain.normalize();
          codomain.normalize();
          modified = true;
          break;
        }
      }
    }
  }

  /**
   * Determines if all the leaves of the trees, share the same parent. this
   * function assumes that t1.size() = t2.size()
   *
   * @param t1
   *          the first tree's leaves.
   * @param t2
   *          the second tree's leaves.
   * @return ` true if all the leaves of t1 and t2 share the same parent.
   */
  private boolean sameLeavesParent(Tree t1, Tree t2) {
    for (int i = 0; i < t1.leaves.size() - 1; i++) {
      if (t1.leaves.get(i).parent != t1.leaves.get(i + 1).parent &&
          t2.leaves.get(i).parent != t2.leaves.get(i + 1).parent) {
        return false;
      }
    }
    return true;
  }

  /**
   * computes the generators f_{n} of the Thompson group F
   *
   * @param n
   *          the generator number
   * @return the generator
   */
  public static TreePair generator(int n) {
    return generator(n / (ARI-1), n % (ARI-1));
  }
  /**
   * computes the generators f_{n} of the Thompson group F
   *
   * @param n
   *          the depth of the tree
   * @param m
   *          the leave number to attach
   * @return the generator
   */
  public static TreePair generator(int n, int m) {
    Tree c = Tree.allRightTree(n + 2);
    Tree d = Tree.allRightTree(n + 1);

    Node node = d.root;
    for (int i = 1; i < n + 1; i++) {
      node = node.rightMostChild();
    }

    for (int i = 0; i < ARY; i++) {
      node.children[m].children[i] = new Node(ARY);
    }

    d.normalize();
    return new TreePair(d, c);
  }

  /**
   * computes the identity element consisting of the pair of two one node trees
   *
   * @return the identity tree pair
   */
  public static TreePair id() {
    Tree d = new Tree();
    d.root = new Node(0);
    d.normalize();
    Tree c = new Tree();
    c.root = new Node(0);
    c.normalize();
    return new TreePair(d, c);
  }

  /**
   * computes the powers of this
   *
   * @param i
   * @return this^i
   */
  public TreePair power(int i) {
    if (i == 0) {
      return id();
    } else if (i > 0) {
      TreePair result = this.copy();
      for (int j = 1; j < i; j++) {
        result = result.multiply(this);
      }
      return result;
    } else {
      TreePair result = this.copy().inverse();
      for (int j = 1; j < i; j++) {
        result = result.multiply(this);
      }
      return result;
    }
  }

  public Element toElement() {
    Vector<Integer> dExp = domain.leafExponents();
    Vector<Integer> cExp = codomain.leafExponents();
    Vector<Generator> word = new Vector<Generator>();
    for (int i = 0; i < dExp.size(); i++) {
      int j = dExp.get(i);
      if (j != 0) {
        word.add(new Generator(i, j));
      }
    }
    for (int i = cExp.size() - 1; i >= 0; i--) {
      int j = cExp.get(i);
      if (j != 0) {
        word.add(new Generator(i, -j));
      }
    }
    return new Element(word);
  }

  /**
   * computes the multiplicative inverse of this
   *
   * @return the inverse
   */
  public TreePair inverse() {
    return new TreePair(codomain, domain);
  }

  @Override
  public String toString() {
    return domain + "\n" + codomain;
  }
}
