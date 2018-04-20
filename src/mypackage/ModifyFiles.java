package mypackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ModifyFiles {
	
	
	//A2_10m to the CD format(_formatted)
	public static void mod1(String path) {
		try {
			FileReader f = new FileReader(path);
			BufferedReader b = new BufferedReader(f);
			String line; //temp var
			File file = new File("D2_10m_formated.txt");
			FileWriter fw = new FileWriter(file);
			fw.write(b.readLine());
			while((line = b.readLine())!=null) {
				fw.write("\nD2,-,1," + line);
			}
			fw.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	//busstops1 to CD format
	static void mod2(String path) {
		try {
			FileReader f = new FileReader(path);
			BufferedReader b = new BufferedReader(f);
			String line; //temp var
			File file = new File("busstops1_formated.txt");
			FileWriter fw = new FileWriter(file);
			int i;
			while((line = b.readLine())!=null) {
				if(line.contains("caption")) {
					i = line.indexOf(":");
					String bus = line.substring(i+3, line.length()-2);
					fw.write("\n" + bus + ",-,1,");
					
					String lat = b.readLine();
					String log = b.readLine();
					i = lat.indexOf(":");
					lat= lat.substring(i+2, lat.length()-2);
					i = log.indexOf(":");
					log= log.substring(i+2, log.length()-2);
					fw.write(lat + "," + log);
					
				}
			}
			fw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//busstops1 to {1.234234, 2.234234, ...}*2 and {Utown,KR,...} and 
	static void mod3(String path) {
		try {
			FileReader f = new FileReader(path);
			BufferedReader b = new BufferedReader(f);
			String line; //temp var
			int i;
			String a = "{",q = "{",c = "{";
			while((line = b.readLine())!=null) {
				if(line.contains("caption")) {
					i = line.indexOf(":");
					a+=line.substring(i+3, line.length()-2) + ", ";
					
					String lat = b.readLine();
					String log = b.readLine();
					i = lat.indexOf(":");
					q+= lat.substring(i+2, lat.length()-2) + ", ";
					i = log.indexOf(":");
					c+= log.substring(i+2, log.length()-2) + ", ";
					
				}
				
			}
			a+="}";q+="}";c+="}";
				System.out.println(a);
				System.out.println(q);
				System.out.println(c);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//input: 4 arrays of the busstops
	//output: CD format data
	static void mod4(String path){
		String[] A = new String[]{"AS7", "BIZ 2", "Botanic Gardens MRT", "BTC - Oei Tiong Ham Building", "Central Library", "College Green Hostel", "COM2 (CP13)", "Computer Centre", "Kent Ridge Bus Terminal", "Kent Ridge MRT", "LT13", "LT29", "Museum", "Opp Block EA", "Opp HSSML", "Opp Kent Ridge MRT", "Opp NUSS", "Opp PGP Hse No 12", "Opp UHall", "Opp University Health Centre", "Opp YIH", "PGP Hse No 12", "PGP Hse No 14 and No 15", "PGP Hse No 7", "PGPR", "Prince George's Park Terminal", "Raffles Hall", "S17", "The Japanese School", "UHall", "University Health Centre", "University Town", "Ventus (Opp LT13)", "YIH"};
	    double[] B = new double[]{1.29361105d, 1.293333054d, 1.32249999d, 1.319722056d, 1.29666698d, 1.323333025d, 1.294167042d, 1.296944022d, 1.294167042d, 1.294721961d, 1.294443965d, 1.297222018d, 1.300832987d, 1.300277948d, 1.292778015d, 1.294721961d, 1.293055534d, 1.29361105d, 1.297222018d, 1.298611045d, 1.298611045d, 1.29361105d, 1.293056011d, 1.293056011d, 1.290832996d, 1.291944027d, 1.300832987d, 1.297222018d, 1.300555944d, 1.297222018d, 1.298611045d, 1.30361104d, 1.295277953d, 1.298889041d};
	    double[] C = new double[]{103.7719421d, 103.7750015d, 103.8150024d, 103.8177795d, 103.7722244d, 103.8161087d, 103.773613d, 103.7724991d, 103.769722d, 103.7841644d, 103.7705536d, 103.7808304d, 103.773613d, 103.7699966d, 103.7750015d, 103.7844467d, 103.7722244d, 103.776947d, 103.7780533d, 103.7755585d, 103.7738876d, 103.776947d, 103.7777786d, 103.7777786d, 103.7808304d, 103.7802734d, 103.7727814d, 103.7805557d, 103.769722d, 103.7786102d, 103.7761078d, 103.7744446d, 103.7702789d, 103.7741699d};
	    String[] D = new String[]{"AS7", "BIZ2", "BG-MRT", "BUKITTIMAH-BTC2", "CENLIB", "CGH", "COM2", "COMCEN", "KR-BT", "KR-MRT", "LT13", "LT29", "MUSEUM", "BLK-EA-OPP", "HSSML-OPP", "KR-MRT-OPP", "NUSS-OPP", "PGP12-OPP", "UHALL-OPP", "STAFFCLUB-OPP", "YIH-OPP", "PGP12", "PGP14-15", "PGP7", "PGP", "PGPT", "RAFFLES", "S17", "JAPSCHOOL", "UHALL", "STAFFCLUB", "UTOWN", "LT13-OPP", "YIH"};
	    
		System.out.println(A.length + " " +B.length + " " +C.length + " " +D.length + " " );
		try {
			File file = new File("busstops_formated_oldapp.txt");
			FileWriter f = new FileWriter(file);
			BufferedWriter b = new BufferedWriter(f);
			String line;//temp line
			int counter=0;
			for(int i=0;i<34;i++) {
				b.write(A[i]+",-,1,"+B[i]+","+C[i]+"\n");
			}
			b.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//convert CD formatted file into arraylist coordinates
	//A@_10m_formatted ---> {{lat,lon,angle},{...}, {...},..}
	//use replace to format
	//(to input into NUSAPP Constants folder)
	public static ArrayList<ArrayList<Double>> mod5(String filename) {
			FileReader f;
			ArrayList<ArrayList<Double>> lat = new ArrayList<ArrayList<Double>>();
			
			try {
				f = new FileReader(filename);
				String line ="";
				BufferedReader b = new BufferedReader(f);
				while((line=b.readLine())!=null) {
					double a = CDDataSorter.getlat(line);
					double h = CDDataSorter.getlon(line);				
					double j = (double) CDDataSorter.getangle(line);
					ArrayList<Double> t = new ArrayList<Double>(){{add(a);add(h);add(j);}};
					lat.add(t);
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return lat;
			
		}
	
	//converts a CD formatted path into minimum 10m path
	public static String mod6(String path, int n) {
		String lines = "";//resut
		try {
			FileReader f = new FileReader(path);
			String line="";//temp line
			double dist = 0;//temp dist
			 
			
			BufferedReader b = new BufferedReader(f);
			b.readLine(); //num of data points, which is not necessary
			String previousline = b.readLine();
			String lasttxtline = previousline;
			lines = previousline;//new file
			boolean lessThanNMeters = true;
			
			while((line = b.readLine())!=null) {
				if(CDDataSorter.isvalid(line)) {
					double D = CDDataSorter.pointdist(previousline,line);
					dist+=D;
					if(dist<n){
						lessThanNMeters=true;
					}
					else {
						String theta = Integer.toString(CDDataSorter.angle(lasttxtline,line));
						lasttxtline = line;
						lines += "," + theta + "\n" + line;
						
						dist=0;
						lessThanNMeters=false;
					}
					previousline = line;
				}
				
			}
			if(lessThanNMeters && dist!=0) {
				String theta = Integer.toString(CDDataSorter.angle(lasttxtline,previousline));
				lines+= "," + theta + "\n" + previousline;//adding last line
			}
			lines += ",0";//adding default bearing
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lines;
	}
	//converts a route from the android studio porject
	//eg. {{1.2, 103.77, 204}, {1.2, 103.731, 213},...} into 
	//a CD formatted file for printing on the map
	public static String mod7(double[][] route) {
		String s = "";
		for(double[] cood: route) {
			//A2,-,1,1.291939,103.780326,-39.097573
			s+= "-,2017-01-03 08:10:05,1," + cood[0] + "," + cood[1] + ",-,-,2017-01-03 08:10:05,-\n";
 		}
		return s;
	}
	
	//converts 4 arrays of the busstop details in to a combined array
	public static void mod8(String[] a, double[] c,double[] d){
		System.out.println("{");
		for(int i=0;i<a.length;i++) {
			System.out.println("{\""+a[i]+", busstop\", \"\", " + "" + c[i] + ", " + d[i] + "},");
		}
		System.out.println("}");
	}
	
	public static void main(String[] args) {
		String[] CAPTION_LIST = new String[]{"AS7", "BIZ 2", "Block EA", "Botanic Gardens MRT", "BTC - Oei Tiong Ham Building", "Central Library", "College Green Hostel", "COM2 (CP13)", "Computer Centre", "Kent Ridge Bus Terminal", "Kent Ridge MRT", "Kent Vale", "LT13", "LT29", "Museum", "Opp Block EA", "Opp HSSML", "Opp Kent Ridge MRT", "Opp NUSS", "Opp PGP Hse No 12", "Opp UHall", "Opp University Health Centre", "Opp YIH", "PGP Hse No 12", "PGP Hse No 14 and No 15", "PGP Hse No 7", "PGPR", "Prince George's Park", "Raffles Hall", "S17", "UHall", "University Health Centre", "University Town", "Ventus (Opp LT13)", "YIH", "NUS Fac of Design and Env", "Opp Ayer Rajah Ind Est", "Aft Anglo-Chinese JC", "Opp NUS Fac of Architect"};

	    String[] NAME_LIST = new String[]{"AS7", "BIZ2", "BLK-EA", "BG-MRT", "BUKITTIMAH-BTC2", "CENLIB", "CGH", "COM2", "COMCEN", "KR-BT", "KR-MRT", "KV", "LT13", "LT29", "MUSEUM", "BLK-EA-OPP", "HSSML-OPP", "KR-MRT-OPP", "NUSS-OPP", "PGP12-OPP", "UHALL-OPP", "STAFFCLUB-OPP", "YIH-OPP", "PGP12", "PGP14-15", "PGP7", "PGP", "PGPT", "RAFFLES", "S17", "UHALL", "STAFFCLUB", "UTOWN", "LT13-OPP", "YIH", "NIL", "NIL", "NIL", "NIL"};

	    double[] LATITUDE_LIST = new double[]{1.293611049652, 1.2933399741193, 1.3005000352859, 1.3227000236511, 1.3191000223159, 1.2963999509811, 1.3233330249786, 1.2941670417785, 1.2975000143051, 1.2947000265121, 1.2937999963760, 1.3019551412708, 1.2942999601364, 1.2973999977111, 1.3010643554673, 1.3004000186920, 1.2927780151367, 1.293699979782, 1.2933000326156, 1.2937999963760, 1.2975000143051, 1.2987999916076, 1.2991000413894, 1.293611049652, 1.2930560111999, 1.2932000160217, 1.2908329963684, 1.2919440269470, 1.3010286952978, 1.2974965572357, 1.2976000308990, 1.2989187240600, 1.3036110401153, 1.2953000068664, 1.2986999750137, 1.296426, 1.297618, 1.304165, 1.296188};
	    double[] LONGITUDE_LIST = new double[]{103.77194213867, 103.77515971660, 103.7701034545, 103.81510162353, 103.81680297851, 103.77220153808, 103.81630706787, 103.77361297607, 103.77290344238, 103.76979827880, 103.78489685058, 103.76938068866, 103.77079772949, 103.78089904785, 103.77373647689, 103.7701034545, 103.77500152587, 103.78520202636, 103.77239990234, 103.77700042724, 103.77829742431, 103.77549743652, 103.77429962158, 103.77694702148, 103.77777862548, 103.77780151367, 103.78083038330, 103.780273437, 103.77270555496, 103.78138732910, 103.77790069580, 103.77605438232, 103.77444458007, 103.77059936523, 103.77429962158, 103.770669, 103.785361, 103.787929, 103.770392};
	    mod8(CAPTION_LIST, LATITUDE_LIST, LONGITUDE_LIST);
		System.out.println("done");
		
//		String path2 = "C_10m_formatted.txt";
//		System.out.println(mod5(path2));
	}
}
