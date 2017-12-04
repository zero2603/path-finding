package pathfinding;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;

/************************************************************
 * A visual representation of a uniform grid representing the
 * search space of the pathfinding class.
 * @author Adam Beecham
 ************************************************************/
public class Grid extends JPanel{

	private int width;
	private int height;

	private int rowHeight;
	private int columnWidth;

	private int rows;
	private int columns;

	private boolean isSearching;

	private Cell cells[][];
	private Cell startCell;
	private Cell goalCell;
	private ArrayList<Cell> walls = new ArrayList<>();     //array list of walls on grid
	final int numOfWall = 50;          //number of walls on grid
	private int cellType;

	private Pathfinder pathfinder;
	private boolean horizionable, diagonalable;
	int algorithmHeuristic = 0;

	public Grid(int width, int height, int rows, int columns){

		this.width = width;
		this.height = height;
		this.rows = rows;
		this.columns = columns;
		rowHeight = height / rows;
		columnWidth = width / columns;

		isSearching = false;
		horizionable = true;
		diagonalable = true;
		
		buildGraph();
		addEvents();
	}

	public void start(int step){
		isSearching = true;
		pathfinder.findShortestPath(startCell, goalCell, this, step);
	}

	public void update(){
		this.repaint();
	}

	public void creatMap(){
		isSearching = false;
		Pathfinder.stop();
		pathfinder = new Pathfinder();
		buildGraph();
		this.repaint();
	}
	
	public void pause() {
		isSearching = false;
		Pathfinder.stop();
	}
	
	public void reset() {
		pause();
		
		for(int i=1; i<rows-1; i++) {
			for(int j=1; j<columns-1; j++) {
				deleteRoute(i, j);
			}
		}
		
		update();
	}

