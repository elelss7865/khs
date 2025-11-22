package com.seazon.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.seazon.board.domain.Festival;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/festivals") // 기본 URL 경로를 /festivals로 설정
public class FestivalController {
	

    @GetMapping({"", "/"}) // GET /festivals 또는 /festivals/ 둘 다 처리
    public String getFestivalRoot(Model model) {
        List<Festival> initialFestivals = createDummyData(); // 기존과 동일
        model.addAttribute("festivals", initialFestivals);
        return "festivalList"; // src/main/resources/templates/festivalList.html
    }


    /**
     * 축제 목록 페이지 (festivalList.html)를 렌더링하고, 초기 데이터를 모델에 담아 전달합니다.
     * * @param model Thymeleaf로 데이터를 전달하기 위한 모델 객체
     * @return 뷰 이름 (festivalList.html)
     */
    @GetMapping("/list")
    public String getFestivalList(Model model) {
        
        // 1. 임시 축제 데이터 생성 (데이터베이스 연동 전 임시 사용)
        List<Festival> initialFestivals = createDummyData();

        // 2. 데이터를 모델에 담아 View로 전달
        model.addAttribute("festivals", initialFestivals);
        
        // 3. Thymeleaf 템플릿 이름 반환 (src/main/resources/templates/festivalList.html을 찾음)
        return "festivalList";
    }

    /**
     * 임시 데이터 생성 메서드
     */
    private List<Festival> createDummyData() {
        // HTML의 JavaScript에서 사용했던 더미 데이터를 그대로 사용합니다.
        return Arrays.asList(
            new Festival("메리마마 크리스마스", "2025-11-01", "2025-11-23", "전남 화순군", "전남", "크리스마스", "개최 중", "/image/christmas.png"),
            new Festival("별빛 축제", "2025-11-18", "2025-12-25", "경북 구미시", "경북", "빛", "개최 중", "/image/starlight.png"),
            new Festival("힐링 콘서트", "2025-12-01", "2025-12-01", "서울 마포구", "서울", "음악", "예정", "/image/concert.png"),
            new Festival("전국 음식 박람회", "2025-10-10", "2025-10-12", "부산 해운대", "부산", "음식", "종료", "/image/food.png")
            // 실제로는 Service 계층에서 데이터베이스로부터 데이터를 가져와야 합니다.
        );
    }
}