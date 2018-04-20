package mypackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class DataReader {
	
	//Input: filepath of any large file and num of lines
	//output: String data
	//if n more than the num of line in the file error
	public static String read_n_lines(String filename,int n) {
		FileReader f;
		String lines ="";
		try {
			f = new FileReader(filename);
			BufferedReader b = new BufferedReader(f);
			for(int i=0;i<n;i++) {
				lines+=b.readLine() + "\n";
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lines;
	}
	
	public static String read_all_lines(String filename) {
		FileReader f;
		String lines ="";
		try {
			f = new FileReader(filename);
			String line ="";
			BufferedReader b = new BufferedReader(f);
			//b.readLine();//remove first line
			int counter = 1;
			while((line=b.readLine())!=null) {
				lines+=line + "\n";
				counter++;
				//if((counter%100)==0) {System.out.println(counter);}
			}
			b.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lines;
	}
	
	
	//IN: filepath
	//OUT: String of distances(with next line)
	public static String readfiletodist(String path) {
		String lines = "";
		int numlines = 0;
		try {
			FileReader f = new FileReader(path);
			String line="";//temp line
			int linenum = 1;
			
			BufferedReader b = new BufferedReader(f);
			numlines = Integer.parseInt(b.readLine()); //num of data points
			String lastline= b.readLine();
			
			while((line = b.readLine())!=null) {
				String D = Double.toString(dist(lastline,line));
				lines+=D + "\n";
				lastline=line;
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return numlines + "\n" + lines;
	}
	
	
	//IN: filepath, min num meters
	//OUT: String of new points(with next line)
	//which are all more than n meters in accumulated distance apart.
	//with their bearing to the next point also writted
	//note: last point will have default 0 bearing
	public static String readfiletoeqipath(String path, int n) {
		String lines = "";
		int linenum = 1;
		try {
			FileReader f = new FileReader(path);
			String line="";//temp line
			double dist = 0;//temp dist
			 
			
			BufferedReader b = new BufferedReader(f);
			b.readLine(); //num of data points, which is not necessary
			String previousline = b.readLine();
			String lasttxtline = previousline;
			lines=cutline(previousline);//new file
			boolean check = true;
			
			while((line = b.readLine())!=null) {
				double D = dist(previousline,line);
				dist+=D;
				if(dist<n){
					check=true;
				}
				else {
					String theta = getDirections(lasttxtline,line);
					lasttxtline = line;
					lines += "," + theta + "\n" + cutline(line);
					
					linenum++;
					dist=0;
					check=false;
				}
				previousline = line;
				
			}
			if(check && dist!=0) {
				String theta = getDirections(lasttxtline,previousline);
				lines+= "," + theta + "\n" + cutline(previousline);//adding last line
			}
			lines += ",0";//adding default bearing
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return linenum + "\n" + lines;
	}
	
	static String cutline(String a) {
		String[] alist = a.split(",");
		Double first = round(Double.parseDouble(alist[0]));
		Double sec = round(Double.parseDouble(alist[1]));
		return Double.toString(first) + "," + Double.toString(sec);
	}
	static String getDirections(String a,String b) {
		String[] alist = a.split(",");
		String[] blist = b.split(",");
		double opposite = Double.parseDouble(blist[1]) - Double.parseDouble(alist[1]);
		double hyp = Double.parseDouble(blist[0]) - Double.parseDouble(alist[0]);
		double theta = Math.toDegrees(Math.atan(opposite/hyp));
		return "" + round(theta);
	}
	
	static double dist(String a, String b) {
		String[] alist = a.split(",");
		String[] blist = b.split(",");
		return pointdist(Double.parseDouble(alist[0]),
				Double.parseDouble(alist[1]),
				Double.parseDouble(blist[0]),
				Double.parseDouble(blist[1]));
	}
	
	// returns lat lon distance in meters
	static double pointdist(double x1,double y1, double x2, double y2) {
		return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2))*111111;
	}
	
	
	static DecimalFormat roundFormatter = new DecimalFormat("########0.0#####");
	public static Double round(Double d){
		//System.out.println("input : " + d);
	    return Double.parseDouble(roundFormatter.format(d));
	}
	
	public static void save(String name, String s) {
		File file = new File(name + ".txt");
	    FileWriter writer = null;
	    try {
	        writer = new FileWriter(file);
	        writer.write(s);
	        writer.close();
	        System.out.println("SAVED");
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	   
	}
	
	
	public static void main (String args[]) {
		String s = readfiletoeqipath("editedD2BusRoute2.txt" , 10);
		save("D2_10m",s);
		
		
		//READING N LINES FROM A LARGE FILE
//		String s = "C:\\Users\\sival\\Desktop\\SIVA\\NUSBUS\\DATA\\vehiclelocation_apr2017.txt";
//		String para = read_n_lines(s,500);
		
		
		//SAVING FILE OF DISTANCES
//		String out = readfiletodist("editedA2BusRoute2.txt");
//		System.out.println(Integer.toString(numlines -1));
//		System.out.println(out);
//		save("A2distances",Integer.toString(numlines -1) , out);
		
	}


	

}
