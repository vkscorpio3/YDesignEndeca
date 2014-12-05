<%@page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>

<%@include file="/WEB-INF/views/include.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <endeca:pageHead rootContentItem="${rootComponent}"/>
    <title><c:out value="${component.title}"/></title>
    <meta name="keywords" content="${component.metaKeywords}"/>
    <meta name="description" content="${component.metaDescription}"/>
    <link rel="StyleSheet" href="<c:url value="/css/discover_electronics.css"/>"/>
    <link rel="StyleSheet" href="<c:url value="/css/discover_static.css"/>"/>
    <link rel="canonical" href="${pageContext.request.contextPath}${util:getUrlForAction(rootComponent.canonicalLink)}"/>
    <script type="text/javascript" src="<c:url value="/js/jquery-1.4.4.min.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/jquery.cookie.js"/>"></script>
    <script type="text/javascript">
        $j = jQuery.noConflict();
    </script>
    <script type="text/javascript" src="<c:url value='/js/global.js'/>"></script>
</head>
<body>
    <endeca:pageBody rootContentItem="${rootComponent}" contentItem="${component}">
        <div class="PageContent">
            <%--include user panel --%>
            <%@ include file="/WEB-INF/views/userPanel.jsp" %>
            <%--include user page logo --%>
            <%@ include file="/WEB-INF/views/pageLogo.jsp" %>

            <c:if test ="${not empty rootComponent['endeca:assemblerRequestInformation']}">
                <discover:include component="${rootComponent['endeca:assemblerRequestInformation']}"/>
            </c:if>

            <div class="PageHeader">
                <c:forEach var="element" items="${component.headerContent}">
                    <discover:include component="${element}"/>
                </c:forEach>
            </div>

            <div class="PageLeftColumn">
                <c:forEach var="element" items="${component.secondaryContent}">
                    <discover:include component="${element}"/>
                </c:forEach>
            </div>
            <div class="PageCenterRightColumn">
                <c:forEach var="element" items="${component.mainContent}">
                    <discover:include component="${element}"/>
                </c:forEach>
            </div>
            <div class="PageFooter">
                <%--include copyright --%>
                <%@include file="/WEB-INF/views/copyright.jsp" %>
            </div>
        </div>
    </endeca:pageBody>
</body>
</html>
