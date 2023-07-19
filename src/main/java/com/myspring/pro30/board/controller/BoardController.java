package com.myspring.pro30.board.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

public interface BoardController {

	
	public ModelAndView listArticles(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	public ModelAndView viewArticle(@RequestParam("articleNO") int articleNO,
            HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	
	
	public ResponseEntity  removeArticle(@RequestParam("articleNO") int articleNO,
            HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	
	
	//새글 추가
	public ResponseEntity addNewArticle(MultipartHttpServletRequest multipartRequest,HttpServletResponse response) throws Exception;
	
	
	
	
	//글 수정
	  public ResponseEntity modArticle(MultipartHttpServletRequest multipartRequest,  
			    HttpServletResponse response) throws Exception;
	  
	  
	  
	  
	  //답글 추가
	  public ResponseEntity addReplyArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
				throws Exception;
}
