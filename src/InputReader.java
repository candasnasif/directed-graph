import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class InputReader {
	/*
	 * This function take airport list file read that then load in airports map and
	 * return this map.
	 */
	public Map<String, Airport> AirportRead(String airport, String flight)
			throws IOException, NumberFormatException, ParseException {
		Map<String, Airport> airports = new HashMap<String, Airport>();
		BufferedReader bf = new BufferedReader(new FileReader(airport));
		String line;
		while ((line = bf.readLine()) != null) {
			String[] Parts = line.split("\t");

			for (int i = 1; i < Parts.length; i++) {
				Airport na = new Airport(Parts[0], Parts[i]);
				airports.put(Parts[i], na);
			}
		}
		airports = FlightRead(flight, airports);
		bf.close();
		return airports;

	}

	/*
	 * This function take read the flights list then load in airports
	 */
	public Map<String, Airport> FlightRead(String flight, Map<String, Airport> airports)
			throws IOException, NumberFormatException, ParseException {
		BufferedReader bf = new BufferedReader(new FileReader(flight));
		String line;
		while ((line = bf.readLine()) != null) {
			String[] Parts = line.split("\t");
			String[] rotation = Parts[1].split("->");
			String[] date = Parts[2].split(" ");
			String deparature_time = date[0] + " " + date[1];
			Flight na = new Flight(Parts[0], rotation[0], rotation[1], deparature_time, Parts[3],
					Integer.valueOf(Parts[4]));
			airports.get(na.dept).flights.add(na);
		}
		bf.close();
		return airports;
	}
}
