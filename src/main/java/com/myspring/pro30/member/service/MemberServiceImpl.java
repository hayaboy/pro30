package com.myspring.pro30.member.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.myspring.pro30.member.dao.MemberDAO;
import com.myspring.pro30.member.vo.MemberVO;




@Service("memberService")

public class MemberServiceImpl implements MemberService{

	@Autowired
	private MemberDAO memberDAO;
	
	
	
	
//	
//	public void setMemberDAO(MemberDAO memberDAO) {
//		this.memberDAO = memberDAO;
//	}





	@Override
	public List<MemberVO> listMembers() throws DataAccessException {
		List<MemberVO> membersList = memberDAO.selectAllMembers();
		return membersList;
	}





	
	
	@Override
	public void addMember(MemberVO memberVO) throws DataAccessException {
	
		memberDAO.addMember(memberVO);
	}
	
	
	
	
	public void delMember(String id) throws DataAccessException{
		memberDAO.delMember(id);
	}





	@Override
	public MemberVO searchMemberbyID(String id) throws DataAccessException {
		MemberVO memberVO = memberDAO.searchMemberbyID(id);
		return memberVO;
	}





	@Override
	public void updateMember(MemberVO member) throws DataAccessException {
		memberDAO.updateMember(member);
		
	}





	@Override
	public List<MemberVO> searchMember(String memberName) throws DataAccessException {
		List<MemberVO> membersList = memberDAO.searchMemberbyName(memberName);
		return membersList;
	}





	@Override
	public MemberVO searchMemberID(String memberName) throws DataAccessException {
		MemberVO memberVO=memberDAO.searchMemberbyID(memberName);
		return memberVO;
	}





	@Override
	public List selectMemberByNameOrEmail(String nameOrEmail) throws DataAccessException {
		List memberList=memberDAO.selectMemberByNameOrEmail(nameOrEmail);
		return memberList;
	}
	
	
	
	public MemberVO loginById(MemberVO memberVO) throws DataAccessException{
		
		MemberVO member=memberDAO.loginById(memberVO);
		
		return member;
	}
	

}
