package example.movies.backend;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.servlet.SparkApplication;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static spark.Spark.get;
import static spark.Spark.post;

public class MovieRoutes implements SparkApplication {

    private final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    private final MovieService service;

    public MovieRoutes(MovieService service) {
        this.service = service;
    }

    public void init() {
        get("/movie/:title", (req, res) -> gson.toJson(service.findMovie(URLDecoder.decode(req.params("title"), StandardCharsets.UTF_8))));
        post("/movie/vote/:title", (req, res) -> {
            Integer updates = service.voteInMovie(URLDecoder.decode(req.params("title"), StandardCharsets.UTF_8));
            return gson.toJson( Map.of("updated", updates));
        });

       /* BEGIN Code adddd by Padma */

       post("/addmovie/:moviename,:tagline,:movieyear,:actorname,:actoryear,:directorname,:directoryear",  (req, res) -> gson.toJson(service.addMovie(
            URLDecoder.decode(req.params("moviename"), StandardCharsets.UTF_8),
            URLDecoder.decode(req.params("tagline"), StandardCharsets.UTF_8),
            URLDecoder.decode(req.params("movieyear"), StandardCharsets.UTF_8),
            URLDecoder.decode(req.params("actor"), StandardCharsets.UTF_8),
            URLDecoder.decode(req.params("actoryear"), StandardCharsets.UTF_8),
            URLDecoder.decode(req.params("director"), StandardCharsets.UTF_8),
            URLDecoder.decode(req.params("directoryear"), StandardCharsets.UTF_8))));

        /* END Code adddd by Padma */

        get("/search", (req, res) -> gson.toJson(service.search(req.queryParams("q"))));
        get("/graph", (req, res) -> {
            int limit = req.queryParams("limit") != null ? Integer.parseInt(req.queryParams("limit")) : 100;
            return gson.toJson(service.graph(limit));
        });
    }
}
