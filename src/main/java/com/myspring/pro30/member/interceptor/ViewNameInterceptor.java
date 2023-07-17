package com.myspring.pro30.member.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;



public class ViewNameInterceptor  extends HandlerInterceptorAdapter{

	private static final Logger logger = LoggerFactory.getLogger(ViewNameInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		try {
			String viewName=getViewName(request);
			logger.info("인터셉터에서 가져온 뷰네임 : " + viewName);
			request.setAttribute("viewName", viewName);
		}catch(Exception e) {
			logger.info("여긴 인터셉터 요청 이름 가져오면서(getViewName()) 에러남");
		}
		
		
		
		
		
		return true;
	}
	
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}
	
	
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}
	
	
	private String getViewName(HttpServletRequest request) throws Exception{
		String contextPath = request.getContextPath();
		
		System.out.println("컨텍스트 경로 : " + contextPath);
		String uri=(String) request.getAttribute("javax.servlet.include.request_uri");
		System.out.println(uri);
		if(uri ==null || uri.trim().equals("")) {
			uri=request.getRequestURI();
			System.out.println("요청하는 uri:" + uri);
		}
		
		System.out.println("컨텍스트패스 길이:" + contextPath.length());
		
		int begin=0;  // 시작 위치
		
		if((contextPath != null) && ( ! ("".equals(contextPath)))) {
			begin=contextPath.length();
			System.out.println("시작위치:" + begin);
		} 
		
		System.out.println();
		
		int end;
		if(uri.indexOf(";") != -1) {
			end=uri.indexOf(";");
			System.out.println(end);
		}else if(uri.indexOf("?") != -1) {
			end=uri.indexOf("?");
			System.out.println(end);
		}else {
			end=uri.length();
			System.out.println("uri의 길이 :" + end);
		}
		
		String fileName=uri.substring(begin, end);
		System.out.println("중간 파일명: "+ fileName);
		
		
		if(fileName.indexOf(".")  !=-1) {
			fileName=fileName.substring(0, fileName.lastIndexOf("."));
			System.out.println(fileName);
		}
		
		if(fileName.lastIndexOf("/") != -1) {
			fileName=fileName.substring(fileName.lastIndexOf("/", 1),fileName.length() );
			System.out.println("최종 파일명 : "+ fileName);
		}
		
		return fileName;
	}
}
