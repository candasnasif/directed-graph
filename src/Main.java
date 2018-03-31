import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

public class Main {

	public static void main(String[] args) throws ParseException, IOException {
		String airport_list_file = args[0];/* take airports list with airport_list_file */
		String flight_list_file = args[1];/* take flight list with flight_list_file */
		String commands_file = args[2];/* take commands list with commands_file */
		String output_file = args[3];/* take output filename with output_file */
		InputReader in = new InputReader();/* This class does airports and flights reading operation */
		Map<String, Airport> airports = in.AirportRead(airport_list_file,
				flight_list_file);/* Take all airports with their own flights ins airports map. */
		Commands command = new Commands();/* This class does commands operation */
		command.CommandOperations(commands_file, output_file,
				airports);/* call the commandsOperation function and do all commands. */
	}

}
