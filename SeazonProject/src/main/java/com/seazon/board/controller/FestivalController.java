package com.seazon.board.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.seazon.board.domain.Travel; 
import com.seazon.board.service.TravelService; 
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/festivals")
public class FestivalController {
	
    // TravelService 주입
    private final TravelService travelService; 
    
    // createDummyData 메서드는 삭제합니다.

    @GetMapping({"", "/"}) // GET /festivals 또는 /festivals/ 둘 다 처리
    public String getFestivalRoot(Model model) {
        // travelService를 통해 모든 travel 데이터를 가져옵니다.
        List<Travel> travelList = this.travelService.getList(); 
        
        // 모델에 "travelList"라는 이름으로 데이터를 담습니다.
        model.addAttribute("travelList", travelList); 
        return "festivalList";
    }


    /**
     * 축제 목록 페이지 (GET /festivals/list)
     * 카테고리 필터 기능을 포함하며, 인자 없는 요청도 처리합니다 (category = null).
     * 기존의 public String getFestivalList(Model) 메서드를 이 메서드가 대체합니다.
     */
    @GetMapping("/list")
    public String getFestivalList(Model model, 
          @RequestParam(value = "category", required = false) String category,
          @RequestParam(value = "place", required = false) String place) {

        List<Travel> travelList = this.travelService.getListByFilter(category, place);

        model.addAttribute("travelList", travelList);
        model.addAttribute("selectedCategory", category); 
        model.addAttribute("selectedPlace", place); 
        
        return "festivalList";
    }
    
    
    // 전체검색
    @GetMapping("/search")
    public String searchFestival(Model model,
            @RequestParam(value="kw", required=false) String kw) {

        // 검색 실행
        Page<Travel> paging = this.travelService.getList(0, kw);

        // 결과 화면으로 전달
        model.addAttribute("travelList", paging.getContent());
        model.addAttribute("kw", kw);

        return "festivalList";
    }

}