package com.myspring.pro30.member.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;  //컨트롤러가 리디렉션 시나리오에 대한 특성을 선택하는 데 사용할 수 있는 모델 인터페이스의 특수화입니다. 리디렉션 속성을 추가하는 의도는 매우 명시적이므로(즉, 리디렉션 URL에 사용하기 위해) 속성 값은 문자열로 형식화되고 쿼리 문자열에 추가되거나 URI 변수로 확장될 수 있도록 그런 방식으로 저장될 수 있습니다

import com.myspring.pro30.member.service.MemberService;
import com.myspring.pro30.member.vo.MemberVO;




@Controller("memberController")
public class MemberControllerImpl    implements MemberController{
	
	
	 private static final Logger logger = LoggerFactory.getLogger(MemberControllerImpl.class);
	
	
	
	@Autowired
	private MemberVO memberVO;
	
	@Autowired
	private MemberService memberService;
	
	
	@RequestMapping(value = "/main.do", method = RequestMethod.GET)
	public String main(Locale locale, Model model) {

		logger.info("당신은 지금 main.do를 쳐서 메이페이지를 요청함");
		
		
		return "main";
	}
	
	
	
	
	
	
//	public void setMemberService(MemberService memberService) {
//		this.memberService = memberService;
//	}


	@RequestMapping(value = "/member/loginForm.do", method = RequestMethod.GET)
	public ModelAndView loginForm(@RequestParam(value= "result", required=false) String result,HttpServletRequest request, HttpServletResponse response) throws Exception{
		//String viewName=getViewName(request);
		
		String viewName=(String) request.getAttribute("viewName");
		logger.info("뷰네임" + viewName);
		ModelAndView mav= new ModelAndView(viewName);
		
		mav.addObject("result",result);
		
		return mav;
	}
	
	
	


	@Override
	@RequestMapping(value = "/member/listMembers.do", method = RequestMethod.GET)
	public ModelAndView listMembers(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//String viewName=getViewName(request);
		
		String viewName=(String) request.getAttribute("viewName");
		logger.info("인터셉터에서 넘어온 뷰네임" + viewName);
		List<MemberVO> membersList = memberService.listMembers();
		
		ModelAndView mav= new ModelAndView(viewName);
		
		mav.addObject("membersList", membersList);
		
		
		System.out.println("뷰네임 : " + viewName);
		
		logger.info("이것은 sl4j로 만든 로그여" + viewName);
		return mav;
	}

	
	@RequestMapping(value = "/member/memberForm.do", method = RequestMethod.GET)
	public ModelAndView memberForm(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//String viewName=getViewName(request);
		String viewName=(String) request.getAttribute("viewName");
		System.out.println("뷰네임"+viewName);
		ModelAndView mav= new ModelAndView(viewName);
		
		
		return mav;
	}
	
	
	
	@Override
	@RequestMapping(value = "/member/logout.do", method =  RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		session.removeAttribute("member");
		session.removeAttribute("isLogOn");
		session.invalidate();
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/member/listMembers.do");
		return mav;
	}

	@RequestMapping(value = "/member/login.do", method = {RequestMethod.POST, RequestMethod.GET })
	public ModelAndView login(@ModelAttribute("memberVO") MemberVO memberVO,  RedirectAttributes rAttr,HttpServletRequest request, HttpServletResponse response) throws Exception{
		logger.info("로그인 하는 과정으로 들어옴");
		logger.info(memberVO.getId());
		//memberVO=memberService.searchMemberbyID(member.getId());
		
		memberVO=memberService.loginById(memberVO);
		
		logger.info("로그인객체"+memberVO);
		
		
		if(memberVO != null) {
			logger.info("로그인성공");
			HttpSession sess=request.getSession();
			sess.setAttribute("member", memberVO);
			sess.setAttribute("isLogon", true);
			
			ModelAndView mav= new ModelAndView();			
			
//			List<MemberVO> membersList= memberService.listMembers();
//			mav.addObject("membersList", membersList);			
//			mav.setViewName("/member/listMembers");
			mav.setViewName("redirect:/member/listMembers.do");
			
			return mav;
		}else {
			rAttr.addAttribute("result", "loginFailed");
			ModelAndView mav= new ModelAndView();
			mav.setViewName("redirect:/member/loginForm.do");
			return mav;
		}
		
		
		
	}
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	@RequestMapping(value = "/member/addMember.do", method = RequestMethod.POST)
	public ModelAndView addMember(@ModelAttribute("member") MemberVO member, HttpServletRequest request, HttpServletResponse response) throws Exception{
		System.out.println("회원 추가 코드 동작");
		
		ModelAndView mav= new ModelAndView();
		
		
//		String id=request.getParameter("id");
//		String pwd=request.getParameter("pwd");
//		String name=request.getParameter("name");
//		String email=request.getParameter("email");
		
//		System.out.println(id + ","  + pwd + "," + name + "," +email );
		
//		MemberVO memberVO = new MemberVO(id,pwd, name,email  );
		//MemberVO memberVO = new MemberVO(); 
		//bind(request, memberVO);    //Bind request parameters onto the given command bean
				
		memberService.addMember(member);  //회원 추가
		
		List<MemberVO> membersList = memberService.listMembers();
		mav.addObject("membersList", membersList);
		mav.setViewName("/member/listMembers");
		return mav;
	}
	
	
	
	

