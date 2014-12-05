
<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.io.StringWriter"%>
<%@ page isErrorPage="true" %>
<%

String format = request.getParameter("format");
if("json".equals(format)) {
    response.setHeader("content-type", "application/json");
    response.getWriter().write("{\"status\":\"404\"}");
} else if ("xml".equals(format)) {
    response.setHeader("content-type", "application/xml");
    response.getWriter().write("<status>404</status>");
} else {

%>
<%@ include file="WEB-INF/views/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Page Not Found</title>
<link rel="StyleSheet" href="<c:url value="/css/discover_electronics.css"/>"/>
</head>
<body>
<div class="ErrorPageContent">
    <img src="<c:url value="/images/ydesigngroup-logo.gif" />" class="ErrorPageLogo" />
    <div class="ErrorPageMessage">
        Page Not Found
    </div>

    <%@ include file="WEB-INF/views/copyright.jsp" %>
</div>

</body>
</html>
<%
}
%>