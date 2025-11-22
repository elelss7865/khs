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
import com.seazon.board.domain.SignUp;
import com.seazon.board.domain.SignUpForm;
import com.seazon.board.domain.SiteUser;
import com.seazon.board.repository.UserRepository;
import com.seazon.board.service.SignUpService;
import com.seazon.board.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/signUp")
@RequiredArgsConstructor
@Controller
public class SignUpController {

	private final SignUpService signUpService;
	private final UserService userService;
	private final UserRepository userRepository;

//    @GetMapping("/list")
//    public String list(Model model) {
//        List<signUp> questionList = this.questionService.getList();
//        model.addAttribute("questionList", questionList);
//        return "question_list";
//    }
	
	// 페이징 구현(검색 기능 추가)
	@GetMapping("/list")
	public String list(Model model, 
            @RequestParam(value="page", defaultValue="0") int page,
            @RequestParam(value = "kw", defaultValue = "") String kw) {
		Page<SignUp> paging = this.signUpService.getList(page, kw);
		
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		return "signUp_list";
		}
		
	// 레시피 추가
	@GetMapping(value = "/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
	    SignUp signUp = signUpService.getsignUp(id);
	    signUpService.incrementViewCount(id);
	    
//	    log.info("유저네임" + question.getAuthor().getProfilePath());
	    String profilePath = signUp.getAuthor().getProfilePath();
	    model.addAttribute("profilePath", profilePath);
	    model.addAttribute("signUp", signUp);
	    return "signUp_detail";
	}
    
	// 접근제한(로그인 한 사용자만 접근가능)
//    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String signUpCreate(SignUpForm signUpForm) {
        return "signUp_form";
    }
    
    // 레시피 작성
//    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String signUpCreate(@Valid SignUpForm signUpForm,
            BindingResult bindingResult, Principal principal, MultipartFile file) throws Exception{
        if (bindingResult.hasErrors()) {
            return "signUp_form";
            
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.signUpService.create(signUpForm.getSubject(), siteUser, signUpForm.getFile(),
        		signUpForm.getCookIntro(), signUpForm.getCategory(), signUpForm.getCookInfo_level(), 
        		signUpForm.getCookInfo_people(), signUpForm.getCookInfo_time(), signUpForm.getIngredient(),
        		signUpForm.getCapacity(), signUpForm.getContent(), signUpForm.getContentFile());
        log.info("로그(질문작성):" + signUpForm);
        return "redirect:/signUp/list";
    }
    
    // 질문 수정
//    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String signUpModify(SignUpForm signUpForm, @PathVariable("id") Integer id, Principal principal, 
    		MultipartFile file) {
    	SignUp signUp = this.signUpService.getsignUp(id);
    	if(!signUp.getAuthor().getUsername().equals(principal.getName())) {
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
    	}
    	signUpForm.setSubject(signUp.getSubject());
    	signUpForm.setCookIntro(signUp.getCookIntro());
    	signUpForm.setCategory(signUp.getCategory());
    	signUpForm.setCookInfo_people(signUp.getCookInfo_level());
    	signUpForm.setCookInfo_time(signUp.getCookInfo_people());
    	signUpForm.setCookInfo_level(signUp.getCookInfo_time());
    	signUpForm.setFile(file);
    	return "signUp_form";
    }
    
    // 질문 수정 후 저장
//    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String signUpModify(@Valid SignUpForm signUpForm, BindingResult bindingResult, 
            Principal principal, @PathVariable("id") Integer id, MultipartFile file) throws Exception {
        if (bindingResult.hasErrors()) {
            return "signUp_form";
        }
        SignUp signUp = this.signUpService.getsignUp(id);
        if (!signUp.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.signUpService.modify(signUp, signUpForm.getSubject(), signUpForm.getFile(),
        		signUpForm.getCookIntro(),signUpForm.getCategory(),signUpForm.getCookInfo_level(),
        		signUpForm.getCookInfo_people(),signUpForm.getCookInfo_time());
        return String.format("redirect:/signUp/detail/%s", id);
    }
    
    // 질문 삭제
//    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String signUpDaelete(Principal principal, @PathVariable("id") Integer id) {
    	SignUp signUp = this.signUpService.getsignUp(id);
    	if (!signUp.getAuthor().getUsername().equals(principal.getName())) {
    		throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제권한이 없습니다.");
    	}
    	this.signUpService.delete(signUp);
    	return "redirect:/";
    }
    
    // 추천 버튼 클릭 시 호출
//    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String signUpVote(Principal principal, @PathVariable("id") Integer id) {
    	SignUp signUp = this.signUpService.getsignUp(id);
    	SiteUser siteuser = this.userService.getUser(principal.getName());
    	this.signUpService.vote(signUp, siteuser);
    	return String.format("redirect:/signUp/detail/%s", id);
    }
      
}