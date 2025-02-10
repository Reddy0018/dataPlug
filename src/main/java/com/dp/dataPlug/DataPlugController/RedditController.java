package com.dp.dataPlug.DataPlugController;

import com.dp.dataPlug.Model.TokenService;
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
@RequestMapping("/oauth/reddit")
public class RedditController {

    @Value("${reddit.client.id}")
    private String clientId;

    @Value("${reddit.token.url}")
    private  String TOKEN_URL;

    @Value("${reddit.client.secret}")
    private String clientSecret;

    @Value("${reddit.redirect.uri}")
    private String redirectUri;

    @Value("${reddit.auth.url}")
    private String AUTH_URL;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TokenService tokenService;


    @GetMapping("/test")
    public String test(){
        System.out.println("test123");
        return "Hello";
    }

    @GetMapping("/login")
    public ResponseEntity<String> redirectToReddit() {
        System.out.println("In login REST:: ");
        String url = AUTH_URL + "?client_id=" + clientId +
                "&response_type=code" +
                "&state=random_string" +
                "&redirect_uri=" + redirectUri +
                "&duration=permanent" +
                "&scope=identity history read";
        return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION, url).build();
    }

    @GetMapping("/callback")
    public ResponseEntity<String> getToken(@RequestParam("code") String code) {
        System.out.println("In Call back REST:: "+ code);
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", code);
        body.add("redirect_uri", redirectUri);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map<String, String>> response = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, request, new ParameterizedTypeReference<Map<String, String>>() {});

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, String> responseBody = response.getBody();
            System.out.println(responseBody);
            //tokenService.saveToken(tokenResponse); // Save token to DB
            String accessToken = responseBody.get("access_token");
            String userName = sendGenericRESTCallToReddit(accessToken,"https://oauth.reddit.com/api/v1/me").get("name").toString();
            System.out.println("UserName:: "+ userName);
            String getSavedPosts = "https://oauth.reddit.com/user/"+userName+"/saved";
            sendGenericRESTCallToReddit(accessToken,getSavedPosts);
            return ResponseEntity.ok("User authenticated successfully!");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OAuth failed");
    }

    public Map<String, Object>  sendGenericRESTCallToReddit(String accessToken, String URL) {
        //String accessToken = getValidAccessToken(userId);
        System.out.println("In sendGenericRESTCalltoReddit:: ");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(URL, HttpMethod.GET, request, new ParameterizedTypeReference<Map<String, Object>>() {});
        Map<String, Object> responseBody = response.getBody();
        System.out.println(responseBody);
        return responseBody;
    }

    /**public Map<String, String>  getSavedPosts(String accessToken, String userName) {
        //String accessToken = getValidAccessToken(userId);
        System.out.println("In getSavedPosts REST:: "+ userName);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);
        String redditUrl = "https://oauth.reddit.com/user/"+userName+"/saved";

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(redditUrl, HttpMethod.GET, request, new ParameterizedTypeReference<Map<String, Object>>() {});
        Map<String, Object> responseBody = response.getBody();
        System.out.println(responseBody);
        return null;
    }

    public String  getUserName(String accessToken) {
        //String accessToken = getValidAccessToken(userId);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);
        String redditUrl = "https://oauth.reddit.com/api/v1/me";

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(redditUrl, HttpMethod.GET, request, new ParameterizedTypeReference<Map<String, Object>>() {});
        Map<String, Object> responseBody = response.getBody();
        System.out.println(responseBody);
        return responseBody.get("name").toString();
    }*/

}
