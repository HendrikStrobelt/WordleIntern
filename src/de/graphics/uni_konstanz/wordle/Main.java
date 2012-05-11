package de.graphics.uni_konstanz.wordle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Main {

  private final JFrame fenster;

  public Main() {
    fenster = new JFrame("Fenster");

    final Canvas canvas = new Canvas(new WordlePainter() {

      @Override
      public void paint(final Graphics2D g) {
        g.setColor(Color.RED);
        g.fill(new Rectangle2D.Double(0, 0, 100, 100));
      }

    });

    final JPanel guiPanel = new JPanel();
    guiPanel.setLayout(new BoxLayout(guiPanel, BoxLayout.Y_AXIS));
    guiPanel.add(new JButton(new AbstractAction("load csv.. ") {

      private static final long serialVersionUID = -1332014568175053524L;

      @Override
      public void actionPerformed(final ActionEvent e) {
        final JFileChooser fc = new JFileChooser();

        final int returnVal = fc.showOpenDialog(guiPanel);

        if(returnVal == JFileChooser.APPROVE_OPTION) {
          final File file = fc.getSelectedFile();
          final List<TextItem> loadCSV = InputDataReader.loadCSV(file, ",");
          System.out.println(loadCSV);
        } else {
          System.out.println("nothing selected");
        }

      }
    }));
    guiPanel.add(new JButton("load color.."));
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
