package vttp2022.ssf.assessment.videosearch.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vttp2022.ssf.assessment.videosearch.models.Game;
import vttp2022.ssf.assessment.videosearch.service.SearchService;

@Controller
@RequestMapping(path="")
public class SearchController {

    @Autowired
    SearchService searchSvc;

    @GetMapping(path="/search", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String getSearch (@RequestParam(name="gameSearch", defaultValue="10") String searchString, @RequestParam(name="gameNumber") Integer count, Model model) {
        List<Game> gameLists = searchSvc.search(searchString, count);
        System.out.println(searchString);
        System.out.println(count);

        if (gameLists.isEmpty()) {
            return "errorPage";
        } else {
            model.addAttribute("games", gameLists);
            return "gameResult";
        }
    }
}
