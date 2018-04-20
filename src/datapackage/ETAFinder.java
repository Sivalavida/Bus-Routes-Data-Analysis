package datapackage; 
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

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
public class ETAFinder {
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
	
	public ETAFinder(){
		gradient = null; 
		data = null; 
		constant = null; 
		//Last stop is the earlier stop between the two points that the bus was
		//last check in as. 
		lastPoint = null; 
	}
	
	/**
	 * Generate a gradient between every two point, this is to facilitate calculating shortest perpendicular 
	 * distance. 
	 * 
	 */
	public void createGradient(){
		gradient = new double[data.length - 1];
		for(int i = 0; i < data.length - 1; i++){
			double xOne = data[i].getX(); 
			double yOne = data[i].getY(); 
			double xTwo = data[i+1].getX(); 
			double yTwo = data[i+1].getY(); 
			
			double xDisplacement = xTwo - xOne; 
			double yDisplacement = yTwo - yOne; 
			gradient[i] = yDisplacement/xDisplacement; 
		}
	}
	
	/**
	 * given the gradient of a line and a point of that line,
	 * return the C. Where C is the C in y = mx + C; 
	 * @param gradient
	 * @param xCor
	 * @param yCor
	 * @return
	 */
	public double getC(double gradient, double xCor, double yCor){
		return (yCor - gradient*xCor);
	}
	
	/**
	 * Given the equation of two lines, return the point of intercept 
	 * @param mOne mTwo are gradients of the first line and second line respectively. 
	 * @param cOne and cTwo are the C constant of the first line and the second line respectively. 
	 * correspond to the linear line equation: y = mx + C
	 * @return the point of intercept. 
	 */
	public Point getIntercept(double mOne, double cOne, double mTwo, double cTwo){
		double xCor = (cTwo - cOne)/(mOne - mTwo);
		double yCor = mOne*xCor + cOne; 
		return new Point(xCor, yCor); 
	}
	

	public double getDistance(Point pointOne, Point pointTwo){
		double xOne = pointOne.getX(); 
		double yOne = pointOne.getY(); 
		double xTwo = pointTwo.getX(); 
		double yTwo = pointTwo.getY(); 
		double xDisplacement = xTwo - xOne; 
		double yDisplacement = yTwo - yOne;
		return Math.sqrt(xDisplacement*xDisplacement + yDisplacement*yDisplacement);
	}
	
	//Populates the aray of constants from the Y = mX + C line for all stops on a graph of the school
	public void createC(){
		constant = new double[data.length - 1];
		for(int i = 0; i < data.length - 1; i++){
			double xOne = data[i].getX(); 
			double yOne = data[i].getY();  
			double m_gradient = gradient[i];
			constant[i] = getC(m_gradient, xOne, yOne); 
		}
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
				//Create the point using the data in the form of |xCor, yCor, time|
				line = buff.readLine(); 
				String[] lineInfo = line.split(" ");
				double xCor = Double.parseDouble(lineInfo[0]); 
				double yCor = Double.parseDouble(lineInfo[1]);
				double time = Double.parseDouble(lineInfo[2]);
				Point newPoint = new Point(xCor, yCor, time, i);// i is the point number (0 indexed)
				data[i] = newPoint; 
			}
			buff.close(); 
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	
	//Takes in 2 points and returns the gradient of the line joining them
	public double getGradient(Point pointOne, Point pointTwo){
		double xOne = pointOne.getX(); 
		double yOne = pointOne.getY();
		double xTwo = pointTwo.getX(); 
		double yTwo = pointTwo.getY(); 
		double m_gradient = (yTwo - yOne)/(xTwo - xOne);
		return m_gradient; 
	}
	
	/**
	 * Check if test lies within pointOne and pointTwo. Note that all
	 * three point must be colinear. 
	 * @param pointOne
	 * @param pointTwo
	 * @param test
	 * @return
	 */
	public boolean gotWithin(Point pointOne, Point pointTwo, Point test){
		//check if there are collinear. 
		if(getGradient(pointOne, pointTwo) != getGradient(pointTwo, test)){
			//Need to identify what is considered as close enough. 
	//		System.out.println("M1 = "+getGradient(pointOne, test)+" ; M2 = "+getGradient(pointTwo, test));
	//		System.out.println("Three points are not collinear");
		}
		System.out.print(" test.getX() = "+test.getX()); 
		System.out.print(" pointOne.getX() = "+pointOne.getX()); 
		System.out.print(" pointTwo.getX() = "+pointTwo.getX()); 
		//does this work if it is a horizontal line?
		if(test.getX() > pointOne.getX() && test.getX() > pointTwo.getX()){
			return false; 
		}
		if(test.getX() < pointOne.getX() && test.getX() < pointTwo.getX()){
			return false; 
		}
		return true; 
	}
	
