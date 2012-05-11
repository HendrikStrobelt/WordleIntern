package de.graphics.uni_konstanz.wordle;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

public class WordlePainterSimple implements WordlePainter {

	List<TextItem> items = new ArrayList<TextItem>();
	List<Shape> shapes = new ArrayList<Shape>();
	
	
	
	
	
	public List<TextItem> getItems() {
		return items;
	}





	public void setItems(List<TextItem> items) {
		this.items = items;
	}





	@Override
	public void paint(Graphics2D g) {
		// TODO Auto-generated method stub

	}

}
