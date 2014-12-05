/*
 * Copyright 2001, 2014, Oracle and/or its affiliates. All rights reserved.
 * Oracle and Java are registered trademarks of Oracle and/or its
 * affiliates. Other names may be trademarks of their respective owners.
 * UNIX is a registered trademark of The Open Group.
 *
 * This software and related documentation are provided under a license
 * agreement containing restrictions on use and disclosure and are
 * protected by intellectual property laws. Except as expressly permitted
 * in your license agreement or allowed by law, you may not use, copy,
 * reproduce, translate, broadcast, modify, license, transmit, distribute,
 * exhibit, perform, publish, or display any part, in any form, or by any
 * means. Reverse engineering, disassembly, or decompilation of this
 * software, unless required by law for interoperability, is prohibited.
 * The information contained herein is subject to change without notice
 * and is not warranted to be error-free. If you find any errors, please
 * report them to us in writing.
 * U.S. GOVERNMENT END USERS: Oracle programs, including any operating
 * system, integrated software, any programs installed on the hardware,
 * and/or documentation, delivered to U.S. Government end users are
 * "commercial computer software" pursuant to the applicable Federal
 * Acquisition Regulation and agency-specific supplemental regulations.
 * As such, use, duplication, disclosure, modification, and adaptation
 * of the programs, including any operating system, integrated software,
 * any programs installed on the hardware, and/or documentation, shall be
 * subject to license terms and license restrictions applicable to the
 * programs. No other rights are granted to the U.S. Government.
 * This software or hardware is developed for general use in a variety
 * of information management applications. It is not developed or
 * intended for use in any inherently dangerous applications, including
 * applications that may create a risk of personal injury. If you use
 * this software or hardware in dangerous applications, then you shall
 * be responsible to take all appropriate fail-safe, backup, redundancy,
 * and other measures to ensure its safe use. Oracle Corporation and its
 * affiliates disclaim any liability for any damages caused by use of this
 * software or hardware in dangerous applications.
 * This software or hardware and documentation may provide access to or
 * information on content, products, and services from third parties.
 * Oracle Corporation and its affiliates are not responsible for and
 * expressly disclaim all warranties of any kind with respect to
 * third-party content, products, and services. Oracle Corporation and
 * its affiliates will not be responsible for any loss, costs, or damages
 * incurred due to your access to or use of third-party content, products,
 * or services.
 */

package com.endeca.mobile.services.detection;

