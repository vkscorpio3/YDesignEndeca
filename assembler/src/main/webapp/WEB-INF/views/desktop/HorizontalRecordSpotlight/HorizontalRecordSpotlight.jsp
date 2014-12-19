<%@page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>

<%@include file="/WEB-INF/views/include.jsp"%>
	   
<c:if test="${not empty component.records}">
	<c:choose>
		<c:when test="${requestScope['localHorizontalSpotlightCounter'] eq null}">
			<c:set var="localHorizontalSpotlightCounter" scope="page" value="1"/>
		</c:when>
		<c:otherwise>
		   	<c:set var="localHorizontalSpotlightCounter" scope="page" value="${localHorizontalSpotlightCounter+1}"/>
		</c:otherwise>
	</c:choose>
	<c:set var="localHorizontalSpotlightCounter" scope="request" value="${localHorizontalSpotlightCounter}"/>				
		
	<div id="HorizontalSpotlight_${localHorizontalSpotlightCounter}" class="ui-autocomplete-table HorizontalSpotlightTable HorizontalSpotlight">
		<div class="tableRow HorizontalSpotlightHeaderRow">
			<div class="HorizontalSpotlightText">
				<c:if test="${not empty component.title}">
					<c:out value="${component.title}"/>
				</c:if>
				
			</div>
		</div>

		<div class="tableRow HorizontalSpotlightRecordRow">
			<div class="HorizontalSpotlightGrandCell">
				<div class="prevArrowCell">
					<div class="prevArrow">
						<a id ="HorizontalSpotlight_${localHorizontalSpotlightCounter}_prevLink" class="prevClass"> <img src="<c:url value="/images/listArrowBackwards.png"/>"></a>
					</div>
				</div>
				<c:forEach var="record" items="${component.records}">
					<div class = "BasicProductInfo_HorizontalSpotlight">
						<div class = "thumbnail">
							<c:choose>
								<c:when test="${not empty record.attributes['product.img_url_thumbnail'][0]}">
								   <c:set var="thumbnail" value="${record.attributes['product.img_url_thumbnail'][0]}"/>
								</c:when>
								<c:otherwise>
								   <c:url var="noImage" value="/images/no_image_75x75.gif"/>
								   <c:set var="thumbnail" value="${noImage}"/>
								</c:otherwise>
							</c:choose>
							<a href="<c:url value='${util:getUrlForAction(record.detailsAction)}'/>"><img src="${thumbnail}"/></a>
						</div>
						
						<div class="brand">
							<c:out value="${record.attributes['Brand'][0]}"/>
						</div>
						
						<div class="productName">
							<a href="<c:url value='${util:getUrlForAction(record.detailsAction)}'/>">
								<c:set var="productName">${record.attributes['name']}</c:set>
									<c:choose>
										<c:when test="${fn:length(productName) le 16 }">
											<c:out value="${productName}"/>
										</c:when>
										<c:otherwise>
											<c:out value="${fn:substring(productName, 0, 30)}..."/>     
										</c:otherwise>
									</c:choose>
							</a>
						</div>
						<div class="price">
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
						</div>
						<div>
							<c:forEach begin="0" end="4" varStatus="status">
								 <c:choose>
									 <c:when test="${(record.attributes['p_customer_ratings'][0] + 0.5) ge (status.index + 1.0)}">
									   <img alt="" src="<c:url value="/images/yellowstar.gif"/>">
									 </c:when>
									 <c:otherwise>
									   <img alt="" src="<c:url value="/images/whitestar.gif"/>">
									 </c:otherwise>
								 </c:choose>
							 </c:forEach>
						</div>
					</div>
				</c:forEach>
				<div class="nextArrowCell">
					<div class="nextArrow">
						<a id="HorizontalSpotlight_${localHorizontalSpotlightCounter}_nextLink" class="nextClass"> <img src="<c:url value="/images/listArrow.png"/>"></a>
					</div>	
				</div>
			</div>
		</div>
		
		<c:if test="${not empty component.seeAllLink.label}">
			<div class="HorizontalMoreLink">
				<discover:link action="${component.seeAllLink}" displayText="${component.seeAllLink.label}"/>
			</div>
		</c:if>	
	</div>
</c:if>

