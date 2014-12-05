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
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="endeca" uri="/endeca-infront-assembler/utilityTags" %>
<%@tag language="java" pageEncoding="UTF-8"%>
<%@tag import="com.endeca.infront.assembler.ContentItem"%>
<%@tag import="java.io.FileNotFoundException"%>

<%@ attribute name="component" type="com.endeca.infront.assembler.ContentItem" required="true"
    description="The component to render."%>

<c:if test="${not empty component }">

    <%-- default to including the jsp /WEB-INF/views/desktop/Default/Default.jsp --%>
    <c:set var="resource" value="${component['@type']}"/>
    <c:if test="${empty(resource)}">
        <c:set var="resource" value="Default"/>
    </c:if>

    <c:set var="errorMessage" scope="request" value="${component['@error']}"/>
    <c:choose>
        <c:when test="${not empty errorMessage}">
            <c:import url="/componentError.jsp" charEncoding="UTF-8"/>
        </c:when>
        <c:otherwise>
            <c:set var="resourcePath" value="/WEB-INF/views/desktop/${resource}/${resource}.jsp"/>

            <c:if test="${not empty requestScope['Endeca.InFront.ComponentResourcePath']}">
                <c:set var="resourcePath" value="${requestScope['Endeca.InFront.ComponentResourcePath']}${resource}/${resource}.jsp"/>
            </c:if>

            <%-- save the parent's component currently in request scope into page scope --%>
            <c:set var="parentComponent" scope="page" value="${requestScope['component']}"/>
            <%-- set the content item the child will use as this one (the one passed in the tag) --%>
            <c:set var="component" scope="request" value="${component}"/>
            <c:catch var="importException">
                <endeca:previewAnchor contentItem="${component}">
                    <c:import url="${resourcePath}" charEncoding="UTF-8"/>
                </endeca:previewAnchor>
            </c:catch>

            <c:if test="${!empty(importException)}">
                <%
                Object obj = jspContext.getAttribute("importException");
                Throwable t = ((JspException) obj).getRootCause();

                if(t instanceof FileNotFoundException){
                	String resource = (String)jspContext.getAttribute("resource");
                	request.setAttribute("errorMessage", "The " + resource + " render is missing.");
                } else {
                    request.setAttribute("errorMessage", "Import resulted in an exception");
                }

                t.printStackTrace();
                %>
                <c:import url="/componentError.jsp" charEncoding="UTF-8"/>
            </c:if>

            <%-- reset the component in the request to be the parent's component --%>
            <c:set var="component" scope="request" value="${parentComponent}"/>
        </c:otherwise>
    </c:choose>
</c:if>
