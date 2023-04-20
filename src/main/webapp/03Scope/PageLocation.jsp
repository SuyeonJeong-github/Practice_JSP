<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>page 영역</title>
</head>
<body>
	<h2>이동 후 page 영역의 속성값 읽기</h2>
	<%
	Object pInteger = pageContext.getAttribute("pInteger");
	Object pString = pageContext.getAttribute("pString");
	Object pPerson = pageContext.getAttribute("pPerson");
	%>
	<ul>
		<li>Integer 객체 : <%= (pInteger == null) ? "값 없음" : pInteger %></li>
		<li>String 객체 : <%= (pString == null) ? "값 없음" : pString %></li>
		<li>Person 객체 : <%= (pPerson == null) ? "값 없음" : pPerson %></li>
	</ul>
	
</body>
</html>