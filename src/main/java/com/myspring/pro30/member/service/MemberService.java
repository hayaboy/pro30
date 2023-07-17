package com.myspring.pro30.member.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.myspring.pro30.member.vo.MemberVO;



public interface MemberService {
	
	
	public List<MemberVO> listMembers() throws DataAccessException;
	
	public void addMember(MemberVO memberVO)throws DataAccessException;
	
	public void delMember(String id) throws DataAccessException;
	
	
	public MemberVO searchMemberbyID(String id) throws DataAccessException;
	
	
	public void updateMember(MemberVO member) throws DataAccessException;
	
	
	public List<MemberVO> searchMember(String memberName) throws DataAccessException;
	
	
	
	public MemberVO searchMemberID(String memberName) throws DataAccessException;
	
	
	
	
	public List selectMemberByNameOrEmail(String nameOrEmail)  throws DataAccessException;
	
	public MemberVO loginById(MemberVO memberVO) throws DataAccessException;
}
