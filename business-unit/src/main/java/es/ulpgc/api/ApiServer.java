package es.ulpgc.api;

import java.util.Map;

import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

public class ApiServer {
    private final int port;
    private Javalin app;

    public ApiServer(int port) { this.port = port; }

    public void start() {
        app = Javalin.create(cfg -> cfg.http.defaultContentType = "application/json").start(port);

        app.get("/health", ctx -> ctx.json(Map.of("status", "UP")));
        app.get("/stats", ctx -> ctx.json(DatamartQueries.stats()));

        app.get("/artists/top", ctx -> {
            int limit = parseLimit(ctx.queryParam("limit"), 20);
            ctx.json(DatamartQueries.topArtists(limit));
        });
        app.get("/events/top", ctx -> {
            int limit = parseLimit(ctx.queryParam("limit"), 20);
            ctx.json(DatamartQueries.topEvents(limit));
        });

        app.get("/artists/{id}", ctx -> {
            var id = ctx.pathParam("id");
            var artist = DatamartQueries.getArtist(id);
            if (artist == null) ctx.status(HttpStatus.NOT_FOUND).json(Map.of("error", "artist not found"));
            else ctx.json(artist);
        });
        app.get("/events/{id}", ctx -> {
            var id = ctx.pathParam("id");
            var event = DatamartQueries.getEvent(id);
            if (event == null) ctx.status(HttpStatus.NOT_FOUND).json(Map.of("error", "event not found"));
            else ctx.json(event);
        });

        app.get("/artists/search", ctx -> {
            String raw = ctx.queryParam("q");
            String q = raw == null ? "" : raw;
            int limit = parseLimit(ctx.queryParam("limit"), 25);
            ctx.json(DatamartQueries.searchArtists(q, limit));
        });
        app.get("/events/search", ctx -> {
            String raw = ctx.queryParam("q");
            String q = raw == null ? "" : raw;
            int limit = parseLimit(ctx.queryParam("limit"), 25);
            ctx.json(DatamartQueries.searchEvents(q, limit));
        });
    }

    public void stop() { if (app != null) app.stop(); }

    private int parseLimit(String raw, int def) {
        if (raw == null) return def;
        try {
            int v = Integer.parseInt(raw);
            return v <= 0 ? def : Math.min(v, 200);
        } catch (NumberFormatException e) {
            return def;
        }
    }
}
