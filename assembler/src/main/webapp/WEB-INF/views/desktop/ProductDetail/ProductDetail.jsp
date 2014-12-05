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

<script language="javascript">
    $j(document).ready(function(){
        $j('img[alt=Add to Cart]').click(function() {
            var sessionId = endecaAssemblerRequestInformation['endeca:sessionId'];
            var log = "{\"converted\":\"TRUE\",\"sessionId\":\"" + sessionId + "\"}";
            $j.post("<%=request.getContextPath() %>/clicklog", {data:log}, function(data) {
                //alert(data.status);
            })
        });
    });

    $j(function(){
        //bind event handlers for images
        $j('.RecordDetails div.thumbnailGroup img').each(function(index, element) {
            $j(element).bind('click',function(event) {
                //Switch the Medium image to the selected item
                $j('.RecordDetails img.mediumImage').attr("src", $j($j('.RecordDetails div.thumbnailGroup div')[index]).html());
                //If have multiple colors, also change the selected color and price
                if($j("#colorSelector").size() > 0)
                {
                    //change color
                    $j($j("option", "#colorSelector")[index]).attr("selected", "true");
                    //change price
                    $j('.RecordDetails .shippingCart .price').addClass("hiddenBlock");
                    $j($j('.RecordDetails .shippingCart .price')[index]).removeClass("hiddenBlock");
                }
            });
            $j(element).bind('mouseover',function(event) {
                $j(this).css("border-color", "black");
            });
            $j(element).bind('mouseout',function(event) {
                $j(this).css("border-color", "white");
            });
        })
        //bind event handlers for the color selector, once different color is selected,
        //switch the images.
        $j("#colorSelector").change(
            function(event)
            {
                $j('.RecordDetails img.mediumImage').attr("src", $j($j('.RecordDetails div.thumbnailGroup div')[this.selectedIndex]).html());
                $j('.RecordDetails .shippingCart .price').addClass("hiddenBlock");
                $j($j('.RecordDetails .shippingCart .price')[this.selectedIndex]).removeClass("hiddenBlock");
            }
        );
        $j("#colorSelector").attr("selectedIndex", 0);
        //bind event handlers for Tabs.
        $j('.RecordDetails div.tabHead span').each(function(index, element) {
            $j(element).bind('click',function(event) {
                $j(this).removeClass("tabTitleUnfocus");
                $j(this).addClass("tabTitleFocus");
                $j($j('.RecordDetails div.tabContent div')[index]).addClass("showBlock").show();
                $j('.RecordDetails div.tabHead span').each(function(secondIndex, element) {
                    if(index != secondIndex)
                    {
                        $j(this).removeClass("tabTitleFocus");
                        $j(this).addClass("tabTitleUnfocus");
                        $j($j('.RecordDetails div.tabContent div')[secondIndex]).hide();
                    }
                });
            })
        });
    });
