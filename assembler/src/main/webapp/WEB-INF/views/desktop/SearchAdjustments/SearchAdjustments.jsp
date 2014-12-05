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

<script>
    $j(document).ready(function(){
        $j('.dymAction').click(function() {
            var sessionId = endecaAssemblerRequestInformation['endeca:sessionId'];
            var log = "{\"clickedDidYouMean\":\"TRUE\",\"sessionId\":\"" + sessionId + "\"}";
            $j.post("<%=request.getContextPath() %>/clicklog", {data:log}, function(data) {
                //alert(data.status);
            })
        });
    });
</script>

<c:if test="${not empty component.originalTerms}">
    <div class="SearchAdjustments">
        <div>
             <c:forEach var="originalTerm" items="${component.originalTerms}" varStatus="status">
                 <c:if test="${not empty component.adjustedSearches[originalTerm]}">
                     Your search for <span style="font-weight: bold;">${originalTerm}</span> was adjusted to
                     <c:forEach var="adjustment" items="${component.adjustedSearches[originalTerm]}" varStatus="status">
                         <span class="autoCorrect">${adjustment.adjustedTerms}</span>
                         <c:if test="${!status.last}">, </c:if>
                     </c:forEach>
                     <br>
                 </c:if>
                 <c:if test="${not empty component.suggestedSearches[originalTerm]}">
                     Did you mean
                     <c:forEach var="suggestion" items="${component.suggestedSearches[originalTerm]}" varStatus="status">
                         <c:set var="label">
                             ${suggestion.label}
                             <c:if test="${not empty suggestion.count}">
                                 (${suggestion.count} results)
                             </c:if>
                         </c:set>
                         <discover:link displayClass="dymAction" displayText="${label}" action="${suggestion}"></discover:link>
                         <c:if test="${!status.last}">, </c:if>
                     </c:forEach>?
                     <br>
                 </c:if>
             </c:forEach>
        </div>
    </div>
</c:if>
