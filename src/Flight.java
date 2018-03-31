import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Flight {
	String flightID = "";
	String dept = "";/* the place of departure */
	String arr = "";/* the place of arrival */
	String departure_time = "";
	String duration = "";
	int price = 0;
	java.util.Date deptTime;/* departure time in Date format */
	java.util.Date arrTime;/* arrival time in Date format */
	String flightpath = "";/* rotation of flight */
	boolean visited;/*
					 * When we doing searching in graph use this for if we visit this flight visited
					 * will be true
					 */

	public Flight(String flightID, String dept, String arr, String departure_time, String duration, int price)
			throws ParseException {
		super();
		this.flightID = flightID;
		this.dept = dept;
		this.arr = arr;
		this.departure_time = departure_time;
		this.duration = duration;
		this.price = price;
		this.flightpath = this.flightID + "\t" + this.dept + "->" + this.arr;
		this.visited = false;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		this.deptTime = sdf.parse(this.departure_time);
		int[] time = timeSplit(this.duration);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf.parse(this.departure_time));
		calendar.add(Calendar.HOUR, time[0]);
		calendar.add(Calendar.MINUTE, time[1]);
		String arrTimes = sdf.format(calendar.getTime());
		this.arrTime = sdf.parse(arrTimes);

	}

	Flight() {
		this.visited = false;
		this.flightID = "";
		this.dept = "";
		this.arr = "";
		this.departure_time = "";
		this.duration = "";
		this.price = 0;
		this.flightpath = "";
	}

	public int[] timeSplit(String x) {/*
										 * This function take hour with string type and split hours and minutes and
										 * return these in integer array
										 */
		String[] time = x.split(":");
		int h = Integer.valueOf(time[0]);
		int min = Integer.valueOf(time[1]);
		int[] splitTimes = { h, min };
		return splitTimes;
	}

	public String toString() {
		return String.format("%s", flightpath);
	}

}
