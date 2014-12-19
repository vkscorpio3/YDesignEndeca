<%@page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@include file="/WEB-INF/views/include.jsp"%>
<c:if test="${!empty(component.name)}">
    Logo for ${component.name} goes here
</c:if>
