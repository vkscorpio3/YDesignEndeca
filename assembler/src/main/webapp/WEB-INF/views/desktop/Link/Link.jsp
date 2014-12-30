<%@page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@include file="/WEB-INF/views/include.jsp"%>
<c:choose>
	<c:when test="${not empty component.link}">
	    <c:url value="${util:getUrlForAction(component.link)}" var="hrefUrl"></c:url>	
	</c:when>
	<c:otherwise>
		<c:set var="hrefUrl" value="#"></c:set> 
	</c:otherwise>
</c:choose>
<c:if test="${not empty component.link}">
    <c:url value="${util:getUrlForAction(component.link)}" var="hrefUrl"></c:url>
</c:if>
<a href="${hrefUrl}">${component.text }</a>
