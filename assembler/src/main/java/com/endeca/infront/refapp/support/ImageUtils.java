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

import com.endeca.infront.cartridge.model.Attribute;
import com.endeca.infront.cartridge.model.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility methods related to record images.
 */
public class ImageUtils {

    private ImageUtils() {
    }

    private enum ImageSize {
        LARGE("Large"), MEDIUM("Medium"), THUMBNAIL("Thumbnail");

        private final String description;

        private ImageSize(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return this.description;
        }
    }

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
        if (record.getNumRecords() > 1) {
            //It is aggregated record, get images for aggregated record.
            return getAggrRecordImages(record);
        }
        else {
            return getRecordImages(record);
        }
    }

    /**
     * For Aggregated record, get one image pair for each sub record.
     */
    private static List<Map<String, String>> getAggrRecordImages(Record record) {
        List<Map<String, String>> images = new ArrayList<Map<String, String>>();

        for (Record rec : record.getRecords()) {
            images.add(getImageInfo(rec.getAttributes()));
        }

        return images;
    }

    /**
     * For normal record, get the basic image and also get the extra images.
     */
    private static List<Map<String, String>> getRecordImages(Record record) {
        List<Map<String, String>> imageGallery = new ArrayList<Map<String, String>>();

        imageGallery.add(getImageInfo(record.getAttributes()));
        imageGallery.addAll(getExtraImages(record.getAttributes()));

        return imageGallery;
    }

    private static Map<String, String> getImageInfo(Map<String, Attribute> properties) {
        Map<String, String> images = new HashMap<String, String>();

        if (null != properties.get("product.img_url_thumbnail")) {
            images.put(ImageSize.THUMBNAIL.toString(), properties.get("product.img_url_thumbnail").toString());
        }
        if (null != properties.get("product.img_url_large")) {
            images.put(ImageSize.LARGE.toString(), properties.get("product.img_url_large").toString());
        }

        return images;
    }

    private static List<Map<String, String>> getExtraImages(Map<String, Attribute> properties) {
        Attribute galleryImages = properties.get("product.img_gallery_pair");
        List<Map<String, String>> extraImages = new ArrayList<Map<String, String>>();

        if (null == galleryImages) {
            return extraImages;
        }

        for (Object imagePair : galleryImages) {
            String imagePairStr = imagePair.toString();
            Map<String, String> images = new HashMap<String, String>();
            String[] pairs = imagePairStr.split("\\|");
            images.put(ImageSize.LARGE.toString(), pairs[0]);
            images.put(ImageSize.THUMBNAIL.toString(), pairs[1]);

            extraImages.add(images);
        }

        return extraImages;
    }
}
