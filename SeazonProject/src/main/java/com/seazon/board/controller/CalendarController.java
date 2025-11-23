package com.seazon.board.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
// import 필요한 Service (FestivalService 등) 및 Domain (Festival) Import
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seazon.board.domain.Travel;
import com.seazon.board.service.CalendarService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/calendar") // 기본 URL 경로 설정
@RequiredArgsConstructor
public class CalendarController {

	private final CalendarService calendarService; // 축제 데이터를 가져올 서비스

	  @GetMapping
	  public String showCalendar(
		        @RequestParam(required = false) Integer year,
		        @RequestParam(required = false) Integer month,
		        Model model) {

		    if (year == null || month == null) {
		        LocalDate today = LocalDate.now();
		        year = today.getYear();
		        month = today.getMonthValue();
		    }

		    List<Travel> festivalList = calendarService.getFestivalsByMonth(year, month);

		    model.addAttribute("festivallist", festivalList);

		    // JSON 추가
		    try {
		        ObjectMapper mapper = new ObjectMapper();

		        List<Map<String, Object>> list = new ArrayList<>();

		        for (Travel t : festivalList) {
		            Map<String, Object> map = new HashMap<>();
		            map.put("subject", t.getSubject());
		            map.put("travelInfo_place", t.getTravelInfo_place());

		            String day = t.getTravelInfo_day();   // "2025.11.19 ~ 2025.11.28"
		            map.put("date_start", day.substring(0, 10).replace(".", "-"));
		            map.put("date_end",   day.substring(13, 23).replace(".", "-"));

		            list.add(map);
		        }

		        String json = mapper.writeValueAsString(list);
		        model.addAttribute("festivalJson", json);

		    } catch (Exception e) {
		        model.addAttribute("festivalJson", "[]");
		    }

		    model.addAttribute("currentYear", year);
		    model.addAttribute("currentMonth", month);

		    return "calendar";
		}
}