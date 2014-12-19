<%@page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@include file="/WEB-INF/views/include.jsp"%>
Price Slider
<c:if test="${not empty component.priceSortMinMinimum}">
	   <p>priceSortMinMinimum: ${component.priceSortMinMinimum}</p>
</c:if>
<c:if test="${not empty component.priceSortMinMaximum}">
	    <p>priceSortMinMaximum: ${component.priceSortMinMaximum}</p>
</c:if>


<c:if test="${not empty component.priceSortMaxMinimum}">
	     <p>priceSortMaxMinimum: ${component.priceSortMaxMinimum}</p>
</c:if>
<c:if test="${not empty component.priceSortMaxMaximum}">
	     priceSortMaxMaximum: ${component.priceSortMaxMaximum}</p>
</c:if>



<c:if test="${not empty component.currentMin}">
	     currentMin: ${component.currentMin}</p>
</c:if>

<c:if test="${not empty component.currentMax}">
	     currentMax: ${component.currentMax}</p>
</c:if>