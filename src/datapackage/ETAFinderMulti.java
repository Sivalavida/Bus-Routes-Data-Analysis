package datapackage; 

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

import mypackage.StdDraw;


/**
 * This application will be used as a prove of concept for mapless bus app. 
 * Data Input: Bus coordinates and time. 
 * 
 * Query: ETA
 * Output: Using data input, calculated the expected eta. 
 * 
 * Query: Map with current bus location
 * Output: Using all the input nodes, create a graph. The path where the bus is
 * will be highlighted in a different color. Also, with some magic, able to add
 * various landmarks such as Utown, engineering, etc inside the GUI. HOW?? IDK
 * 
 * For simplicity, note that all time will be in seconds. 
 * 
 * @author Yew Onn
 *
 */

public class ETAFinderMulti {
	//Used for scaling real GPS coordinates into coordinates that fits inside 
	//the canvas size. maxRange is the largest range for the real GPS coordinates
	//longMin and latMin are the minimum longtitude and latitude respectively 
	//among all the datas
	//double maxRange; 
	//double longMin; 
	//double latMin; 
	public int restTime; 

	public Point[] inputPoint(String fileName){
		Point[] tempData = new Point[0];
		try{
			FileReader f = new FileReader(fileName); 
			BufferedReader buff = new BufferedReader(f);
			String line = buff.readLine(); 

			int tempNumPoint = Integer.parseInt(line);

			tempData = new Point[tempNumPoint];

			for(int i = 0; i < tempNumPoint; i++){
				//Create the point using the data
				line = buff.readLine(); 
				String[] lineInfo = line.split(" ");
				double xCor = Double.parseDouble(lineInfo[0]); 
				double yCor = Double.parseDouble(lineInfo[1]);
				double time = Double.parseDouble(lineInfo[2]);
				Point newPoint = new Point(xCor, yCor, time, i);
				tempData[i] = newPoint; 
			}
			buff.close(); 
		}
		catch(Exception e){
			System.out.println(e);
		}
		return tempData; 
	}

