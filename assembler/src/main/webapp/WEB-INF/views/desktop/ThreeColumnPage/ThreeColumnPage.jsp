<%@page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%--
  ~ Copyright 2001, 2014, Oracle and/or its affiliates. All rights reserved.
  ~ Oracle and Java are registered trademarks of Oracle and/or its
  ~ affiliates. Other names may be trademarks of their respective owners.
  ~ UNIX is a registered trademark of The Open Group.
  ~
  ~ This software and related documentation are provided under a license
  ~ agreement containing restrictions on use and disclosure and are
  ~ protected by intellectual property laws. Except as expressly permitted
  ~ in your license agreement or allowed by law, you may not use, copy,
  ~ reproduce, translate, broadcast, modify, license, transmit, distribute,
  ~ exhibit, perform, publish, or display any part, in any form, or by any
  ~ means. Reverse engineering, disassembly, or decompilation of this
  ~ software, unless required by law for interoperability, is prohibited.
  ~ The information contained herein is subject to change without notice
  ~ and is not warranted to be error-free. If you find any errors, please
  ~ report them to us in writing.
  ~ U.S. GOVERNMENT END USERS: Oracle programs, including any operating
  ~ system, integrated software, any programs installed on the hardware,
  ~ and/or documentation, delivered to U.S. Government end users are
  ~ "commercial computer software" pursuant to the applicable Federal
  ~ Acquisition Regulation and agency-specific supplemental regulations.
  ~ As such, use, duplication, disclosure, modification, and adaptation
  ~ of the programs, including any operating system, integrated software,
  ~ any programs installed on the hardware, and/or documentation, shall be
  ~ subject to license terms and license restrictions applicable to the
  ~ programs. No other rights are granted to the U.S. Government.
  ~ This software or hardware is developed for general use in a variety
  ~ of information management applications. It is not developed or
  ~ intended for use in any inherently dangerous applications, including
  ~ applications that may create a risk of personal injury. If you use
  ~ this software or hardware in dangerous applications, then you shall
  ~ be responsible to take all appropriate fail-safe, backup, redundancy,
  ~ and other measures to ensure its safe use. Oracle Corporation and its
  ~ affiliates disclaim any liability for any damages caused by use of this
  ~ software or hardware in dangerous applications.
  ~ This software or hardware and documentation may provide access to or
  ~ information on content, products, and services from third parties.
  ~ Oracle Corporation and its affiliates are not responsible for and
  ~ expressly disclaim all warranties of any kind with respect to
  ~ third-party content, products, and services. Oracle Corporation and
  ~ its affiliates will not be responsible for any loss, costs, or damages
  ~ incurred due to your access to or use of third-party content, products,
  ~ or services.
  --%>
<%@include file="/WEB-INF/views/include.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <endeca:pageHead rootContentItem="${rootComponent}"/>
    <title><c:out value="${component.title}"/></title>
    <meta name="keywords" content="${component.metaKeywords}" />
    <meta name="description" content="${component.metaDescription}" />
    <link rel="canonical" href="${pageContext.request.contextPath}${util:getUrlForAction(rootComponent.canonicalLink)}"/>
    <link rel="StyleSheet" href="<c:url value="/css/discover_electronics.css"/>"/>
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
                <c:forEach var="element" items="${component.leftContent}">
                    <discover:include component="${element}"/>
                </c:forEach>
            </div>
            <div class="PageCenterColumn">
                <c:forEach var="element" items="${component.mainContent}">
                    <discover:include component="${element}"/>
                </c:forEach>
            </div>
            <div class="PageRightColumn">
                <c:forEach var="element" items="${component.rightContent}">
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
