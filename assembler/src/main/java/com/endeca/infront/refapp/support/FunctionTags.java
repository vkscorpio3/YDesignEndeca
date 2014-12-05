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

package com.endeca.infront.refapp.support;

import com.endeca.infront.cartridge.ResultsList;
import com.endeca.infront.cartridge.model.Action;
import com.endeca.infront.cartridge.model.NavigationAction;
import com.endeca.infront.cartridge.model.Record;
import com.endeca.infront.cartridge.model.RecordAction;
import com.endeca.infront.cartridge.model.UrlAction;
import com.endeca.infront.site.SiteUtils;
import com.endeca.infront.site.model.SiteState;

import java.lang.String;
import java.net.URISyntaxException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Defines functions which can be invoked in JSP expression language.
 */
public class FunctionTags {

    private FunctionTags() {}

    /**
     * Retrieves images in the record.
     * If the record is a regular record, it retrieves all the Thumbnail/Large image
     * pairs of this record.
     * If the record is a aggregated record, for each sub record, it retrieves the
     * first pair of Thumbnail/Large image.
     *
     * @return images in the record.
     */
    public static List<Map<String, String>> getImageGallary(Record record) {
        return ImageUtils.getImageGallary(record);
    }

    /**
     * Retrieves pagination information from ResultsList module.
     *
     * @param result      ResultsList module
     * @param pageLinkNum page link numbers
     * @return pagination information
     */
    public static Pagination getPagination(ResultsList result, int pageLinkNum) {
        return PaginationUtils.getPagination(result, pageLinkNum);
    }

    /**
     * Retrieves pagination action
     *
     * @param actionTemplate a NavigationAction that is used to construct paging actions
     * @param pageNum        current page number 1st page is 0
     * @param recordsPerPage number of records per page
     * @return paging action for given arguments
     */
    public static PagingControl getPaginationAction(NavigationAction actionTemplate, int pageNum, int recordsPerPage) {
        return PaginationUtils.getPaginationAction(actionTemplate, pageNum, recordsPerPage);
    }

    public static String replace(String target, CharSequence oldChar, CharSequence newChar) {
        return target.replace(oldChar, newChar);
    }
    
    public static String escapeQuotes(String str) {
        return str.replace("\"","\\\"");
    }
    
    public static String printEscapedArray(List<String> paths) {
        List<String> escapedPaths = new ArrayList<String>();
        for (String str : paths) {
            escapedPaths.add("\"" + escapeQuotes(str) + "\"");
        }
        return escapedPaths.toString();
    }

    /**
     * Constructs a URL for the given action
     *
     * @param action the action that the url is being constructed for
     * @return String the constructed URL
     */
    public static String getUrlForAction(Action action) throws URISyntaxException {
        String href = "";

        if (action instanceof NavigationAction) {
            NavigationAction navAction = (NavigationAction) action;

            if (navAction.getContentPath() != null)
                href += navAction.getContentPath();
            if (navAction.getNavigationState() != null)
                href += navAction.getNavigationState();
        }
        else if (action instanceof RecordAction) {
            RecordAction recAction = (RecordAction) action;

            if (recAction.getContentPath() != null)
                href += recAction.getContentPath();
            if (recAction.getRecordState() != null)
                href += recAction.getRecordState();
        }
        else if (action instanceof UrlAction) {
            href = UrlActionUtils.getUrl((UrlAction) action);
        }
        
        href = SiteUtils.getSiteUrl(action.getSiteState(), href);
        return href;
    }

    /**
     * Converts a URL into a site-aware URL.
     * @see com.endeca.infront.site.SiteUtils#getSiteUrl(SiteState, String)
     *
     * @param action the current site state
     * @return String the url that we are making site aware.
     */
    public static String getSiteUrl(SiteState siteState, String href) throws URISyntaxException {
        href = SiteUtils.getSiteUrl(siteState, href);
        return href;
    }
}
