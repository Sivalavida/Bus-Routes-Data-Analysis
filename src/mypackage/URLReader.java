package mypackage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;

//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;

public class URLReader {
	
	
	public static void urlReader(String urlstring) {
		URL url;
		try {
			url = new URL(urlstring);
		
	        HttpURLConnection yc = (HttpURLConnection) url.openConnection();
	        System.out.println("initiating buffered reader");
	        BufferedReader in = new BufferedReader(
	                                new InputStreamReader(
	                                yc.getInputStream()));
	        String i;//TEMP VAR
	        System.out.println("going to read");
	        while ((i = in.readLine()) != null) {
	        	System.out.println(i);
	        	System.out.println("------------");
	        }
        } catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void readLTAData() {
		URL url;
		try {
//			url = new URL("http://datamall2.mytransport.sg/ltaodataservice/BusArrival?SST=True&BusStopID=50011");
			url = new URL("http://datamall2.mytransport.sg/ltaodataservice/BusArrivalv2?BusStopCode=83139&ServiceNo=15");
	        HttpURLConnection yc = (HttpURLConnection) url.openConnection();
	        yc.setRequestMethod("GET");
	        yc.setRequestProperty("AccountKey", "QgivWz7gTM6Ykcu9TUuk6Q==");
	        yc.setRequestProperty("accept", "application/json");
	        
	        System.out.println("initiating buffered reader");
	        BufferedReader in = new BufferedReader(
	                                new InputStreamReader(
	                                yc.getInputStream()));
	        String i;
	        System.out.println("going to read");
	        while ((i = in.readLine()) != null) {//there is only one line
	        	System.out.println(i);
	        }	
	        in.close();
	        System.out.println("done");
        } catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		urlReader("https://siva922.wixsite.com/testingsite");
		readLTAData();
		//
    }

}
