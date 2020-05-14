package dreambike.gateway;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@RestController
	  @RequestMapping(path = "/api/employees")
//	  @PreAuthorize("hasAnyAuthority('ROLE_USER')")
	  public static class EmployeeRestController {

	    @Autowired
	    private EmployeeService employeeService;

	    @GetMapping
	    public String getEmployeeAndDepartment() {
	      return employeeService.getEmployeeAndDepartment();
	    }
	    
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
	


	  @Service
	  public static class EmployeeService {

	    @Autowired
	    private DepartmentRestClient departmentRestClient;

	    public String getEmployeeAndDepartment() {
	      String employeeName = "Rick";
	      String departmentName = departmentRestClient.getDepartmentName();

	      return "Employee Service Returned: " + employeeName + ", \nDepartment Service Returned: " + departmentName;
	    }
	  }

	  @Component
	  public static class DepartmentRestClient {

	    @Autowired
	    private OAuth2RestTemplate oAuth2RestTemplate;

	    public String getDepartmentName() {
	      return oAuth2RestTemplate.getForObject("http://localhost:8095/api/departments/1", String.class);
	    }
	  }
}
