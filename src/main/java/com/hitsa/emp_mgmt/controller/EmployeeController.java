package com.hitsa.emp_mgmt.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hitsa.emp_mgmt.exception.ResourceNotFoundException;
import com.hitsa.emp_mgmt.model.Employee;
import com.hitsa.emp_mgmt.repository.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// In controller we create REST apis
@RestController
// RestController annotation is combination of Controller and ResponseBody annotations.
// RestController annotation is used to create RESTful web services using Spring MVC. 
//Spring RestController takes care of mapping request data to the defined request handler method. 
//Once response body is generated from the handler method, it converts it to JSON or XML response.
//If our method is returning a list or array, then spring will only support JSON response because 
//XML root element can’t be anonymous but JSON can. If you want to support returning list as XML, 
//then you will have to create a wrapper class to hold this list and return it.
@RequestMapping("/api/v1/")
/**
 * RequestMapping annotation is used to map web requests onto specific handler classes and/or 
 * handler methods. @RequestMapping can be applied to the controller class as well as methods.
 * The @RequestMapping annotation maps all the HTTP request methods by default.
 * You could use specific methods by using the method property of RequestMapping. For example, you could
write a @RequestMethod annotation in the following way to use the POST method:
@RequestMapping(value = "/power", method = POST)
For passing the parameters along the way, the sample demonstrates both request parameters and path
parameters using annotations @RequestParam and @PathVariable, respectively.

 *1. @RequestMapping with Class: We can use it with class definition to create the base URI as we have done.
 *2.@RequestMapping with Method: We can use it with method to provide the URI pattern for which handler 
method will be used.
 
@RequestMapping(value="/method0")
@ResponseBody
public String method0(){
	return "method0";
}
Above annotation can also be written as @RequestMapping("/method0"). On a side note, I am using 
@ResponseBody to send the String response for this web request, this is done to keep the example simple.
 
 * 3. @RequestMapping with Multiple URI: We can use a single method for handling multiple URIs, 
for example:

@RequestMapping(value={"/method1","/method1/second"})
@ResponseBody
public String method1(){
	return "method1";
}
If you will look at the source code of RequestMapping annotation, you will see that all of it’s variables 
are arrays. We can create String array for the URI mappings for the handler method.

 *4. @RequestMapping with HTTP Method: Sometimes we want to perform different operations based on the 
 HTTP method used, even though request URI remains same. We can use @RequestMapping method variable to 
 narrow down the HTTP methods for which this method will be invoked. For example:

@RequestMapping(value="/method2", method=RequestMethod.POST)
@ResponseBody
public String method2(){
	return "method2";
}
	
@RequestMapping(value="/method3", method={RequestMethod.POST,RequestMethod.GET})
@ResponseBody
public String method3(){
	return "method3";
}

 *5. @RequestMapping with @PathVariable: RequestMapping annotation can be used to handle dynamic URIs where 
 one or more of the URI value works as a parameter. We can even specify Regular Expression for URI 
 dynamic parameter to accept only specific type of input. It works with @PathVariable annotation through 
 which we can map the URI variable to one of the method arguments. For example:

@RequestMapping(value="/method7/{id}")
@ResponseBody
public String method7(@PathVariable("id") int id){
	return "method7 with id="+id;
}
	
@RequestMapping(value="/method8/{id:[\\d]+}/{name}")
@ResponseBody
public String method8(@PathVariable("id") long id, @PathVariable("name") String name){
	return "method8 with id= "+id+" and name="+name;
}

 *6. @RequestMapping with @RequestParam for URL parameters: Sometimes we get parameters in the request 
 URL, mostly in GET requests. We can use @RequestMapping with @RequestParam annotation to retrieve the 
 URL parameter and map it to the method argument. For example:

@RequestMapping(value="/method9")
@ResponseBody
public String method9(@RequestParam("id") int id){
	return "method9 with id= "+id;
}
 */
public class EmployeeController {

/**
 * Spring @Autowired annotation is used for automatic dependency injection. Spring framework is built 
 * on dependency injection and we inject the class dependencies through spring bean configuration file.
 * Usually we provide bean configuration details in the spring bean configuration file and we also 
 * specify the beans that will be injected in other beans using ref attribute.
 * But Spring framework provides autowiring features too where we don’t need to provide bean injection 
 * details explicitly.
 */
    @Autowired
    private EmployeeRepository employeeRepository;
//all the business logic is part of Repository class.

    //get all employees api
    @GetMapping("/employees")
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    @PostMapping("/employees")
    // create employee rest api
    public Employee createEmployee(@RequestBody Employee employee){
        return employeeRepository.save(employee);
    }

    //get employee by id rest api
    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id){
        Employee employee = employeeRepository
        .findById(id)
        .orElseThrow(
            () -> new ResourceNotFoundException("Employee not exist with id :" + id)
        );
        return ResponseEntity.ok(employee);
    }

    //update employee rest api
    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails){
        Employee employee = employeeRepository
        .findById(id)
        .orElseThrow(
            () -> new ResourceNotFoundException("Employee not exist with id :" + id)
        );
        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setEmailId(employeeDetails.getEmailId());

        Employee updatedEmployee = employeeRepository.save(employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    // delete employee rest api
    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable Long id){
        Employee employee = employeeRepository
        .findById(id)
        .orElseThrow(
            () -> new ResourceNotFoundException("Employee not exist with id :" + id)
        );

        employeeRepository.delete(employee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}

/**
 * Accept and Content-Type Request Headers
We have configured our REST application to work with both XML and JSON. So how it will know that 
whether the request is XML or JSON. And if the response should be sent in JSON or XML format. 
This is where Accept and Content-Type Request Headers are used.

Content-Type: Defined the type of content in request body, if its value is “application/xml” then Spring 
will treat request body as XML document. If its value is “application/json” then the request body is 
treated as JSON.

Accept: Defined the type of content client is expecting as response. If its value is “application/xml” 
then XML response will be sent. If its value is “application/json” then JSON response will be sent.
 */