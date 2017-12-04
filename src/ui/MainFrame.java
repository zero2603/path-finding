package ui;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JFrame;

import pathfinding.Grid;
public class MainFrame extends JFrame{
	ButtonPanel pnButton;
	public MainFrame() {
		addControls();
		addEvents();
	}
	
	private void addControls() {
		Container con = getContentPane();
		con.setLayout(null);
		Grid grid = new Grid(770,630, 36, 44);
		this.add(grid);
		grid.setBounds(0, 0, 770, 630);
		grid.setBackground(Color.blue);
		con.add(grid);
		
		pnButton = new ButtonPanel(grid);
		pnButton.setBounds(770, 10, 350, 630);
		con.add(pnButton);
		
	}
	
	private void addEvents() {		
	}

	public void showWindow() {
		setTitle("A* Path fingding");
		setSize(1120, 670);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		
//		this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
////		this.setUndecorated(true);
	}
//	public static void repaints() {
//		repaints();
//	}
}
