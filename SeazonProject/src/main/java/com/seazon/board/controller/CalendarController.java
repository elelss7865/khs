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

	private final CalendarService calendarService;

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

		// 해당 월에 '진행 중'이거나 '포함되는' 모든 축제를 가져옵니다.
		List<Travel> festivalList = calendarService.getFestivalsByMonth(year, month);

		model.addAttribute("festivallist", festivalList);

		// JSON 데이터 생성 (프론트엔드 달력 렌더링용)
		try {
			ObjectMapper mapper = new ObjectMapper();

			List<Map<String, Object>> list = new ArrayList<>();

			for (Travel t : festivalList) {
				Map<String, Object> map = new HashMap<>();
				map.put("subject", t.getSubject());
				map.put("travelInfo_place", t.getTravelInfo_place());
				
				// 이미지 경로 및 ID 추가
				map.put("image", t.getFilePath()); 
				map.put("id", t.getId()); 

                // DB의 분리된 필드(travelInfo_startdate, travelInfo_enddate)를 사용
                // DB 필드가 'YYYY.MM.DD' 형식이라고 가정하고, JS에서 사용하기 쉽도록 'YYYY-MM-DD'로 변환합니다.
                // 기존의 t.getTravelInfo_day()를 파싱하는 로직을 대체했습니다.
                String startDate = t.getTravel_start_date() != null ? t.getTravel_start_date().replace(".", "-") : "2000-01-01";
                String endDate = t.getTravel_end_date() != null ? t.getTravel_end_date().replace(".", "-") : "2000-01-01";
                
				map.put("date_start", startDate); 
				map.put("date_end", endDate); 
				
				// 전체 기간 문자열 (표시용)
				map.put("travelInfo_day", t.getTravelInfo_day());

				list.add(map);
			}

			String json = mapper.writeValueAsString(list);
			model.addAttribute("festivalJson", json);

		} catch (Exception e) {
			// 오류 발생 시 로깅
			e.printStackTrace();
			model.addAttribute("festivalJson", "[]"); // 오류 시 빈 JSON 전달
		}

		model.addAttribute("currentYear", year);
		model.addAttribute("currentMonth", month);

		return "calendar";
	}
}