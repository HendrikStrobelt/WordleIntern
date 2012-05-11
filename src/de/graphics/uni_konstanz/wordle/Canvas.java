package de.graphics.uni_konstanz.wordle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class Canvas extends JComponent {

  private static final long serialVersionUID = 5148536262867772166L;

  private final WordlePainter painter;

  public Canvas(final WordlePainter painter) {
    setPreferredSize(new Dimension(800, 600));
    this.painter = painter;
  }

  @Override
  public void paint(final Graphics g) {
    final Graphics2D g2 = (Graphics2D) g.create();
    g2.setColor(Color.WHITE);
    g2.fill(getVisibleRect());
    painter.paint(g2);
    g2.dispose();
  }

}
