package de.graphics.uni_konstanz.wordle;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Main {

  private static final File LAST_DIR = new File(".lastCsv");

  private final JFrame fenster;

  public Main() {
    fenster = new JFrame("Fenster");

    final WordlePainterSimple wordlePainterSimple = new WordlePainterSimple();

    final Canvas canvas = new Canvas(wordlePainterSimple);

    final JPanel guiPanel = new JPanel();
    guiPanel.setLayout(new BoxLayout(guiPanel, BoxLayout.Y_AXIS));
    guiPanel.add(new JButton(new AbstractAction("load csv.. ") {

      private static final long serialVersionUID = -1332014568175053524L;

      @Override
      public void actionPerformed(final ActionEvent ae) {
        File start = new File(".");
        if(LAST_DIR.exists()) {
          try {
            final Scanner s = new Scanner(LAST_DIR, "UTF-8");
            if(s.hasNextLine()) {
              start = new File(s.nextLine().trim());
            }
            s.close();
          } catch(final IOException e) {
            // no worries
          }
        }
        final JFileChooser fc = new JFileChooser(start);

        final int returnVal = fc.showOpenDialog(canvas);

        if(returnVal == JFileChooser.APPROVE_OPTION) {
          final File file = fc.getSelectedFile();
          final List<TextItem> loadCSV = InputDataReader.loadCSV(file, ",");
          wordlePainterSimple.setItems(loadCSV);
          canvas.repaint();

          final File par = file.getParentFile();
          try {
            final PrintWriter pw = new PrintWriter(LAST_DIR, "UTF-8");
            pw.println(par.toString());
            pw.close();
          } catch(final IOException e) {
            // no worries
          }

          System.out.println(loadCSV);
        } else {
          System.out.println("nothing selected");
        }

      }
    }));
    guiPanel.add(new JButton("load color.."));
    guiPanel.add(new JButton(new AbstractAction("reset view") {

      private static final long serialVersionUID = 2154458079066313145L;

      @Override
      public void actionPerformed(final ActionEvent e) {
        canvas.reset();
      }

    }));
    guiPanel.add(new JButton(new AbstractAction("Save SVG...") {

      private static final long serialVersionUID = -9119742082960796042L;

      @Override
      public void actionPerformed(final ActionEvent ae) {
        final BatikSVG svg = new BatikSVG();
        final File file = svg.saveSVGDialog(canvas);
        if(file == null) {
          return;
        }
        final Graphics2D g = svg.getGraphics("WordleIntern");
        canvas.paint(g);
        g.dispose();
        try {
          svg.write(file, g);
        } catch(final IOException e) {
          e.printStackTrace();
        }
      }

    }));
    guiPanel.setMinimumSize(new Dimension(200, 0));

    fenster.setLayout(new BorderLayout());
    fenster.add(guiPanel, BorderLayout.WEST);
    fenster.add(canvas, BorderLayout.CENTER);
    fenster.pack();

    canvas.reset();

    fenster.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    fenster.setLocationRelativeTo(null);
  }

  public static void main(final String[] args) {

    final Main main = new Main();
    main.fenster.setVisible(true);

    final String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment()
        .getAvailableFontFamilyNames();

    for(int i = 0; i < fonts.length; i++) {
      System.out.println(fonts[i]);
    }
  }

}
