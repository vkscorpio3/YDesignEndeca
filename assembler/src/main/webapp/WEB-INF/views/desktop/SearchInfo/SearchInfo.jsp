<%@page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@include file="/WEB-INF/views/include.jsp"%>
<c:if test="${!empty(component.searchCrumbs)}">
	<c:set var="searchCrumb" value="${component.searchCrumbs[0]}" />	
	<c:choose>
		<c:when test="${not empty searchCrumb.correctedTerms}">
			<span class="breadcrumbOriginalText">corrected original(:<c:out	value="${searchCrumb.terms}" />)</span>
		</c:when>
		<c:otherwise>
			<span class="breadcrumbOriginalTextSearched"><c:out	value="${searchCrumb.terms}" escapeXml="true" /></span>
		</c:otherwise>
	</c:choose>
	<span class="breadcrumbCorrectedText">
	<c:if test="${searchCrumb.correctedTerms != null}">
	 	<c:out value="${searchCrumb.correctedTerms}"/>
	</c:if>
	</span>
</c:if>
<c:if test="${component.totalNumRecs > 1}">
	(${component.firstRecNum} - ${component.lastRecNum} of ${component.totalNumRecs} results)
</c:if>
