package de.graphics.uni_konstanz.wordle;

import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

/**
 * Writes graphics operations in an SVG writer. This class needs Apache Batik.
 * 
 * @author Joschi <josua.krause@googlemail.com>
 */
public class BatikSVG {

  /**
   * The currently open graphics objects to write the contents to the output
   * stream.
   */
  private final Map<Graphics2D, SVGGraphics2D> openGfx =
      new IdentityHashMap<Graphics2D, SVGGraphics2D>();

  public Graphics2D getGraphics(final String name) {
    final DOMImplementation domImpl =
        GenericDOMImplementation.getDOMImplementation();
    final String svgNS = "http://www.w3.org/2000/svg";
    final Document document = domImpl.createDocument(svgNS, "svg", null);
    final SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(document);
    ctx.setEmbeddedFontsOn(true);
    ctx.setComment("generated by " + name);
    final SVGGraphics2D g = new SVGGraphics2D(ctx, true);
    openGfx.put(g, g);
    return g;
  }

  public void write(final Writer out, final Graphics2D g) throws IOException {
    final SVGGraphics2D gfx = openGfx.remove(g);
    if(gfx == null) throw new IllegalArgumentException(
        "invalid graphics object");
    gfx.stream(out, true);
  }

  public static final String UTF8 = "UTF-8";

  public static final File HOME = new File(System.getProperty("user.home"));

  private static final File LAST_DIR = new File(".lastSave");

  public File saveSVGDialog(final JComponent parent) {
    File start = HOME;
    if(LAST_DIR.exists()) {
      try {
        final Scanner s = new Scanner(LAST_DIR, UTF8);
        if(s.hasNextLine()) {
          start = new File(s.nextLine().trim());
        }
        s.close();
      } catch(final IOException e) {
        // no worries
      }
    }
    final JFileChooser choose = new JFileChooser(start);
    choose.setFileFilter(new FileNameExtensionFilter("Vector image (*.svg)",
        "svg"));
    final boolean approved =
        choose.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION;
    final File res = approved ? choose.getSelectedFile() : null;
    if(res == null) return null;
    final File par = res.getParentFile();
    final String name = res.getName();
    try {
      final PrintWriter pw = new PrintWriter(LAST_DIR, UTF8);
      pw.println(par.toString());
      pw.close();
    } catch(final IOException e) {
      // no worries
    }
    if(!name.contains(".")) return new File(par, name + ".svg");
    return res;
  }

  public void write(final File file, final Graphics2D g) throws IOException {
    write(new PrintWriter(file, UTF8), g);
  }

}
