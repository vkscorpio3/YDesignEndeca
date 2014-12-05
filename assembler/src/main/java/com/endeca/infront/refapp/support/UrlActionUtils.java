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

import com.endeca.infront.cartridge.model.UrlAction;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Utility class for UrlAction.
 */
public class UrlActionUtils {

    private UrlActionUtils() {
    }

    /**
     * Constructs a URL for the given UrlAction.
     *
     * @param action the UrlAction that the url is being constructed for
     * @return String the constructed URL
     */
    public static String getUrl(UrlAction action) throws URISyntaxException {

        // Use the URI class to ensure proper encoding.
        // However, the one-arg URI constructor cannot
        // handle certain characters like "|" in the
        // URL parameter values.
        String url = action.getUrl();

        String fragment = null;
        int fragmentIndex = url.indexOf("#");
        if (fragmentIndex != -1) {
            fragment = url.substring(fragmentIndex + 1);
            url = url.substring(0, fragmentIndex);
        }

        String query = null;
        int queryIndex = url.indexOf("?");
        if (queryIndex != -1) {
            query = url.substring(queryIndex + 1);
            url = url.substring(0, queryIndex);
        }

        String href = new URI(url).toASCIIString();
        if (query != null || fragment != null) {
            href += new URI(null, null, null, query, fragment).toASCIIString();
        }
        return href;
    }
}