	private void buildGraph(){
		pathfinder = new Pathfinder();
		cells = new Cell[rows][columns];
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < columns; j++){
				cells[i][j] = new Cell(new Point(j * columnWidth ,i * rowHeight ),columnWidth,rowHeight);
				if (i == 0 || i == rows - 1 || j == 0 || j == columns -1)
					cells[i][j].setColor(Color.ORANGE);
			}
		}

		for(int i = 1; i < rows - 1; i++){
			for(int j = 1; j < columns - 1; j++){
				setEdges(i, j);
			}
		}
		
		startCell = cells[1][1];
		startCell.setColor(Color.MAGENTA);
		goalCell = cells[rows - 2][columns - 2];
		goalCell.setColor(Color.RED);

		for (int x = 1; x <= numOfWall; x++) {
			int i = new Random().nextInt(rows - 1) + 1;
			int j = new Random().nextInt(columns - 1 ) + 1;
			setWall(i, j);
		}
		update();

	}
	
	public void setWall(int i, int j) {
		if (cells[i][j].getColor() != Color.WHITE) return;
			
		cells[i][j].setColor(Color.ORANGE);
		cells[i][j].deleteAllEdge();
		
		walls.add(cells[i][j]);

	}
	
	public void deleteWall(int i, int j) {
		Color temp = cells[i][j].getColor();
		if (temp != Color.ORANGE || i == rows -1 || i == 0 || j == columns -1 || j == 0) return;
		
		cells[i][j].setColor(Color.WHITE);
		setEdges(i, j);
		walls.remove(cells[i][j]);

	}
	
	public void deleteRoute(int i, int j) {
		Color temp = cells[i][j].getColor();
		if (temp != Color.GREEN && temp != Color.BLUE && temp != Color.CYAN) return;
		
		cells[i][j].setColor(Color.WHITE);
		setEdges(i, j);
		
		pathfinder = new Pathfinder();
	}
	
	public void setHeuristic(int index) {
		this.algorithmHeuristic = index;
		
		if(this.algorithmHeuristic==0) {
			diagonalable = false;
		}
		
		System.out.println(algorithmHeuristic);
		update();
	}

	public void setEdges(int i, int j) {
		
			if(cells[i +1][j].getColor() != Color.ORANGE)
				cells[i][j].addEdge(new Edge(columnWidth, cells[i + 1][j]));
			if(cells[i][j + 1].getColor() != Color.ORANGE)
				cells[i][j].addEdge(new Edge(rowHeight, cells[i][j + 1]));
			if(cells[i - 1][j].getColor() != Color.ORANGE)
				cells[i][j].addEdge(new Edge(columnWidth, cells[i - 1][j]));
			if(cells[i][j - 1].getColor() != Color.ORANGE)
				cells[i][j].addEdge(new Edge(rowHeight, cells[i][j - 1]));
		
		if (algorithmHeuristic != 0) {
			if(cells[i +1][j + 1].getColor() != Color.ORANGE)
				cells[i][j].addEdge(new Edge((int)(rowHeight * 1.41421), cells[i + 1][j + 1]));
			if(cells[i - 1][j - 1].getColor() != Color.ORANGE)
				cells[i][j].addEdge(new Edge((int)(rowHeight * 1.41421), cells[i - 1][j - 1]));
			if(cells[i +1][j - 1].getColor() != Color.ORANGE)
				cells[i][j].addEdge(new Edge((int)(rowHeight * 1.41421), cells[i + 1][j - 1]));
			if(cells[i - 1][j + 1].getColor() != Color.ORANGE)
				cells[i][j].addEdge(new Edge((int)(rowHeight * 1.41421), cells[i - 1][j + 1]));
		}
	}
	
	public void deleteMap() {
		for(int i=1; i<rows-1; i++) {
			for(int j=1; j<columns-1; j++) {
				deleteWall(i, j);
				deleteRoute(i, j); 
			}
		}
		
		pause();
		pathfinder = new Pathfinder();
		update();
	}

	public void setPositionable(int cellType){
		this.cellType = cellType;
		
	}

	public void paintComponent(Graphics g){
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width, height);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		
		g.setColor(Color.BLACK);
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < columns; j++){
				cells[i][j].draw(g);
			}
		}

	}
	
	private void addEvents() {
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				if(!isSearching){
					Point mousePos = new Point(e.getX() ,e.getY());
					System.out.println(""+ e.getX() + " " + e.getY());
					int j = (int)(mousePos.x/columnWidth);
					int i = (int)(mousePos.y/rowHeight);
					
					if(cellType == 0){
						if (cells[i][j].getColor() != Color.white)
							return;
						startCell.setColor(Color.WHITE);
						startCell = cells[i][j];
						startCell.setColor(Color.MAGENTA);
					}

					else if(cellType == 1){
						if (cells[i][j].getColor() != Color.white)
							return;
						goalCell.setColor(Color.WHITE);
						goalCell = cells[i][j];
						goalCell.setColor(Color.RED);
					}
					update();
				}
			}
		});
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				Point mousePos = new Point(e.getX() ,e.getY());
				int j = (int)(mousePos.x/columnWidth);
				int i = (int)(mousePos.y/rowHeight);

				if(cellType == 2) {
					setWall(i, j);
					System.out.println("setwall");
				}
				if (cellType == 3) {
					deleteWall(i, j);
					System.out.println("deleteWall");
				}
				update();		
			}
		});

	}
	
	public void increaseResolution() {
		if (columns == 220) return;
		columns += 11;
		rows += 9;
		rowHeight = height / rows;
		columnWidth = width / columns;
		creatMap();
	}
	public void decreaseResolution() {
		if (columns == 11) return;
		columns -= 11;
		rows -= 9;
		rowHeight = height / rows;
		columnWidth = width / columns;
		creatMap();
	}
	
	 public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}
	
	public Pathfinder getPathfinder() {
		return pathfinder;
	}

	public void setHorizionable(boolean horizionable) {
		this.horizionable = horizionable;
	}

	public void setDiagonalable(boolean diagonalable) {
		this.diagonalable = diagonalable;
	}

	public Cell getStartCell() {
		return startCell;
	}

	public Cell getGoalCell() {
		return goalCell;
	}
	
}
