package utg.sp.thompson.treepair;

import utg.sp.thompson.utils.Message;

/**
 * A test class for relations between generators
 *
 * @author jeb
 *
 */
public class TestRelations { 
  public static Message m = new Message("TreePairTest");

  /**
   * @param args
   */
  public static void main(String[] args) {
    m.message("> testing relations");
    m.i();
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        boolean b = TreePair.generator(i).multiply(TreePair.generator(j))
            .equals(TreePair.generator(j).multiply(TreePair.generator(i + 1)));
        System.out.println(
            "f" + i + "*f" + j + " = f" + j + "*f" + (i + 1) + " : " + b);
      }
    }
    m.d();
  }
}
