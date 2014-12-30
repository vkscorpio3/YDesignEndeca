<%@page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@include file="/WEB-INF/views/include.jsp"%>
<h3>${component.title }</h3>
<c:forEach var="element" items="${component.links}">
	<discover:include component="${element}" />
</c:forEach>