	@RequestMapping(value = "/member/delMember.do", method = RequestMethod.GET)
	public ModelAndView delMember(HttpServletRequest request, HttpServletResponse response) throws Exception{
		System.out.println("회원 삭제 코드 동작");
		String id=request.getParameter("id");
		
		memberService.delMember(id);
		
		ModelAndView mav= new ModelAndView();
		List<MemberVO> membersList = memberService.listMembers();
		mav.addObject("membersList", membersList);
		mav.setViewName("/member/listMembers");
		return mav;
	}
	
	
	
//	private String getViewName(HttpServletRequest request) throws Exception{
//		String contextPath = request.getContextPath();
//		
//		System.out.println("컨텍스트 경로 : " + contextPath);
//		String uri=(String) request.getAttribute("javax.servlet.include.request_uri");
//		System.out.println(uri);
//		if(uri ==null || uri.trim().equals("")) {
//			uri=request.getRequestURI();
//			System.out.println("요청하는 uri:" + uri);
//		}
//		
//		System.out.println("컨텍스트패스 길이:" + contextPath.length());
//		
//		int begin=0;  // 시작 위치
//		
//		if((contextPath != null) && ( ! ("".equals(contextPath)))) {
//			begin=contextPath.length();
//			System.out.println("시작위치:" + begin);
//		} 
//		
//		System.out.println();
//		
//		int end;
//		if(uri.indexOf(";") != -1) {
//			end=uri.indexOf(";");
//			System.out.println(end);
//		}else if(uri.indexOf("?") != -1) {
//			end=uri.indexOf("?");
//			System.out.println(end);
//		}else {
//			end=uri.length();
//			System.out.println("uri의 길이 :" + end);
//		}
//		
//		String fileName=uri.substring(begin, end);
//		System.out.println("중간 파일명: "+ fileName);
//		
//		
//		if(fileName.indexOf(".")  !=-1) {
//			fileName=fileName.substring(0, fileName.lastIndexOf("."));
//			System.out.println(fileName);
//		}
//		
//		if(fileName.lastIndexOf("/") != -1) {
//			fileName=fileName.substring(fileName.lastIndexOf("/", 1),fileName.length() );
//			System.out.println("최종 파일명 : "+ fileName);
//		}
//		
//		return fileName;
//	}




	@Override
	@RequestMapping(value = "/member/modMemberForm.do", method = RequestMethod.GET)
	public ModelAndView modMemberForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//String viewName=getViewName(request);
		String viewName=(String) request.getAttribute("viewName");
		logger.info("인터셉터에서 넘어온 뷰네임" + viewName);
		//System.out.println(viewName);
		ModelAndView mav= new ModelAndView(viewName);
		return mav;
	}



	
	@Override
	@RequestMapping(value = "/member/modMember.do", method = RequestMethod.POST)
	public ModelAndView updateMember(@ModelAttribute("member") MemberVO member,HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("회원 수정 코드 실행");
//		String id=request.getParameter("id");
//		String pwd=request.getParameter("pwd");
//		String name=request.getParameter("name");
//		String email=request.getParameter("email");
//		
//		System.out.println(id + ","  + pwd + "," + name + "," +email );
		
//		MemberVO memberVO = new MemberVO(id,pwd, name,email  );
 
		memberService.updateMember(member);
		ModelAndView mav= new ModelAndView();
		List<MemberVO> membersList = memberService.listMembers();
		mav.addObject("membersList", membersList);
		mav.setViewName("/member/listMembers");
		return mav;
	}



	@Override
	@RequestMapping(value = "/member/member.do", method = RequestMethod.GET)
	public ModelAndView search(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String action = request.getParameter("action");
		System.out.println("액션 값" + action);
		
		ModelAndView mav = new ModelAndView();
		if(action.equals("searchMember")) {
			List<MemberVO> membersList  =searchMember(request,response );
			mav.addObject("membersList", membersList);
			mav.setViewName("/member/listMembers");
		}else if(action.equals("listMembers")) {
			List<MemberVO> membersList =memberService.listMembers();
			mav.addObject("membersList", membersList);
			mav.setViewName("/member/listMembers");
		}else if(action.equals("selectMemberById")) {
			MemberVO member =searchMemberID(request,response);
			mav.addObject("member", member);
			mav.setViewName("/member/listMembers");
		}else if(action.equals("selectMemberByNameOrEmail")) {
			
			List<MemberVO> membersList=selectMemberByNameOrEmail(request,response);
			mav.addObject("membersList", membersList);
			mav.setViewName("/member/listMembers");
		}
		
		return mav;
	}
	
	
	
	
	private List<MemberVO> searchMember(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		System.out.println("회원 이름 검색 코드 작성");
		String memberName=request.getParameter("value");
		System.out.println(memberName);			
		
		List<MemberVO> membersList =memberService.searchMember(memberName);
		
		return membersList;
	}


	
	
	
	
	

	private MemberVO searchMemberID (HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		System.out.println("회원 아이디 검색 코드 작성");
		String memberName=request.getParameter("value");
		System.out.println(memberName);			
		ModelAndView mav= new ModelAndView();
		MemberVO memberVO =memberService.searchMemberID(memberName);
		
		return memberVO;
	}
	


	private List selectMemberByNameOrEmail(HttpServletRequest request, HttpServletResponse response) throws Exception{
		System.out.println("회원 이름 및 이메일 검색 코드 작성");
		String nameOrEmail=request.getParameter("value");
		System.out.println(nameOrEmail);
		List membersList=memberService.selectMemberByNameOrEmail(nameOrEmail);
		return membersList;
	}






	
	
}
