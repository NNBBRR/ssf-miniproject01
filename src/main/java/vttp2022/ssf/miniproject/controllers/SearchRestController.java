package vttp2022.ssf.miniproject.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp2022.ssf.miniproject.models.Data;
import vttp2022.ssf.miniproject.repositories.ReviewsRepository;


@RestController
public class SearchRestController {

	@Autowired
	private ReviewsRepository RevRepo;

	@GetMapping(path = "/api/{userName}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> restJsonResponse (@PathVariable String userName){
        
        Optional<List<Data>> dataset = RevRepo.getRedis(userName.toUpperCase());

        if (dataset.isEmpty()) {
			JsonObject payload = Json.createObjectBuilder()
				.add("error", "Cannot find user %s".formatted(userName))
				.build();
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(payload.toString());
		}

        List<Data> data = dataset.get();
        return ResponseEntity.ok(data.toString());
    }

}
