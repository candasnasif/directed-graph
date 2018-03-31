
import java.util.ArrayList;

public class Airport {
	String cityName;/* the city where the airport */
	String airportName;/* Airports name */
	ArrayList<Flight> flights = new ArrayList<Flight>();/* Airports has these flights */

	public Airport(String cityName, String airportName) {
		super();
		this.cityName = cityName;
		this.airportName = airportName;
	}

}
