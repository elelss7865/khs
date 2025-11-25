package com.seazon.board.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.seazon.board.domain.AnswerForm;
import com.seazon.board.domain.Travel;
import com.seazon.board.domain.TravelForm;
import com.seazon.board.domain.SiteUser;
import com.seazon.board.repository.UserRepository;
import com.seazon.board.service.TravelService;
import com.seazon.board.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/travel")
@RequiredArgsConstructor
@Controller
public class travelController {

	private final TravelService travelService;
	private final UserService userService;
	private final UserRepository userRepository;

//    @GetMapping("/list")
//    public String list(Model model) {
//        List<travel> questionList = this.questionService.getList();
//        model.addAttribute("questionList", questionList);
//        return "question_list";
//    }
	
	// 페이징 구현(검색 기능 추가)
	@GetMapping("/list")
	public String list(Model model, 
            @RequestParam(value="page", defaultValue="0") int page,
            @RequestParam(value = "kw", defaultValue = "") String kw) {
		Page<Travel> paging = this.travelService.getList(page, kw);
		
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		return "travel_list";
		}
		
	// 축제 추가
	@GetMapping(value = "/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
	    Travel travel = travelService.getTravel(id);
	    travelService.incrementViewCount(id);
	    
//	    log.info("유저네임" + question.getAuthor().getProfilePath());
	    String profilePath = travel.getAuthor().getProfilePath();
	    model.addAttribute("profilePath", profilePath);
	    model.addAttribute("travel", travel);
	    return "travel_detail";
	}
    
	// 접근제한(로그인 한 사용자만 접근가능)
//    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String travelCreate(TravelForm travelForm) {
        return "travel_form";
    }
    
    // 축제 작성
//    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String travelCreate(@Valid TravelForm travelForm,
            BindingResult bindingResult, Principal principal, MultipartFile file) throws Exception{
        if (bindingResult.hasErrors()) {
            return "travel_form";
            
        }
//        SiteUser siteUser = this.userService.getUser("관리자");
        String loginId = principal.getName(); // 관리자

     // name → username 매핑
     SiteUser siteUser = userRepository.findByusername(loginId)
    	        .orElseThrow();
        this.travelService.create(travelForm.getSubject(), siteUser, travelForm.getFile(),
        		travelForm.getTravelIntro(), travelForm.getCategory(),travelForm.getPlace(), travelForm.getTravelInfo_day(), 
        		travelForm.getTravelInfo_place(), travelForm.getTravelInfo_pay(),
        		travelForm.getContent(),travelForm.getTravelInfo_phone(),travelForm.getTravelInfo_organizer(),
        		travelForm.getTravelInfo_homepage());
        log.info("로그(질문작성):" + travelForm);
        return "redirect:/travel/list";
    }
    
    // 질문 수정
//    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String travelModify(TravelForm travelForm, @PathVariable("id") Integer id, Principal principal, 
    		MultipartFile file) {
    	Travel travel = this.travelService.getTravel(id);
    	if(!travel.getAuthor().getUsername().equals(principal.getName())) {
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
    	}
    	travelForm.setSubject(travel.getSubject());
    	travelForm.setTravelIntro(travel.getTravelIntro());
    	travelForm.setCategory(travel.getCategory());
    	travelForm.setTravelInfo_day(travel.getTravelInfo_day());
    	travelForm.setTravelInfo_place(travel.getTravelInfo_place());
    	travelForm.setTravelInfo_pay(travel.getTravelInfo_pay());
    	travelForm.setContent(travel.getContent());
    	travelForm.setTravelInfo_phone(travel.getTravelInfo_phone());
    	travelForm.setTravelInfo_organizer(travel.getTravelInfo_organizer());
    	travelForm.setTravelInfo_homepage(travel.getTravelInfo_homepage());
    	travelForm.setFile(file);
    	return "travel_form";
    }
    
    // 질문 수정 후 저장
//    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String travelModify(@Valid TravelForm travelForm, BindingResult bindingResult, 
            Principal principal, @PathVariable("id") Integer id, MultipartFile file) throws Exception {
        if (bindingResult.hasErrors()) {
            return "travel_form";
        }
        Travel travel = this.travelService.getTravel(id);
        if (!travel.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.travelService.modify(travel, travelForm.getSubject(), travelForm.getFile(),
        		travelForm.getTravelIntro(),travelForm.getCategory(),travelForm.getTravelInfo_day(),
        		travelForm.getTravelInfo_place(),travelForm.getTravelInfo_pay());
        return String.format("redirect:/travel/detail/%s", id);
    }
    
    // 질문 삭제
//    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String travelDaelete(Principal principal, @PathVariable("id") Integer id) {
    	Travel travel = this.travelService.getTravel(id);
    	if (!travel.getAuthor().getUsername().equals(principal.getName())) {
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제권한이 없습니다.");
    	}
    	this.travelService.delete(travel);
    	return "redirect:/";
    }
    
    // 추천 버튼 클릭 시 호출
//    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String travelVote(Principal principal, @PathVariable("id") Integer id) {

        // 로그인 안 되어 있으면 로그인 페이지로 이동
        if (principal == null) {
            return "redirect:/user/login";
        }

        Travel travel = this.travelService.getTravel(id);
        SiteUser siteuser = this.userService.getUser(principal.getName());
        this.travelService.vote(travel, siteuser);

        return String.format("redirect:/travel/detail/%s", id);
    }
      
}