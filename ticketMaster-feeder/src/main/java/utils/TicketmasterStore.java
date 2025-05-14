package utils;

import model.Event;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TicketmasterStore {

	public static void exportEventsToCsv(List<Event> events, String filename) {
		try (FileWriter writer = new FileWriter(filename)) {
			// Encabezado
			writer.append("Name,Artist,Date,Time,Venue,Address,Price,Segment,Genre,Link,Image\n");

			for (Event e : events) {
				writer.append(escape(e.name)).append(",")
						.append(escape(e.artist)).append(",")
						.append(escape(e.date)).append(",")
						.append(escape(e.time)).append(",")
						.append(escape(e.venue)).append(",")
						.append(escape(e.address)).append(",")
						.append(escape(e.price)).append(",")
						.append(escape(e.segment)).append(",")
						.append(escape(e.genre)).append(",")
						.append(escape(e.link)).append(",")
						.append(escape(e.image)).append("\n");
			}

			System.out.println("✅ Events exported to CSV: " + filename);

		} catch (IOException ex) {
			System.err.println("❌ Error writing CSV: " + ex.getMessage());
		}
	}

	private static String escape(String s) {
		if (s == null) return "";
		return "\"" + s.replace("\"", "\"\"") + "\"";
	}
}
