package com.dp.dataPlug.DataPlugController;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/oauth/spotify")
public class SpotifyOAuthController {

    @Value("${spotify.client.id}")
    private String clientId;

    @Value("${spotify.client.secret}")
    private String clientSecret;

    @Value("${spotify.token.url}")
    private  String TOKEN_URL;

    @Value("${spotify.redirect.uri}")
    private String redirectUri;

    @Value("${spotify.auth.url}")
    private String AUTH_URL;

    private String ACCESS_TOKEN;

    @Autowired
    private RestTemplate restTemplate;
//    private static final String AUTH_URL = "https://accounts.spotify.com/authorize";

    @GetMapping("/login")
    public ResponseEntity<String> redirectToSpotify() {
        String url = AUTH_URL + "?client_id=" + clientId +
                "&response_type=code" +
                "&state=random_string" +
                "&redirect_uri=" + redirectUri +
                "&scope=user-library-read user-top-read playlist-read-private";

        return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, url).build();
    }

    @GetMapping("/callback")
    public ResponseEntity<String> getToken(@RequestParam("code") String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        body.add("redirect_uri", redirectUri);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map<String, String>> response =
                restTemplate.exchange(TOKEN_URL, HttpMethod.POST, request, new ParameterizedTypeReference<Map<String, String>>() {});

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, String> responseBody = response.getBody();
            System.out.println(responseBody);
            ACCESS_TOKEN = responseBody.get("access_token");;
            return ResponseEntity.ok("User authenticated successfully!");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OAuth failed");
    }

    //Get top Artists
    @GetMapping("/getTopArtists")
    public  Map<String, Object> getUserTopArtists() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ACCESS_TOKEN);

        HttpEntity<String> request = new HttpEntity<>(headers);
        String spotifyUrl = "https://api.spotify.com/v1/me/top/artists";

        ResponseEntity<Map<String, Object>> response =
                restTemplate.exchange(spotifyUrl, HttpMethod.GET, request, new ParameterizedTypeReference<Map<String, Object>>() {});

        Map<String, Object> responseBody = response.getBody();
        System.out.println(responseBody);
        return responseBody;
    }

    //Get User Playlists
    @GetMapping("/getUserPlaylists")
    public Map<String, Object> getUserSavedPlaylists() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ACCESS_TOKEN);

        HttpEntity<String> request = new HttpEntity<>(headers);
        String spotifyUrl = "https://api.spotify.com/v1/me/playlists";

        ResponseEntity<Map<String, Object>> response =
                restTemplate.exchange(spotifyUrl, HttpMethod.GET, request, new ParameterizedTypeReference<Map<String, Object>>() {});

        Map<String, Object> responseBody = response.getBody();
        System.out.println(responseBody);
        return responseBody;
    }
}
