package pathfinding;

import java.awt.Color;
import java.awt.Point;
import java.util.*;
import javax.swing.JOptionPane;

/************************************************************
 * Finds the shortest path between 2 points in a grid based
 * map.
 * @author Adam Beecham
 ************************************************************/
public class Pathfinder {
	

	//cells we have already looked at
	private ArrayList<Cell>closedList;
	//cells we need to look at
	private PriorityQueue<Cell>openList;
	public static boolean isRunning;
	public static boolean isFoundPath = false;
	Cell current;
	private int pathLength, numNodeInCloseList, numNodeInOpenList;
	private long searchTime, startTime;
	double distanceToGoal, distanceFromStart, estimate;

	/************************************************************
	 * Constructs a pathfinder and initialises our data
	 * structures.
	 ************************************************************/
	public Pathfinder(){
		pathLength = 0;
		numNodeInCloseList = 0;
		numNodeInOpenList = 0;
		//priority queue means that we can just pull the cells
		//with the best path from the top of the list
		openList = new PriorityQueue<Cell>();
		closedList = new ArrayList<Cell>();
	}

	/************************************************************
	 * Finds the shortest path between 2 points in a grid.
	 * @param Start the start cell for the algorithm.
	 * @param Goal the cell this algorithm must find a path to
	 * @param grid the grid to be searched.
	 * @param step the time taken to make 1 iteration of the
	 * algorithm.
	 * @param algorithm the search algorithm for finding the
	 * shortest path.
	 ************************************************************/
	public ArrayList<Cell> findShortestPath(Cell start, Cell goal, Grid grid, int step){

		isRunning = true;
		start.setDistanceFromStart(0);
		switch(grid.algorithmHeuristic) {
			case 0: {
				start.setCost(manhattanDistance(start.position, goal.position));
				System.out.println("manhattan");
				break;
			}
			
			case 1: {
				start.setCost(diagonalDistance(start.position, goal.position));
				System.out.println("diagonal");
				break;
			}
			
			case 2: {
				start.setCost(euclideanDistance(start.position, goal.position));
				System.out.println("euclidean");
				break;
			}
		}
		//begin search from start cell
		openList.add(start);
		numNodeInCloseList = 1;
		System.out.println("add start to openList");/*************************************************/

		long currentTime = System.currentTimeMillis();
		startTime = currentTime;

		//keep looping until we find the goal or search every
		//node
		while(!openList.isEmpty() && isRunning){

			if(!isRunning)
				break;

			long timeSinceLastStep = System.currentTimeMillis() - currentTime;
			

			//Check if its time to take another step in the search
			if(timeSinceLastStep >= step){
				System.out.println();
				System.out.println();
				System.out.println();
				
				currentTime = System.currentTimeMillis();

				//show the user the progress of the last step on the grid
				grid.update();

				//get the cell with the best path
				current = openList.poll();
				//we've visited this cell so we color it green
				if (current.getColor() != Color.ORANGE)
					current.setColor(Color.GREEN);

				//if we're using A-Star search, we no longer need to look at this
				//node again so we add it to the closed list
					closedList.add(current);				

				//start node is always magenta
				if(current == start) {
					start.setColor(Color.MAGENTA);
				}

				//goal node is always red
				if(current == goal){
					goal.setColor(Color.RED);
					grid.update();
					break;
				}

				//examine the edges of the current cell to find the best
				//neighbouring cell to navigate to based on the algorithm the user chose
				for(Edge e : current.getEdges()){
						
					//get the cell at the end of the current edge
					Cell next = (Cell) e.getDestination();

					//we only look at cells once with a star
					if(closedList.contains(next) || next.getColor() == Color.ORANGE) {/*************************************************/
						continue;
					}

					distanceFromStart = current.getDistanceFromStart() + e.getCost();
					//we use a heuristic to estimate how far away from the goal we are
					if(grid.algorithmHeuristic==0) distanceToGoal = manhattanDistance(next.getPosition(), goal.getPosition());
					else if(grid.algorithmHeuristic==1) distanceToGoal = diagonalDistance(next.getPosition(), goal.getPosition());
					else if(grid.algorithmHeuristic==2) distanceToGoal = euclideanDistance(next.getPosition(), goal.getPosition());
					//sum the start and goal distances to get an idea of how good this cell would be
					//to navigate to next. this pushes the search towards the goal.
					estimate = distanceFromStart + distanceToGoal;
					if(next.getColor() != Color.GREEN && next.getColor() != Color.MAGENTA ) {
						next.setColor(Color.BLUE);
					}
					//add the cell to the open list if it isnt already there
					if(!openList.contains(next) || distanceFromStart < next.getDistanceFromStart()){
						next.setDistanceFromStart(distanceFromStart);
						next.setCost(estimate);
						next.setPredecessor(current);
						openList.add(next);
						numNodeInOpenList++;
					}
				}
			}
		}
		

		if (current != goal) {
			JOptionPane.showMessageDialog(null, "Không tìm thấy đường đi");
			searchTime = System.currentTimeMillis() - startTime;
			return null;
		}
		
		
		
		//the search is finished so we get the shortest path and return it
		ArrayList<Cell> shortestPath = new ArrayList<Cell>();
		Cell current = goal;
		shortestPath.add(current);
		//navigate backwards from the goal to the start via the shortest path
		while(current.getPredecessor()!= null){
			//store the current cell in the shortest path
			shortestPath.add((Cell)current.getPredecessor());

			current = (Cell)current.getPredecessor();
			
			//highlight it
			if(current != start) {
				pathLength ++;
				current.setColor(Color.CYAN);
			}
		}
		//show the path to the user
		pathLength = shortestPath.size();
		grid.update();
		//reverse the list so the path is the correct way round
		Collections.reverse(shortestPath);
		searchTime = System.currentTimeMillis() - startTime;
		isFoundPath = true;
		return shortestPath;
	}

	//stop the search
	public static void stop(){
		isRunning = false;
	}

	//a heauristic estimate of the distance between two points in the grid.
	//it slightly overestimates the distance between two cells but provides an
	//adequate estimate for our a star search
	
	public double manhattanDistance(Point p1, Point p2) {
		double dx = Math.abs(p2.getX() - p1.getX());
		double dy =  Math.abs(p2.getY() - p1.getY());
		return dx + dy;
	}
	
	public double diagonalDistance(Point p1, Point p2){
		double dx = Math.abs(p2.getX() - p1.getX());
		double dy =  Math.abs(p2.getY() - p1.getY());
		return dx + dy +(Math.sqrt(2) - 1)*Math.min(dx, dy);
	}
	
	public double euclideanDistance(Point p1, Point p2) {
		double dx = Math.abs(p2.getX() - p1.getX());
		double dy =  Math.abs(p2.getY() - p1.getY());
		return Math.sqrt(dx*dx + dy*dy);
	}

	public int getPathLength() {
		return pathLength;
	}

	public int getNumNodeInCloseList() {
		return numNodeInCloseList;
	}

	public int getNumNodeInOpenList() {
		return numNodeInOpenList;
	}

	public long getSearchTime() {
		return searchTime;
	}

	
}
