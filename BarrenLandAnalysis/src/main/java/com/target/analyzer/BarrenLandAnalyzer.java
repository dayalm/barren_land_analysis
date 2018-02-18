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
	private final int WIDTH = 400;
	private final int HEIGHT = 600;
	
	private final int BOTTOM_LEFT_X_ARRAY_INDEX = 0;
	private final int BOTTOM_LEFT_Y_ARRAY_INDEX = 1;
	private final int TOP_RIGHT_X_ARRAY_INDEX = 2;
	private final int TOP_RIGHT_Y_ARRAY_INDEX = 3;
	
	private final int X_COORDINATE_ARRAY_INDEX = 0;
	private final int Y_COORDINATE_ARRAY_INDEX = 1;
	
	private final int NUM_COORDINATES_PER_RECTANGLE = 4;
	
	private final String SPACE = " ";
	
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
	 * @param farm model with the barren parts marked
	 * @return unsorted list of areas for all fertile regions
	 */
	
	private List<Integer> findFertileAreas(Integer[][] farm) {
		int x = 0;
		int y = 0;
		LinkedList<Integer[]> queue = new LinkedList<Integer[]>();
		
		Map<Integer,Integer> fertileAreaMap = new HashMap<Integer,Integer>();
		
		int currentFertileArea = 0;
		
		while(x < WIDTH && y < HEIGHT) {
			
			if(queue.isEmpty()) {
				if(!isPartVisited(x,y,farm)) {
					queue.add(new Integer[]{x,y});
					currentFertileArea++;
					fertileAreaMap.put(currentFertileArea, 0);
				}
				
				if (x == WIDTH-1) {
					x = 0;
					y ++;
				} else {
					x += 1;
				}
			} 
			
			if(!queue.isEmpty()) {
				
				Integer[] part = queue.poll();
				
				int currentX = part[X_COORDINATE_ARRAY_INDEX];
				int currentY = part[Y_COORDINATE_ARRAY_INDEX];
				
				if(!isPartVisited(currentX,currentY,farm)) {
					
					farm[currentX][currentY] = currentFertileArea;
					
					fertileAreaMap.put(currentFertileArea, fertileAreaMap.get(currentFertileArea)+1);
					
					List<Integer[]> neighboringParts = this.neighbors(currentX, currentY);
					
					for(Integer[] neighboringPart:neighboringParts) {
						if(!isPartVisited(neighboringPart,farm)) {
							queue.add(neighboringPart);
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
			System.out.println(fertileAreas.stream().sorted().map(value->value.toString()).collect(Collectors.joining(SPACE)));
		}
		
	}
	
	/**
	 * All immediate neighbors of a given coordinate
	 * @param x
	 * @param y
	 * @return neighboring parts
	 */
	private List<Integer[]> neighbors(int x, int y) {
		List<Integer[]> neighbors = new ArrayList<Integer[]>();
		if (x - 1 >=0) {
			neighbors.add(new Integer[]{x-1, y});
		}
		
		if (x + 1 < WIDTH) {
			neighbors.add(new Integer[]{x+1, y});
		}
		
		if (y -1 >=0) {
			neighbors.add(new Integer[]{x,y-1});
		}
		
		if(y +1 < HEIGHT) {
			neighbors.add(new Integer[]{x,y+1});
		}
		
		return neighbors;
	}
	
	private boolean isPartVisited(int x, int y, Integer[][]farm) {
		return (farm[x][y] != 0);
	}
	
	private boolean isPartVisited(Integer[] part, Integer[][]farm) {
		return this.isPartVisited(part[X_COORDINATE_ARRAY_INDEX],part[Y_COORDINATE_ARRAY_INDEX],farm);
	}
	
	private void handleInputError() {
		printUsage();
		System.exit(-1);
	}
	
	/**
	 * Constructs a 2-D model of the farm land marking all the barren parts
	 * @param barrenLandRectangles bottom left and top right coordinates of barren land rectangles
	 * @return
	 */
	
	private Integer[][] buildFarmModelWithBarrenLand(String[] barrenLandRectangles) {
		
		if(barrenLandRectangles.length == 0) {
			handleInputError();
		}
		
		Integer[][] farm = this.buildFarm();
		
		for(String barrenLandRectangle:barrenLandRectangles) {
			
			String [] coordinates = barrenLandRectangle.trim().split(SPACE);
			
			if (coordinates.length != NUM_COORDINATES_PER_RECTANGLE) {
				handleInputError();
			}
			
			int bottomLeftX=0,bottomLeftY=0,topRightX=0,topRightY=0;
			
			try {
					bottomLeftX = Integer.parseInt(coordinates[BOTTOM_LEFT_X_ARRAY_INDEX]);
					bottomLeftY = Integer.parseInt(coordinates[BOTTOM_LEFT_Y_ARRAY_INDEX]);
					topRightX = Integer.parseInt(coordinates[TOP_RIGHT_X_ARRAY_INDEX]);
					topRightY = Integer.parseInt(coordinates[TOP_RIGHT_Y_ARRAY_INDEX]);
					
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
		
		if(bottomLeftX >= topRightX || bottomLeftY >= topRightY) {
			throw new IllegalArgumentException();
		}
		
		if (bottomLeftX > WIDTH-1 || topRightX > WIDTH-1 || bottomLeftY > HEIGHT-1 || topRightY > HEIGHT-1 ) {
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Constructs a 2-D model of the farm land
	 * @return The farm model
	 */
	
	private Integer[][] buildFarm() {
		Integer[][] farm = new Integer[WIDTH][HEIGHT];
		for (int x = 0; x < WIDTH;x++) {
			for (int y =0;y< HEIGHT;y++) {
				farm[x][y] = 0;
			}
		}
		return farm;
	}
}
