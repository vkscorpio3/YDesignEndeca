
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
