package com.myspring.pro30.board.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myspring.pro30.board.dao.BoardDAO;
import com.myspring.pro30.board.vo.ArticleVO;


@Service("boardService")
public class BoardServiceImpl implements BoardService {
	
	
	@Autowired
	BoardDAO boardDAO;
	

	
	public List<ArticleVO> listArticles() throws Exception{
		List<ArticleVO>  articlesList= boardDAO.selectAllArticles();
		return articlesList;
	}



	@Override
	public ArticleVO viewArticle(int articleNO) throws Exception {
		ArticleVO articleVO = boardDAO.selectArticle(articleNO);
		return articleVO;		
	}



	@Override
	public void removeArticle(int articleNO) throws Exception {
		boardDAO.deleteArticle(articleNO);
		
	}



	@Override
	public int addNewArticle(Map articleMap) throws Exception {
		
		return boardDAO.insertNewArticle(articleMap);
	}



	@Override
	public void modArticle(Map articleMap) throws Exception {
		
		boardDAO.updateArticle(articleMap);
	};
	
	
	
	
	
	
	
	
	// 총 글의 갯수를 저장할 객체를 해시맵에 저장(키값을 문자열, 총 글의 갯수를 Integer)
	
//	public Map listArticles(Map<String, Integer> pagingMap){
//		Map articlesMap=new HashMap();
//		
//		
//		List<ArticleVO> articlesList= boardDAO.selectAllArticles2(pagingMap);
//		
//		int totArticles = boardDAO.selectTotArticles();
//		System.out.println(totArticles);
//		
//		
//		
//		articlesMap.put("articlesList", articlesList);
//		articlesMap.put("totArticles", totArticles);
//		
//		//articlesMap.put("totArticles", 101);
//		
//		
//		return articlesMap;
//	}
//	
//	
//	
//	public int addArticle(ArticleVO article){
//		int articleNO=boardDAO.insertNewArticle(article);
//		return articleNO;
//	}
//	
//	
//	
//	public ArticleVO viewArticle(int articleNo){
//		ArticleVO article = null;
//		article=boardDAO.selectArticle(articleNo);
//		return article;
//	}
//	
	
	
	
//	public void modArticle(ArticleVO article){
//		boardDAO.updateArticle(article);
//	}
//	
//	public List<Integer> removeArticle(int articleNo){
//		List<Integer> articleNOList= boardDAO.selectRemovedArticles(articleNo); //articleNOList�뒗 湲�踰덊샇媛� 2踰덉씪 寃쎌슦 2(遺�紐�) �옄�떇 3,5,6
//		boardDAO.deleteArticle(articleNo);
//		return articleNOList;
//	}
//	
//	
//
//	
//	public int addReply(ArticleVO articleVO){
//		
//		int articleNo=boardDAO.insertNewArticle(articleVO);
//		
//		return articleNo;
//	}
	
	
}
