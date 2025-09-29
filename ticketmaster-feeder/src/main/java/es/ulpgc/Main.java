package es.ulpgc;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import es.ulpgc.control.TicketmasterController;

public class Main {
    public static void main(String[] args) {
        String sourceSystem = "ticketmaster-feeder";

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            System.out.println("Starting scheduled task...");
            try {
                TicketmasterController.processSpotifyEvents(sourceSystem);

                System.out.println("Scheduled task completed successfully.");
            } catch (Exception exception) {
                System.err.println("Error during scheduled task: " + exception.getMessage());
            }
        };

        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.HOURS);

        System.out.println("Application is running. Press Ctrl+C to exit.");
    }
}
