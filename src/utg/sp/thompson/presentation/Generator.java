package utg.sp.thompson.presentation;

/**
 * This class implements the generators fi of the Thompson group F
 *
 * @author jeb
 *
 */
public class Generator {
  /**
   * the generator index
   */
  public int index;
  /**
   * the exponent of the generator
   */
  public int exponent;

  /**
   * simple constructor
   *
   * @param index
   *          the index
   * @param exponent
   *          the exponent
   */
  public Generator(int index, int exponent) {
    super();
    this.index = index;
    this.exponent = exponent;
  }

  @Override
  public String toString() {
    String s;
    if (exponent == 0) {
      s = "id";
    } else if (exponent == 1) {
      s = "f" + index;
    } else {
      s = "f" + index + "^{" + exponent + "}";
    }
    return s;
  }
}
