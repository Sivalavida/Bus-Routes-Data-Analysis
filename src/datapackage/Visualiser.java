package datapackage; 

import java.io.BufferedReader;
import java.io.FileReader;

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

public class Visualiser {
	//gradient[i] represent the gradient of line connecting point[i] and point[i+1]; 
	double[] gradient; 
	double[] constant; 
	Point[] data; 
	Point lastPoint;
	//refer to the earlier one. 
	Point currPoint; 
	//the point that is within a and a+1; 
	Point currPerPoint; 
	//destination point; 
	Point destPoint; 
	//which two point in the data the currPerPoint belong to. Include the earlier point. 
	int currDataPoint;
	//number of point
	int numPoint;
	//Used for scaling real GPS coordinates into coordinates that fits inside 
	//the canvas size. maxRange is the largest range for the real GPS coordinates
	//longMin and latMin are the minimum longtitude and latitude respectively 
	//among all the datas
	double maxRange; 
	double longMin; 
	double latMin; 
	
	int restTime; 
	int scaleFactor; 
	int minDrawingRadius; 

	public Visualiser(){
		gradient = null; 
		data = null; 
		constant = null; 
		//Last stop is the earlier stop between the two points that the bus was
		//last check in as. 
		lastPoint = null; 
		maxRange = 0.01469760436334866; 
		longMin = 103.77053476372032; 
		latMin = 1.29095178; 
	}

	/**
	 * File need to have the format as shown in data.txt
	 * @param fileName
	 */
	public void inputPoint(String fileName){
		try{
			FileReader f = new FileReader(fileName); 
			BufferedReader buff = new BufferedReader(f);
			String line = buff.readLine(); 

			this.numPoint = Integer.parseInt(line);

			data = new Point[this.numPoint];

			for(int i = 0; i < this.numPoint; i++){
				//Create the point using the data
				line = buff.readLine(); 
				String[] lineInfo = line.split(" ");
				double xCor = Double.parseDouble(lineInfo[0]); 
				double yCor = Double.parseDouble(lineInfo[1]);
				double time = Double.parseDouble(lineInfo[2]);
				Point newPoint = new Point(xCor, yCor, time, i);
				data[i] = newPoint; 
			}
			buff.close(); 
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	
	/**
	 * 
	 * @param fileName
	 */
	public void visualiser(String fileName){
		inputPoint(fileName);
		for(int i = 0; i < data.length; i++){//does i jump values??
			try {
				Thread.sleep(restTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			double x = data[i].getX(); 
			double y = data[i].getY();

			//StdDraw.circle(x, y, 1);

			boolean toDraw = true; //to draw indicates whether to draw the last line
			if(i == data.length-1){
				toDraw = false; 
			}
			
			int radius = clusterSize(i); //raduius is the num of points in the cluster
			
			if(radius > minDrawingRadius){
				
				StdDraw.circle(x, y, radius/scaleFactor);
			}
			

			if(toDraw){
				StdDraw.line(data[i].getX(), data[i].getY(), data[i+1].getX(), data[i+1].getY());
			}
		}
	
	}

	/**
	 * Given the index of the current point, determine how large 
	 * the clusters are. The method will look at point i+1 and determine
	 * if they are close enough
	 * @param i is the current index to be analyse. 
	 * @return 
	 */
	public int clusterSize(int i){
		int clusterSize = 1; //num points in cluster
		boolean cluster = true; 

		while ((cluster) && (i + clusterSize < data.length)){
			if(isCluster(data[i].getX(), data[i + clusterSize].getX(), data[i].getY(), 
					data[i + clusterSize].getY())){
				//comparing bet the first and last point of the cluster
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

		if(diff < 0.5){
			return true; 
		}
		return false; 
	}
	public static void main (String[] args){
		Visualiser testOne = new Visualiser(); 
		testOne.restTime = 0;//dont really need this because it already is laggy 
		testOne.scaleFactor = 2;
		testOne.minDrawingRadius = 5; 
		StdDraw.setCanvasSize(1000, 1000);
		StdDraw.setScale(0, 740);
		//StdDraw.setPenColor(StdDraw.GREEN);
		StdDraw.clear(StdDraw.BLACK);
		//testOne.visualiser("scaledA1BusRoute.txt");
		StdDraw.setPenColor(StdDraw.BLUE);
		testOne.visualiser("scalededitedD1BusRoute2.txt");
		StdDraw.setPenColor(StdDraw.RED); 
		testOne.visualiser("scalededitedD2BusRoute2.txt");
		StdDraw.setPenColor(StdDraw.GREEN);
		testOne.visualiser("scalededitedA2BusRoute2.txt");
	}

}
