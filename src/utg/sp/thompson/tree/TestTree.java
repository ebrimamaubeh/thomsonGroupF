package utg.sp.thompson.tree;

import java.util.Vector;

import utg.sp.thompson.utils.Message;

/**
 * A test class for the methods of the Tree class
 *
 * @author jeb
 *
 */
public class TestTree {
  private static Message m = new Message("TestTree");

  /**
   * @param args
   */
  public static void main(String[] args) {
    int size = 4;
    m.message("> testing plots");
    m.i();
    m.message("partition plot");
    Tree t1 = Tree.randomTree(size);
    Tree t2 = Tree.randomTree(size);
    System.out.println(t2);
    t2.plotPartitionIpe("testP2");
    m.message("tree plot");
    t1.plotPartitionIpe("testP1");
    t2.plotTreeDot("testT2");
    m.d();

    m.message("> testing copy and normalize");
    Tree t3 = t1.copy();
    t3.normalize();
    System.out.println(t3);

    m.message("> testing superpose");
    m.i();
    m.message("> first tree");
    size = 4;
    t1 = Tree.randomTree(size);
    System.out.println(t1);
    m.message("> second tree");
    t2 = Tree.randomTree(size);
    System.out.println(t2);
    m.message("> superposition");
    Node n1 = t1.root;
    Node n2 = t2.root;
    Vector<Node> addedToT1 = new Vector<Node>();
    for (int i = 0; i < t1.leaves.size(); i++) {
      addedToT1.add(null);
    }
    Vector<Node> addedToT2 = new Vector<Node>();
    for (int i = 0; i < t2.leaves.size(); i++) {
      addedToT2.add(null);
    }
    Tree.superpose(t1, t2, n1, n2, addedToT1, addedToT2);
    m.d();

    m.message("> testing all right tree");
    Tree t = Tree.allRightTree(3);
    t.plotTreeDot("allRight");
    size = 7;
    // t = Tree.randomTree(size, ary);

    /*
     * m.message("> testing leaf exponents"); Vector<Integer> exponents =
     * t.leafExponents(); for (Integer i : exponents) { System.out.print(i + " "
     * ); } System.out.println(); t.plotTreeDot("exponents");
     */

    /*
     * m.message("> testing carets plot"); t.plotCaretsDot("carets");
     */
  }
}