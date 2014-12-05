<%@tag language="java" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="util" uri="/WEB-INF/tlds/functions.tld"  %>

<%@tag import="java.util.Properties"%>
<%@tag import="com.endeca.infront.cartridge.model.Action"%>

<%@ attribute name="action" type="com.endeca.infront.cartridge.model.Action" required="true"
    description="The action associated with the component."%>

<%@ attribute name="displayText" type="java.lang.String" required="true"
    description="Text to be displayed."%>

<%@ attribute name="displayClass" type="java.lang.String" required="false"
    description="Css for the text to be displayed."%>

<a class="${displayClass}" href="<c:url value='${util:getUrlForAction(action)}' />"
><c:out value="${displayText}" escapeXml="false"/></a>
