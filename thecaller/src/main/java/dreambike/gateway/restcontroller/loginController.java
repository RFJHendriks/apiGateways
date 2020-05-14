package dreambike.gateway.restcontroller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/")
public class loginController {
	
	    
		@GetMapping("/login")
		public Map<String,Object> login(@RequestParam String username, @RequestParam String password) throws IOException {
			final String uri = "http://localhost:8080/auth/realms/DreamBikeKeycloak/protocol/openid-connect/token/";
			String clientSecret = "e729b79d-4280-4ce4-bcf3-4fd6321bc491";
			String body = "grant_type=password&username="+username+"&password="+password+"&client_id=login-app&client_secret="+clientSecret+"&scope=openid";
			URL url = new URL(uri);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.getOutputStream().write(body.getBytes("UTF-8"));
			BufferedReader reader = new BufferedReader(new InputStreamReader((con.getInputStream()), StandardCharsets.UTF_8));
//			JsonParser jsonParser = new JsonParser();
//			JsonObject jsonObject = (JsonObject)jsonParser.parse(new InputStreamReader(con.getInputStream(), "UTF-8"));
//			System.out.println(jsonObject);
//			JsonReader readerJson = new JsonReader(new InputStreamReader((con.getInputStream()), StandardCharsets.UTF_8));
//			System.out.println(readerJson.toString());
//			JsonArray jsonArray = readerJson.readArray();
	        String json = reader.readLine();
	        reader.close();
	        JSONObject jsonObject = new JSONObject(json);	
	        Iterator<String> test = jsonObject.keys();
	        Map<String,Object> testJson = new HashMap<String,Object>();
	        while (test.hasNext()) {
	        	String temp = test.next();
 	        	testJson.put(temp, jsonObject.get(temp).toString());;
	        }
	        con.disconnect();
			return testJson;			
		}	
		
//		@GetMapping("/login")
//		public ResponseEntity<String> login(@Valid @RequestBody String username, @Valid @RequestBody String password) {
//			System.out.println("hoi");
//			final String uri = "http://localhost:8080/auth/realms/DreamBikeKeycloak/protocol/openid-connect/token/";
//			HttpResponse<String> response = Unirest.post(uri)
//					  .header("content-type", "application/x-www-form-urlencoded")
//					  .body("grant_type=password&username="+username+"&password="+password+"&client_id=login-appD&client_secret=e729b79d-4280-4ce4-bcf3-4fd6321bc491")
//					  .asString();
//			System.out.println(response);
//			return null;
//		}
		
}
