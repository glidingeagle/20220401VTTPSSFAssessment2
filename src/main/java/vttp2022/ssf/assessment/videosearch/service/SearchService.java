package vttp2022.ssf.assessment.videosearch.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.ssf.assessment.videosearch.models.Game;

@Service
public class SearchService {

    @Value("${rawg.api.key}")
    private String apiKey;

    private boolean hasKey;

    @PostConstruct
    public void init() {
        hasKey = null != apiKey;
    }

    Game games = new Game();
    List<Game> gameLists = new LinkedList<>();

    public List<Game> search (String searchString, Integer count) {
        String url = "https://api.rawg.io/api/games";
        url = UriComponentsBuilder
                .fromUriString(url)
                .queryParam("search", searchString)
                .queryParam("page_size", count)
                .queryParam("key", apiKey)
                .toUriString();

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> response = template.getForEntity(url, String.class);

        try (InputStream is = new ByteArrayInputStream(response.getBody().getBytes())) {
            JsonReader reader = Json.createReader(is);
            JsonObject data = reader.readObject();
        
            JsonArray gamesArr = data.getJsonArray("results");
            for (int i=0; i < gamesArr.size(); i++) {
                Game games = new Game();
                String name = gamesArr.getJsonObject(i).getString("name");
                games.setName(name);
                String backgroundImage = gamesArr.getJsonObject(i).getString("background_image");
                games.setBackgroundImage(backgroundImage);
                Float rating = gamesArr.getJsonObject(i).getJsonNumber("rating").bigDecimalValue().floatValue();
                games.setRating(rating);    
                gameLists.add(games);
            }
            System.out.println(gameLists);
        } catch (IOException ex) {
            System.out.println("ERROR >>> " + ex.getMessage());
        }
        return gameLists;

    }
    
}
