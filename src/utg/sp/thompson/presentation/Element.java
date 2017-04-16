package utg.sp.thompson.presentation;

import java.util.Scanner;
import java.util.Vector;
import java.util.regex.MatchResult;

import utg.sp.thompson.treepair.TreePair;

/**
 * This class implements the elements of the infinite presentation of the
 * Thompson group F
 *
 * @author jeb
 *
 */
public class Element {

  /**
   * an element is a finite product of power of the generators fi
   */
  public Vector<Generator> word = new Vector<Generator>();

  /**
   * simple constructor returning an element from a string representation of a
   * finite product of power of the generators fi
   *
   * @param input
   *          the string
   */
  public Element(String input) {
    super();
    this.word = parse(input);
  }

  /**
   * simple constructor returning an element from a vector of generators
   *
   * @param word
   *          the vector of generators
   */
  public Element(Vector<Generator> word) {
    super();
    this.word = word;
  }

  /**
   * converts an element into an equivalent tree pair
   *
   * @return the tree pair
   */
  public TreePair toTreePair() {
    TreePair result = TreePair.id();
    if (word.size() > 0) {
      for (Generator g : word) {
        result = result.multiply(TreePair.generator(g.index).power(g.exponent));
      }
    }
    result.reduce();
    return result;
  }

  /**
   * converts a string representation of a finite product of power of the
   * generators fi to an element
   *
   * @param input
   *          a string representation of a finite product of power of the
   *          generators fi
   * @return the element
   */
  public Vector<Generator> parse(String input) {
    Vector<Generator> word = new Vector<Generator>();
    Scanner scanner = new Scanner(input);
    scanner.useDelimiter("\\*");
    while (scanner.hasNext()) {
      int i = 0;
      int j = 1;
      scanner.findInLine("f(\\d+)(\\^\\{(\\-)?(\\d+)\\})?");
      MatchResult result = scanner.match();
      i = Integer.parseInt(result.group(1));
      if (result.group(4) != null) {
        j = Integer.parseInt(result.group(4));
        if (result.group(3) != null) {
          j = -j;
        }
      }
      if (j != 0) {
        word.add(new Generator(i, j));
      }
    }
    return word;
  }

  @Override
  public String toString() {
    String s = "";
    if (word.size() == 0) {
      s = "id";
    } else {
      for (int i = 0; i < word.size(); i++) {
        Generator g = word.get(i);
        s += g;
        if (i < word.size() - 1) {
          s += "*";
        }
      }
    }
    return s;
  }
}
