package com.seazon.board.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.seazon.board.domain.Travel;
import com.seazon.board.repository.TravelRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CalendarService {

	private final TravelRepository travelRepository;

	/**
	 * 특정 년/월에 걸쳐 진행되는 모든 축제를 조회합니다.
	 * @param year 조회할 년도
	 * @param month 조회할 월
	 * @return 해당 월에 진행 중인 Travel 목록
	 */
	public List<Travel> getFestivalsByMonth(int year, int month) {
		
		// 1. 해당 월의 시작일과 종료일 계산
		LocalDate startOfMonth = LocalDate.of(year, month, 1);
		LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());
		
		// 2. Repository의 날짜 형식이 'YYYY.MM.DD'라고 가정하고 쿼리 파라미터 생성
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
		
		String startOfMonthQuery = startOfMonth.format(formatter); // 예: 2025.11.01
		String endOfMonthQuery = endOfMonth.format(formatter); 	  // 예: 2025.11.30

		// 3. Repository 호출
		return travelRepository.findFestivalsIntersectingMonth(startOfMonthQuery, endOfMonthQuery);
	}
}