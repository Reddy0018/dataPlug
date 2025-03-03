package com.dp.dataPlug.DataPlugController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @GetMapping("/")
    public String homePage(Model model) {
        return "index"; // Loads the Thymeleaf template
    }

    @GetMapping("/oauth/reddit/fetchSavedPosts")
    public ResponseEntity<?> fetchSavedPosts() {
        return fetchdata("http://localhost:8080/oauth/reddit/savedPosts");
    }

    @GetMapping("/oauth/reddit/upVotedPosts")
    public ResponseEntity<?> fetchUpVotedPosts() {
        return fetchdata("http://localhost:8080/oauth/reddit/upVoted");
    }

    @GetMapping("/oauth/reddit/downVotedPosts")
    public ResponseEntity<?> fetchDownVotedPosts() {
        return fetchdata("http://localhost:8080/oauth/reddit/downVoted");
    }

    @GetMapping("/oauth/spotify/getTopArt")
    public ResponseEntity<?> fetchTopArtists() {
        return fetchdata("http://localhost:8080/oauth/spotify/getTopArtists");
    }

    @GetMapping("/oauth/spotify/getPlaylists")
    public ResponseEntity<?> fetchPlaylists() {
        return fetchdata("http://localhost:8080/oauth/spotify/getUserPlaylists");
    }

    private ResponseEntity<?> fetchdata(String url){
        try{
            Map<String, Object> response = webClientBuilder.build()
                    .get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("error", e.getMessage());
            response.put("error_description", "User not authorized");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
