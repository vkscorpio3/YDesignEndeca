<%@page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@include file="/WEB-INF/views/include.jsp"%>
<c:if test="${!empty(component.name)}">
    <h1>${component.categoryName}</h1>
</c:if>
