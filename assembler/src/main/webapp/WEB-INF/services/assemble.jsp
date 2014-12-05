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
<%@page import="com.endeca.infront.assembler.Assembler"%>
<%@page import="com.endeca.infront.assembler.AssemblerFactory"%>
<%@page import="com.endeca.infront.assembler.ContentItem"%>
<%@page import="com.endeca.infront.serialization.JsonSerializer"%>
<%@page import="com.endeca.infront.serialization.XmlSerializer"%>
<%@page import="com.endeca.infront.navigation.UserState"%>
<%@page import="com.endeca.mobile.services.detection.DeviceManager"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="java.util.Properties"%>
<%@page import="java.util.LinkedHashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@ page import="com.endeca.infront.site.model.SiteState" %>
<%@ page import="com.endeca.infront.site.ContentPathTranslator" %>
<%@ page import="com.endeca.infront.cartridge.RedirectAwareContentInclude" %>
<%@ page import="com.endeca.infront.web.tags.PreviewAnchor" %>

<%@page language="java" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="discover" tagdir="/WEB-INF/tags/discover" %>
<%@ taglib prefix="util" uri="/WEB-INF/tlds/functions.tld"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%

    //WebApplicationContext initialization
    WebApplicationContext webappCtx = WebApplicationContextUtils.getRequiredWebApplicationContext(application);

    //--------------------------------------------------------------------
    // Device detection and redirection section
    //--------------------------------------------------------------------

    //This is the map between channel and page prefix. Discover electronics is structured
    //in such a way that all mobile pages are under /mobile. In the same way, tablet specific pages would go
    //under /tablet, and a new mapping between the the tablet channel and the page prefix would need to be added.
    //The desktop channel is the default channel and does not include any prefix. The implementation of the
    //DeviceManager getContentPath method requires the default entry to be the last element in the map.
    //Note: LinkedHashMap preserves the order of entries
    Map<String,String> channelToPagePrefixMap = new LinkedHashMap<String,String>();
    channelToPagePrefixMap.put("Endeca.Infront.Channel.Mobile","/mobile");
    channelToPagePrefixMap.put("Endeca.Infront.Channel.Desktop","");

    //This is the map between the content and renderers path.
    Map<String,String> resourcesMap = new LinkedHashMap<String,String>();
    resourcesMap.put("/desktop", "/WEB-INF/views/desktop/");
    resourcesMap.put("/mobile", "/WEB-INF/views/mobile/");

    //This is the list of the pages that have a mobile specific renderers. Redirection will only happen when requesting
    //these pages.
    List<String> validMobilePages = new ArrayList<String>();
    validMobilePages.add("/browse");
    validMobilePages.add("/detail");

    //DeviceManager initialization.
    //The device manager determines the content uri and renderers path for the current channel.
    SiteState siteState = (SiteState) webappCtx.getBean("siteState");
    ContentPathTranslator pathTranslator = (ContentPathTranslator) webappCtx.getBean("contentPathTranslator");
    DeviceManager deviceManager = new DeviceManager(channelToPagePrefixMap, validMobilePages, resourcesMap,
            siteState, pathTranslator);


    //--------------------------------------------------------------------
    // Preview support section
    //--------------------------------------------------------------------

    //Get the user state defined on the assembler.properties file
    Properties properties = webappCtx.getBean("properties", Properties.class);
    UserState userState = webappCtx.getBean(properties.getProperty("user.state.ref"), UserState.class);

    //The user agent needs to be retrieved from the user state because the request could have originated from preview.
    String userAgent =  userState.getUserAgent();

    //If the userState has no specified userAgent, use the one from the request header.
    if(userAgent == null){
        userAgent = request.getHeader("user-agent");
    }

    request.setAttribute(PreviewAnchor.ENDECA_PREVIEW_ENABLED, Boolean.valueOf(properties.getProperty("preview.enabled")));
    //--------------------------------------------------------------------
    // End of preview support section
    //--------------------------------------------------------------------

    String contentUri = deviceManager.getContentPath(request, userAgent);

    //After getting the content uri from the device manager, it needs to be compared with the current
    //path info of the request. If they don't match, redirect so that the requested content is
    //the correct one for the current channel.
    if(!contentUri.equals(request.getPathInfo())){
        String query = request.getQueryString() == null ? "" : "?" + request.getQueryString();
        response.sendRedirect(request.getContextPath() + contentUri + query);

    //--------------------------------------------------------------------
    // End of the device detection and redirection section
    //--------------------------------------------------------------------


    }else{

    //--------------------------------------------------------------------
    // Assembler section
    //--------------------------------------------------------------------

        AssemblerFactory assemblerFactory = (AssemblerFactory)webappCtx.getBean("assemblerFactory");
        Assembler assembler = assemblerFactory.createAssembler();

        //Retrieve the content for the given content uri
        ContentItem contentItem = new RedirectAwareContentInclude();

        // Assemble the content
        ContentItem responseContentItem = assembler.assemble(contentItem);

        // If the response contains an error, send an error
        Object error = responseContentItem.get("@error");
        if (error != null)
        {
            if (((String) error).contains("FileNotFound"))
            {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getRequestURI());
            }
            else
            {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, (String)error);
            }
        }
        else
        {

            // Determine the output format and write to response
            String format = request.getParameter("format");
            if("json".equals(format)) {
                response.setHeader("content-type", "application/json");
                new JsonSerializer(response.getWriter()).write(responseContentItem);
            } else if ("xml".equals(format)) {
                response.setHeader("content-type", "application/xml");
                new XmlSerializer(response.getWriter()).write(responseContentItem);
            } else {
                request.setAttribute("component", responseContentItem);
                request.setAttribute("rootComponent", responseContentItem);

                Map map = (Map) request.getAttribute("component");
                if (map.containsKey("endeca:redirect")) {
                    request.setAttribute("action", ((ContentItem) map.get("endeca:redirect")).get("link"));
                    %>
                    <c:redirect url="${util:getUrlForAction(action)}"/>
                    <%
                }
                else {
                    //Given the current uri, determine which renderer path should be used by the discover:include tag.
                    request.setAttribute("Endeca.InFront.ComponentResourcePath", deviceManager.getResourcePath(request, contentUri));

                    %>
                    <discover:include component="${component}"/>
                    <%
                }
            }
        }
    }
    //--------------------------------------------------------------------
    // End of Assembler section
    //--------------------------------------------------------------------
%>
