<%@page import="utils.JSFunction"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
if(session.getAttribute("UserId") == null){
	JSFunction.alertLocation("로그인 후 이용해주십시오.", "../06Session/LoginForm.jsp", out);
	return; // 특정 조건에서 실행을 멈추고 싶을 때는 return문 써줘야 함
}
%>