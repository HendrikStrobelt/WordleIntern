package de.graphics.uni_konstanz.wordle;

public class TextItem implements Comparable<TextItem> {

  String term;
  float size;
  float fontSize;

  public TextItem(final String term, final float size) {
    super();
    this.term = term;
    this.size = size;
  }

  public String getTerm() {
    return term;
  }

  public void setTerm(final String term) {
    this.term = term;
  }

  public float getSize() {
    return size;
  }

  public void setSize(final float size) {
    this.size = size;
  }

  public float getFontSize() {
    return fontSize;
  }

  public void setFontSize(final float fontSize) {
    this.fontSize = fontSize;
  }

  @Override
  public String toString() {
    return "TextItem [term=" + term + ", size=" + size + ", fontSize="
        + fontSize + "]";
  }

  @Override
  public int compareTo(final TextItem o) {

    return (size - o.size > 0) ? -1 : 1;
  }

}