	public void visualiser(Point[] points, boolean drawObstacle){
		for(int i = 0; i < points.length; i++){
			try {
				Thread.sleep(restTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			double x = points[i].getX(); 
			double y = points[i].getY();

			int radius = clusterSize(i, points); 

			if(radius > 1 && drawObstacle){

				StdDraw.circle(x, y, radius/2);
			} 

			if(i != points.length - 1){
				StdDraw.line(x, y, points[i+1].getX(), points[i+1].getY());
			}
		}
	}

	/**
	 * Given the starting index and ending index, as well as the array of point, 
	 * plot out the route from startIndex to EndIndex, and circle startIndex and endIndex
	 * @param startIndex
	 * @param endIndex
	 * @param data
	 */
	public void visualiseRoute(int startIndex, int endIndex, Point[] data){
		//StdDraw.setPenColor(StdDraw.RED);
		for(int i = startIndex; i <= endIndex; i++){
			double x = data[i].getX(); 
			double y = data[i].getY(); 
			for(int j = 0; j < 2; j++){
				StdDraw.circle(x, y, j);
			}
			if(i < data.length){
				StdDraw.line(x, y, data[i+1].getX(), data[i+1].getY());
			}
		}
		int[] twoIndex = new int[2];
		twoIndex[0] = startIndex; 
		twoIndex[1] = endIndex; 
		visualisePoint(twoIndex, data);
	}

	public void visualiseRoute(int startIndex, int endIndex, Point[] data,  
			int innerRadius, int outerRadius){
		//StdDraw.setPenColor(StdDraw.RED);
		for(int i = startIndex; i <= endIndex; i++){
			double x = data[i].getX(); 
			double y = data[i].getY(); 
			for(int j = 0; j < 2; j++){
				StdDraw.circle(x, y, j);
			}
			if(i < data.length){
				StdDraw.line(x, y, data[i+1].getX(), data[i+1].getY());
			}
		}
		int[] twoIndex = new int[2];
		twoIndex[0] = startIndex; 
		twoIndex[1] = endIndex; 
		visualisePoint(twoIndex, data, innerRadius, outerRadius);
	}

	/**
	 * Visualise an array of points by drawing a thick ring around it. 
	 * @param pointIndexes
	 * @param data
	 */
	public void visualisePoint(int[] pointIndexes, Point[] data){
		visualisePoint(pointIndexes, data, 0, 20);
	}

	/**
	 * Visualise an array of points by drawign a ring/circle around it. 
	 * @param pointIndexes are the array of point indexes to be visualised. 
	 * @param data contains the information of all the poitns
	 * @param circleInnerRadius is the inner radius of the circle to be drawn. 
	 * @param circleOuterRadius is the outer radius of the circle to be drawn. 
	 */
	public void visualisePoint(int[] pointIndexes, Point[] data, 
			int circleInnerRadius, int circleOuterRadius){
		for(int i = 0; i < pointIndexes.length; i++){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			int index = pointIndexes[i];
			double x = data[index].getX(); 
			double y = data[index].getY(); 
			//Use clusterSize if you want to identify the cluster according to the size
			//of clustering. 
			//int clusterSize = clusterSize(index, data);
			for(int j = circleInnerRadius; j <= circleOuterRadius; j++){
				StdDraw.circle(x, y, j);
			}
		}
	}

	public void visualisePoint(double xCor, double yCor, int circleInnerRadius, 
			int circleOuterRadius){
		for(int j = circleInnerRadius; j <= circleOuterRadius; j++){
			StdDraw.circle(xCor, yCor, j);
		}
	}

	public void visualisePoint(double xCor, double yCor){
		visualisePoint(xCor, yCor, 0, 20);
	}

	public void visualisePoint(Point point){
		visualisePoint(point.getX(), point.getY()); 
	}

	public void visualisePoint(Point point, int innerRadius, int outerRadius){
		visualisePoint(point.getX(), point.getY(), innerRadius, outerRadius);
	}

	/**
	 * Given the index of the current point, determine how large 
	 * the clusters are. The method will look at point i + 1 and determine 
	 * if they are close enough
	 * @param i is the current index to be analyse
	 * @param data is the array countaining all the points information
	 * @return the size of clustering. 
	 */
	public int clusterSize(int i, Point[] data){
		int clusterSize = 1; 
		boolean cluster = true; 

		while ((cluster) && (i + clusterSize < data.length)){
			if(isCluster(data[i].getX(), data[i + clusterSize].getX(), data[i].getY(), 
					data[i + clusterSize].getY())){
				clusterSize++; 
			} else {
				cluster = false; 
			}

		}
		return clusterSize; 
	}

	/**
	 * Given the coordinates of two points, return whether the points are 
	 * counted as a cluster. 
	 * @param xOne yOne are the coordinates of the first point. 
	 * @param xTwo yTwo are the coordinates of the second point. 
	 * @return true if the two points are "close enough", false otherwise 
	 */
	public boolean isCluster(double xOne, double xTwo, double yOne, double yTwo){
		double xDiff = xOne - xTwo; 
		double yDiff = yOne - yTwo; 

		if(xDiff < 0){
			xDiff = xDiff * -1; 
		}

		if(yDiff < 0){
			yDiff = yDiff * -1; 
		}

		double diff = Math.sqrt(xDiff*xDiff + yDiff*yDiff);

		if(diff < 1){
			return true; 
		}
		return false; 
	}

	/**
	 * Given two points, and the cutOff distance to be counted as "cluster/close enough", 
	 * determine if two points are counted as cluster; 
	 * @param pointOne and pointTwo are the two Point. 
	 * @param cutOff is the maximum distance between two points to qualify as "cluster/close enough"
	 * @return true if the two points are counted as cluster, false otherwise. 
	 */
	public boolean isCluster(Point pointOne, Point pointTwo, double cutOff){
		double xOne = pointOne.getX();
		double yOne = pointOne.getY(); 
		double xTwo = pointTwo.getX();
		double yTwo = pointTwo.getY();

		double xDiff = xOne - xTwo; 
		double yDiff = yOne - yTwo; 

		if(xDiff < 0){
			xDiff = xDiff * -1; 
		}

		if(yDiff < 0){
			yDiff = yDiff * -1; 
		}

		double diff = Math.sqrt(xDiff*xDiff + yDiff*yDiff);

		if(diff < cutOff){
			return true; 
		}
		return false;
	}

	/**
	 * determine if two points are within a distance of 1 pixel)
	 * @param pointOne and pointTwo are the two points. 
	 * @return return true if the two points are within 1 pixel, false otherwise. 
	 */
	public boolean isCluster(Point pointOne, Point pointTwo){
		return isCluster(pointOne, pointTwo, 1);
	}

	/**
	 * Given an array of points, identify all the centre of clusters
	 * @param points are objects that stores coordinates location. 
	 * @return the index of all the centre of clusters (NOT THE POINT OBJECT)
	 */
	public int[] clusterFinder(Point[] points){
		//refer to number of data in points.  
		int numPoint = points.length; 
		//refer to number of cluster in points. 
		int numCluster = 0; 
		int i = 0; 

		/**
		 * this for loop identify the number of cluster. this is to initialise cluster
		 * too lazy to use arrayList (if that is what its called, i forget liao). 
		 */
		for(i = 0; i < numPoint; i++){
			if(clusterSize(i, points) > 10){
				while(clusterSize(i, points) > 10 && i < numPoint){
					i++; 
				}
				numCluster++; 
			}
		}
		int[] cluster = new int[numCluster]; 
		numCluster = 0; 
		for(i = 0; i < numPoint; i++){
			int maxClusterSize = -1; 
			int maxClusterIndex = -1; 
			if(clusterSize(i, points) > 10){
				while(clusterSize(i, points) > 10 && i < numPoint){
					if(clusterSize(i, points) > maxClusterSize){
						maxClusterSize = clusterSize(i, points);
						maxClusterIndex = i; 
					}
					i++; 
				}
				cluster[numCluster] = maxClusterIndex; 
				numCluster++; 
			}
		}
		return cluster; 
	}

	public double getETA(Point currentBusLocation, Point userLocation, Point[] data){
		int closestBusIndex = findClosestPointIndex(currentBusLocation, data);
		int closestUserIndex = findClosestPointIndex(userLocation, data); 
		double timeTaken = data[closestUserIndex].getTime() - data[closestBusIndex].getTime(); 
		//System.out.println("timeTaken = "+timeTaken);
		return timeTaken; 
	}

	public double getETA(Point currentBusLocation, int clusterIndex, Point[] data){
		return getETA(currentBusLocation, data[clusterIndex], data);
	}

	/**
	 * Find the point in the bus route closest to input point. 
	 * @param point 
	 * @param data contains the bus route. 
	 * @return the closest point. 
	 */
	public int findClosestPointIndex(Point point, Point[] data){
		double minDist = Double.MAX_VALUE; 
		int closestPoint = -1; 
		for(int i = 0; i < data.length; i++){
			double dist = getDistance(data[i], point); 
			if(dist < minDist){
				minDist = dist; 
				closestPoint = i;
			}
		}

		return closestPoint; 
	}

	public double getDistance(Point pointOne, Point pointTwo){
		double xOne = pointOne.getX(); 
		double yOne = pointOne.getY(); 

		double xTwo = pointTwo.getX(); 
		double yTwo = pointTwo.getY(); 

		double output = (xOne - xTwo)*(xOne - xTwo) + (yOne - yTwo)*(yOne - yTwo);
		return Math.sqrt(output);
	}

	public int findNextBusStopIndex(Point currentBusLocation, Point[] data){
		int closestPoint = findClosestPointIndex(currentBusLocation, data);
		//System.out.println("closestPoint = "+closestPoint);
		int[] clustersIndex = clusterFinder(data);
		int count = 0; 
		while(closestPoint > clustersIndex[count] && count < clustersIndex.length){
			count++; 
		}
		return count; 
	}

	public void runTwo(String fileName){
		Point[] points = new Point[0];
		points = inputPoint(fileName);
		StdDraw.setPenColor(StdDraw.BLUE);
		visualiser(points, true);
		int[] clusters = clusterFinder(points);
		StdDraw.setPenColor(StdDraw.RED);
		visualisePoint(clusters, points, 20, 30);
	}

	public void runOne(String fileName){
		//String fileName = "scalededitedA2BusRoute2.txt"; 
		Point[] points = new Point[0];
		points = inputPoint(fileName);
		StdDraw.setPenColor(StdDraw.BLUE);
		visualiser(points, true);
	}

	public void runThree(String fileName){
		Point[] points = new Point[0];
		points = inputPoint(fileName);
		StdDraw.setPenColor(StdDraw.BLUE);
		visualiser(points, false);
		int[] clusters = clusterFinder(points);
		StdDraw.setPenColor(StdDraw.BLUE);
		visualisePoint(clusters, points, 0, 15);
	}

	public void runFour(String fileName){
		Point[] data = inputPoint(fileName);
		StdDraw.setPenColor(StdDraw.BLUE);
		visualiser(data, false);
		int[] clusters = clusterFinder(data);
		StdDraw.setPenColor(StdDraw.BLUE);
		visualisePoint(clusters, data, 0, 10);


		Point busPoint = new Point(300, 300);
		int nextBusStopIndex = findNextBusStopIndex(busPoint, data);
		StdDraw.setPenColor(StdDraw.RED);
		visualisePoint(busPoint, 0, 10);
		StdDraw.setPenColor(StdDraw.ORANGE);
		visualiseRoute(clusters[nextBusStopIndex - 1], clusters[nextBusStopIndex], data, 
				10, 20);

		int dIndex = 8; 
		StdDraw.setPenColor(StdDraw.GREEN);
		double eta = getETA(busPoint, clusters[dIndex], data);
		int etaMinute = (int) eta/60; 
		int etaSecond = (int) eta - etaMinute*60 ; 
		System.out.println("Estimated Time of Arrival = "+etaMinute+" Minutes "
				+etaSecond+ " seconds");
		visualisePoint(data[clusters[dIndex]], 10, 20);		
	}

	public void toWait(Boolean toWait){
		if(toWait == false){
			return; 
		}
		Scanner sc = new Scanner(System.in); 
		System.out.println("Continue ?");
		String input = sc.nextLine(); 
	}
	public void integrated (String fileName, int busStopNumber, Point busLocation, 
			boolean toWait, boolean interactive){
		Point[] data = inputPoint(fileName);
		int[] clusters = clusterFinder(data);

		int nextBusStopIndex = findNextBusStopIndex(busLocation, data);
		double eta = getETA(busLocation, clusters[busStopNumber], data);
		int etaMinute = (int) eta/60; 
		int etaSecond = (int) eta - etaMinute*60 ; 
		System.out.println("Estimated Time of Arrival = "+etaMinute+" Minutes "
				+etaSecond+ " seconds");

		toWait(toWait);
		
		if(interactive == false){
		//Draw route and obstacles
		StdDraw.setPenColor(StdDraw.PINK);
		visualiser(data, false);
		//StdDraw.setPenColor(StdDraw.BLUE);
		visualisePoint(clusters, data, 0, 10);

		toWait(toWait);
		}

		//Draw the current bus location
		StdDraw.setPenColor(StdDraw.RED);
		visualisePoint(busLocation, 0, 10);


		toWait(toWait);

		//Draw the userLocation, aka busStopNumber
		StdDraw.setPenColor(StdDraw.GREEN);
		visualisePoint(data[clusters[busStopNumber]], 10, 15);	


		toWait(toWait);

		//Draw the route in which the bus is currently in
		StdDraw.setPenColor(StdDraw.YELLOW);
		if(nextBusStopIndex > 0){
			visualiseRoute(clusters[nextBusStopIndex - 1], clusters[nextBusStopIndex], data, 
					10, 15);
		} else {
			visualiseRoute(0, clusters[nextBusStopIndex], data, 10, 15);
		}

	}

	public void integrated(String fileName, int busStopNumber, double latitude, double longtitude
			, boolean toWait, boolean interactive){
		double[] scaled = scaledIndex(latitude, longtitude);
		double xCor = scaled[0];
		double yCor = scaled[1];
		Point busLocation = new Point(xCor, yCor);
		integrated(fileName, busStopNumber, busLocation, toWait, interactive);
	}

	public void interactive(String fileName, int busStopNumber, boolean toWait){
		//Draw route and obstacles
		Point[] data = inputPoint(fileName);
		int[] clusters = clusterFinder(data);
		StdDraw.setPenColor(StdDraw.BLUE);
		visualiser(data, false);
		visualisePoint(clusters, data, 0, 10);

		Scanner sc = new Scanner(System.in);
		System.out.println("Enter coordinates: ");
		String message = sc.nextLine();
		String[] splitMessage = message.split(",");
		double latitude = Double.parseDouble(splitMessage[0]); 
		double longtitude = Double.parseDouble(splitMessage[1]);
		sc.close(); 
		integrated(fileName, busStopNumber, latitude, longtitude, toWait, true);
	}

	public double[] scaledIndex(double latitude, double longtitude){
		double longMin = 103.77053476372032; 
		double latMin =  1.29095178;
		double maxRange = 0.01469760436334866; 
		double scale = 600/maxRange; 
		double scaledLong = (longtitude - longMin) * scale; 
		double scaledLat = 600 - (latitude - latMin) * scale; 
		double[] output = new double[2];
		output[0] = scaledLat; 
		output[1] = scaledLong; 
		System.out.println("x = "+scaledLat+ ", y = "+scaledLong);
		return output; 
	}

	public static void main (String[] args){
		//go to line 67 to change speed of drawing
		String fileName = "scalededitedA2BusRoute2.txt";
		ETAFinderMulti testOne = new ETAFinderMulti(); 
		testOne.restTime = 1; 
		StdDraw.setCanvasSize(1000, 1000);
		StdDraw.setScale(0, 700);
		StdDraw.clear(StdDraw.BLACK);

		//Run one is for visualising A2 route. 
		//testOne.runOne(fileName);

		//Visualising cluster finder
		//testOne.runTwo(fileName); 

		//Visualising only cluster, all of same size
		//testOne.runFour(fileName);

		Point point = new Point(300, 300);
		testOne.integrated(fileName, 8, point, false, false);

		//testOne.integrated(fileName, 8, 1.298444, 103.777228, false, false);
		//testOne.interactive(fileName, 8, false);
	}

}
