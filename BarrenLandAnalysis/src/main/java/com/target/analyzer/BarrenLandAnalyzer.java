package com.target.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** 
 * Given a set of rectangular barren lands in a 400m X 600m rectangular farm, outputs the areas of all fertile regions in 
 * ascending order.
 * 
 * The lower left and upper right coordinates of all barren land rectangles should be passed as program arguments
 * with each coordinate separated by a space. Each argument represents a barren land rectangle.
 * 
 * Usage
 * java BarrenLandAnalyzer <barren land rectangles>
 * 
 * Example
 * java BarrenLand Analyzer  "48 192 351 207" "48 392 351 407" "120 52 135 547" "260 52 275 547"
 * 
 * @author  Dayal Murukutla
 * @version 1.0     
 */

public class BarrenLandAnalyzer {
	private final int HEIGHT = 400;
	private final int WIDTH = 600;
	private final String OUTPUT_SEPARATOR = " ";
	
	public static void main(String[]args) {
		BarrenLandAnalyzer analyzer = new BarrenLandAnalyzer();
		analyzer.reportFertileLandAreas(args);
	}
	
	
	private void printUsage() {
		System.out.println("ERROR  : Invalid coordinates for barren land.");
		System.out.println("         Each barren land rectangle's coordinates should consist of four positive integers separated by spaces."
				           +         "\n         The first two integers should be the x,y coordinates of bottom left corner and the next two integers should be the x,y coordinates of the top right corner.");
		System.out.println("\nUSAGE  : java BarrenLandAnalyzer \"<barren land coordinates>\"");
		System.out.println("EXAMPLE: java BarrenLandAnalyzer \"48 192 351 207\" \"48 392 351 407\" \"120 52 135 547\" \"260 52 275 547\"");
	}
	
	/**
	 * Outputs areas of all fertile regions in ascending order
	 * 
	 * @param barrenLandRectangles lower left and upper right coordinates of barren land rectangles separated by a space
	 * 
	 */
	private void reportFertileLandAreas(String[] barrenLandRectangles) {
		
		List<Integer> fertileAreas = this.findFertileAreas(barrenLandRectangles);
		
		this.outputFertileAreas(fertileAreas);
	
	}
	
	/**
	 * Identifies all fertile regions in the farm
	 * 
	 * @param barrenLandRectangles bottom left and top right coordinates of barren land rectangles
	 * @return
	 */
	
	public List<Integer> findFertileAreas(String[] barrenLandRectangles) {
		Integer[][] farm = this.buildFarmModelWithBarrenLand(barrenLandRectangles);
		return this.findFertileAreas(farm);
	}
	
	/**
	 *
	 * Identifies fertile regions using a modified Breadth First Search (BFS) algorithm
	 * 
	 *
	 * @param farm farm model with the barren squares marked
	 * @return unsorted list of areas for all fertile regions
	 */
	
	private List<Integer> findFertileAreas(Integer[][] farm) {
		int row = 0;
		int column = 0;
		LinkedList<Integer[]> queue = new LinkedList<Integer[]>();
		
		Map<Integer,Integer> fertileAreaMap = new HashMap<Integer,Integer>();
		
		int currentFertileArea = 0;
		
		while(row < HEIGHT && column < WIDTH) {
			
			if(queue.isEmpty()) {
				if(!isSquareVisited(row,column,farm)) {
					queue.add(new Integer[]{row,column});
					currentFertileArea++;
					fertileAreaMap.put(currentFertileArea, 0);
				}
				
				if (row == HEIGHT-1) {
					row = 0;
					column ++;
				} else {
					row += 1;
				}
			} 
			
			if(!queue.isEmpty()) {
				
				Integer[] square = queue.poll();
				
				int currentRow = square[0];
				int currentColumn = square[1];
				
				if(!isSquareVisited(currentRow,currentColumn,farm)) {
					
					farm[currentRow][currentColumn] = currentFertileArea;
					
					fertileAreaMap.put(currentFertileArea, fertileAreaMap.get(currentFertileArea)+1);
					
					List<Integer[]> neighboringSquares = this.neighboringSquares(currentRow, currentColumn);
					
					for(Integer[] neighboringSquare:neighboringSquares) {
						if(!isSquareVisited(neighboringSquare,farm)) {
							queue.add(neighboringSquare);
						}
					}
					
				}
			}
		}
		
		List<Integer> fertileAreas = new ArrayList<Integer>();
		
		fertileAreas.addAll(fertileAreaMap.values());
		
		return fertileAreas;
		
	}
	
