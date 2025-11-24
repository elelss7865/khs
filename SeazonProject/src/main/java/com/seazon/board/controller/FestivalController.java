package com.seazon.board.controller;

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
                                  @RequestParam(value = "category", required = false) String category) {
        
        // category 파라미터가 없거나 비어있는 경우, Service에서 전체 목록을 반환하도록 처리합니다.
        List<Travel> travelList = this.travelService.getListByCategory(category);

        model.addAttribute("travelList", travelList);
        // 선택 상태 유지를 위해 다시 모델에 담습니다.
        model.addAttribute("selectedCategory", category); 
        
        return "festivalList";
    }

}