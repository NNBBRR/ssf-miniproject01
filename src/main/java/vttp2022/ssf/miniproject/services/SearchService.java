package vttp2022.ssf.miniproject.services;

import java.io.StringReader;
import java.net.URLEncoder;
import java.io.Reader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import vttp2022.ssf.miniproject.models.Reviews;
import vttp2022.ssf.miniproject.models.User;
import vttp2022.ssf.miniproject.models.Data;
import vttp2022.ssf.miniproject.repositories.ReviewsRepository;


@Service
public class SearchService {

   @Autowired
   private ReviewsRepository RevRepo;

	public static final String URL = "https://tih-api.stb.gov.sg/content/v1/food-beverages/search";

	@Value("${API_KEY}")
	private String key;

    String newUsername;
    RedisTemplate<String,User> redisTemplate;

	public List<Reviews> getReviews(String keyword) {

		String payload;

		try {
			// Create the url with query string
			String url = UriComponentsBuilder.fromUriString(URL)
					.queryParam("keyword", URLEncoder.encode(keyword, "UTF-8"))
					.queryParam("apikey", key)
					.toUriString();
			
			// Create the GET request
			RequestEntity<Void> req = RequestEntity
			.get(url)
			.build();

			// Make the call to API
			RestTemplate template = new RestTemplate();
			ResponseEntity<String> resp;

			resp = template.exchange(req, String.class);

			// Get the payload
			payload = resp.getBody();

		} catch (Exception ex) {
			System.err.printf("Error: %s\n", ex.getMessage());
			return Collections.emptyList();
		}

        Reader strReader = new StringReader(payload);
        JsonReader jsonReader = Json.createReader(strReader);
        
        JsonObject payloadJsonObject = jsonReader.readObject();
        JsonArray dataArray = payloadJsonObject.getJsonArray("data");

        JsonObject data = dataArray.getJsonObject(0);
        JsonArray dataReviews = data.getJsonArray("reviews");

        String Name = data.getString("name");
        String Uuid = data.getString("uuid");
        String Body = data.getString("body");

        List<Reviews> list = new LinkedList<>();

       // for (int i = 0; i < dataReviews.size(); i++) {
            Reviews review = new Reviews();
            JsonObject jo = dataReviews.getJsonObject(0);
            review.setName(Name);
            review.setUuid(Uuid);
            review.setBody(Body);
            review.setAuthorName(jo.getString("authorName"));
            review.setText(jo.getString("text"));
            review.setRating(String.valueOf(jo.getInt("rating")));
            //review.setTime(jo.getString("time"));
            review.setProfilePhoto(jo.getString("profilePhoto"));
            list.add(review);
            
       // }
        return list;
    }

    public Data generateData(String name, String text) {
        Data data = new Data(name, text);
        return data;
    }

    public void saveToRepo(Data data, String userName) {
        RevRepo.saveToRedis(userName, data);
    }

    public List<Data> getRedisUser(String userName) {

         Optional<List<Data>> opt = RevRepo.getFromRedis(userName);
         if (opt.isEmpty())
            return List.of(); 

         return opt.get();
    }


    public Optional<User> getByUsername(final String userName) {
        
        try{User result = (User) redisTemplate.opsForValue().get(userName);
            return Optional.of(result);
        }catch (Exception e) {
            e.printStackTrace();
        }
        
        return Optional.empty();
    }

}

/*  For future

// instantiate a list to store the data
        List<Reviews> list = new LinkedList<>();
        

        for (int j = 0; j < dataArray.size(); j++) {
        JsonObject data = dataArray.getJsonObject(j);

        JsonArray dataReviews = data.getJsonArray("reviews");
        String Name = data.getString("name");
        String Uuid = data.getString("uuid");
        String Body = data.getString("body");

            
        //for (int i = 0; i < dataReviews.size(); i++) {
            Reviews review = new Reviews();
            JsonObject jo = dataReviews.getJsonObject(0);
            review.setName(Name);
            review.setUuid(Uuid);
            review.setBody(Body);
            //review.setAuthorName(jo.getString("authorName"));
            //review.setText(jo.getString("text"));
            //review.setRating(String.valueOf(jo.getInt("rating")));
            //review.setTime(jo.getString("time"));
            //review.setProfilePhoto(jo.getString("profilePhoto"));
            list.add(review);
            // }
        }
        return list;
    }

 */