package de.graphics.uni_konstanz.wordle;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WordlePainterSimple implements WordlePainter {

  List<TextItem> items = new ArrayList<TextItem>();
  List<Shape> shapes = new ArrayList<Shape>();
  List<Color> colors = new ArrayList<Color>();

  float colorMin = 0;
  float colorMax = 1;
  float power = 1;
  int minFont = 4;
  int maxFont = 12;

  Color bgColor;
  WordleLayouter.RotationMode rotationMode =
      WordleLayouter.RotationMode.ROT_90_DEG_RIGHT;

  FontManager fm = new FontManager(minFont, maxFont, "Times");
  String fontName = "Times";
  BufferedImage colorscaleImage = null;

  public WordlePainterSimple() {
    bgColor = Color.white;
  }

  public String getFontName() {
    return fontName;
  }

  public void setFontName(final String fontName) {
    this.fontName = fontName;
    fm = new FontManager(minFont, maxFont, fontName);

  }

  public int getMinFont() {
    return minFont;
  }

  public void setMinFont(final int minFont) {
    this.minFont = minFont;
  }

  public int getMaxFont() {
    return maxFont;
  }

  public void setMaxFont(final int maxFont) {
    this.maxFont = maxFont;
  }

  public float getPower() {
    return power;
  }

  public void setPower(final float power) {
    this.power = power;
  }

  public WordleLayouter.RotationMode getRotationMode() {
    return rotationMode;
  }

  public void setRotationMode(final WordleLayouter.RotationMode rotationMode) {
    this.rotationMode = rotationMode;
  }

  public List<TextItem> getItems() {
    return items;
  }

  public void setItems(final List<TextItem> items) {
    this.items = items;

    updateAllStuff();

  }

  public BufferedImage getColorscaleImage() {
    return colorscaleImage;
  }

  public void setColorscaleImage(final BufferedImage colorscaleImage) {
    this.colorscaleImage = colorscaleImage;
  }

  public void setColorMin(final float colorMin) {
    this.colorMin = colorMin;
    updateColorStuff();
  }

  public void setColorMax(final float colorMax) {
    this.colorMax = colorMax;
    updateColorStuff();
  }

  public void updateAllStuff() {
    shapes.clear();

    for(final TextItem textItem : items) {
      final float weight = (float) Math.pow(textItem.size, power);

      final Font font = fm.get(weight);
      final Shape outline = TextOutliner.getOutline(font, textItem.getTerm());

      shapes.add(outline);
    }

    updateColorStuff();

    final List<Shape> free = WordleLayouter.generateLayoutCircular(shapes,
        false, rotationMode);
    shapes = free;

  }

  private void updateColorStuff() {
    colors.clear();
    for(final TextItem textItem : items) {
      final float weight =
          (float) Math.pow(textItem.size, power) * (colorMax - colorMin)
              + colorMin;
      // if(weight < colorMin) {
      // weight = colorMin;
      // }
      // if(weight > colorMax) {
      // weight = colorMax;
      // }
      System.out.println(weight);
      if(colorscaleImage != null) {
        final Color color = new Color(colorscaleImage.getRGB(
            (int) (weight * (colorscaleImage.getWidth() - 1)), 2), true);
        System.out.println(color);
        colors.add(color);
      } else {
        colors.add(Color.black);
      }
    }

  }

  @Override
  public void paint(final Graphics2D g) {

    g.setColor(Color.black);
    final Iterator<Color> colIterator = colors.iterator();

    for(final Shape shape : shapes) {
      if(colIterator.hasNext()) {
        g.setColor(colIterator.next());
      }
      g.fill(shape);
    }

  }

  public Rectangle2D getBBox() {
    Rectangle2D res = null;
    for(final Shape shape : shapes) {
      if(res == null) {
        res = shape.getBounds2D();
      } else {
        res.add(shape.getBounds2D());
      }
    }
    return res;
  }

}