</script>
<div class="RecordDetails">
    <div class="toMainPage">
        <a href="javascript: window.history.back()">&nbsp;&lt; Back to Results</a>
    </div>
    <c:set var="record" value="${component.record}"></c:set>
    <c:set var="imageGallery" value="${util:imageGallery(component.record)}"></c:set>
    <!-- Record Details Cartridge Main part -->
    <div class="mainPart floatLeft">
        <!-- image part -->
        <div class="imagePart floatLeft">
            <c:choose>
                <c:when test="${not empty imageGallery[0]['Large']}">
                   <c:set var="largeImage" value="${imageGallery[0]['Large']}"/>
                </c:when>
                <c:otherwise>
                   <c:url var="noImage" value="/images/no_image_325x216.gif"/>
                   <c:set var="largeImage" value="${noImage}"/>
                </c:otherwise>
            </c:choose>
            <img class="mediumImage" alt="" src="${largeImage}"/>
            <div style="margin-top: 15px;">
                <div class="thumbnailGroup floatLeft">
                    <span class="showBlock">More images:</span>
                    <c:forEach var="img" items="${imageGallery}">
                         <img src="${img['Thumbnail']}" />
                         <div class="hiddenBlock">${img["Large"]}</div>
                    </c:forEach>
                </div>
            </div>
        </div>
        <div class="adhoc">
            <!-- introduction part -->
            <div class="introPart floatLeft">
                 <div class="brand">${record.attributes['product.brand.name']}</div>
                 <span class="title">${record.attributes['product.name']}</span>
                 <!-- handle colors here -->
                 <c:if test="${record.numRecords gt 1}">
                     Color:
                     <select id="colorSelector">
                         <c:forEach var="rec" items="${record.records}" varStatus="status">
                            <option
                                <c:if test="${status.first}"> selected</c:if>
                                value="${rec.attributes['product.sku']}"
                            >
                                ${rec.attributes['camera.color']}
                            </option>
                         </c:forEach>
                     </select>
                 </c:if>
                 <div class="introDetails">
                     <!-- Handle Camera backpack and cases here -->
                     <c:choose>
                         <c:when test="${fn:contains(record.attributes['product.category'], 'backpacks & cases')}">
                             <!-- Here are camera cases -->
                             <div class="firstColumn floatLeft">
                                 <span>Interior dimensions (W x D x H): </span>
                                 <span>Weight: ${record.attributes['camera.weight']}</span>
                                 <span>Material: ${record.attributes['camera.material']}</span>
                             </div>
                         </c:when>
                         <c:otherwise>
                             <!-- Here are cameras -->
                             <div class="firstColumn floatLeft">
                                 <span>Video Capture Resolution: ${record.attributes['camera.video_capture_resolution']}</span>
                                 <span>Lens Focal Range: ${record.attributes['camera.focal_length']}</span>
                                 <span>Processor Model: ${record.attributes['camera.processor_model']}</span>
                             </div>
                             <div class="secondColumn floatLeft">
                                 <span>Sensor Type: ${record.attributes['camera.sensor_type']}</span>
                                 <span>Interface: ${record.attributes['camera.interface']}</span>
                             </div>
                         </c:otherwise>
                     </c:choose>
                     <div class="clear"></div>
                 </div>
                 <!-- Ranking Part -->
                 <div class="ranking">
                   <div class="floatLeft">
                       <c:forEach begin="0" end="4" varStatus="status">
                         <c:choose>
                             <c:when test="${(record.attributes['product.review.avg_rating'][0] + 0.5) ge (status.index + 1.0)}">
                               <img alt="" src="<c:url value="/images/yellowstar.gif"/>">
                             </c:when>
                             <c:otherwise>
                               <img alt="" src="<c:url value="/images/whitestar.gif"/>">
                             </c:otherwise>
                         </c:choose>
                       </c:forEach>
                       &nbsp;&nbsp;&nbsp;
                   </div>
                   <div class="floatLeft">
                     <c:choose>
                         <c:when test="${not empty record.attributes['product.review.count']}">
                            Reviews(${record.attributes['product.review.count']})
                         </c:when>
                         <c:otherwise>
                           Reviews(0)
                         </c:otherwise>
                     </c:choose>
                   </div>
                 </div>
            </div>
            <div class="offerBox  floatLeft">
                <div class="floatLeft shippingDetails">
                    <span class="title">Shipping Details</span>
                    <div>
                        <div class="properties floatLeft">
                            <span>Next day delivery</span>
                            <span>Free delivery </span>
                            <span>Standard delivery</span>
                        </div>
                        <div class="values floatRight">
                            <span>order before 2pm</span>
                            <span>5 - 8 days</span>
                            <span>1 - 5 days</span>
                        </div>
                    </div>
                </div>
                <div class="specialOffers floatLeft">
                    <span class="title">Special Offers</span>
                    <div  class="properties">
                        <span>Free Giftwrap</span>
                    </div>
                </div>
            </div>
        </div>
        <div class="clear"></div>
        <!-- tab parts -->
        <div class="tab">
            <!-- titles -->
            <div class="tabHead">
                <span class="tabTitleFocus">Overview</span>
                <span class="tabTitleUnfocus">Specifications</span>
                <span class="tabTitleUnfocus">Accessories</span>
                <span class="tabTitleUnfocus">Warranty Info</span>
            </div>
            <!-- content -->
            <div class="tabContent">
                <div class="showBlock">${record.attributes['product.long_desc']}</div>
                <div>
                    <c:set var="specificationsAttribute" value="${record.attributes['product.specifications']}"/>
                    <c:choose>
                        <c:when test="${empty specificationsAttribute}">
                            No specifications available.
                        </c:when>
                        <c:otherwise>
                            <br/>
                            <c:forEach items="${specificationsAttribute}" var="attrValue" varStatus="varStatus">
                               <!-- Model Attribute has no key here -->
                               ${attrValue}
                               <c:if test="${not varStatus.last}">
                                   ,
                               </c:if>
                               <br/><br/>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div>No accessories available.</div>
                <div>
                    <c:choose>
                        <c:when test="${empty record.attributes['camera.warranty']}">
                            No warranty information available.
                        </c:when>
                        <c:otherwise>
                            ${record.attributes['camera.warranty']}
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
    <!-- Shipping Cart Part -->
    <div class="rightPart floatLeft">
        <div class="shippingCart">
            <c:choose>
                <c:when test="${record.numRecords gt 1}">
                    <c:forEach var="rec" items="${record.records}" varStatus="status">
                        <span class="price <c:if test="${not status.first}">hiddenBlock</c:if>">
                            <!-- output price range here -->
                            <fmt:formatNumber value="${rec.attributes['product.price'][0]}" type="currency"/>
                        </span>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <span class="price">
                        <fmt:formatNumber value="${record.attributes['product.price'][0]}" type="currency"/>
                    </span>
                </c:otherwise>
            </c:choose>
            <div class="addToCart">
                <a href="#">
                    <img src="<c:url value='/images/addToCart.png' />" alt="Add to Cart" />
                </a>
            </div>
            <span class="addToWishList">+&nbsp;Add to Wishlist</span>
            <hr class="line"/>
        </div>
    </div>
    <div class="clear"></div>
</div>
