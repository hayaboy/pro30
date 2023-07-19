package com.myspring.pro30.board.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.myspring.pro30.board.vo.ArticleVO;

@Repository("boardDAO")
public class BoardDAOImpl implements BoardDAO{
	
	@Autowired
	private SqlSession sqlSession;
	

	@Override
	public List<ArticleVO> selectAllArticles() throws DataAccessException {
		List<ArticleVO> articlesList = sqlSession.selectList("mapper.board.selectAllArticles");
		return articlesList;
	}

	
	public int  selectTotArticles(){				
		int totArticles = sqlSession.selectOne("mapper.board.selectTotArticles");
		return totArticles;
	}




	@Override
	public ArticleVO selectArticle(int articleNO) throws DataAccessException {
		
		return sqlSession.selectOne("mapper.board.selectArticle", articleNO);
	}
	
		
	
	
	public List<ArticleVO> selectAllArticles2(Map pagingMap){
		
		
		int section = (Integer)pagingMap.get("section");
		int pageNum=(Integer)pagingMap.get("pageNum");
		
		
		List<ArticleVO> articlesList = sqlSession.selectList("mapper.board.selectAllArticles2");	
			
		
		return articlesList;
	}


	@Override
	public void deleteArticle(int articleNO) throws DataAccessException {
		
		sqlSession.delete("mapper.board.deleteArticle", articleNO);
	}


	@Override
	public int insertNewArticle(Map articleMap) throws DataAccessException {
		int articleNO = selectNewArticleNO();
		articleMap.put("articleNO", articleNO);
		sqlSession.insert("mapper.board.insertNewArticle",articleMap);
		return articleNO;
	}
	
	private int selectNewArticleNO() throws DataAccessException {
		return sqlSession.selectOne("mapper.board.selectNewArticleNO");
	}


	@Override
	public void updateArticle(Map articleMap) throws DataAccessException {
		sqlSession.update("mapper.board.updateArticle", articleMap);
		
	}
	
	
	
	
	
	
	
//	public int getNewArticleNO() {
//		try {
//			conn = dataFactory.getConnection();
//			String query = "SELECT  max(articleNO) from t_board ";
//			System.out.println(query);
//			pstmt = conn.prepareStatement(query);
//			ResultSet rs = pstmt.executeQuery(query);
//			if(rs.next()) {
//				return rs.getInt(1)+1;
//			}
//			rs.close();
//			pstmt.close();
//			conn.close();
//			
//		}catch(Exception e) {
//			System.out.println("湲� 異붽��떆 �깉濡쒖슫 �떎�쓬 湲�踰덊샇 媛��졇�삤�떎 �뿉�윭�궓 ");
//		}
//		return 0;
//	};
	
//	public int insertNewArticle(ArticleVO article){
//		int articleNO=getNewArticleNO();
//		try {
//			conn = dataFactory.getConnection();
//			
//			int parentNO = article.getParentNO();
//			System.out.println("遺�紐④�踰덊샇:" + parentNO);
//			String title = article.getTitle();
//			String content = article.getContent();
//			String id = article.getId();
//			String imageFileName = article.getImageFileName();
//			
//			
//			String query = "INSERT INTO t_board (articleNO, parentNO, title, content, imageFileName, id)"
//					+ " VALUES (?, ? ,?, ?, ?, ?)";
//			
//			System.out.println(query);
//			
//			pstmt = conn.prepareStatement(query);
//			
//			pstmt.setInt(1, articleNO);
//			pstmt.setInt(2, parentNO);
//			pstmt.setString(3, title);
//			pstmt.setString(4, content);
//			pstmt.setString(5, imageFileName);
//			pstmt.setString(6, id);
//			pstmt.executeUpdate();
//			pstmt.close();
//			conn.close();
//			
//		}catch(Exception e) {
//			System.out.println("�깉濡쒖슫 湲� 異붽��떆 �뿉�윭");
//		}
//		return articleNO;
//		
//	}
	
	

		
//	public void updateArticle(ArticleVO article){
//		int articleNO = article.getArticleNO();
//		String title = article.getTitle();
//		String content = article.getContent();
//		String imageFileName = article.getImageFileName();
//		
//		try {
//			conn = dataFactory.getConnection();
//			
//			String query = "update t_board  set title=?,content=?";
//			
//			if (imageFileName != null && imageFileName.length() != 0) {
//				query += ",imageFileName=?";
//			}
//			
//			query += " where articleNO=?";
//			
//			
//			System.out.println(query);
//			
//			pstmt = conn.prepareStatement(query);
//			pstmt.setString(1, title);
//			pstmt.setString(2, content);
//			
//			if (imageFileName != null && imageFileName.length() != 0) {
//				pstmt.setString(3, imageFileName);
//				pstmt.setInt(4, articleNO);
//			} else {
//				pstmt.setInt(3, articleNO);
//			}
//			
//			
//			
//			pstmt.executeUpdate();
//			pstmt.close();
//			conn.close();
//			
//			
//			
//			
//		} catch (SQLException e) {
//			System.out.println("湲� �닔�젙�떆 �뿉�윭");
//			//e.printStackTrace();
//		}
//		
//		
//		
//		
//	}
	
	
	
//	public void deleteArticle(int articleNo){
//		try {
//			conn = dataFactory.getConnection();
//			
//			String query =	
//			"delete from t_board"
//			+ " where articleno in(select articleno "
//			+ " from t_board"
//			+ " start with articleno=?"
//			+ " connect by prior articleno=parentno )" ;
//			
//			System.out.println(query);
//			pstmt = conn.prepareStatement(query);
//			pstmt.setInt(1, articleNo);
//			pstmt.executeUpdate();
//			pstmt.close();
//			conn.close();
//		} catch (SQLException e) {
//			System.out.println("�궘�젣�떆 �뿉�윭");
//			//e.printStackTrace();
//		}
//	}
	
	
	
//	public List<Integer>  selectRemovedArticles(int articleNo){
//		List<Integer> articleNOList = new ArrayList<Integer>();
//		try {
//			conn = dataFactory.getConnection();
//			String query = "SELECT articleNO FROM  t_board  ";
//			query += " START WITH articleNO = ?";
//			query += " CONNECT BY PRIOR  articleNO = parentNO";
//			System.out.println(query);
//			pstmt = conn.prepareStatement(query);
//			pstmt.setInt(1, articleNo);
//			ResultSet rs = pstmt.executeQuery();
//			while (rs.next()) {
//				articleNo = rs.getInt("articleNO");
//				articleNOList.add(articleNo);
//			}
//			pstmt.close();
//			conn.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return articleNOList;
//	}
	
	
	
	
	
	
	
}
