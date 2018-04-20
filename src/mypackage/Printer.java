package mypackage;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class Printer {
	
	static Double R;
	static Double RADIUSSCALE =200.0;
	static int CANVASSIZE = 1000;
	static double BOARDER = 0.005;
	
	static void printfile(String path) {
		printfile(path,StdDraw.BLACK);
	}
	
	//PRINTS FILE IN JSWING
	static void printfile(String path,Color c) {
		Double[] extremes = getextremes(path);
		System.out.println("EXTREMES: " + Arrays.toString(extremes));
		StdDraw.setCanvasSize(CANVASSIZE, CANVASSIZE);
		StdDraw.BORDER = BOARDER;
		StdDraw.setXscale(extremes[2], extremes[3]);
		StdDraw.setYscale(extremes[0], extremes[1]);
		StdDraw.setPenColor(c);
		Double[] canvpara = {StdDraw.ymin,StdDraw.ymax,StdDraw.xmin,StdDraw.xmax};
		System.out.println("CANV PARA: " +  Arrays.toString(canvpara));
		R=(extremes[1]-extremes[0])/RADIUSSCALE;
		
		try {
			FileReader f = new FileReader(path);
			BufferedReader b = new BufferedReader(f);
			String line;//temp line
			Double[] linedata;//temp data
			Double[] prevlinedata;
			b.readLine();//num of data points, which is not necessary
			
			linedata = convert(b.readLine());
			StdDraw.filledCircle(linedata[1], linedata[0], R);
			prevlinedata = linedata;
			while((line = b.readLine())!=null){
				linedata = convert(line);
				StdDraw.line(prevlinedata[1], prevlinedata[0], linedata[1], linedata[0]);
				StdDraw.filledCircle(linedata[1], linedata[0], R);
				prevlinedata = linedata;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	//get extreme values of the graph and sets the canvas size
	//static double scale = 90;
	static Double[] getextremes(String path) {
		double minlat = 1000,maxlat = 0, minlon =1000,maxlon = 0;
		try {
			FileReader f = new FileReader(path);
			BufferedReader b = new BufferedReader(f);
			String line;
			Double[] doubledata;
			b.readLine();//num of data points, which is not necessary
			while((line = b.readLine())!=null) {
				doubledata = convert(line);
				if(doubledata[0]>maxlat) {maxlat=doubledata[0];}
				if(doubledata[0]<minlat) {minlat=doubledata[0];}
				if(doubledata[1]>maxlon) {maxlon=doubledata[1];}
				if(doubledata[1]<minlon) {minlon=doubledata[1];}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
//		Double addlat = (maxlat -minlat)*(100-scale)/(2*scale);
//		Double addlon = (maxlon -minlon)*(100-scale)/(2*scale);
//		Double[] array =  {minlat-addlat,
//				maxlat+addlat,
//				minlon-addlon,
//				maxlon+addlon};
		Double[] array =  {minlat,maxlat,minlon,maxlon};
		
		return array;
	}
	
	
	//convert a line of data into double[] data
	static Double[] convert(String line) {
		String[] s = line.split(",");
		Double[] ans = new Double[3];
		for(int i=0;i<3;i++) {
			ans[i]= Double.parseDouble(s[i]);
		}
		return ans;
	}
	
	public static void main(String[] args) {
		String path = "A2_10m.txt";
		printfile(path,StdDraw.BLUE);
		path = "D2_10m.txt";
		printfile(path,StdDraw.GREEN);
//		 StdDraw.setCanvasSize(512, 512);
//		 StdDraw.setXscale(0, 1);
//		 StdDraw.setYscale(0, 1);
//		 StdDraw.circle(1, 1, 1);
		
	}

}
