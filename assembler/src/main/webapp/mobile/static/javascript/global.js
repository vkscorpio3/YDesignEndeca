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
if (typeof(Endeca) == "undefined") Endeca = {};  

if (typeof(Endeca.Menu) == "undefined") Endeca.Menu = {

	hideMore: function() {
		var menu = document.getElementById("moreMenu");
		if(menu) {
			var menuLink = document.getElementById("moreMenuLink");
			menu.style.display = "none";
			menuLink.innerHTML = "more <small>▼</small>";
		}
	},
		
	toggleMore: function() {
		var menu = document.getElementById("moreMenu");
		var menuLink = document.getElementById("moreMenuLink");
		if(menu.style.display == "none") {
			menu.style.display = "block";
			menuLink.innerHTML = "more <small>▲</small>";
		} else {
			Endeca.Menu.hideMore();
		}
		
	}
};

// Function that hides the URL
// addEventListener not supported on IE 8 and below.
if (window.addEventListener !== undefined) {
    window.addEventListener("load",
        function () {
            window.scrollTo(0, 1);
        }, false);
}
else if (window.attachEvent !== undefined) {
    window.attachEvent("onload",
        function () {
            window.scrollTo(0, 1);
        });
}

//Function that flips between the records of a horizontal spotlight back and forth (desktop)
$(document).ready(function(){
	
	$('div.spotlightProductAttributes').hide();
	// This value decides the number of records displayed in the spotlight at a time
	var numOfDisplayedRecords = 1;

	$('div.mobileSpotlightMain').each( function() {
		// This line gets the div id of the current 'div.horizontal-spotlight-table'
		var id = $(this).attr("id");
		
		var firstVisibleProduct = $('div[id='+id+']').find("div.spotlightProductAttributes").eq(0);

		// The index is initially equal to 0
		var index = firstVisibleProduct.prevAll().length - 1;
		
		// Displaying the first 'numOfDisplayedRecords' records
		for (var g = 0; g < numOfDisplayedRecords; g++) {
			$('div[id='+id+']').find("div.spotlightProductAttributes").eq(g).show();
		}

		// This is the total number of records
		var numOfRecords = $('div[id='+id+']').find("div.spotlightProductAttributes").length;

		if (numOfDisplayedRecords > numOfRecords) {
			numOfDisplayedRecords = numOfRecords;
		}
		
		// If the total number of records is <= what is configured to be displayed on the spotlight, then there is no need for previous and next arrows
		if (numOfRecords <= numOfDisplayedRecords) {
			$('div[id='+id+']').find('div.prevArrow').hide();	
			$('div[id='+id+']').find('div.nextArrow').hide();	
		}
		
		$('div[id='+id+']').find("#"+id+"_nextLink").click(function(event) {
		
			event.preventDefault();
			// This is the div id of the evantual 'horizontal-spotlight-table' parent of the button
			var clickerId = $(this).parents('div.mobileSpotlightMain').attr("id");
			var recordsNum = $('div[id='+clickerId+']').find("div.spotlightProductAttributes").length;

			var lastDisplayedRecord;

			for (var i = 0; i < recordsNum; i++) {
			
				// Look for the first current visible record
				if ($('div[id='+clickerId+']').find("div.spotlightProductAttributes").eq(i).is(':visible')) {
					
					// Hide as many records from the screens as 'numOfDisplayedRecords' is starting with the first visible one
					for (var j = 0; j < numOfDisplayedRecords; j++) {
						if ((i+j) >= recordsNum) {
							break;
						}
						$('div[id='+clickerId+']').find("div.spotlightProductAttributes").eq(i+j).hide();
						lastDisplayedRecord = i+j;
					}
					
					// Wrap around to the start if we reached the last record
					if (lastDisplayedRecord+1 >= recordsNum) {
						lastDisplayedRecord = -1;
					}
					
					// Starting with one after the last displayed record, display 'numOfDisplayedRecords' records
					for (var k = lastDisplayedRecord + 1; k <= lastDisplayedRecord + numOfDisplayedRecords; k++) {
						$('div[id='+clickerId+']').find("div.spotlightProductAttributes").eq(k).fadeIn(100);
					}
					
					break;
				}
			}
			
		});
		
		$('div[id='+id+']').find("#"+id+"_prevLink").click(function(event) {
		
			event.preventDefault();
			// This is the div id of the evantual 'horizontal-spotlight-table' parent of the button
			var clickerId = $(this).parents('div.mobileSpotlightMain').attr("id");
			var recordsNum = $('div[id='+clickerId+']').find("div.spotlightProductAttributes").length;

			var lastDisplayedRecord;
			var recordToDisplayNext;

			for (var i = 0; i < recordsNum; i++) {
			
				// Look for the first current visible record
				if ($('div[id='+clickerId+']').find("div.spotlightProductAttributes").eq(i).is(':visible')) {
					
					// Hide as many records from the screens as 'numOfDisplayedRecords' is starting with the first visible one
					for (var j = 0; j < numOfDisplayedRecords; j++) {
						if ((i+j) >= recordsNum) {
							break;
						}
						$('div[id='+clickerId+']').find("div.spotlightProductAttributes").eq(i + j).hide();
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
						$('div[id='+clickerId+']').find("div.spotlightProductAttributes").eq(k).fadeIn(100);
					}
					
					break;
				}
			}

		});

	});

});



