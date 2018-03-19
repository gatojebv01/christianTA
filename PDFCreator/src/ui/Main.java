package ui;

//import java.awt.Dimension;

import javax.swing.JFrame;
//import javax.swing.JScrollPane;

public class Main {

	public static void main(String[] args) {
		JFrame frame = new JFrame("Proposal Creator");
		//JScrollPane pane = new JScrollPane(new PDFCreatorPanel(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		//pane.setSize(new Dimension(200, 500));
		//frame.setContentPane(pane);
		
		frame.getContentPane().add(new PDFCreatorPanel());
		//frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}