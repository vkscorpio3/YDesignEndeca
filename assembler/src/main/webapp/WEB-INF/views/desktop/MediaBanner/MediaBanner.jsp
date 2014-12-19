<%@page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>


<%@include file="/WEB-INF/views/include.jsp"%>

<script type="text/javascript" src="<c:url value="/js/swfobject.js"/>"></script>
<c:if test="${not empty component.link}">
    <c:url value="${util:getUrlForAction(component.link)}" var="hrefUrl"></c:url>
</c:if>

<c:url value="${component.media['uri']}" var="mediaSrc"></c:url>
<c:url value="${component.media['uri']}" var="videoSrc"></c:url>
<c:url value="${component.media['contentHeight']}" var="mediaHeight"></c:url>
<c:url value="${component.media['contentWidth']}" var="mediaWidth"></c:url>
<c:url value="${component.media['contentType']}" var="contentType"></c:url>
<c:url value="${component.imageAlt}" var="imageAlt"></c:url>

<c:set value="<%=java.lang.System.nanoTime()%>" var="randomNumber"></c:set>
    
<script type="text/javascript">

    $j(function()
    {
        var swf;
        var enabled = false;

        var flashVersionRequired = "9.0.115";
        if (!swfobject.hasFlashPlayerVersion(flashVersionRequired))
        {
            var msg = "Flash player version " + flashVersionRequired + " (or higher) is required.";
            var flashVersion = swfobject.getFlashPlayerVersion();
            if (flashVersion.major == 0)
            {
                msg += " This browser does not have a Flash Plug-in.";
            }
            else
            {
                msg += " This browser's Flash Plug-in is version " + flashVersion.major + "." + flashVersion.minor + ".";
            }
            $j("#theSwffer").html(msg);
        }
        else
        {
            swfobject.embedSWF("<c:url value='player/VideoPlayer.swf' />", "theSwffer${randomNumber}", 
                               "100%", "100%",
                               flashVersionRequired,
                               null,
                               {videoURL: '${videoSrc}', width: <c:out value='${mediaWidth}' />, height: <c:out value='${mediaHeight}' />
                               <c:if test="${not empty hrefUrl}">
                                   , linkURL: encodeURIComponent('${hrefUrl}')
                               </c:if>
                               });
            enabled = true;
        }
    });
</script>

<c:if test="${not empty mediaSrc}">
    <div class="MediaBanner" style="height: <c:out value='${mediaHeight}' />px; width: <c:out value='${mediaWidth}' />px;">
        <c:if test="${not empty hrefUrl}">
            <a href="<c:out value='${hrefUrl}'/>">
        </c:if>
        <c:choose>
            <c:when test="${contentType eq 'Video'}">
                <div id="theSwffer${randomNumber}"></div>
            </c:when>
            <c:otherwise>
                <img src="<c:out value='${mediaSrc}' />" 
                     height="<c:out value='${mediaHeight}' />" 
                     width="<c:out value='${mediaWidth}' />" 
                     alt="<c:out value='${imageAlt}' />" />
            </c:otherwise>
        </c:choose>
        <c:if test="${not empty hrefUrl}">
            </a>
        </c:if>
    </div>
</c:if>
