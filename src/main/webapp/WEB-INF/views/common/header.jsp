<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>

<body>
	<table border=0 width="100%">

		<tr>
			<td><a href="${contextPath}/main.do"> <img
					src="${contextPath}/resources/image/rabbit.jpg" width="120"
					height="100" />
			</a></td>
			<td>
				<h3>
					<font size=20>회원관리와 게시판 관리 </font>
				</h3>
			</td>
			<td>
			
		 	<c:out value="${isLogon}"></c:out>
		 	<c:if test="${isLogon!=true}"><td><a href="${contextPath}/member/loginForm1.do"><h3>로그인</h3></a></td></c:if>
		 	<c:if test="${isLogon==true}"><td><a href="${contextPath}/member/logout.do"><h3>로그아웃</h3></a></td></c:if>
			<c:out value="${member}"></c:out>
			<c:out value="${member.name}"></c:out>
			 
			
			<%-- <a href="${contextPath}/member/loginForm.do"><h3>로그인77</h3></a> --%>
		<%-- 	<c:choose>

				<c:when test = "${isLogOn == true}">
				   <h3>환영합니다. ${member.name }님!</h3>
					<td><a href="${contextPath}/member/logout.do"><h3>로그아웃</h3></a></td>
				</c:when>
				<c:otherwise>
					<td><a href="${contextPath}/member/loginForm.do"><h3>로그인</h3></a></td>
				</c:otherwise>

			</c:choose> --%>
			</td>
			<td>
			<%-- <c:choose>
			
					<c:when test="${isLogOn eq true  && member!= null}">
						<h3>환영합니다. ${member.name }님!</h3>
						<a href="${contextPath}/member/logout.do"><h3>로그아웃</h3></a>
					</c:when>
					<c:otherwise>
						<a href="${contextPath}/member/loginForm.do"><h3>로그인77</h3></a>
					</c:otherwise>
			</c:choose> --%>
			
				
			</td>







		</tr>


	</table>
</body>
</html>