import com.endeca.infront.site.ContentPathTranslator;
import com.endeca.infront.site.model.SiteState;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class DeviceManager {

    final Map<String, String> channelToPagePrefixMap;
    final Map<String, String> resourcesMap;
    final List<String> validMobilePages;
    final ContentPathTranslator pathTranslator;
    final SiteState siteState;

    //The value of the mobile channel, "Endeca.Infront.Channel.Mobile"
    final String MOBILE  = "Endeca.Infront.Channel.Mobile";

    //The value of the desktop channel, "Endeca.Infront.Channel.Desktop"
    final String DESKTOP = "Endeca.Infront.Channel.Desktop";

    // Regex's from http://detectmobilebrowser.com/
    final Pattern userAgentPattern1 = Pattern.compile(".*(android|avantgo|blackberry|blazer|compal|elaine|fennec|hiptop" +
            "|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\\/|plucker" +
            "|pocket|psp|symbian|treo|up\\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino).*", Pattern.CASE_INSENSITIVE);

    final Pattern userAgentPattern2 = Pattern.compile("1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\\-)" +
            "|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\\-m|r |s )|avan|be(ck|ll|nq)" +
            "|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\\-(n|u)|c55\\/|capi|ccwa|cdm\\-|cell|chtm|cldc|cmd\\-|co(mp|nd)|craw" +
            "|da(it|ll|ng)|dbte|dc\\-s|devi|dica|dmob|do(c|p)o|ds(12|\\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0" +
            "|os|wa|ze)|fetc|fly(\\-|_)|g1 u|g560|gene|gf\\-5|g\\-mo|go(\\.w|od)|gr(ad|un)|haie|hcit|hd\\-(m|p|t)|hei\\-" +
            "|hi(pt|ta)|hp( i|ip)|hs\\-c|ht(c(\\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\\-(20|go|ma)|i230|iac( |\\-|\\/)|ibro" +
            "|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\\/)|klon|kpt |kwc\\-|kyo(c|k)" +
            "|le(no|xi)|lg( g|\\/(k|l|u)|50|54|e\\-|e\\/|\\-[a-w])|libw|lynx|m1\\-w|m3ga|m50\\/|ma(te|ui|xo)|mc(01|21|ca)" +
            "|m\\-cr|me(di|rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]" +
            "|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran" +
            "|owg1|p800|pan(a|d|t)|pdxg|pg(13|\\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\\-2|po(ck|rt|se)|prox|psio|pt\\-g" +
            "|qa\\-a|qc(07|12|21|32|60|\\-[2-7]|i\\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\\/|sa(ge|ma|mm|ms|ny|va)" +
            "|sc(01|h\\-|oo|p\\-)|sdk\\/|se(c(\\-|0|1)|47|mc|nd|ri)|sgh\\-|shar|sie(\\-|m)|sk\\-0|sl(45|id)|sm(al|ar|b3" +
            "|it|t5)|so(ft|ny)|sp(01|h\\-|v\\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\\-|tdg\\-|tel(i|m)" +
            "|tim\\-|t\\-mo|to(pl|sh)|ts(70|m\\-|m3|m5)|tx\\-9|up(\\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]" +
            "|\\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700" +
            "|xda(\\-|2|g)|yas\\-|your|zeto|zte\\-", Pattern.CASE_INSENSITIVE);

    /**
     * Constructor
     *
     * @param channelToPagePrefixMap Mapping from channel to page prefix
     * @param validMobilePages List of pages that are mobile optimized
     * @param resourcesMap Mapping from page prefix to renderer path
     */
    public DeviceManager(Map<String, String> channelToPagePrefixMap,
                         List<String> validMobilePages,
                         Map<String, String> resourcesMap){

        this(channelToPagePrefixMap, validMobilePages, resourcesMap, null, null);
    }

    public DeviceManager(Map<String, String> channelToPagePrefixMap,
                         List<String> validMobilePages,
                         Map<String, String> resourcesMap,
                         SiteState siteState,
                         ContentPathTranslator pathTranslator){

        this.channelToPagePrefixMap = channelToPagePrefixMap;
        this.validMobilePages = validMobilePages;
        this.resourcesMap = resourcesMap;
        this.pathTranslator = pathTranslator;
        this.siteState = siteState;
    }

    /**
     * Builds a content path from a request and a page prefix determined by the userAgent.
     * This request can go through a ContentPathTranslator to resolve a relative content path
     * without site context.
     *
     * @return content path
     */
    public String getContentPath(HttpServletRequest request, String userAgent) {
        String contentUri;

        if (pathTranslator != null && siteState != null){
            contentUri = pathTranslator.getContentPathTranslated(request, siteState);
        } else {
            contentUri = request.getPathInfo();
        }

        return getContentPath(contentUri, userAgent);
    }

    /**
     * Builds a content path from the specified contentUri and a page prefix determined by the userAgent.
     *
     * @param contentUri
     * @param userAgent
     * @return content path
     */
    public String getContentPath(String contentUri, String userAgent){
        final String channel = getChannel(userAgent);
        final String prefix = channelToPagePrefixMap.get(channel);

        String sitePrefix = "";
        if (siteState != null && siteState.getMatchedUrlPattern() != null){
            sitePrefix = siteState.getMatchedUrlPattern();
        }

        if(!"".equals(channel) && prefix != null){
            String redirectedContentUri = "";

            //We need to remove the current prefix of the contentUri before appending a new one.
            for (String pagePrefix : channelToPagePrefixMap.values()) {
                if (contentUri.startsWith(pagePrefix)) {
                    redirectedContentUri = contentUri.replaceFirst(pagePrefix, "");
                    break;
                }
            }

            //Check if it's a valid mobile page
            for(String mobilePage: validMobilePages){
                if(redirectedContentUri.startsWith(mobilePage)){
                    return sitePrefix + prefix + redirectedContentUri;
                }
            }
        }

        return sitePrefix + contentUri;
    }

    /**
     * Returns the resource path of the renderer given a content path
     * which requires content path translation.
     * @return resource path
     */
    public String getResourcePath(HttpServletRequest request, final String contentPath) {
        HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(request) {
            public String getPathInfo(){
                return contentPath;
            }
        };

        String translatedContentPath;
        if (pathTranslator != null && siteState != null){
            translatedContentPath = pathTranslator.getContentPathTranslated(requestWrapper,siteState);
        } else {
            translatedContentPath = contentPath;
        }
        return getResourcePath(translatedContentPath);
    }

    /**
     * Returns the resource path of the renderer given a content path
     * @param contentPath
     * @return resource path
     */
    public String getResourcePath(String contentPath){
        for (Map.Entry<String, String> entry : resourcesMap.entrySet()) {
            if (contentPath.startsWith(entry.getKey())) {
                return entry.getValue();
            }
        }

        return resourcesMap.get("/desktop");
    }

    /**
     * Returns the channel for the particual user agent
     * @param userAgent User agent must not be encoded
     * @return Endeca.Infront.Channel.Mobile, Endeca.Infront.Channel.Desktop, or the empty string
     */
    private String getChannel(String userAgent){
        if("".equals(userAgent)){
            return userAgent;
        } else if( userAgent.length() > 4) {

            final String uaPart = userAgent.substring(0, 4);
            if(userAgentPattern1.matcher(userAgent).matches() || userAgentPattern2.matcher(uaPart).matches()) {
                return MOBILE;
            }
        }

        return DESKTOP;
    }
}