	/**
	 * Outputs areas of fertile regions to System Out sorted in ascending order
	 * @param fertileAreas list of areas for all fertile regions
	 */
	
	private void outputFertileAreas(List<Integer> fertileAreas) {
		if (fertileAreas.isEmpty()) {
			System.out.println("No fertile areas");
		} else {
			System.out.println(fertileAreas.stream().sorted().map(value->value.toString()).collect(Collectors.joining(OUTPUT_SEPARATOR)));
		}
		
	}
	
	/**
	 * All neighboring squares of a square at a particular row and column
	 * @param row
	 * @param column
	 * @return neighboring squares
	 */
	private List<Integer[]> neighboringSquares(int row, int column) {
		List<Integer[]> neighboringSquares = new ArrayList<Integer[]>();
		if (column - 1 >=0) {
			neighboringSquares.add(new Integer[]{row, column-1});
		}
		
		if (column + 1 < WIDTH) {
			neighboringSquares.add(new Integer[]{row, column+1});
		}
		
		if (row -1 >=0) {
			neighboringSquares.add(new Integer[]{row-1,column});
		}
		
		if(row +1 < HEIGHT) {
			neighboringSquares.add(new Integer[]{row+1,column});
		}
		
		return neighboringSquares;
	}
	
	private boolean isSquareVisited(int row, int column, Integer[][]farm) {
		return (farm[row][column] != 0);
	}
	
	private boolean isSquareVisited(Integer[] square, Integer[][]farm) {
		return this.isSquareVisited(square[0],square[1],farm);
	}
	
	private void handleInputError() {
		printUsage();
		System.exit(-1);
	}
	
	/**
	 * Constructs a 2-D model of the farm land with 1X1 squares, marking all the barren squares
	 * @param barrenLandRectangles bottom left and top right coordinates of barren land rectangles
	 * @return
	 */
	
	private Integer[][] buildFarmModelWithBarrenLand(String[] barrenLandRectangles) {
		
		if(barrenLandRectangles.length == 0) {
			handleInputError();
		}
		
		Integer[][] farm = this.buildFarm();
		
		for(String barrenLandRectangle:barrenLandRectangles) {
			// Each coordinate in the rectangle is separated by a space
			String [] coordinates = barrenLandRectangle.trim().split(" ");
			
			if (coordinates.length != 4) {
				handleInputError();
			}
			
			int bottomLeftX=0,bottomLeftY=0,topRightX=0,topRightY=0;
			
			try {
					bottomLeftX = Integer.parseInt(coordinates[0]);
					bottomLeftY = Integer.parseInt(coordinates[1]);
					topRightX = Integer.parseInt(coordinates[2]);
					topRightY = Integer.parseInt(coordinates[3]);
					
					validateCoordinates(bottomLeftX,bottomLeftY,topRightX,topRightY);
			}
			
			catch(IllegalArgumentException ex) {
				handleInputError();
			}
			
			
			for(int x=bottomLeftX;x<=topRightX;x++) {
				for (int y=bottomLeftY;y<=topRightY;y++) {
					
					farm[x][y] = -1;
					
				}
			}
		}
		
		return farm;
		
	}
	
	/**
	 * Validates the bottom left and top right coordinates of a rectangular region 
	 * @param bottomLeftX
	 * @param bottomLeftY
	 * @param topRightX
	 * @param topRightY
	 */
	
	public void validateCoordinates(int bottomLeftX,int bottomLeftY,int topRightX,int topRightY) {
		
		if (bottomLeftX < 0 || bottomLeftY < 0 || topRightX < 0 || topRightY < 0) {
			
			throw new IllegalArgumentException();
		}
		
		if(bottomLeftX > topRightX || bottomLeftY > topRightY) {
			throw new IllegalArgumentException();
		}
		
		if (bottomLeftX > 399 || topRightX > 399 || bottomLeftY > 599 || topRightY > 599 ) {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Constructs a 2-D model of the farm land with 1X1 squares 
	 * @return The farm model
	 */
	
	private Integer[][] buildFarm() {
		Integer[][] farm = new Integer[HEIGHT][WIDTH];
		for (int row = 0; row < farm.length;row++) {
			for (int column =0;column< farm[row].length;column++) {
				farm[row][column] = 0;
			}
		}
		return farm;
	}
}
