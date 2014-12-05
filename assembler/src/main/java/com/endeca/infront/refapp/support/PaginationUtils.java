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
import com.endeca.infront.cartridge.model.NavigationAction;

/**
 * Utility methods related to pagination.
 */
public class PaginationUtils {

    private PaginationUtils() {
    }

    /**
     * Retrieves pagination information from ResultsList module.
     *
     * @param result      ResultsList module
     * @param pageLinkNum page link numbers
     * @return pagination information
     */
    public static Pagination getPagination(ResultsList result, int pageLinkNum) {
        Pagination pagination = new Pagination();

        int currentPageNum = (int) (result.getFirstRecNum() / result.getRecsPerPage() + 1);

        //set current page number
        pagination.setPageNum(currentPageNum);

        //set paging control
        int currentPageLocation = currentPageNum % pageLinkNum == 0 ? pageLinkNum : currentPageNum % pageLinkNum;

        int lastPage = (int) Math.ceil((double) result.getTotalNumRecs() / (double) result.getRecsPerPage());
        for (int i = 1; i <= pageLinkNum; i++) {
            int pageNum = currentPageNum - currentPageLocation + i;

            if (pageNum > lastPage) {
                break;
            }

            pagination.getPagingControls().add(getPaginationAction(
                    result.getPagingActionTemplate(), pageNum, (int) result.getRecsPerPage()));
        }

        //set next page action.
        if (currentPageNum - currentPageLocation + pageLinkNum < lastPage) {
            pagination.setNextPagingAction(getPaginationAction(
                    result.getPagingActionTemplate(), currentPageNum - currentPageLocation + pageLinkNum + 1, (int) result.getRecsPerPage()));
        }

        //set previous page action
        if (currentPageNum - currentPageLocation != 0) {
            pagination.setPreviousPagingAction(getPaginationAction(
                    result.getPagingActionTemplate(), currentPageNum - currentPageLocation, (int) result.getRecsPerPage()));
        }

        return pagination;
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
        PagingControl pagingControl = new PagingControl();
        pagingControl.setSiteRootPath(actionTemplate.getSiteRootPath());
        pagingControl.setContentPath(actionTemplate.getContentPath());
        pagingControl.setPageNum(pageNum);
        pagingControl.setSiteState(actionTemplate.getSiteState());

        int offset = 0;
        String template = actionTemplate.getNavigationState();
        if (pageNum != 0) {
            offset = (pageNum - 1) * recordsPerPage;
        }

        String navState = template.replace("%7Boffset%7D", Integer.toString(offset))
                .replace("%7BrecordsPerPage%7D", Integer.toString(recordsPerPage));

        pagingControl.setNavigationState(navState);
        return pagingControl;
    }
}
