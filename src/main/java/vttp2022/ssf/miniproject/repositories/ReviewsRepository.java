package vttp2022.ssf.miniproject.repositories;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import vttp2022.ssf.miniproject.models.Data;


@Repository
public class ReviewsRepository {
    
    @Autowired
    @Qualifier("redislab")
    private RedisTemplate<String,String> redisTemplate;

    public void save(String userName, Data data) {

        // store userName as the key in Redis
        String jsonObjectName = userName;
        ListOperations<String, String> listOps = redisTemplate.opsForList();
		listOps.leftPush(jsonObjectName, data.toJson().toString());
        
    }

    public Optional<List<Data>> getRedis(String userName) {

        // return empty container if username is not in Redis
        if (!redisTemplate.hasKey(userName)) {
            return Optional.empty();
        }

        List<Data> dataset = new LinkedList<>();
		ListOperations<String, String> listOps = redisTemplate.opsForList();
		long listSize = listOps.size(userName);
        
		for (long i = 0; i < listSize; i++) {
			String payloadStr = listOps.index(userName, i);
			dataset.add(Data.create(payloadStr));
		}

		return Optional.of(dataset);
    }

}
