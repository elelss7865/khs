package com.seazon.board.service;

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

//	  public List<Travel> getFestivalsByMonth(int year, int month) {
//	        String monthStr = String.format("%02d", month); // '01' ~ '12'
//	        String yearStr = String.valueOf(year);
//
//	        return travelRepository.findByYearAndMonth(yearStr, monthStr);
//	    }
	public List<Travel> getFestivalsByMonth(int year, int month) {
	    String mm = String.format("%02d", month);
	    return travelRepository.findByMonth(mm);
	}
}