<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
// 방법 1: 회원인증정보 속성 삭제
session.removeAttribute("UserId");
session.removeAttribute("UserName");

// 방법 2: 모든 속성 한꺼번에 삭제 (2번 방법을 사용하는 것이 좋다.)
session.invalidate();

// 속성 삭제 후 페이지 이동
response.sendRedirect("LoginForm.jsp");
%>