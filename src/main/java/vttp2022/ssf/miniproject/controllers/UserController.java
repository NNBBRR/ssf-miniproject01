package vttp2022.ssf.miniproject.controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vttp2022.ssf.miniproject.models.Data;
import vttp2022.ssf.miniproject.models.User;
import vttp2022.ssf.miniproject.services.SearchService;

@Controller
@RequestMapping
public class UserController {

	@Autowired
	private SearchService searchSvc;

    @GetMapping(value="/user")
    public String getUser(@RequestParam (name = "userName", required = true) String userName, Model model) {

		User currentuser = new User();
		Optional<User> newUser = searchSvc.getByUsername(userName.toUpperCase());
		
		if (newUser.isEmpty()){
            currentuser.setUserName(userName.toUpperCase());
        } else {
            currentuser = newUser.get();
        }
			
        List<Data> dataset = new LinkedList<>();
        dataset = searchSvc.getRedisUser(userName.toUpperCase());

		System.out.println("dataset");

        model.addAttribute("userName", userName.toUpperCase());
        model.addAttribute("dataset", dataset);
        
        return "user";

    }


    @GetMapping(value="/summary")
    public String getSummary(@RequestParam (name = "userName") String userName, Model model) {

		User currentuser = new User();
		Optional<User> newUser = searchSvc.getByUsername(userName);
		
		if (newUser.isEmpty()){
            currentuser.setUserName(userName);
        } else {
            currentuser = newUser.get();
        }
		
        List<Data> dataset = new LinkedList<>();
        dataset = searchSvc.getRedisUser(userName);

		System.out.println("datasetsummary");

        model.addAttribute("userName", userName.toUpperCase());
        model.addAttribute("dataset", dataset);
        
        return "summary";

    }

}

	/* @PostMapping
	public String postUser(@RequestBody MultiValueMap<String, String> form
				, Model model) {

		String userName = form.getFirst("userName");
		if ((null == userName) || (userName.trim().length() <= 0))
			userName = "anonymous";

		model.addAttribute("userName", userName.toUpperCase());
		
		User user = new User();
		user.setUserName(userName);	


		return "user";
	} 
	*/
