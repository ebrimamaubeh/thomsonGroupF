package de.uniwue.smooth.draw;

import java.io.FileWriter;
import java.io.IOException;

public class IpeDrawTest {

  public static void main(String[] args) {
    try {
      FileWriter f = new FileWriter("test.ipe");
      f.write(IpeDraw.getIpePreamble());
      f.write(IpeDraw.getIpeConf());
      f.write(IpeDraw.drawIpeBox(10, 10, 610, 610));
      f.write(IpeDraw.getIpeEnd());
      f.close();
      Process p = Runtime.getRuntime()
          .exec("ipetoipe -pdf -runlatex test.ipe test.pdf");
      p.waitFor();
      System.out.println("done.");
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

  }

}
