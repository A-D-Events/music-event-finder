package es.ulpgc;

import java.io.IOException;

import es.ulpgc.database.DatabaseManager;


public class Main {
    public static void main(String[] args) throws IOException {
        DatabaseManager.initializeDatabase();
        System.out.println("Aplicación iniciada correctamente.");
    }
}