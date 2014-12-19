<%@page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>

<%@include file="/WEB-INF/views/include.jsp"%>

<c:set var="emptyList" value="true"></c:set>

<c:forEach var="element" items="${component.navigation}">
    <c:if test="${not empty element.refinements}">
        <c:set var="emptyList" value="false"></c:set>
    </c:if>
</c:forEach>
<c:if test="${(not empty component.navigation) && !emptyList}">
    <div class="WidgetBoxTitle">Narrow Your Results</div>
    <div class="WidgetBox">
        <c:forEach var="element" items="${component.navigation}">
            <discover:include component="${element}"/>
        </c:forEach>
    </div>
</c:if>
