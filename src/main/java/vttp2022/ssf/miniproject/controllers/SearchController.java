package vttp2022.ssf.miniproject.controllers;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import vttp2022.ssf.miniproject.models.Reviews;
import vttp2022.ssf.miniproject.services.SearchService;


@Controller
@RequestMapping
public class SearchController {

    @Autowired
    private SearchService searchSvc;
    private String keyword;
    private String userName;

    @PostMapping (value = "/search", consumes="application/x-www-form-urlencoded", produces="text/html")
    public String getReviews(@RequestBody MultiValueMap<String, String> form, Model model) {
        
        userName = form.getFirst("userName");
        keyword = form.getFirst("keyword");
       
        List<Reviews> listOfReviews = new LinkedList<>();
        listOfReviews = searchSvc.getReviews(keyword);

        model.addAttribute("keyword", keyword.toUpperCase());
        model.addAttribute("userName", userName.toUpperCase());
        model.addAttribute("listOfReviews", listOfReviews);

        return "search";
    }


    @PostMapping (value = "/review", consumes="application/x-www-form-urlencoded", produces="text/html")
    public String saveReview(@RequestBody MultiValueMap<String, String> form, Model model) {

        //System.out.println(form.getFirst("userName"));
        //System.out.println(form.getFirst("name"));
        //System.out.println(form.getFirst("text"));
               
        String userName = form.getFirst("userName");
        String name = form.getFirst("name");
        String text = form.getFirst("text");

        // inject model for html
        model.addAttribute("userName", userName);
        model.addAttribute("name", name);
        model.addAttribute("text", text);

        // generate the data and send to repo 
        searchSvc.saveToRepo(searchSvc.generateData(name, text), userName);

        return "review";
    }

}

    // Further Version

    /* @GetMapping
    public String getReviews(@RequestParam (name = "userName", required = true) String userName, @RequestParam String keyword, Model model) {

        List<Reviews> listOfReviews = new LinkedList<>();
        listOfReviews = searchSvc.getReviews(keyword);

        User user = new User();
		user.setUserName(userName);

        System.out.println(userName);
        //String Name = "";
        //String Body = "";
        model.addAttribute("keyword", keyword);
        //model.addAttribute("name", Name);
        //model.addAttribute("body", Body);
        model.addAttribute("userName", userName);
        model.addAttribute("listOfReviews", listOfReviews);
       
        return "search";

        } */