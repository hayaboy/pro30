package com.myspring.pro30.board.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.myspring.pro30.board.vo.ArticleVO;

public interface BoardDAO {
	public List<ArticleVO> selectAllArticles() throws DataAccessException;
	
	public int  selectTotArticles() throws DataAccessException;
	
	public List<ArticleVO> selectAllArticles2(Map pagingMap) throws DataAccessException;
	
	
	public ArticleVO selectArticle(int articleNO) throws DataAccessException;
	
	public int insertNewArticle(Map articleMap) throws DataAccessException;
	
	public void deleteArticle(int articleNO) throws DataAccessException;
	
	
	
	public void updateArticle(Map articleMap) throws DataAccessException;
	
	
}
