package es.ulpgc;

import es.ulpgc.api.ApiServer;
import es.ulpgc.datamart.DatamartInitializationCheck;
import es.ulpgc.datamart.DatamartInitializer;
import es.ulpgc.utils.EventSubscriber;

public class Main {
    public static void main(String[] args) {
        System.out.println("Checking if datamart is initialized...");
        if (!DatamartInitializationCheck.isDatabaseInitialized()) {
            System.out.println("Datamart not initialized. Initializing...");
            DatamartInitializer.initializeDatamart();
            System.out.println("Datamart initialized successfully.");
        } else {
            System.out.println("Datamart is already initialized.");
        }
        System.out.println("Starting event subscriber...");
        EventSubscriber.subscribe();

        int port = 8080;
        System.out.println("Starting API server on port " + port + "...");
        ApiServer api = new ApiServer(port);
        api.start();
        System.out.println("API server started. Endpoints: /health /stats /artists/top /events/top /artists/{id} /events/{id} /artists/search /events/search");
    }
}