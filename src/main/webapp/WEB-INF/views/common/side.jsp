<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath"  value="${pageContext.request.contextPath}"  />    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
 <style>
   .no-underline{
      text-decoration:none;
   }
 </style>
</head>
<body>
	<h1>사이드 메뉴</h1>
	<h1>
		<a href="${contextPath}/member/loginForm.do"  class="no-underline">회원관리</a><br>
	    <a href="#"  class="no-underline">게시판관리</a><br>
	    <a href="#"  class="no-underline">상품관리</a><br>
    </h1>
</body>
</html>