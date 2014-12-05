<%@ taglib prefix="util" uri="/WEB-INF/tlds/functions.tld"  %>
<div class="PageLogo">
    <c:set var="siteState" value="${rootComponent['endeca:siteState']}"/>
    <a href="<c:url value="${util:getSiteUrl(siteState, '/browse')}" />"><img src="<c:url value="/images/ydesigngroup-logo.gif"/>"
                                      alt="Discover Electronics" /></a>
</div>
  