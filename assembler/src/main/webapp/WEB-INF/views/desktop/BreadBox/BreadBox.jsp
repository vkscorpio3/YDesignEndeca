<%@page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>

<%@include file="/WEB-INF/views/include.jsp"%>  

<c:if test="${!empty(component.searchCrumbs) || !empty(component.refinementCrumbs)}">
    <div class="Breadcrumbs">
	    <div class="breadcrumbsHeaderText" >Your Selections</div>
	    <div class="WidgetBox">
	        <c:forEach var="searchCrumb" items="${component.searchCrumbs}">
	            <div class="SearchCrumb">
	                <span class="breadcrumbDimText">Search </span>
	                <div class="breadcrumbText">
                        <a class="breadcrumbRemoveIcon"
                                href="<c:url value='${util:getUrlForAction(searchCrumb.removeAction)}' />"
                            ><img src="<c:url value='/images/remove_X.png' />" alt="Remove Breadcrumb"
                        ></a>
	                    <div class="floatLeft">
		                    <c:choose>
		                        <c:when test="${not empty searchCrumb.correctedTerms}">
		                            <span class="breadcrumbOriginalText"><c:out value="${searchCrumb.terms}"/> </span>
		                        </c:when>
		                        <c:otherwise>
		                            <span class="breadcrumbOriginalTextSearched"><c:out value="${searchCrumb.terms}" escapeXml="true" /></span>
		                        </c:otherwise>
		                    </c:choose>
		                    <span class="breadcrumbCorrectedText">
		                        <c:if test="${searchCrumb.correctedTerms != null}">
		                            <c:out value="${searchCrumb.correctedTerms}"/>
		                        </c:if>
		                    </span>
		                </div>
		                <div class="clearBoth"></div>
	                </div>
	            </div>
	        </c:forEach>
	        <c:forEach var="dimCrumb" items="${component.refinementCrumbs}">
	            <div class="DimensionCrumb">
	                <span class="breadcrumbDimText"><c:out value="${dimCrumb.displayName}"/><br></span>
                    <a class="breadcrumbRemoveIcon"
                            href="<c:url value='${util:getUrlForAction(dimCrumb.removeAction)}' />"
                        ><img src="<c:url value='/images/remove_X.png' />" alt="Remove Breadcrumb"
                    ></a>
	                <div class="floatLeft">
		                <c:forEach var="ancestor" items="${dimCrumb.ancestors}">
		                    <discover:link action="${ancestor}" displayClass="breadcrumbText" displayText="${ancestor.label}" /> :
		                </c:forEach>
		                <span class="breadcrumbText">${dimCrumb.label}</span>
	                </div>
	                <div class="clearBoth"></div>
	            </div>
	        </c:forEach>
	        <div class="clearAllLink">
                <discover:link displayClass="breadcrumbsClearAllText" action="${component.removeAllAction}" displayText="Clear All" />
	        </div>
	    </div>
    </div>
</c:if>
