
<%@page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>

<%@include file="/WEB-INF/views/include.jsp"%>

<c:if test="${(component['@null']) != 'true'}">
    <div class="RefinementDimension">
        <div class="RefinementDimensionHeader">
            <span class="refinementDimensionHeaderText"><c:out value="${component.name}"/></span><br/>
        </div>
        <div class="RefinementDimensionText">
            <c:forEach var="refinement" items="${component.refinements}">
                <discover:link2 action="${refinement}" displayText="${refinement.label}" /> (<c:out value="${refinement.count}" />)<br/>
            </c:forEach>
            <c:if test="${!empty(component.moreLink)}">
                <br/>
              <discover:link action="${component.moreLink}"
                          displayText="${component.moreLink.label}"
                          displayClass="moreLink"/>
            </c:if>
            <c:if test="${!empty(component.lessLink)}">
                <br/>
              <discover:link action="${component.lessLink}"
                          displayText="${component.lessLink.label}"
                          displayClass="moreLink"/>
            </c:if>
        </div>
    </div>
</c:if>

