package utg.sp.thompson.treepair;

import utg.sp.thompson.tree.Tree;
import utg.sp.thompson.utils.Message;
 
/**
 * A test class for the methods of the TreePair class
 *
 * @author jeb
 *
 */
public class TestTreePair {
  public static Message m = new Message("TreePairTest");

  /**
   * @param args
   */
  public static void main(String[] args) {
    int size = 2;
    m.message("> testing plots");
    TreePair p = TreePair.randomTreePair(size);
    m.i();
    m.message("> partition graph");
    p.plotGraphIpe("pairipe");
    m.message("> tree");
    p.plotTreePairDot("pairdot");
    m.d();

    m.message("> testing multiplication");
    m.i();
    m.message("> first tree pair p1");
    TreePair p1 = TreePair.randomTreePair(size);
    p1.plotTreePairDot("p1");
    m.message("> second tree pair p2");
    TreePair p2 = TreePair.randomTreePair(size);
    p2.plotTreePairDot("p2");
    m.message("> product p1 * p2");
    TreePair product = p1.multiply(p2);
    product.plotTreePairDot("product");
    m.message("> multiplication with inverse");
    TreePair id = p1.multiply(p1.inverse());
    id.plotTreePairDot("id");
    m.d();

    m.message("> testing reduction");
    m.i();
    m.message("> unreduced identity");
    Tree t = Tree.randomTree(size);
    p = new TreePair(t, t);
    p.plotTreePairDot("T");
    m.message("> reduced identity");
    p.reduce();
    p.plotTreePairDot("TReduced");
    m.d();

    m.message("> testing generators");
    m.i();
    m.message("> f10");

    TreePair f10 = TreePair.generator(10);
    f10.plotTreePairDot("f10");
    m.d();
  }
}
