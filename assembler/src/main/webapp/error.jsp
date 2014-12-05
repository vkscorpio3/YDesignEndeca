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

<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.io.StringWriter"%>
<%@ page isErrorPage="true" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
Object obj = request.getAttribute("exception");

StringWriter writer = new StringWriter();

if (obj != null && obj instanceof Throwable) {

    Throwable t = (Throwable) obj;
    t.printStackTrace(new PrintWriter(writer));
    request.setAttribute("errorMessage", writer.getBuffer().toString());
    
} else if (exception != null) {

    exception.printStackTrace(new PrintWriter(writer));
    request.setAttribute("errorMessage", writer.getBuffer().toString());
    
} else if (request.getAttribute("errorMessage") == null) {

    request.setAttribute("errorMessage", "Unknown error");
}
%>
<%@ include file="WEB-INF/views/include.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<c:if test="${not empty rootComponent}">
    <endeca:pageHead rootContentItem="${rootComponent}"/>
</c:if>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Error</title>
<link rel="StyleSheet" href="<c:url value="/css/discover_electronics.css"/>"/>
</head>
<body>
<div class="ErrorPageContent">
    <img src="<c:url value="/images/discover-logo.png" />" class="ErrorPageLogo"/>

    <div class="ErrorPageMessage">
        <div>
            <table>
                <tr>
                    <td><img src="<c:url value="/images/warning.png" />" class="WarningIcon"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${not empty errorMessageFormatted}">
                                <c:out escapeXml="false" value="${errorMessageFormatted}"/>
                            </c:when>
                            <c:otherwise>
                                <c:out escapeXml="true" value="${errorMessage}"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </table>
        </div>
    </div>

    <%@ include file="WEB-INF/views/copyright.jsp" %>
</div>

</body>
</html>
