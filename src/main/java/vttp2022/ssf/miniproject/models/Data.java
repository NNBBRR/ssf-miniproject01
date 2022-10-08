package vttp2022.ssf.miniproject.models;

import java.io.StringReader;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Data {

    private String name;
    private String text;
    //private String authorName;
    //private String profilePhoto;
    //private String rating;

    public static String userName;

    public Data(String name, String text) {
        this.name = name;
        this.text = text;
    }
    
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JsonObject toJson() {
		return Json.createObjectBuilder()
            .add("name", name)
            //.add("uuid", this.uuid)
			//.add("authorName", this.authorName)
			.add("text", text)
			//.add("rating", this.rating)
			//.add("time", this.time)
			//.add("profilePhoto", this.profilePhoto)
			.build();
	}

    public static Data create(String json) {
		    StringReader strReader = new StringReader(json);
			JsonReader j = Json.createReader(strReader);
            JsonObject jo = j.readObject();
            
            String name = jo.getString("name");
            String text = jo.getString("text");

            Data d = new Data(name, text);

			return d;	
	}

    @Override
    public String toString() {
        return "Data [name=" + name + ", text=" + text + "]";
    }
    
}
