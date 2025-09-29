package es.ulpgc;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import es.ulpgc.control.SpotifyController;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Please enter your Spotify refresh token:");
        String refreshToken;
        try (Scanner scanner = new Scanner(System.in)) {
            refreshToken = scanner.nextLine();
        }
        SpotifyController.addToken(refreshToken);

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable task = () -> {
            System.out.println("Starting scheduled task...");
            try {
                SpotifyController.updateApi();

                System.out.println("Scheduled task completed successfully.");
            } catch (Exception e) {
                System.err.println("Error during scheduled task: " + e.getMessage());
            }
        };

        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.HOURS);

        System.out.println("Application is running. Press Ctrl+C to exit.");
    }
}