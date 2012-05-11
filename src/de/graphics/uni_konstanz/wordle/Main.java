package de.graphics.uni_konstanz.wordle;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Panel;

import javax.swing.*;

public class Main {

	private JFrame fenster;

	public Main() {
		fenster = new JFrame("Fenster");
		fenster.setSize(300, 300);
		fenster.setLocation(300, 300);
		fenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenster.setVisible(true);
		
		
		BorderLayout borderLayout = new BorderLayout();
		JPanel guiPanel = new JPanel();
		guiPanel.setLayout(new BoxLayout(guiPanel, BoxLayout.Y_AXIS));
		guiPanel.add(new JButton("yes"));
		guiPanel.add(new JButton("no"));
		guiPanel.setMinimumSize(new Dimension(200,0));
		Panel panel = new Panel(borderLayout);
		panel.add(guiPanel, BorderLayout.WEST);
		
		
		fenster.add(panel);
		
		
		
		
		
		
		fenster.pack();
		
		
		
		
	}

	public static void main(String[] args) {
		
		Main main = new Main();
		
		
		
		
		
		String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getAvailableFontFamilyNames();

		for (int i = 0; i < fonts.length; i++) {
			System.out.println(fonts[i]);
		}
	}

}
