<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.net.* "%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>




	<!-- 회원 이름만 조회할 경우  -->
	<%-- <div align="center">
<form  method="get" action="${contextPath}/member.do">
   <input  type="hidden" name="action" value="searchMember" />
   이름 : <input  type="text" name="memberName" /> 
   <input type="submit" value="검색"  />  
</form>
</div>  --%>




	<!-- 전체 조회, 회원 아이디, 회원 이름, 회원 비밀번호, 회원 이메일 중 선택해서 조회할 경우  -->


	<div align="center">
		<form method="get"
			action="${contextPath}/member/member.do?action=${value}">
			<select name="action">
				<option value="listMembers">전체</option>
				<option value="selectMemberById">회원 아이디</option>
				<option value="searchMember">회원 이름</option>
				<!-- <option  value="selectMemberByNameOrEmail">회원 이름 + 회원 이메일</option> -->
			</select> <input type="text" name="value" /> <input type="submit" value="검색" />
		</form>
	</div>

	<table border="1" align="center" width="80%">
		<tr align="center" bgcolor="lightgreen">
			<td><b>아이디</b></td>
			<td><b>비밀번호</b></td>
			<td><b>이름</b></td>
			<td><b>이메일</b></td>
			<td><b>가입일</b></td>
			<td><b>회원정보수정</b></td>
			<td><b>삭제</b></td>
		</tr>
		<c:if test="${not empty membersList}">
			<c:forEach var="member" items="${membersList}">
				<tr align="center">
					<td>${member.id}</td>
					<td>${member.pwd}</td>
					<td>${member.name}</td>
					<td>${member.email}</td>
					<td>${member.joinDate}</td>



					<td><a
						href="${contextPath}/member/modMemberForm.do?id=${member.id}&pwd=${member.pwd}&name=${member.name}&email=${member.email}">수정창나오기</a></td>
					<td><a
						href="${contextPath}/member/delMember.do?id=${member.id}"> 삭제</a></td>

				</tr>
			</c:forEach>
		</c:if>

		<%-- <c:if test="${not empty member}">
			<tr align="center">
				<td>${member.id}</td>
				<td>${member.pwd}</td>
				<td>${member.name}</td>
				<td>${member.email}</td>
				<td>${member.joinDate}</td>
				<td><a
					href="${contextPath}/member.do?action=delMember&id=${member.id}">
						삭제</a></td>
			</tr>
		</c:if> --%>




	</table>

	
</body>
</html>