	/**
	 * Given the coordinate of the current bus location, determine where is the next
	 * point. Includes the XY coordinate and the time. 
	 * Ignore time for now, just check every point.
	 * @param xCor
	 * @param yCor
	 * @return
	 */
	public int findTwoPoints(Point point){
		double minDistance = Integer.MAX_VALUE; 
		
		int minPoint = -1; 
		for(int i = 0; i < data.length -1; i++){
			//find point of intercept. 
			double mOne = gradient[i];
			double cOne = constant[i];
			double mTwo = (1/mOne) * -1; 
			double cTwo = getC(mTwo, point.getX(), point.getY());
			//intPoint is the point of intersection; 
			Point intPoint = getIntercept(mOne, cOne, mTwo, cTwo);
			double tempDistance = getDistance(intPoint, point);
			System.out.print("i = "+i+" ");
			System.out.print("tempDistance = "+tempDistance);
			//check if it lies outside of the two points. Ask me to elaborate further. 
			if(gotWithin(data[i], data[i+1], intPoint) == false){
				System.out.print(" gotWithin ");
				double secondDistance = getDistance(intPoint, data[i]);
				double thirdDistance = getDistance(intPoint, data[i+1]);
				double tempMinDistance = thirdDistance; 
				if(secondDistance < thirdDistance){
					tempMinDistance = secondDistance; 
				}
				if(tempMinDistance > tempDistance){
					System.out.println(" i = "+i);
					tempDistance = tempMinDistance; 
				}
				System.out.print(" tempMinDistance = "+tempMinDistance);
			}
			if(tempDistance < minDistance){
				minPoint = i; 
				minDistance = tempDistance; 
			}
			System.out.println(); 
		}
		this.currDataPoint = minPoint; 
		return minPoint; 
	}
	
	public void printPoint(){
		for(int i = 0; i < data.length; i++){
			System.out.print("Point "+i+" has X-Coordinate: " +data[i].getX()+ " Y-Coordinate: "+data[i].getY());
			System.out.println(" time: "+data[i].getTime());
		}
	}
	
	public void printGradient(){
		for(int i = 0; i < gradient.length; i++){
			System.out.println("Line "+i+" has gradient: "+gradient[i]); 
		}
	}
	
	public void printConstant(){
		for(int i = 0; i < constant.length; i++){
			System.out.println("Equation "+i+" : y = "+gradient[i]+"x + "+constant[i]); 
		}
	}
	
	
	public double getETA(Point curr, Point dest){
		this.currPoint = curr; 
		this.destPoint = dest; 
		int currStop = findTwoPoints(curr);
		double mOne = gradient[currStop];
		double cOne = constant[currStop];
		double mTwo = (1/mOne) * -1; 
		double cTwo = getC(mTwo, curr.getX(), curr.getY());
		//intcurr is the point of intersection; 
		Point intPoint = getIntercept(mOne, cOne, mTwo, cTwo);
		
		double distOne = intPoint.getX() - data[currStop].getX(); //difference in xCoor to prev stop
		double distTwo = intPoint.getX() - data[currStop + 1].getX();//difference in xCoor to next stop
		double distThree = data[currStop].getX() - data[currStop + 1].getX();//difference in xCoor between prev stop and next stop
		//make all xCoor difference values positive
		if(distOne < 0){
			distOne = distOne * -1; 
		}
		if(distTwo < 0){
			distTwo = distTwo * -1; 
		}
		if(distThree < 0){
			distThree = distThree * -1; 
		}
		double timeOne = data[currStop].getTime();//time from start for prev stop 
		double timeTwo = data[currStop + 1].getTime();//time from start for next stop
		double eta = timeOne + ((timeTwo - timeOne)*distOne)/distThree;// time from start to prev stop  + fraction of time taken from prev to next stop
		this.currPerPoint = intPoint; 
		return dest.getTime() - eta; 
	}
	
	public void visualiser(){
		StdDraw.setCanvasSize(500, 500);
		StdDraw.setPenColor(StdDraw.BLUE);
		StdDraw.setScale(0, 200);
		for(int i = 0; i < data.length; i++){
			double x = data[i].getX(); 
			double y = data[i].getY();
			
			if(i == destPoint.getPointNumber()){
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.circle(x, y, 5); 
			} else {
				StdDraw.setPenColor(StdDraw.BLUE);
				StdDraw.circle(x, y, 5);
			}
			
			boolean toDraw = true; 
			if(i == data.length-1){
				toDraw = false; 
			}
			if(i == currDataPoint){
				toDraw = false;
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.line(data[i].getX(), data[i].getY(), data[i+1].getX(), data[i+1].getY());
			}
			if(toDraw){
				StdDraw.setPenColor(StdDraw.BLUE);
				StdDraw.line(data[i].getX(), data[i].getY(), data[i+1].getX(), data[i+1].getY());
				//StdDraw.setPenColor(StdDraw.BLUE);
			}
		}
		//draw the current location
		StdDraw.setPenColor(StdDraw.GREEN);
		StdDraw.circle(currPoint.getX(), currPoint.getY(), 5);
		
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.circle(currPerPoint.getX(), currPerPoint.getY(), 5);
	}
	
	
	public void run(String fileName){
		inputPoint(fileName);
		createGradient(); 
		createC();
	//	printPoint();
	//	printGradient(); 
	//	printConstant(); 
	}
	public static void main (String[] args){
		ETAFinder testOne = new ETAFinder(); 
		String fileName = "data.txt";
		testOne.run(fileName);
		Point testPoint = new Point(70, 50); 
		System.out.println(Arrays.toString(testOne.data));
		Point testDest = testOne.data[4];
		System.out.println(testOne.findTwoPoints(testPoint));
		System.out.print("eta = " +testOne.getETA(testPoint, testDest));
		System.out.println(" seconds");
		testOne.visualiser(); 
	}

}
