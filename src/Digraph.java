
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Digraph {

	/*
	 * This function take departure place and arrival place and search all flights
	 * departure place to arrival place in graph. This flight searching in stated
	 * date.
	 */
	public ArrayList<Flight> GraphSearchAndPaths(String dept, String arr, Map<String, Airport> airports,
			java.util.Date startDate) throws ParseException {
		Map<String, ArrayList<String>> sameCities = CitiesAirport(airports);
		Map<String, Airport> copyAirport = new HashMap<String, Airport>();/*
																			 * i use copy of graph for search.Beacuse i
																			 * do not want to change graph
																			 */
		ArrayList<String> deptAirport = sameCities.get(dept);/* I take all airports in stated city */
		ArrayList<String> arrAirport = sameCities.get(arr);/* I take all airports in stated city */
		Set<Flight> pathFlights = new HashSet<Flight>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		java.util.Date arrTime = sdf.parse("01/01/1950 01:00");
		Flight paths = new Flight();
		/*
		 * I search in all airport possibilities.
		 */
		for (int i = 0; i < deptAirport.size(); i++) {/* I search all places of departure in the city */
			for (int j = 0; j < arrAirport.size(); j++) {/* I search all places of arrival in city */
				copyAirport.clear();
				copyAirport.putAll(airports);
				pathFlights.addAll(Search(deptAirport.get(i), arrAirport.get(j), copyAirport, pathFlights, arrTime,
						paths, startDate));
			}
		}
		ArrayList<Flight> finalPathFlights = new ArrayList<Flight>();
		finalPathFlights.addAll(pathFlights);
		return finalPathFlights;/* I return all flight end of the function */

	}

	/*
	 * This function search flights x from y.This recursive function look an
	 * airport's all flight if a flight has a match on place of arrival add these
	 * flight paths in path arraylist.If flight has not a match call these function
	 * new place of departure. At the end of the turn return all flight paths
	 * possibility in an arraylist.
	 */
	public Set<Flight> Search(String deptAirport, String arrAirport, Map<String, Airport> airports, Set<Flight> path,
			java.util.Date arrTime, Flight paths, java.util.Date startDate) throws ParseException {
		for (int i = 0; i < airports.get(deptAirport).flights.size(); i++) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

			Flight temp1 = new Flight();
			temp1 = SwapFlight(temp1, paths);/* we keep unchanged path */

			if (arrTime.compareTo(airports.get(deptAirport).flights.get(i).deptTime) < 0
					&& startDate.compareTo(airports.get(deptAirport).flights.get(i).deptTime) < 0) {
				if (paths.flightpath != "")
					paths.flightpath = paths.flightpath + "||" + airports.get(deptAirport).flights.get(i).flightpath;
				else
					paths.flightpath = airports.get(deptAirport).flights.get(i).flightpath;
				paths.price = paths.price + airports.get(deptAirport).flights.get(i).price;
				paths.dept = airports.get(deptAirport).flights.get(i).dept;
				paths.arr = airports.get(deptAirport).flights.get(i).arr;
				paths.duration = airports.get(deptAirport).flights.get(i).duration;
				if (paths.departure_time == "" && paths.departure_time == "") {
					paths.departure_time = airports.get(deptAirport).flights.get(i).departure_time;
					paths.deptTime = sdf.parse(paths.departure_time);
				}
				paths.arrTime = airports.get(deptAirport).flights.get(i).arrTime;
				if (airports.get(deptAirport).flights.get(i).arr.equals(arrAirport)) {
					path.add(paths);
				} else if (airports.get(deptAirport).flights
						.get(i).visited == false) {/*
													 * if an airport's flight has been visited we do not call this
													 * function any more
													 */
					airports.get(deptAirport).flights
							.get(i).visited = true;/* we visit this function and flight's visited must be true. */
					path.addAll(Search(airports.get(deptAirport).flights.get(i).arr, arrAirport, airports, path,
							airports.get(deptAirport).flights.get(i).arrTime, paths, startDate));
					airports.get(deptAirport).flights
							.get(i).visited = false;/*
													 * we assign false in visited because we have to take other flight
													 * path possibilities.
													 */
				}
				paths = new Flight();
				paths = SwapFlight(paths, temp1);/*
													 * we have to clean the paths for new possibilities. Actually we do
													 * not clean the paths we just take back our changes
													 */
			}
		}
		return path;

	}

	/*
	 * This function take the graph and create a new arraylist.This arraylist
	 * grouping airports. All airports in the same city collects a single structure
	 */
	public Map<String, ArrayList<String>> CitiesAirport(Map<String, Airport> airports) {
		Map<String, ArrayList<String>> sameCities = new HashMap<String, ArrayList<String>>();
		for (String key : airports.keySet()) {
			if (!sameCities.containsKey(airports.get(key).cityName)) {
				sameCities.put(airports.get(key).cityName, new ArrayList<String>());
				for (String key2 : airports.keySet()) {
					if (airports.get(key2).cityName.equals(airports.get(key).cityName)) {
						sameCities.get(airports.get(key).cityName).add(airports.get(key2).airportName);
					}
				}
			}

		}
		return sameCities;
	}

	/*
	 * This function take two flight object and assign second's values to first
	 * value
	 */
	public Flight SwapFlight(Flight a, Flight b) {
		a.arr = b.arr;
		a.arrTime = b.arrTime;
		a.departure_time = b.departure_time;
		a.dept = b.dept;
		a.deptTime = b.deptTime;
		a.duration = b.duration;
		a.flightID = b.flightID;
		a.flightpath = b.flightpath;
		a.price = b.price;
		a.visited = b.visited;
		return a;
	}

}
