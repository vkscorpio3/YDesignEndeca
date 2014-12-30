<%@page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>


<%@include file="/WEB-INF/views/include.jsp"%>

<script>
    $j(document).ready(function(){
        $j('img[alt=Add to Cart]').click(function() {
            var sessionId = endecaAssemblerRequestInformation['endeca:sessionId'];
            var log = "{\"converted\":\"TRUE\",\"sessionId\":\"" + sessionId + "\"}";
            $j.post("<%=request.getContextPath() %>/clicklog", {data:log}, function(data) {
                //alert(data.status);
            })
        });
    });
</script>

<div class="PagingRecordList">
    <c:choose>
        <c:when test="${empty component.records}">
            <div class="zeroResults">
                <span class="sorry">We're Sorry</span>, no results were found.
            </div>
            <div class="zeroResultsAdvice">Please try another search.</div>

        </c:when>

        <c:otherwise>
            <div class="ResultsStatistic">
                <div class="PagingRecordListCount">
                    <c:if test="${component.totalNumRecs / component.recsPerPage > 1}">
                        <%@include file="ResultsSetPagination.jsp"%>
                    </c:if> 
                    <c:if test="${component.totalNumRecs == 1}">
                        <div class="count">1 item</div>
                    </c:if>
                    <c:if test="${component.totalNumRecs > 1}">
                        <div class="count">Showing ${component.firstRecNum} -
                            ${component.lastRecNum} of ${component.totalNumRecs} items</div>
                    </c:if>
                </div>

                <!-- TODO: add pagination -->
            </div>

            <div class="PagingRecordListHeader">
                <div class="rightAlignedOptions">
                    <div class="display">
                        <span>Display:</span> <select
                            onchange="location = this.options[this.selectedIndex].value">
                            <c:forEach items="${fn:split('12,36,72',',')}" var="i">
                                <c:url var="firstPageURL"
                                    value="${util:getUrlForAction(util:changeRecordsPerPage(component.pagingActionTemplate, 0, i))}" />
                                <option value="${firstPageURL}"
                                    <c:if test="${i == component.recsPerPage}">
                                        selected="true"
                                        </c:if>>${i}
                                    per page</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="sort">
                        <span>Sort By:</span> <select
                            onchange="location = this.options[this.selectedIndex].value">
                            <c:forEach var="sortOption" items="${component.sortOptions}">
                                <c:url value="${util:getUrlForAction(sortOption)}" var="sortAction" />
                                <option
                                    <c:if test="${sortOption.selected}">
                                        selected="true"
                                    </c:if>
                                    value="${sortAction}">${sortOption.label}</option>

                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div style="clear: both;"></div>
            </div>

            <div class="PagingRecordListRecordBox">
                <c:forEach var="record" items="${component.records}"
                    varStatus="status">
                ${status.last ? '<div class="lastRecord">' : ''}
                <div class="Record">

                        <div class="thumbnail">
                            <c:choose>
                                <c:when
                                    test="${not empty record.attributes['product.img_url_thumbnail'][0]}">
                                    <c:set var="thumbnail"
                                        value="${record.attributes['product.img_url_thumbnail'][0]}" />
                                </c:when>
                                <c:otherwise>
                                    <c:url var="noImage" value="/images/no_image_75x75.gif" />
                                    <c:set var="thumbnail" value="${noImage}" />
                                </c:otherwise>
                            </c:choose>
                            <a href="<c:url value='${util:getUrlForAction(record.detailsAction)}'/>"><img
                                class="pic" src="${thumbnail}" />
                            </a>
                        </div>
                        <div class="productPurchase">
                            <div class="price">
                               
                                        <!-- not aggregated record, just output price here -->
                                        <fmt:formatNumber
                                            value="${record.attributes['price_sort_min'][0]}"
                                            type="currency" /> - 
                                            <fmt:formatNumber
                                            value="${record.attributes['price_sort_max'][0]}"
                                            type="currency" />
                            </div>
                            <c:if test="${record.numRecords eq 1}">
                                <div class="addToCart">
                                    <a href="#"> <img
                                        src="<c:url value='/images/addToCart.png' />"
                                        alt="Add to Cart" /> </a>
                                </div>
                                <div class="addToWishlist">
                                    + <a href="#">Add to wishlist</a>
                                </div>
                            </c:if>

                        </div>
                        <div class="basicProductInfo">
                            <div class="productBrand">
                                <c:out value="${record.attributes['Brand']}" escapeXml="false" />
                            </div>

                            <div class="productName">
                                <discover:link2 action="${record.detailsAction}" displayText="${record.attributes['name'][0]}"/>
                            </div>
                            <c:choose>
                                <c:when
                                    test="${!empty(record.attributes['description_short'][0])}">
                                    <div class="productDesc">
                                        <c:out
                                            value="${fn:replace(record.attributes['description_short'][0], 'endeca_term', 'b')}"
                                            escapeXml="false" />
                                    </div>
                                </c:when>
                                <c:when
                                    test="${!empty(record.attributes['product.short_desc'][0])}">
                                    <div class="productDesc">
                                        <c:out value="${record.attributes['product.short_desc'][0]}" />
                                    </div>
                                </c:when>
                            </c:choose>
                            <!-- Ranking Part -->
                            <div class="ranking">
                                <div class="floatLeft">
                                    <c:forEach begin="0" end="4" varStatus="i">
                                        <c:choose>
                                            <c:when
                                                test="${(record.attributes['reviews_star_rating'][0] + 0.5) ge (i.index + 1.0)}">
                                                <img alt="" src="<c:url value="/images/yellowstar.gif"/>">
                                            </c:when>
                                            <c:otherwise>
                                                <img alt="" src="<c:url value="/images/whitestar.gif"/>">
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                    &nbsp;&nbsp;&nbsp;
                                </div>
                                
                            </div>

                            <div style="clear: both;"></div>

                            <c:if test="${record.numRecords gt 1}">
                                <div class="moreOptionsMessage">Additional options
                                    available</div>

                            </c:if>
                        </div>
                        <div style="clear: both;"></div>
                    </div>
                ${status.last ? '</div>' : ''}
            </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
    <c:forEach var="element" items="${component.crossSiteResults}">
           <discover:include component="${element}"/>
    </c:forEach>    
</div>
