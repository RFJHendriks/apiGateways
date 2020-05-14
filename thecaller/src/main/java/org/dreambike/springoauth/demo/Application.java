package org.dreambike.springoauth.demo;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
		public ResponseEntity<String> login(@Valid @RequestBody String username, @Valid @RequestBody String password) {
			System.out.println("hoi");
			final String uri = "http://localhost:8080/auth/realms/DreamBikeKeycloak/protocol/openid-connect/token/";
			HttpResponse<String> response = Unirest.post(uri)
					  .header("content-type", "application/x-www-form-urlencoded")
					  .body("grant_type=password&username="+username+"&password="+password+"&client_id=login-appD&client_secret=e729b79d-4280-4ce4-bcf3-4fd6321bc491")
					  .asString();
			System.out.println(response);
			return null;
		}

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
