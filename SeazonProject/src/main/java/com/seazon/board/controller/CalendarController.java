package com.seazon.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
// import 필요한 Service (FestivalService 등) 및 Domain (Festival) Import

@Controller
@RequestMapping("/calendar") // 기본 URL 경로 설정
public class CalendarController {

    // private final FestivalService festivalService; // 축제 데이터를 가져올 서비스

    @GetMapping
    public String showCalendar(Model model) {
        // 1. 달력에 필요한 기본 데이터 준비 (예: 현재 연도, 월)
        int year = 2025;
        int month = 11;
        
        // 2. 축제 데이터 조회 (DB에서 해당 월의 모든 축제 데이터를 가져옵니다.)
        // List<Festival> festivalList = festivalService.getFestivalsByMonth(year, month); 
        
        // 3. 모델에 데이터 추가
        // model.addAttribute("festivalList", festivalList);
        model.addAttribute("currentYear", year);
        model.addAttribute("currentMonth", month);
        
        // 4. 달력 템플릿 파일(calendar.html)을 렌더링하도록 반환
        return "calendar"; 
    }
    
    // (선택 사항) AJAX 요청을 처리하여 특정 날짜의 축제 리스트를 JSON으로 반환하는 매핑도 추가할 수 있습니다.
    /*
    @GetMapping("/data")
    @ResponseBody
    public List<Festival> getFestivalData(@RequestParam String date) {
        // return festivalService.getFestivalsByDate(date);
    }
    */
}