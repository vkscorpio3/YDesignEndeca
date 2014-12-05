<%@page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>

<%@include file="/WEB-INF/views/include.jsp"%>
<%@ taglib prefix="util" uri="/WEB-INF/tlds/functions.tld"  %>

<c:set var="siteState" value="${rootComponent['endeca:siteState']}"/>

<ydg:getSiteContextPath var="siteContext"/>
<c:set var="autoSuggestServiceUrl" value="/${siteContext}/browse"/>

<div class="SearchBox">
    <form onsubmit="if(this.Ntt.value == '')return false;" autocomplete="off" action='<c:url value="/${siteContext}/browse')}"/>'>
        <input class="submit" type="submit" value=""/>
        <input type="hidden" name="Dy" value="1"/>
        <input type="hidden" name="Nty" value="1"/>
        <input id="searchText" class="searchInput" type="text" name="Ntt"/>
    </form>
</div>

<script language="javascript" src="<c:url value="/views/desktop/SearchBox/endeca-auto-suggest.js"/>"></script>
<script language="javascript">
    $j(document).ready(
             function(event){
                 $j("#searchText").endecaSearchSuggest(
                         {
                             minAutoSuggestInputLength: <c:out value="${component.minAutoSuggestInputLength}"/>,
                             autoSuggestServiceUrl: '<c:url value="/autosuggest.json${util:getSiteUrl(siteState, autoSuggestServiceUrl)}"/>',
                    <c:choose>
                         <c:when test="${not empty component.contentPaths}">
                             contentPaths: ${util:printEscapedArray(component.contentPaths)},
                             templateTypes: ${util:printEscapedArray(component.templateTypes)},
                             templateIds: ${util:printEscapedArray(component.templateIds)},
                         </c:when>
                         <c:otherwise>
                             contentPaths: ["${util:escapeQuotes(component.contentCollection)}"],
                         </c:otherwise>
                    </c:choose>
                             searchUrl: '<%= request.getContextPath() %>',
                             containerClass:'dimSearchSuggContainer',
                             defaultImage:'<c:url value="/images/no_image_auto_suggest.gif"/>'
                         }
                     );
             }
         );
</script>