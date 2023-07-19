package com.myspring.pro30.member.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.myspring.pro30.member.vo.MemberVO;





@Repository("memberDAO")
public class MemberDAOImpl implements MemberDAO {

	@Autowired
	private SqlSession sqlSession;	
	
		
//
//	public void setSqlSession(SqlSession sqlSession) {
//		this.sqlSession = sqlSession;
//	}

	@Override
	public List<MemberVO> selectAllMembers() throws DataAccessException {
		List<MemberVO> memberList= sqlSession.selectList("mapper.member.selectAllMemberList");
		
		return memberList;
	}

	@Override
	public void addMember(MemberVO member) throws DataAccessException {
	
		sqlSession.insert("mapper.member.addMember",member );
//		sqlSession.commit(); //   JDBC 템플릿에서만 sqlSession은 commit 꼭 해줘야 함
	}

	@Override
	public void delMember(String id) throws DataAccessException {
		
		sqlSession.delete("mapper.member.delMember", id);
	}

	@Override
	public MemberVO searchMemberbyID(String id) throws DataAccessException {
		MemberVO memberVO=(MemberVO) sqlSession.selectOne("mapper.member.searchMemberbyID", id);
		return memberVO;
	}

	@Override
	public void updateMember(MemberVO member) throws DataAccessException {
		sqlSession.update("mapper.member.updateMember", member);
		
	}

	@Override
	public List searchMemberbyName(String memberName) throws DataAccessException {
		List membersList=sqlSession.selectList("mapper.member.searchMemberbyName", memberName );
		return membersList;
	}

	@Override
	public List selectMemberByNameOrEmail(String nameOrEmail) throws DataAccessException {
		List membersList = sqlSession.selectList("mapper.member.selectMemberByNameOrEmail", nameOrEmail );
		return membersList;
	}


	public MemberVO loginById(MemberVO memberVO){
		MemberVO member=(MemberVO) sqlSession.selectOne("mapper.member.loginById", memberVO);
		return member;
	}

	@Override
	public void updateArticle(Map articleMap) throws DataAccessException {
	
		sqlSession.update("mapper.board.updateArticle", articleMap);
	}

	
}
