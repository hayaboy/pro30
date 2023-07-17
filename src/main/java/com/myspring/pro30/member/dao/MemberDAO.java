package com.myspring.pro30.member.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.myspring.pro30.member.vo.MemberVO;



public interface MemberDAO {
	
	public List<MemberVO> selectAllMembers() throws DataAccessException ;

	
	
	public void addMember(MemberVO memberVO) throws DataAccessException ;
	
	
	
	public void delMember(String id) throws DataAccessException ;
	
	public MemberVO searchMemberbyID(String id) throws DataAccessException ;
		
	
	public void updateMember(MemberVO memberVO) throws DataAccessException ; 
	
	
	public List<MemberVO>  searchMemberbyName(String memberName) throws DataAccessException;
	
	public List<MemberVO> selectMemberByNameOrEmail(String nameOrEmail) throws DataAccessException;
	
	public MemberVO loginById(MemberVO memberVO) throws DataAccessException;
	
}
