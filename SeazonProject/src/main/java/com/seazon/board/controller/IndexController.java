package com.seazon.board.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.seazon.board.domain.Travel;
import com.seazon.board.domain.SiteUser;
import com.seazon.board.service.TravelService;
import com.seazon.board.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

@Controller

// @Slf4j

public class IndexController {
    private final TravelService travelService;
    private final UserService userService;

    @GetMapping("/index")
    public String list(Model model,
                       @RequestParam(value="page", defaultValue="0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Travel> paging1 = this.travelService.getTopList(page, kw);
        List<SiteUser> siteUsers1 = this.userService.getUsers(paging1);
        Page<Travel> paging2 = this.travelService.getRecentlyList(page, kw);
        List<SiteUser> siteUsers2 = this.userService.getUsers(paging2);

        model.addAttribute("siteUsers1", siteUsers1);
        model.addAttribute("siteUsers2", siteUsers2);
        model.addAttribute("paging1", paging1);
        model.addAttribute("paging2", paging2);
        model.addAttribute("kw", kw);

        return "index";

    }

}