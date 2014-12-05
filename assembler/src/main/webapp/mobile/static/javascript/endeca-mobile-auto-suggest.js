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

//Search Suggestion Module, specific for typeahead dimension search, implemented as a jQuery Plugin 
//and extended from jQuery autocomplete function. For more information refer to jQuery
//Autocomplete API.
(
    function($j)
    {
        //Override jQuery renderItemFunction function to allow html tags
        var renderItemFunction = function(ul, item) {
            return jQuery( "<li></li>" ).
                data( "item.autocomplete", item ).
                append( jQuery( "<a></a>" ).html( item.label )).
                appendTo( ul );
        };


        //Modify how results are rendered to show images and dimension titles
        $.widget("custom.customAutocomplete", $.ui.autocomplete, {
            _renderMenu: function( ul, items ) {
                var self = this,
                    currentCategory = "";

                if(window.hasOwnProperty('Endeca') && Endeca.Framework){
                    var firstItem = items != null && items.length > 0 ? items[0] : null;
                    if(firstItem){

                    if(firstItem.dimSearchName){
                        firstItem.dimSearchAuditInfo['ecr:name'] = firstItem.dimSearchName
                    }
                    ul.attr('data-oc-audit-info', JSON.stringify(firstItem.dimSearchAuditInfo));
                    Endeca.Framework.addItem(ul);
                    }
                }

                $.each( items, function( index, item ) {

                    var count = item.count == null ? '' : '&nbsp;('+item.count+')';

                    //Select a dimension and append results
                    if ( item.dimension != currentCategory ) {
                        ul.append( "<li class='ui-autocomplete-category'>" + item.dimension + "</li>" );
                        currentCategory = item.dimension;
                    }
                    //Render items
                    if(item.imageUrl != null){
                        item.label = "<div class='ui-autocomplete-table'> " +
                            "<div class='ui-autocomplete-table-row'>" +
                            "<div class='ui-autocomplete-table-image-cell'>" +
                            "<img src='" + item.imageUrl + "' class='img-thumbnail' />" +
                            "</div>" +
                            "<div class='ui-autocomplete-table-text-cell'>" +
                            item.label + count
                        "</div>" +
                        "</div></div>"
                    }else{
                        item.label = "<div class='ui-autocomplete-table'> " +
                            "<div class='ui-autocomplete-table-row'>" +
                            "<div class='ui-autocomplete-table-text-cell' style=''>" +
                            item.label + count
                        "</div></div></div>"
                    }
                    self._renderItem = renderItemFunction( ul, item );
                });
            }
        });

        /**
         * Blackberry OSS5 browsers do not have the trim function
         */

        function trim12 (str) {
            var	str = str.replace(/^\s\s*/, ''),
                ws = /\s/,
                i = str.length;
            while (ws.test(str.charAt(--i)));
            return str.slice(0, i + 1);
        }

        function printArrayProperty (array, property)
        {
            var parameter = '';
            for(var i=0; i<array.length; i++) {
                var path = encodeURIComponent(array[i]);
                parameter += '&' + property + '=' + path;
            }
            return parameter;
        };

        /**
         * Send Ajax to backend service to request data
         */
        var requestData = function(request, responseCallback, opts) {
            var term = escape(trim12(unescape(request.term)))
            var serviceUrl = opts.autoSuggestServiceUrl;
            if(opts.autoSuggestServiceUrl.indexOf('?') != -1){
                serviceUrl = opts.autoSuggestServiceUrl + '&Dy=1';
            } else {
                serviceUrl = opts.autoSuggestServiceUrl + '?Dy=1';
            }

            serviceUrl += printArrayProperty(opts.contentPaths, "contentPaths");
            if (opts.templateTypes.length > 0) {
                serviceUrl += printArrayProperty(opts.templateTypes, "templateTypes");
            }
            if (opts.templateIds.length > 0) {
                serviceUrl += printArrayProperty(opts.templateIds, "templateIds");
            }
            serviceUrl += '&Ntt=' + term + '*';


            $.getJSON(serviceUrl,
                function(data) {
                    var autoSuggestCartridges = data.contents[0].autoSuggest;
                    var dimSearchResult = null;
                    var results = new Array();
                    //Check if there is data to suggest
                    if (autoSuggestCartridges != null && autoSuggestCartridges.length > 0){
                        //find the dim search result in the cartridge list,
                        //considering one cartridge for auto-suggest dimension search.
                        for (var i = 0; i < autoSuggestCartridges.length; i++){
                            var cartridge = autoSuggestCartridges[i];
                            if(cartridge['@type'] == "DimensionSearchAutoSuggestItem"){
                                //find dim search result
                                dimSearchResult = cartridge;
                                break;
                            }
                        }
                    }

                    var dimSearchAuditInfo = null;

                    if(dimSearchResult['endeca:auditInfo']){
                        dimSearchAuditInfo = dimSearchResult['endeca:auditInfo'];
                    }

                    //Ensure that there are groups in the dimension search
                    if(dimSearchResult != null && dimSearchResult.dimensionSearchGroups.length > 0){
                        var dimSearchResultGroups = dimSearchResult.dimensionSearchGroups
                        var title = dimSearchResult.title;
                        //Go through every group of the dimension search
                        for (i = 0; i < dimSearchResultGroups.length; i++)
                        {
                            var dimSearchResults = dimSearchResultGroups[i].dimensionSearchValues
                            var currentDimension = null;

                            for(var j = 0; j < dimSearchResults.length; j++){
                                var imageUrl = null

                                if(dimSearchResult.displayImage == true)
                                {
                                    imageUrl = dimSearchResults[j].properties['img_url_thumbnail'] == null ? opts.defaultImage : dimSearchResults[j].properties['img_url_thumbnail'];
                                }

                                //Highlight search term
                                var label = dimSearchResults[j].label;
                                var regExp = new RegExp(unescape(term),'i')
                                var unmodifiedLabel = label.match(regExp);
                                var refinementCount = dimSearchResults[j].count;
                                label =  label.replace(regExp, " <span class='ui-autocomplete-highlight'>" + unmodifiedLabel + "</span>");
                                //Add object to the results that will be shown in
                                //the suggestion box.
                                results.push(
                                    {
                                        label : label,
                                        value: dimSearchResults[j].label,
                                        action: dimSearchResults[j].contentPath + dimSearchResults[j].navigationState,
                                        dimension : dimSearchResultGroups[i].displayName,
                                        imageUrl : imageUrl,
                                        title: title,
                                        count: refinementCount,
                                        dimSearchAuditInfo : dimSearchAuditInfo,
                                        dimSearchName: dimSearchResult['name']
                                    })
                            }
                        }
                    }

                    responseCallback(results);
                }
            );
        }

        /**
         * Main function to enable the search suggestion to the selected element.
         */
        $j.fn.endecaSearchSuggest = function(options)
        {
            var opts = $j.extend({}, $j.fn.endecaSearchSuggest.defaults, options);
            this.each(
                function()
                {
                    $j(this).customAutocomplete({
                        delay: opts.delay,
                        minLength: opts.minAutoSuggestInputLength,
                        source: function(request, responseCallback){
                            requestData(request, responseCallback, opts)
                        },
                        select: function( event, ui ) {
                            //here
                            window.location = opts.searchUrl + ui.item.action;
                        },
                        open: function( event, ui ) {
                            $j(".ui-autocomplete").css("left", "0px");
                            $j("ul.ui-autocomplete li:last-child").css("border-bottom","2px solid grey");
                        },

                        change: function(event, ui) {
                            if(window.hasOwnProperty('Endeca') && Endeca.Framework){
                                $j('ul.ui-autocomplete[data-oc-audit-info]').each(function(idx, el) {
                                    Endeca.Framework.removeItem(el);
                                });
                            }
                        }
                    });
                }
            );
        }

        /**
         * Default settings for the search suggestion.
         */
        $j.fn.endecaSearchSuggest.defaults = {
            minAutoSuggestInputLength: 3,
            delay: 150,
            autoSuggestDimUrl: '',
            autoSuggestServiceUrl: '',
            contentPaths: [],
            templateTypes: [],
            templateIds: [],
            searchUrl: '',
            defaultImage:'no_image.gif'
        }
    }
    )(jQuery);
