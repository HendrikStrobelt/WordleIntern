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
	FontManager fm = new FontManager(6,18,"Times");
	
	
	
	
	public List<TextItem> getItems() {
		return items;
	}





	public void setItems(List<TextItem> items) {
		this.items = items;
		for (TextItem textItem : items) {
			Font font = fm.get(textItem.size);
			
		}
		
		
	}





	@Override
	public void paint(Graphics2D g) {
		int x=0;
		for (TextItem textItem : items) {
			Font font = fm.get(textItem.size);
			g.setColor(Color.black);
			g.setFont(font);
			g.drawString(textItem.getTerm(), x, x);
			x++;
			
		}		

	}

}
