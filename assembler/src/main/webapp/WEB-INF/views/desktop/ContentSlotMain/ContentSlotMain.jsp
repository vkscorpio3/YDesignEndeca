<%@page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>


<%@include file="/WEB-INF/views/include.jsp"%>

<c:forEach var="element" items="${component.contents}">
    <discover:include component="${element}"/>
</c:forEach>