package de.graphics.uni_konstanz.wordle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

public class Main {

  public static final File LAST_DIR = new File(".lastCsv");

  private final JFrame fenster;

  public Main() {

    fenster = new JFrame("WordleIntern");

    // The Painter Routine
    final WordlePainterSimple wordlePainterSimple = new WordlePainterSimple();

    // The DisplayCanvas
    final Canvas canvas = new Canvas(wordlePainterSimple);

    // create gui panel
    final JPanel guiPanel = new JPanel();
    guiPanel.setLayout(new BoxLayout(guiPanel, BoxLayout.Y_AXIS));

    // -- LOAD CSV
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
          final List<TextItem> loadCSV = InputDataReader.loadCSV(file);
          wordlePainterSimple.setItems(loadCSV);
          canvas.reset(wordlePainterSimple.getBBox());

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

    final JSeparator sep = new JSeparator();
    sep.setMaximumSize(new Dimension(200, 15));
    guiPanel.add(sep);

    // -- LOAD COLOR
    guiPanel.add(new JButton(new AbstractAction("load color..") {

      @Override
      public void actionPerformed(final ActionEvent arg0) {
        final JFileChooser fc = new JFileChooser("./color_schemes");
        fc.setFileFilter(new FileFilter() {

          @Override
          public String getDescription() {
            return "Image Files";
          }

          @Override
          public boolean accept(final File f) {
            final String fName = f.getName();
            if(fName.endsWith("jpg") || fName.endsWith(".gif")
                || fName.endsWith(".png")) return true;
            else return false;
          }
        });
        final int returnVal = fc.showOpenDialog(canvas);

        if(returnVal == JFileChooser.APPROVE_OPTION) {
          final File file = fc.getSelectedFile();
          BufferedImage img = null;
          try {
            img = ImageIO.read(file);
          } catch(final IOException e) {
            System.out.println(e.getLocalizedMessage());
          }
          if(img != null) {
            wordlePainterSimple.setColorscaleImage(img);
          }

        } else {
          System.out.println("nothing selected");
        }

      }
    }));

    /*
     * slider for color adjustment
     */
    final JSlider colorMinSlider = new JSlider();
    colorMinSlider.setValue(0);
    colorMinSlider.setMaximumSize(new Dimension(200, 20));
    final JSlider colorMaxSlider = new JSlider();
    colorMaxSlider.setValue(100);
    colorMaxSlider.setMaximumSize(new Dimension(200, 20));

    colorMinSlider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(final ChangeEvent e) {
        final JSlider source = (JSlider) e.getSource();
        if(!source.getValueIsAdjusting()) {
          if(colorMinSlider.getValue() < colorMaxSlider.getValue()) {
            final float colMin = source.getValue() / 100f;
            wordlePainterSimple.setColorMin(colMin);
            canvas.repaint();
          } else {
            System.out.println("min too big");
          }
        }

      }
    });

    colorMaxSlider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(final ChangeEvent e) {
        final JSlider source = (JSlider) e.getSource();
        if(!source.getValueIsAdjusting()) {
          if(colorMinSlider.getValue() < colorMaxSlider.getValue()) {
            final float colMax = source.getValue() / 100f;
            wordlePainterSimple.setColorMax(colMax);
            canvas.repaint();
          } else {
            System.out.println("max to small");
          }
        }

      }
    });

    guiPanel.add(new JLabel("min color"));
    guiPanel.add(colorMinSlider);
    guiPanel.add(new JLabel("max color"));
    guiPanel.add(colorMaxSlider);

    /*
     * END : slider
     */

    guiPanel.add(new JButton(new AbstractAction("set background") {

      @Override
      public void actionPerformed(final ActionEvent arg0) {
        final JColorChooser jColorChooser = new JColorChooser();
        final Color newBG =
            JColorChooser.showDialog(canvas, "Choose Background Color",
                canvas.getBackground());
        if(newBG != null) {
          canvas.setBackground(newBG);

          canvas.repaint();
        }

      }
    }));

    final JSeparator sep2 = new JSeparator();
    sep2.setMaximumSize(new Dimension(200, 15));
    guiPanel.add(sep2);

    /*
     * powerField
     */

    final NumberFormat nForm = NumberFormat.getNumberInstance(Locale.ENGLISH);
    final JFormattedTextField amountField = new JFormattedTextField(nForm);
    amountField.setValue(1.001);
    amountField.setMaximumSize(new Dimension(200, 20));
    amountField.addPropertyChangeListener("value",

        new PropertyChangeListener() {

          @Override
          public void propertyChange(final PropertyChangeEvent ev) {
            final JFormattedTextField tf = (JFormattedTextField) ev.getSource();
            final float powValue = ((Number) tf.getValue()).floatValue();
            System.out.println(powValue);
            wordlePainterSimple.setPower(powValue);
            canvas.repaint();
          }
        });

    guiPanel.add(new JLabel("weight^x; x="));
    guiPanel.add(amountField);
    /*
     * Find Times font and create combo box
     */

    final String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment()
        .getAvailableFontFamilyNames();
    int timesIndex = 0;
    for(final String string : fonts) {
      if(string.startsWith("Times")) {
        break;
      }
      timesIndex++;
    }
    if(timesIndex > fonts.length) {
      timesIndex = 0;
    }
    final JComboBox fontList = new JComboBox(fonts);
    fontList.setSelectedIndex(timesIndex);
    fontList.setMaximumSize(new Dimension(200, 30));
    fontList.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(final ActionEvent arg0) {
        wordlePainterSimple.setFontName((String) fontList.getSelectedItem());
      }
    });
    guiPanel.add(fontList);

    /*
     * -- end combo box
     */

    /*
     * rotationmode
     */
    final JComboBox rotBox =
        new JComboBox(new String[] { "right", "right ad left", "no"});
    rotBox.setSelectedIndex(0);
    rotBox.setMaximumSize(new Dimension(200, 30));
    rotBox.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(final ActionEvent ev) {
        final int selectedIndex = rotBox.getSelectedIndex();
        if(selectedIndex == 0) {
          wordlePainterSimple.setRotationMode(WordleLayouter.RotationMode.ROT_90_DEG_RIGHT);
        }
        if(selectedIndex == 1) {
          wordlePainterSimple.setRotationMode(WordleLayouter.RotationMode.ROT_RANDOM);
        }
        if(selectedIndex == 2) {
          wordlePainterSimple.setRotationMode(WordleLayouter.RotationMode.NO_ROTATION);
        }
      }
    });
    guiPanel.add(rotBox);
    // ===========

    // layout button
    guiPanel.add(new JButton(new AbstractAction("layout!") {

      @Override
      public void actionPerformed(final ActionEvent e) {
        wordlePainterSimple.updateAllStuff();
        canvas.reset(wordlePainterSimple.getBBox());

      }
    }));

    final JSeparator sep3 = new JSeparator();
    sep3.setMaximumSize(new Dimension(200, 15));
    guiPanel.add(sep3);

    /*
     * --- > END CONFIG
     */

    guiPanel.add(new JButton(new AbstractAction("zoom all") {

      private static final long serialVersionUID = 2154458079066313145L;

      @Override
      public void actionPerformed(final ActionEvent e) {
        canvas.reset(wordlePainterSimple.getBBox());
      }

    }));
    guiPanel.add(new JButton(new AbstractAction("Save SVG...") {

      private static final long serialVersionUID = -9119742082960796042L;

      @Override
      public void actionPerformed(final ActionEvent ae) {
        final BatikSVG svg = new BatikSVG();
        final File file = svg.saveSVGDialog(canvas);
        if(file == null) return;
        final Graphics2D g = svg.getGraphics("WordleIntern");
        final Color back = canvas.getBackground();
        canvas.setBackground(null);
        canvas.paint(g);
        canvas.setBackground(back);
        g.dispose();
        try {
          svg.write(file, g);
        } catch(final IOException e) {
          e.printStackTrace();
        }
      }

    }));
    guiPanel.setMinimumSize(new Dimension(200, 0));

    canvas.setBackground(Color.WHITE);
    canvas.reset();

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

  }

}
