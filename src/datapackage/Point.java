/**
 * for simplicity, all time are in seconds. 
 */
package datapackage; 

public class Point {
	//pNumber is the Point number
	private double pNumber; 
	private double xCor; 
	private double yCor; 
	private double time; 
	private boolean initialised = false; 

	Point(){
		this(-1, -1, -1, -1);
		this.initialised = false; 
		pNumber = -1; 
	}
	
	Point(double xCor, double yCor){
		this(xCor, yCor, -1, -1);
	}

	Point(double xCor, double yCor, double time){
		this(xCor, yCor, time, -1);
	}
	
	Point(double xCor, double yCor, double time, double pNumber){
		this.xCor = xCor; 
		this.yCor = yCor; 
		this.time = time; 
		this.pNumber = pNumber; 
		this.initialised = true; 
		pNumber = -1; 
	}
	
	public void prdouble(){
		if(initialised == false){
			System.out.println("Yet to be initialised");
			return;
		}
		System.out.println("X-Coordinate: "+this.xCor);
		System.out.println("Y-Coordinate: "+this.yCor); 
		System.out.println("Time from source: "+this.time);
	}
	
	public void setCor(double xCor, double yCor){
		this.xCor = xCor; 
		this.yCor = yCor; 
	}
	
	public double getX(){
		return xCor; 
	}
	
	public double getY(){
		return yCor; 
	}
	
	public void setTime(double time){
		this.time = time; 
	}
	
	public double getTime(){
		return time; 
	}
	
	public double getPointNumber(){
		return this.pNumber;
	}
	
	public void setPointNumber(double pNumber){
		this.pNumber = pNumber; 
	}

}
