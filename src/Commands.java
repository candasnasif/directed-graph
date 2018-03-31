import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Commands {
	/*
	 * CommandOperations function read commands file and do all commands then write
	 * outputs in output file
	 */
	public void CommandOperations(String commands, String outputFileName, Map<String, Airport> airports)
			throws IOException, ParseException {
		Digraph Graph = new Digraph();
		BufferedReader bf = new BufferedReader(new FileReader(commands));
		PrintWriter writer = new PrintWriter(outputFileName, "UTF-8");
		String line;
		while ((line = bf.readLine()) != null) {
			writer.println("Command : " + line);
			String[] Parts = line.split("\t");
			/*
			 * if command is listall, search all flights in graph and return them in an
			 * arraylist then write all results in output file
			 */
			/*
			 * I take arguments from line and search in graph
			 */
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			java.util.Date startDate = sdf.parse(Parts[2]);
			String[] rotation = Parts[1].split("->");
			ArrayList<Flight> paths = Graph.GraphSearchAndPaths(rotation[0], rotation[1], airports, startDate);
			if (Parts[0].equals("listall")) {

				if (paths.size() == 0)
					writer.println("No suitable flight plan is found");/*
																		 * if we could not find any flight path write
																		 * this message in output file
																		 */
				else
					for (int i = 0; i < paths.size(); i++) {
						String time = timeDifference(paths.get(i).deptTime, paths.get(i).arrTime);
						writer.println(paths.get(i).flightpath + "\t" + time + "/" + paths.get(i).price);
					}
			}
			/*
			 * if command is listproper search and find cheapest and quickest flight in
			 * graph then load results in an array list and write them in output file
			 */
			else if (Parts[0].equals("listproper")) {

				ArrayList<Flight> Cheapest = CheapestPath(paths);
				ArrayList<Flight> Quickest = QuickestPath(paths);
				ArrayList<Flight> Proper = new ArrayList<Flight>();
				Set<Flight> filter = new HashSet<Flight>();
				filter.addAll(Cheapest);/* we use set for filter same flight paths */
				filter.addAll(Quickest);
				Proper.addAll(filter);
				if (Proper.size() == 0)
					writer.println("No suitable flight plan is found");/*
																		 * if we could not find any flight path write
																		 * this message in output file
																		 */
				else
					for (int i = 0; i < Proper.size(); i++) {
						String time = timeDifference(Proper.get(i).deptTime, Proper.get(i).arrTime);
						writer.println(Proper.get(i).flightpath + "\t" + time + "/" + Proper.get(i).price);
					}
				Proper.clear();
				Cheapest.clear();
				Quickest.clear();
				filter.clear();
			}
			/*
			 * if command is listcheapest find cheapest flight in graph then write results
			 * in output file
			 */
			else if (Parts[0].equals("listcheapest")) {
				ArrayList<Flight> CheapestProper = CheapestPath(paths);
				ArrayList<Flight> QuickestProper = QuickestPath(paths);
				ArrayList<Flight> Proper = new ArrayList<Flight>();
				Set<Flight> filter = new HashSet<Flight>();
				filter.addAll(CheapestProper);/* we use set for filter same flight paths */
				filter.addAll(QuickestProper);
				Proper.addAll(filter);
				ArrayList<Flight> Cheapest = CheapestPath(Proper);/* I use proper flights for cheapest flights */
				if (Cheapest.size() == 0)
					writer.println("No suitable flight plan is found");/*
																		 * if we could not find any flight path write
																		 * this message in output file
																		 */
				else
					for (int i = 0; i < Cheapest.size(); i++) {
						String time = timeDifference(Cheapest.get(i).deptTime, Cheapest.get(i).arrTime);
						writer.println(Cheapest.get(i).flightpath + "\t" + time + "/" + Cheapest.get(i).price);
					}
				Proper.clear();
				QuickestProper.clear();
				CheapestProper.clear();
				Cheapest.clear();
				filter.clear();
			}
			/*
			 * if command is listquickest find quickest flight in graph then write results
			 * in output file
			 */
			else if (Parts[0].equals("listquickest")) {
				ArrayList<Flight> CheapestProper = CheapestPath(paths);
				ArrayList<Flight> QuickestProper = QuickestPath(paths);
				ArrayList<Flight> Proper = new ArrayList<Flight>();
				Set<Flight> filter = new HashSet<Flight>();
				filter.addAll(CheapestProper);/* we use set for filter same flight paths */
				filter.addAll(QuickestProper);
				Proper.addAll(filter);
				ArrayList<Flight> Quickest = QuickestPath(Proper);/* I use proper flights for quickest flights */
				if (Quickest.size() == 0)
					writer.println("No suitable flight plan is found");/*
																		 * if we could not find any flight path write
																		 * this message in output file
																		 */
				else
					for (int i = 0; i < Quickest.size(); i++) {
						String time = timeDifference(Quickest.get(i).deptTime, Quickest.get(i).arrTime);
						writer.println(Quickest.get(i).flightpath + "\t" + time + "/" + Quickest.get(i).price);
					}
				Proper.clear();
				QuickestProper.clear();
				CheapestProper.clear();
				Quickest.clear();
				filter.clear();
			}
			/*
			 * if command is listcheaper we will find cheaper flight in graph with given
			 * argument. This flight cheaper then given price
			 */
			else if (Parts[0].equals("listcheaper")) {

				ArrayList<Flight> Cheaper = Cheaper(paths, Integer.valueOf(Parts[3]));
				if (Cheaper.size() == 0)
					writer.println("No suitable flight plan is found");/*
																		 * if we could not find any flight path write
																		 * this message in output file
																		 */
				else
					for (int i = 0; i < Cheaper.size(); i++) {
						String time = timeDifference(Cheaper.get(i).deptTime, Cheaper.get(i).arrTime);
						writer.println(Cheaper.get(i).flightpath + "\t" + time + "/" + Cheaper.get(i).price);
					}
				Cheaper.clear();
			}
			/*
			 * if command is listquicker we will find quicker flight in graph with given
			 * argument. This flight arrive the place before the given date.
			 */
			else if (Parts[0].equals("listquicker")) {
				SimpleDateFormat arr = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				String[] latestDateString = Parts[3].split(" ");
				java.util.Date latestDate = arr.parse(latestDateString[0] + " " + latestDateString[1]);
				ArrayList<Flight> Cheapest = CheapestPath(paths);
				ArrayList<Flight> Quickest = QuickestPath(paths);
				ArrayList<Flight> QuickerCheaps = Quicker(Cheapest, latestDate);
				ArrayList<Flight> QuickerQuicks = Quicker(Quickest, latestDate);
				ArrayList<Flight> Quicker = new ArrayList<Flight>();
				Quicker.addAll(QuickerCheaps);
				Quicker.addAll(QuickerQuicks);
				Set<Flight> filter = new HashSet<Flight>();/* we use set for filter same flight paths */
				filter.addAll(Quicker);
				Quicker.clear();
				Quicker.addAll(filter);
				if (Quicker.size() == 0)
					writer.println("No suitable flight plan is found");/*
																		 * if we could not find any flight path write
																		 * this message in output file
																		 */
				else
					for (int i = 0; i < Quicker.size(); i++) {
						String time = timeDifference(Quicker.get(i).deptTime, Quicker.get(i).arrTime);
						writer.println(Quicker.get(i).flightpath + "\t" + time + "/" + Quicker.get(i).price);
					}
				Quickest.clear();
				Cheapest.clear();
				QuickerCheaps.clear();
				QuickerQuicks.clear();
				Quicker.clear();
				filter.clear();
			}
			/*
			 * if command is listexcluding we search the graph and if given airport alias
			 * has a flight in the path we do not take this flight
			 */
			else if (Parts[0].equals("listexcluding")) {
				String airport_alias = Parts[3];
				ArrayList<Flight> Excluding = Listexcluding(paths, airport_alias);
				if (Excluding.size() == 0)
					writer.println("No suitable flight plan is found");/*
																		 * if we could not find any flight path write
																		 * this message in output file
																		 */
				else
					for (int i = 0; i < Excluding.size(); i++) {
						String time = timeDifference(Excluding.get(i).deptTime, Excluding.get(i).arrTime);
						writer.println(Excluding.get(i).flightpath + "\t" + time + "/" + Excluding.get(i).price);
					}
				Excluding.clear();
			}
			/*
			 * if command is listonlyfrom we search graph and if all airport alias has all
			 * flights we take that flight and write these flight in output file
			 */
			else if (Parts[0].equals("listonlyfrom")) {
				String airport_alias = Parts[3];
				ArrayList<Flight> OnlyFrom = ListOnlyFrom(paths, airport_alias);
				if (OnlyFrom.size() == 0)
					writer.println("No suitable flight plan is found");/*
																		 * if we could not find any flight path write
																		 * this message in output file
																		 */
				else
					for (int i = 0; i < OnlyFrom.size(); i++) {
						String time = timeDifference(OnlyFrom.get(i).deptTime, OnlyFrom.get(i).arrTime);
						writer.println(OnlyFrom.get(i).flightpath + "\t" + time + "/" + OnlyFrom.get(i).price);
					}
				OnlyFrom.clear();
			}
			/*
			 * skip two lines end of the command
			 */
			paths.clear();
			writer.println();
			writer.println();
		}
		/*
		 * close reader and writer
		 */
		bf.close();
		writer.close();
	}

	/*
	 * (ListOnlyFrom)This function take all flight x from y place and filter that.If
	 * a flights airport alias is not given airport alias do not take that flight
	 * other flights will add in an arraylist then return this arraylist
	 */
	public ArrayList<Flight> ListOnlyFrom(ArrayList<Flight> flights, String airport_alias) {
		ArrayList<Flight> OnlyFrom = new ArrayList<Flight>();
		int control;
		for (int i = 0; i < flights.size(); i++) {
			control = 0;
			String[] first_alias = flights.get(i).flightpath.split("\\|\\|");/* cut the path */
			for (int j = 0; j < first_alias.length; j++) {
				String[] Parts = first_alias[j].split("\t");/* cut a flight */
				if (!Parts[0].contains(airport_alias))
					control = 1;/*
								 * if this flight's airport alias is not given airport alias control will be 1
								 */
			}
			if (control == 0) {/*
								 * if a flight's airport alias is not given airport alias control will be 1 if
								 * not control stay static in 0
								 */
				OnlyFrom.add(flights.get(i));
			}
		}
		return OnlyFrom;
	}

	/*
	 * (Listexcluding)This function take all flight x from y place and filter
	 * them.If a flights airport alias is given airport alias do not take that
	 * flight other flights will add in an arraylist then return this arraylist
	 */
	public ArrayList<Flight> Listexcluding(ArrayList<Flight> flights, String airport_alias) {
		ArrayList<Flight> Excluding = new ArrayList<Flight>();
		for (int i = 0; i < flights.size(); i++) {
			if (!flights.get(i).flightpath.contains(
					airport_alias)) {/*
										 * if flight path is not contain given airport alias name then take this flight
										 */
				Excluding.add(flights.get(i));
			}
		}
		return Excluding;
	}

	/*
	 * (Cheaper)This function take all flight x from y place and filter them.If
	 * flight price cheaper than given parameter price we will take this flight and
	 * return this flight in arraylist
	 */
	public ArrayList<Flight> Cheaper(ArrayList<Flight> flights, int price) {
		ArrayList<Flight> Cheaper = new ArrayList<Flight>();
		for (int i = 0; i < flights.size(); i++) {
			if (flights.get(i).price < price) {
				Cheaper.add(flights.get(i));
			}
		}
		return Cheaper;
	}

	/*
	 * (CheapestPath)This function take all flight x from y place and filter them.At
	 * the begining we will find cheapest flight path in all flights then we take
	 * all cheapest flight and return them
	 */
	public ArrayList<Flight> CheapestPath(ArrayList<Flight> flights) {
		int min = 999999999;
		for (int i = 0; i < flights.size(); i++) {
			if (flights.get(i).price < min)
				min = flights.get(i).price;
		}
		ArrayList<Flight> Cheapest = new ArrayList<Flight>();
		for (int i = 0; i < flights.size(); i++) {
			if (flights.get(i).price == min)
				Cheapest.add(flights.get(i));
		}
		return Cheapest;
	}

	/*
	 * (QuickestPath)This function take all flight x from y place and filter them.At
	 * the begining we will find quickest flight path in all flight then we take all
	 * quickest flight and return them
	 */
	public ArrayList<Flight> QuickestPath(ArrayList<Flight> flights) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		java.util.Date min = sdf.parse("99:99");
		for (int i = 0; i < flights.size(); i++) {
			String time = timeDifference(flights.get(i).deptTime, flights.get(i).arrTime);
			java.util.Date temp = sdf.parse(time);
			if (temp.compareTo(min) < 0) {
				min = temp;
			}
		}
		ArrayList<Flight> Quickest = new ArrayList<Flight>();
		for (int i = 0; i < flights.size(); i++) {
			String time = timeDifference(flights.get(i).deptTime, flights.get(i).arrTime);
			java.util.Date temp = sdf.parse(time);
			if (temp.equals(min)) {
				Quickest.add(flights.get(i));
			}
		}
		return Quickest;

	}

	/*
	 * (Quicker)This function take all flight x from y place and filter them.If a
	 * flight arrival time less than given date we take this flight and return all
	 * these flight
	 */
	public ArrayList<Flight> Quicker(ArrayList<Flight> flights, java.util.Date latestDate) {
		ArrayList<Flight> Quicker = new ArrayList<Flight>();
		for (int i = 0; i < flights.size(); i++) {
			if (flights.get(i).arrTime.compareTo(latestDate) < 0) {
				Quicker.add(flights.get(i));
			}
		}
		return Quicker;
	}

	/*
	 * (timeDifference)This function take two date(type of data) then we take
	 * difference these date. Function convert difference Date to string and return
	 * (12:23) this format
	 */
	public String timeDifference(java.util.Date x, java.util.Date y) {
		long z = y.getTime() - x.getTime();
		long diffMinutes = z / (60 * 1000) % 60;
		long diffHours = z / (60 * 60 * 1000);
		String StringDiffHours = String.valueOf(diffHours);
		String StringDiffMinutes = String.valueOf(diffMinutes);
		if (diffHours < 10)
			StringDiffHours = "0" + String.valueOf(diffHours);
		if (diffMinutes < 10)
			StringDiffMinutes = "0" + String.valueOf(diffMinutes);
		return (StringDiffHours + ":" + StringDiffMinutes);
	}
}
