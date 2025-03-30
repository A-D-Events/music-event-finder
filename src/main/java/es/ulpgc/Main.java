package es.ulpgc;

import es.ulpgc.database.DatabaseManager;

public class Main {
    public static void main(String[] args) {
        DatabaseManager.initializeDatabase();
        System.out.println("Aplicación iniciada correctamente.");
    }
}