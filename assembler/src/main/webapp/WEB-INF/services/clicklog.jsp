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

<%@ page import="com.endeca.logging.LogEntry" %>
<%@ page import="com.endeca.logging.LogConnection" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="java.util.Properties" %>
<%@ page import="com.endeca.infront.util.deserial.JsonDeserializer" %>
<%@ page import="java.io.StringReader" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>

<%@page language="java" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="discover" tagdir="/WEB-INF/tags/discover" %>

<%
    final String DATA_PARAM = "data";
    final String CONVERTED = "converted";
    final String CLICKED_SPOTLIGHT = "clickedSpotLight";
    final String CLICKED_DYM = "clickedDidYouMean";
    final String CLICKED_DIM_SEARCH = "clickedDimSearch";
    final String RECORD_NAMES = "recordNames";
    final String SESSION_ID = "sessionId";

    LogConnection conn;
    String logServerHost;
    int logServerPort;

    final String data = request.getParameter(DATA_PARAM);
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json");
    String result;

    if (data == null) {
        result = "Expect a parameter of name 'data' providing logging data";
    } else {
        //Get the AssemblerFactory from the spring context and create an Assembler
        WebApplicationContext webappCtx = WebApplicationContextUtils.getRequiredWebApplicationContext(application);
        Properties properties = (Properties) webappCtx.getBean("properties");
        logServerHost = properties.getProperty("logserver.host");
        logServerPort = Integer.parseInt(properties.getProperty("logserver.port"));
        conn = new LogConnection(logServerHost,logServerPort);

        try {
            JsonDeserializer deserializer = new JsonDeserializer(new StringReader(data));
            Map logData = (Map) deserializer.deserialize();

            LogEntry entry = new LogEntry();

            if (logData.containsKey(SESSION_ID)) {
                entry.putString("SESSION_ID", (String) logData.get(SESSION_ID));
            }

            if (logData.containsKey(CONVERTED)) {
                entry.putString("CONVERTED", (String) logData.get(CONVERTED));
            }
            if (logData.containsKey(CLICKED_SPOTLIGHT)) {
                entry.putString("IN_MERCH", (String) logData.get(CLICKED_SPOTLIGHT));
            }
            if (logData.containsKey(CLICKED_DYM)) {
                entry.putString("IN_DYM", (String) logData.get(CLICKED_DYM));
            }
            if (logData.containsKey(CLICKED_DIM_SEARCH)) {
                entry.putString("IN_DIM_SEARCH", (String) logData.get(CLICKED_DIM_SEARCH));
            }
            if (logData.containsKey(RECORD_NAMES)) {
                List<String> recordNames = new ArrayList<String>();
                recordNames.add((String) logData.get(RECORD_NAMES));
                entry.putList("RECORD_NAMES", recordNames);
            }

            conn.logAsynchronously(entry);
            result = "Success";
        } catch (Exception e) {
            result = "Failed to parse log data";
        }
    }

    response.getWriter().write("{\"status\":\"" + result + "\"}");
%>
