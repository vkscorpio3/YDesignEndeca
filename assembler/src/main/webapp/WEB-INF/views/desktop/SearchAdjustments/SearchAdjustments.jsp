<%@page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
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
             	<div>Your search ${originalTerm }</div>
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
