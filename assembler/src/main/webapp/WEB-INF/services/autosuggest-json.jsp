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
<%@page language="java" contentType="text/html; charset=UTF-8" %>
<%@page import="java.util.Arrays"%>
<%@page import="com.endeca.infront.assembler.Assembler"%>
<%@page import="com.endeca.infront.assembler.AssemblerFactory"%>
<%@page import="com.endeca.infront.assembler.ContentItem"%>
<%@page import="com.endeca.infront.cartridge.ContentSlotConfig"%>
<%@page import="com.endeca.infront.serialization.JsonSerializer"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%
    // Construct a content slot config to query content
    // in the SearchBoxAutoSuggestContent collection
    ContentSlotConfig contentItem;
    
    contentItem = new ContentSlotConfig("ContentSlot");
    String[] contentPaths = request.getParameterValues("contentPaths");
    contentItem.setContentPaths(Arrays.asList(contentPaths));
    if (request.getParameterValues("templateTypes") != null) {
        String[] templateTypes = request.getParameterValues("templateTypes");
        contentItem.setTemplateTypes(Arrays.asList(templateTypes));
    }
    if (request.getParameterValues("templateIds") != null) {
        String[] templateIds = request.getParameterValues("templateIds");
        contentItem.setTemplateIds(Arrays.asList(templateIds));
    }

    // Get the AssemblerFactory from the spring context and create an Assembler
    WebApplicationContext webappCtx = WebApplicationContextUtils.getRequiredWebApplicationContext(application);
    AssemblerFactory assemblerFactory = (AssemblerFactory)webappCtx.getBean("assemblerFactory");
    Assembler assembler = assemblerFactory.createAssembler();
    
    // Assemble the content
    ContentItem responseContentItem = assembler.assemble(contentItem);
    
    // Send the assembled content response as JSON
    response.setHeader("content-type", "application/json");
    new JsonSerializer(response.getWriter()).write(responseContentItem);
%>