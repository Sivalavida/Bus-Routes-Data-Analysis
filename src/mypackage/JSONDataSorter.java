package mypackage;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONDataSorter {
	
	
	//modifies NUSLocations file
	//note: there will be an extra ccomma at the end
	public static void modData(String path) {
		
		try {
			String lines = DataReader.read_all_lines(path);
			JSONParser p = new JSONParser();
			JSONObject obj = (JSONObject) p.parse(lines);
			JSONArray array = (JSONArray) obj.get("features");
			System.out.println("{");
			JSONObject building;//TEMP VAR
			for(int i = 0; i< array.size();i++) {
				building = (JSONObject) array.get(i);
				JSONArray coordinates = (JSONArray)((JSONObject) building.get("geometry")).get("coordinates");
				
				
				System.out.println("{" 
				+ "\"" + ((JSONObject) building.get("properties")).get("locationName") + "\", "
				+ "\"" + ((JSONObject) building.get("properties")).get("faculty") + "\", "
				+ coordinates.get(0) + ", "
				+ coordinates.get(1)
				+ "}"
				+ ","
				);
			}
			System.out.println("}");
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void main(String args[]) {
		
		String path = "NUSLocations/nuslocations.txt";
		modData(path);
	}
	
}
