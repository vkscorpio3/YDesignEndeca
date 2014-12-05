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

    window.onload = function(){
       var imageContainerWidth = $('#galleryContainer').outerWidth();
       $('#galleryContainer').css("padding-left",(imageContainerWidth - 338)*0.5);
       var oImages = $('div.imgThumbDivCont .productSmallImage');
       var preBtn = $('#preImg');
       var nextBtn = $('#nextImg');
       var numOfDisplayedRecords =4;
       var imgLen = oImages.length;
        if(imgLen<5){
           oImages.show();
        }else{
           nextBtn.show();
           preBtn.show();
           preBtn.bind('click',function(event){
                event.preventDefault();
            // This is the div id of the evantual 'HorizontalSpotlightTable' parent of the button
            var clickerId = $(this).parents('div.imgThumbDivCont').attr("id");
            var recordsNum = $('div[id='+clickerId+']').find("div.productSmallImage").length;

            var lastDisplayedRecord;
            var recordToDisplayNext;

            for (var i = 0; i < recordsNum; i++) {
            
                // Look for the first current visible record
                if ($('div[id='+clickerId+']').find("div.productSmallImage").eq(i).is(':visible')) {
                    
                    // Hide as many records from the screens as 'numOfDisplayedRecords' is starting with the first visible one
                    for (var j = 0; j < numOfDisplayedRecords; j++) {
                        if ((i+j) >= recordsNum) {
                            break;
                        }
                        $('div[id='+clickerId+']').find("div.productSmallImage").eq(i + j).hide();
                        lastDisplayedRecord = i + j;
                    }
                    
                    // Handle wrap-around to the end if we are at the start and 'previous' got clicked again
                    if (lastDisplayedRecord - numOfDisplayedRecords < 0) {
                        if (recordsNum % numOfDisplayedRecords == 0) {
                            recordToDisplayNext = recordsNum - numOfDisplayedRecords;
                        } else {
                            recordToDisplayNext = recordsNum - (recordsNum % numOfDisplayedRecords);
                        }
                    } else {
                        if ((lastDisplayedRecord + 1) % numOfDisplayedRecords == 0) {
                            recordToDisplayNext = lastDisplayedRecord - (2 * numOfDisplayedRecords) + 1;
                        } else {
                            recordToDisplayNext = lastDisplayedRecord - ((recordsNum % numOfDisplayedRecords) + numOfDisplayedRecords) + 1;
                        }
                    }
                    
                    // Starting with 'recordToDisplayNext', display 'numOfDisplayedRecords' records
                    for (var k = recordToDisplayNext; k < recordToDisplayNext + numOfDisplayedRecords; k++) {
                        $('div[id='+clickerId+']').find("div.productSmallImage").eq(k).fadeIn(100);
                    }
                    
                    break;
                }
            }
           });
           nextBtn.bind('click',function(event){
              event.preventDefault();
            
            var clickerId = $(this).parents('div.imgThumbDivCont').attr("id");
            var recordsNum = $('div[id='+clickerId+']').find("div.productSmallImage").length;

            var lastDisplayedRecord;

            for (var i = 0; i < recordsNum; i++) {
            
                
                if ($('div[id='+clickerId+']').find("div.productSmallImage").eq(i).is(':visible')) {
                    
                    // Hide as many records from the screens as 'numOfDisplayedRecords' is starting with the first visible one
                    for (var j = 0; j < numOfDisplayedRecords; j++) {
                        if ((i+j) >= recordsNum) {
                            break;
                        }
                        $('div[id='+clickerId+']').find("div.productSmallImage").eq(i+j).hide();
                        lastDisplayedRecord = i+j;
                    }
                    
                    // Wrap around to the start if we reached the last record
                    if (lastDisplayedRecord+1 >= recordsNum) {
                        lastDisplayedRecord = -1;
                    }
                    
                    // Starting with one after the last displayed record, display 'numOfDisplayedRecords' records
                    for (var k = lastDisplayedRecord + 1; k <= lastDisplayedRecord + numOfDisplayedRecords; k++) {
                        $('div[id='+clickerId+']').find("div.productSmallImage").eq(k).fadeIn(100);
                    }
                    
                    break;
                }
            }
            
           });
           for(var i=0;i<4;i++){
              oImages.eq(i).show();
            }
           
        }
        
        //bind event handlers for images
        oImages.each(function(index, element) {
            $(element).bind('mouseover',function(event) {
                $(this).css("border", "1px solid black");
            });
            $(element).bind('mouseout',function(event) {
                $(this).css("border", "1px solid white");
            });
        })
    }
    window.onresize = function() {
        var fourthWidth = Math.floor($(window).width() / 4);
        var fourthHeight = Math.floor($(window).height() / 4) -32;
        $('.productAttributesR').width(fourthWidth);
        $('.productAttributesR').height(fourthHeight);
        var imageContainerWidth = $('#galleryContainer').outerWidth();
        $('#galleryContainer').css("padding-left",(imageContainerWidth - 338)*0.5);
    };
    
        
    function prodImageChange(imgSrc){
        $('#mobileProductImage').removeClass('mobileProductImage');
        document['prodImage'].src = imgSrc;
        if(!document["prodImage"].complete){
            $('#mobileProductImage').addClass('productImageOut');
            document["prodImage"].onload = 
            function () { 
                $('#mobileProductImage').addClass('mobileProductImage');
                $('#mobileProductImage').removeClass('productImageOut'); 
            };
        }
        else{
            $('#mobileProductImage').addClass('mobileProductImage');
        }
    };
    function pushAddURL(addStr){
        var currUrl = window.parent.location.href;
        var newUrl = currUrl + addStr;
        if(window.parent.history.pushState){
            window.parent.history.pushState(null, this.title, newUrl);
        }
    };
    function replaceStringURL(removeStr,addStr){
        var currUrl = window.parent.location.href;
        var newUrl = currUrl.replace(removeStr, addStr);
        
        if(window.parent.history.pushState){
              window.parent.history.pushState(null, this.title, newUrl);
        }
    };