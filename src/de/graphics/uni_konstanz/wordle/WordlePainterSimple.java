package de.graphics.uni_konstanz.wordle;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

public class WordlePainterSimple implements WordlePainter {

  List<TextItem> items = new ArrayList<TextItem>();
  List<Shape> shapes = new ArrayList<Shape>();
  FontManager fm = new FontManager(6, 18, "Times");
  String fontName = "Times";

  public String getFontName() {
    return fontName;
  }

  public void setFontName(final String fontName) {
    this.fontName = fontName;
    fm = new FontManager(6, 18, fontName);
    updateAllStuff();

  }

  public List<TextItem> getItems() {
    return items;
  }

  public void setItems(final List<TextItem> items) {
    this.items = items;

    updateAllStuff();

  }

  private void updateAllStuff() {
    shapes.clear();
    for(final TextItem textItem : items) {
      final Font font = fm.get(textItem.size);
      final Shape outline = TextOutliner.getOutline(font, textItem.getTerm());
      shapes.add(outline);

    }

    final List<Shape> free = WordleLayouter.generateLayoutCircular(shapes,
        false, WordleLayouter.RotationMode.NO_ROTATION);
    shapes = free;

  }

  @Override
  public void paint(final Graphics2D g) {
    g.setColor(Color.black);
    for(final Shape shape : shapes) {
      g.fill(shape);
    }

  }

}
