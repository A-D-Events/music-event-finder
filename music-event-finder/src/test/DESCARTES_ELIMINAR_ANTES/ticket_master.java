//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ulpgc.API;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.example.db.DatabaseManager;
import org.json.JSONArray;
import org.json.JSONObject;

public class TicketmasterAPI {
	private static final String BASE_URL = "https://app.ticketmaster.com/discovery/v2/events.json";

	public TicketmasterAPI() {
	}

	public static void buscarEventos(String apiKey, String segment, String genre, String countryCode, String city, String keyword) {
		try {
			String urlStr = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=" + apiKey + "&segmentName=" + segment + (genre != null ? "&genreName=" + genre : "") + "&countryCode=" + countryCode + "&city=" + city + (keyword != null && !keyword.isEmpty() ? "&keyword=" + keyword : "") + "&size=10&sort=date,asc";
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			int responseCode = conn.getResponseCode();
			System.out.println("\ud83d\udce1 Código de respuesta: " + responseCode);
			if (responseCode != 200) {
				System.out.println("⚠️ Error al obtener los eventos.");
				return;
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder respuesta = new StringBuilder();

			String inputLine;
			while((inputLine = in.readLine()) != null) {
				respuesta.append(inputLine);
			}

			in.close();
			JSONObject json = new JSONObject(respuesta.toString());
			if (!json.has("_embedded")) {
				System.out.println("\ud83d\ude15 No se encontraron eventos.");
				return;
			}

			JSONArray events = json.getJSONObject("_embedded").getJSONArray("events");

			for(int i = 0; i < events.length(); ++i) {
				JSONObject event = events.getJSONObject(i);
				String nombre = event.optString("name", "Nombre no disponible");
				String fecha = event.getJSONObject("dates").getJSONObject("start").optString("localDate", "Fecha no disponible");
				String hora = event.getJSONObject("dates").getJSONObject("start").optString("localTime", "Hora no disponible");
				String lugar = event.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).optString("name", "Lugar no disponible");
				String direccion = event.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("address").optString("line1", "Dirección no disponible");
				String enlace = event.optString("url", "No disponible");
				JSONArray attractions = event.getJSONObject("_embedded").optJSONArray("attractions");
				String artista = attractions != null && attractions.length() > 0 ? attractions.getJSONObject(0).optString("name", "Artista no disponible") : "Artista no disponible";
				JSONArray images = event.optJSONArray("images");
				String imagen = images != null && images.length() > 0 ? images.getJSONObject(0).optString("url", "Sin imagen") : "Sin imagen";
				JSONArray precios = event.optJSONArray("priceRanges");
				String precio = "No disponible";
				JSONObject clasif;
				if (precios != null && precios.length() > 0) {
					clasif = precios.getJSONObject(0);
					double min = clasif.optDouble("min", -1.0);
					double max = clasif.optDouble("max", -1.0);
					String currency = clasif.optString("currency", "€");
					precio = "" + min + " - " + max + " " + currency;
				}

				clasif = event.optJSONArray("classifications").getJSONObject(0);
				String segmento = clasif.getJSONObject("segment").optString("name", "Desconocido");
				String genero = clasif.getJSONObject("genre").optString("name", "Desconocido");
				System.out.println("\ud83c\udfa4 Evento: " + nombre);
				System.out.println("\ud83c\udfb6 Artista: " + artista);
				System.out.println("\ud83d\udcc5 Fecha: " + fecha + " \ud83d\udd52 " + hora);
				System.out.println("\ud83c\udfdf️ Lugar: " + lugar);
				System.out.println("\ud83d\udccd Dirección: " + direccion);
				System.out.println("\ud83d\udcb5 Precio: " + precio);
				System.out.println("\ud83c\udfad Clasificación: " + segmento + " / " + genero);
				System.out.println("\ud83d\udd17 Enlace: " + enlace);
				System.out.println("\ud83d\uddbc️ Imagen: " + imagen);
				System.out.println("--------------------------------------------------");
				DatabaseManager.guardarEvento(nombre, artista, fecha, hora, lugar, direccion, precio, segmento, genero, enlace, imagen);
			}
		} catch (Exception var35) {
			System.err.println("❌ Error al procesar la respuesta:");
			var35.printStackTrace();
		}

	}

	public static void buscarEventos(String apiKey, String segment, String genre, String countryCode, String city) {
		buscarEventos(apiKey, segment, genre, countryCode, city, (String)null);
	}
}
