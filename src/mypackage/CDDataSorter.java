package mypackage;

import java.awt.Color;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CDDataSorter {
	
	//veh_plate,datetime,pos_valid,latitude,longitude,speed,direction,gps_time
	static String getbusid(String line) {
		return line.split(",")[0];
	}	
	static String getdatetime(String line) {
		return line.split(",")[1];
	}
	static String getdate(String line) {
		return getdatetime(line).split(" ")[0];
	}
	static String gettime(String line) {
		return getdatetime(line).split(" ")[1];
	}
	static String getgpsdatetime(String line) {
		return line.split(",")[7];
	}
	static String getgpsdate(String line) {
		return getgpsdatetime(line).split(" ")[0];
	}
	static String getgpstime(String line) {
		return getgpsdatetime(line).split(" ")[1];
	}
	static Boolean isvalid(String line) {
		return (line.split(",")[2]).equals("1");
	}	
	static Double getlat(String line) {
		return Double.parseDouble(line.split(",")[3]);
	}	
	static Double getlon(String line) {
		return Double.parseDouble(line.split(",")[4]);
	}	
	//extra mtd
	static int getangle(String line) {
		return Integer.parseInt(line.split(",")[8]); 
	}
	
	//static HashMap<String, Double[]> H = new HashMap<String, Double[]>();
	
	//takes in file of all busses in a particular month and the folder you want to send all the 
	//broken down files to
	//saves each busid as a file in the folder
	public static void readSortSaveAllLines(String filename,String folder) {
		FileReader f;
		String lines ="";
		try {
			f = new FileReader(filename);
			String line ="";
			BufferedReader b = new BufferedReader(f);
			b.readLine();//remove first line
			int counter = 1;
			ArrayList<String> busids = new ArrayList<String>();
			String busid;
			
			while((line=b.readLine())!=null) {
				busid = getbusid(line);
				if(busids.contains(busid)) {//just edit the existing file
					BufferedWriter bw = new BufferedWriter(new FileWriter(folder + "\\" + busid + ".txt",true));
					bw.write("\n" + line);
					bw.close();
				}else {
					DataReader.save(folder + "\\" + busid , line);
					busids.add(busid);
				}
				counter++;
				if((counter%1000)==0) {System.out.println(counter);}
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("ALL SAVED");
	}
	
	static Double R;
	static Double RADIUSSCALE =150.0;//the larger this is, the smaller the circle
	static int CANVASSIZE = 1000;
	static double BOARDER = 0.0;
	static int SLEEP = 100;
	static Color COLOR1 = Color.BLACK;
	static Color COLOR2 = Color.RED;
	
	//PRINTS FILE IN JSWING
	//prints the path of file coordinates 
	//and refreshes each day
	//can put in null input if no picture or coordinates
	//coordinates: (botL y,x , topR y,x)
	static void paintFile(String path, String pic,double[] coordinates, double[] boundaries){
		if(boundaries ==null) {
			boundaries = new double[] {0,0,0};
		}
		if(pic!=null) {
			Image image = StdDraw.getImage(pic);
	        int ws = image.getWidth(null);
	        int hs = image.getHeight(null);
	        System.out.println(ws + " " + hs);
	        StdDraw.setCanvasSize(ws, hs);
			StdDraw.picture(0.5, 0.5, pic);
			System.out.println("picture drawn");
		}else {
			StdDraw.setCanvasSize(CANVASSIZE, CANVASSIZE);
		}
		
		StdDraw.BORDER = BOARDER;
		StdDraw.setPenColor(COLOR1);
		if(coordinates!=null) {
			StdDraw.setXscale(coordinates[1], coordinates[3]);
			StdDraw.setYscale(coordinates[0], coordinates[2]);
			R=(coordinates[2]-coordinates[0])/RADIUSSCALE;
		}else {
			Double[] extremes = getextremes(path);
			System.out.println("EXTREMES: " + Arrays.toString(extremes));
			StdDraw.setXscale(extremes[2], extremes[3]);
			StdDraw.setYscale(extremes[0], extremes[1]);
			R=(extremes[1]-extremes[0])/RADIUSSCALE;
		}
		
		Double[] canvpara = {StdDraw.ymin,StdDraw.ymax,StdDraw.xmin,StdDraw.xmax};
		System.out.println("CANV PARA: " +  Arrays.toString(canvpara));
		
		
		try {
			FileReader f = new FileReader(path);
			BufferedReader b = new BufferedReader(f);
			String line;//temp line
			String prevline;
			Boolean colorswitch=true;
			
			line = b.readLine();
			StdDraw.filledCircle(getlon(line), getlat(line), R);//assume first line valid
			StdDraw.circle(boundaries[1], boundaries[0], boundaries[2]/111111);
			prevline =line;
			String today = getdate(line);System.out.println(today);
			int i=1;
			while((line = b.readLine())!=null){
				if(isvalid(line)) {
					if(isWithin(line,boundaries)){
						StdDraw.clear();
						StdDraw.picture((StdDraw.xmin+StdDraw.xmax)/2, (StdDraw.ymin+StdDraw.ymax)/2, pic);
						StdDraw.circle(boundaries[1], boundaries[0], boundaries[2]/111111);
					}
					if(colorswitch) {
						StdDraw.setPenColor(COLOR2);colorswitch=false;
					}else {
						StdDraw.setPenColor(COLOR1);colorswitch=true;
						
					}
					System.out.println(i++ + "," + timediff(gettime(prevline),gettime(line))+ "," + timediff(getgpstime(prevline),getgpstime(line)));
					StdDraw.line(getlon(prevline), getlat(prevline),getlon(line), getlat(line));
					StdDraw.filledCircle(getlon(line), getlat(line), R);
					prevline =line;
					Thread.sleep(SLEEP);
				}
				else {System.out.println("invalidpoint");}
			}
			b.close();
			System.out.println("PRINTED");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	//get extreme values of the graph and sets the canvas size
	static Double[] getextremes(String path) {
		double minlat = 1000,maxlat = 0, minlon =1000,maxlon = 0;
		try {
			FileReader f = new FileReader(path);
			BufferedReader b = new BufferedReader(f);
			String line;
			while((line = b.readLine())!=null) {
				if(!isvalid(line)) {continue;}
				if(getlat(line)>maxlat) {maxlat=getlat(line);}
				if(getlat(line)<minlat) {minlat=getlat(line);}
				if(getlon(line)>maxlon) {maxlon=getlon(line);}
				if(getlon(line)<minlon) {minlon=getlon(line);}
			}
			b.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Double[] array =  {minlat,maxlat,minlon,maxlon};
		return array;
	}
	
	//assuming b>a and time is <30s diff
	static int timediff(String a,String b) {
		int first = Integer.parseInt(a.split(":")[2]);
		int later = Integer.parseInt(b.split(":")[2]);
		if(first<later) {return later-first;}
		else {return 60-first+later;}
	
	}
	
	//says if the line is within the boundaries
	static boolean isWithin(String line,double[] bounds) {
		double lat = getlat(line);
		double lon = getlon(line);
		return pointdist(lat,lon,bounds[0],bounds[1])<bounds[2];
	}
	
	// returns lat lon distance in meters
	static double pointdist(double x1,double y1, double x2, double y2) {
		return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2))*111111;
	}
	
	//return distance between lines in meters
	static double pointdist(String line1, String line2) {
		return pointdist(getlon(line1),
				getlat(line1),
				getlon(line2),
				getlat(line2));
	}
	
	//return angle between two lines
	static int angle(String line1,String line2) {
		return angle(getlat(line1), getlon(line1), getlat(line2), getlon(line2));
	}
	
	static int angle(double y1, double x1, double y2, double x2) {
		double opp = x2-x1;
		double adj = y2-y1;
		int theta = (int) Math.toDegrees(Math.atan(opp/adj));
		
		if(opp>0 && adj>0) {
			return theta;
		}else if(opp<0 && adj>0){
			return 360 + theta;
		}else{//if adj<0
			return 180 + theta;
		}
	}

	//nothing so far
	public static void splitFilesByService(String path) {
		//NOTHING
	}
	
	public static void pathConstructor(String pic,double[] coordinates, String path) {
		Image image = StdDraw.getImage(pic);
        int ws = image.getWidth(null);
        int hs = image.getHeight(null);
        System.out.println(ws + " " + hs);
        StdDraw.setCanvasSize(ws, hs);
		StdDraw.picture(0.5, 0.5, pic);
		System.out.println("picture drawn");
		
		StdDraw.BORDER = BOARDER;
		StdDraw.setPenColor(COLOR1);
		
		StdDraw.setXscale(coordinates[1], coordinates[3]);
		StdDraw.setYscale(coordinates[0], coordinates[2]);
		R=(coordinates[2]-coordinates[0])/RADIUSSCALE;
		
		//draw busstops
		try {
			FileReader f = new FileReader(path);
			BufferedReader b = new BufferedReader(f);
			String line; //TEMP VAR
			while((line = b.readLine())!=null){
				StdDraw.filledCircle(getlon(line), getlat(line), R/2);
			}
			b.close();
			System.out.println("PRINTED");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		double previousY = 0, previousX = 0;
		boolean first = true;
		System.out.print("{");
		while(true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while(!StdDraw.mousePressed()) {}
			//now mouse is pressed
			if(first) {first = false;}
			else {
				System.out.print(angle(previousY, previousX,StdDraw.mouseY(), StdDraw.mouseX()) + "}, " );
			}
			
			System.out.print("{" + StdDraw.mouseY() + ", " + StdDraw.mouseX() + ", ");
			StdDraw.circle(StdDraw.mouseX(), StdDraw.mouseY(), R/2);
			previousY = StdDraw.mouseY();
			previousX = StdDraw.mouseX();
			
		}
	}
	
	public static void main(String[] args) {
		
		//PAINTING FILE
//		String path = "test.txt";
//		paintFile(path,"Pictures/NUS.PNG",CONSTANTS.NUSboundaries, null);
		
		//PATH CONSTRUCTOR
		pathConstructor("Pictures/NUS.PNG",CONSTANTS.NUSboundaries, "finalinfo/busstops1_formatted.txt");
		
		
		
		
		
		
		//PREVIOUS WORK
		
		//SEGMENT MONTH DATA
//		String path = "C:\\Users\\sival\\Desktop\\SIVA\\NUSBUS\\DATA\\vechlelocations2017half\\vehiclelocation_jan2017.txt";
//		CDDataSorter.readsortsave_all_lines(path,"JanBusSorted");
	}
	
}
