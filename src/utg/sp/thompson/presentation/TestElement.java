package utg.sp.thompson.presentation;

import utg.sp.thompson.treepair.TreePair;
import utg.sp.thompson.utils.Message;

/**
 * A test class for the methods of the Element class
 *
 * @author jeb
 *
 */
public class TestElement {
  private static Message m = new Message("TestElement");

  /**
   * @param args
   */
  public static void main(String[] args) {
    m.message("> testing element to tree pair");
    m.i();
    m.message("> the element");
    Element e = new Element("f4*f1");
    System.out.println(e);
    m.message("> building equivalent tree pair");
    TreePair result = e.toTreePair();
    result.plotTreePairDot("element");
    m.message("> the corresponding reduced element");
    Element e1 = result.toElement();
    System.out.println(e1);
    m.d();

    m.message("> testing tree pair to element");
    m.i();
    m.message("> generating random tree pair");
    TreePair t = TreePair.randomTreePair(4);
    m.message("> the element");
    e = t.toElement();
    System.out.println(e);
    t.reduce();
    // System.out.println(t);
    m.message("> the reduced element");
    e = t.toElement();
    System.out.println(e);
    m.d();
  }
}
