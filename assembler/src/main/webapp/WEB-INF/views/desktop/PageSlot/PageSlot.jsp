<%@page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>


<%@include file="/WEB-INF/views/include.jsp"%>

<c:choose>
    <c:when test="${not empty component.contents}">
        <%-- It doesn't make sense to include more than one page --%>
        <discover:include component="${component.contents[0]}"/>
    </c:when>
    <c:otherwise>
        <c:choose>
            <c:when test="${not empty component.contentPaths}">
                <c:set var="errorMessageFormatted" scope="request"
                   value="<b>No content was triggered for this page.</b><br>Please add or activate content in the following folder(s) in Experience Manager:<br>${component.contentPaths}." />
            </c:when>
            <c:otherwise>
                <c:set var="errorMessageFormatted" scope="request"
                   value="<b>No content was triggered for this page.</b><br>Please add or activate content in the following folder in Experience Manager:<br>${component.contentCollection}." />
            </c:otherwise>
        </c:choose>
        <c:import url="/error.jsp"/>
    </c:otherwise>
</c:choose>