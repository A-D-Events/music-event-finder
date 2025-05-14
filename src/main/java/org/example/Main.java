package org.example;

import java.util.Arrays;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		// 🔑 API KEY desde clase ConfigKey
		String apiKey = ConfigKey.getApiKey();

		if (apiKey == null || apiKey.isEmpty()) {
			System.out.println("❌ API Key no encontrada.");
			return;
		}

		// 🗃️ Inicializa la base de datos SQLite
		DatabaseManager.inicializarBaseDeDatos();

		// 🎧 Lista de artistas simulados desde tu cuenta de Spotify (puedes integrarlo luego)
		List<String> topArtistas = Arrays.asList(
				"Arctic Monkeys",
				"Quevedo",
				"Hoke",
				"Bad Bunny",
				"Duki",
				"Maná",
				"Cupido"
		);

		// 🔍 Buscar conciertos para cada artista y guardarlos
		for (String artista : topArtistas) {
			System.out.println("\n🔎 Buscando conciertos para: " + artista);
			TicketmasterAPI.buscarEventos(apiKey, "music", null, "ES", "Madrid", artista);
		}

		System.out.println("\n✅ Todos los conciertos han sido guardados en la base de datos.");
	}
}

