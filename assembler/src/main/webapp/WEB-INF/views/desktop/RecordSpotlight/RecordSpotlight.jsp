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
        $j('.spotlightAction').click(function() {
            if ($j(this).text() != null) {
                var recordname = $j(this).text();
            }
            if ($j(this).attr("alt") != null) {
                var recordname = $j(this).attr("alt");
            }

            var sessionId = endecaAssemblerRequestInformation['endeca:sessionId'];
            var log = "{\"clickedSpotLight\":\"TRUE\",\"recordNames\":\"" + recordname + "\",\"sessionId\":\"" + sessionId + "\"}";
            $j.post("<%=request.getContextPath() %>/clicklog", {data:log}, function(data) {
                //alert(data.status);
            })
        });
    });
</script>

<c:if test="${not empty component.records}">
    <div class="Spotlight">
        <div class="SpotlightText">
            <c:if test="${not empty component.title}">
                <c:out value="${component.title}"/>
            </c:if>
        </div>
        
        <c:forEach var="record" items="${component.records}">
            <div class="SpotlightRecord" >
                <table>
                    <tr>
                        <td>
                            <c:choose>
	                            <c:when test="${not empty record.attributes['product.img_url_thumbnail'][0]}">
	                               <c:set var="thumbnail" value="${record.attributes['product.img_url_thumbnail'][0]}"/>
	                            </c:when>
	                            <c:otherwise>
	                               <c:url var="noImage" value="/images/no_image_75x75.gif"/>
	                               <c:set var="thumbnail" value="${noImage}"/>
	                            </c:otherwise>
                            </c:choose>
                            <a href="<c:url value='${util:getUrlForAction(record.detailsAction)}'/>"><img class="spotlightAction" alt="${record.attributes['product.name'][0]}"  src="${thumbnail}"/></a>
                        </td>
                        <td>
                            <discover:link displayClass="spotlightAction" action="${record.detailsAction}" displayText="${record.attributes['product.name'][0]}" />
                            <br/>
                            <span>Price:</span>
                            <span class="price">
                                <c:choose>
                                    <c:when test="${record.attributes['product.min_price'][0] ne record.attributes['product.max_price'][0]}">
                                        <fmt:formatNumber value="${record.attributes['product.min_price'][0]}" 
                                            type="currency"/>
                                        -
                                        <fmt:formatNumber value="${record.attributes['product.max_price'][0]}" 
                                            type="currency"/>
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber value="${record.attributes['product.price'][0]}" 
                                            type="currency"/>
                                    </c:otherwise>
                                </c:choose>
                            </span>                          
                        </td>
                    </tr>
                </table>
            </div>
         </c:forEach>
        <c:if test="${not empty component.seeAllLink.label}">
            <div class="SpotlightRecord"><discover:link displayClass="moreLink" action="${component.seeAllLink}" displayText="${component.seeAllLink.label}" /></div>
        </c:if>
    </div>
</c:if>
