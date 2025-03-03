import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        String filePath = "src/main/resources/file.json";


        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> jsonData = objectMapper.readValue(new File(filePath), Map.class);

            List<Map<String,Object>> items = (List<Map<String, Object>>) jsonData.get("items");
            List<Map<String,Object>> dMap = new ArrayList<>();
            items.forEach(a->{
                Map<String,Object> data = new HashMap<>();
                Map<String,Object> track = (Map<String, Object>) a.get("track");
                Map<String,Object> album = (Map<String, Object>) track.get("album");
                List<Map<String,Object>> artists = (List<Map<String, Object>>) album.get("artists");
                List<Map<String,String>> list = new ArrayList<>();
                artists.forEach(artist->{
                   Map<String,String> map1 = new HashMap<>();
                   map1.put("name", (String) artist.get("name"));
                   map1.put("type", (String) artist.get("type"));
                   list.add(map1);
                });

                data.put("id", track.get("id"));
                data.put("type", album.get("type"));
                data.put("album_type", album.get("album_type"));
                data.put("name", track.get("name"));
                data.put("artists", list);
                data.put("popularity", track.get("popularity"));
                dMap.add(data);
            });

            System.out.println("Converted JSON to Map: " + dMap);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
