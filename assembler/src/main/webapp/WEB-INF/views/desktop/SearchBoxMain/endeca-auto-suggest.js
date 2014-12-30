

/**
 * Copyright (C) 2011 Endeca Technologies, Inc.
 *
 * The use of the source code in this file is subject to the ENDECA
 * TECHNOLOGIES, INC. SOFTWARE TOOLS LICENSE AGREEMENT. The full text of the
 * license agreement can be found in the ENDECA INFORMATION ACCESS PLATFORM
 * THIRD-PARTY SOFTWARE USAGE AND LICENSES document included with this software
 * distribution.
 */

//Search Suggestion Module, specific for typeahead dimension search, implemented as a jQuery Plugin

(
function($j)
{
    /**
     *Constructor,
     *@param $ele the Element to enable Dim Search Suggestion
     *@param opts the options to be applied
     */
    $j.EndecaSearchSuggestor = function(ele, opts)
    {
        var me = this;
        this._active = true;
        this._options = opts;
        this._lastValue = '';
        this._element = ele;
        this._container = $j('<div class="' + this._options.containerClass + '"></>');
        this._timeOutId;
        this._hideTimeOutId;
        this._selectedIndex = -1;
        
        var suggestor = this;
        
        //append the container to the current page
        $j("body").append(this._container);
        
        /**
         *Capture the keyboard event and dispatch to corresponding handlers. 
         */
        ele.keydown(
                function(e) 
                {
                    switch(e.keyCode) 
                    {
                        case 38: //up, select the previous item
                        {
                            if (suggestor._active) 
                            {
                                suggestor.moveToPrev();
                            } 
                            else 
                            {
                                suggestor.show();
                            }
                            break;
                        }
                        case 40: //down, select the next item
                        {
                            if (suggestor._active) 
                            {
                                suggestor.moveToNext();
                            } 
                            else 
                            {
                                suggestor.show();
                            }
                            break;
                        }
                        case 9: //tab, hide the box
                        {
                            suggestor.hide();
                            break;
                        }
                        case 13: //return, select the highlighted item
                        {
                            if (suggestor._active && suggestor._selectedIndex != -1) 
                            {
                                e.preventDefault();
                                suggestor.selectItem();
                                return false;
                            }
                            break;
                        }
                        case 27: // escape, hide the box
                        {
                            if (suggestor._active) 
                            {
                                suggestor.hide();
                            }
                            break;
                        }
                        default:
                        {
                            //other keys, handle the dim search
                            suggestor.handleRequest();
                        }
                    }
                });
        
        //hide box when lost focus
        ele.blur(
                function(e)
                {
                    var dimContainer = $j(me._container);
                    if(!dimContainer.is(':hover')) {
                        var hideFunction = function() { suggestor.hide();};
                        suggestor._hideTimeOutId = setTimeout(hideFunction, 200);
                    }
                }
        );
    };
    
    
    /**
     * Move the focus to and highlight the next result item when user types
     * arrow up key.
     */
    $j.EndecaSearchSuggestor.prototype.moveToPrev = function() 
    {
        if(this._selectedIndex == -1)
        {
            this._selectedIndex = 0;
        }
        else
        {
            if(this._selectedIndex == 0)
            {
                //reach the first one
                return;
            }
            this._selectedIndex--;
        }
        $j(".dimResult", this._container).removeClass("selected");
        $j($j(".dimResult", this._container).get(this._selectedIndex)).addClass("selected");	 
    };
    
    /**
     * Move the focus to and highlight the previous result item when user types
     * arrow down key.
     */
    $j.EndecaSearchSuggestor.prototype.moveToNext = function() 
    {
        if(this._selectedIndex == -1)
        {
            this._selectedIndex = 0;
        }
        else
        {
            if(this._selectedIndex == $j(".dimResult", this._container).size() - 1)
            {  
                //rearch the last one
                return;
            }
            this._selectedIndex++;
            
        }
        
        $j(".dimResult", this._container).removeClass("selected");
        $j($j(".dimResult", this._container).get(this._selectedIndex)).addClass("selected");
    };
    
    /**
     * Select the highlighted item when user click or type enter key
     */
    $j.EndecaSearchSuggestor.prototype.selectItem = function() 
    {
        if(this._selectedIndex == -1)
        {
            return;
        }
        
        var url = $j("a", $j(".dimResult", this._container).get(this._selectedIndex)).attr("href");
        document.location.href = url;
    };
    
    /**
     * Hide the search suggestion box
     */
    $j.EndecaSearchSuggestor.prototype.hide = function() 
    {
        this._container.hide();
            this._removeFromAudit();
        this._active = false;
    };
    
    /**
     * Show the search suggestion box
     */
    $j.EndecaSearchSuggestor.prototype.show = function() 
    {
        if(this._container.is(":hidden"))
        {
            this.setPosition();
            this._addToAudit();
            this._container.show();
            this._active = true;
            this._selectedIndex = -1;
        }
    };
    
    /**
     * Activate the search suggestion box.
     */
    $j.EndecaSearchSuggestor.prototype.handleRequest = function() 
    {
        var suggestor = this;
        
        var callback = function()
        { 
            var text = $j.trim(suggestor._element.val());
            if(text != suggestor._lastValue)
            {
                if(text.length >= suggestor._options.minAutoSuggestInputLength)
                { 
                    suggestor.requestData();
                }
                else
                {
                    suggestor.hide();
                } 
            }
            suggestor._lastValue = text;
        };
        
        if(this._timeOutId)
        {
            clearTimeout(this._timeOutId);
        }
        this._timeOutId = setTimeout(callback, this._options.delay);
    };
    
    /**
     * Send Ajax to backend service to request data
     */
    $j.EndecaSearchSuggestor.prototype.requestData = function() 
    {
        var suggestor = this;
        var response = $j.ajax(
                {
                    url:suggestor.composeUrl(),
                    dataType:'json',
                    async:true,
                    success:function(data){
                        suggestor.showSearchResult(data);
                    }
                }
        );
    };
        
    $j.EndecaSearchSuggestor.prototype.printArrayProperty = function(array, property)
    {
        var parameter = '';
        for(var i=0; i<array.length; i++) {
            var path = encodeURIComponent(array[i]);
            parameter += '&' + property + '=' + path;
        }
        return parameter;
    };
    
    /**
     * Search suggestion is search term sensitive. So it will take the search
     * term applied on current page and add it into the Ajax request url.
     */
    $j.EndecaSearchSuggestor.prototype.composeUrl = function()
    {
        var url = this._options.autoSuggestServiceUrl;
        
        var searchTerm = $j.trim(this._element.val());
        
        
        if (url.indexOf('?') == -1)
        {
        	url += '?Dy=1';
        }
        else
        {
        	url += '&Dy=1';
        }
        
        url += this.printArrayProperty(this._options.contentPaths, "contentPaths");
        if (this._options.templateTypes.length > 0) {
            url += this.printArrayProperty(this._options.templateTypes, "templateTypes");
        }
        if (this._options.templateIds.length > 0) {
            url += this.printArrayProperty(this._options.templateIds, "templateIds");
        }
        url += '&Ntt=' + searchTerm + '*';
        
        return url;
    };
    
    /**
     * Show the search results in the suggestion box
     */
    $j.EndecaSearchSuggestor.prototype.showSearchResult = function(data) 
    {
        var htmlResult = this.processSearchResult(data);
        if(htmlResult != null)
        {
            this._removeFromAudit();
            this._container.html(htmlResult);
            this._addToAudit();
            this.bindEventHandler();
            this.show();
        }
        else
        {
            //hide the result box if there is no result
            this.hide();
        }
    };
    
        $j.EndecaSearchSuggestor.prototype._addToAudit = function() {
            if(window.hasOwnProperty('Endeca') && Endeca.Framework){
                this._container.find('[data-oc-audit-info]').each(function(idx, el) {
                        Endeca.Framework.addItem(el);
                });
            }
        };

        $j.EndecaSearchSuggestor.prototype._removeFromAudit = function() {
            if(window.hasOwnProperty('Endeca') && Endeca.Framework){
                this._container.find('[data-oc-audit-info]').each(function(idx, el) {
                    Endeca.Framework.removeItem(el);
                });
            }
        };

    /**
     * Generate rendering HTML according to data
     */
    $j.EndecaSearchSuggestor.prototype.processSearchResult = function(data) 
    {
        var dimSearchResult = null;
        
        var autoSuggestCartridges = data.contents[0].autoSuggest;
        
        //if no data returned, returns null
        if(autoSuggestCartridges == null || autoSuggestCartridges.length == 0)
        {
            return null;
        }
        
        //find the dim search result in the cartridge list, only consider one cartridge
        //for auto-suggest dimension search.
        for(var j = 0; j < autoSuggestCartridges.length; j++)
        {
            var cartridge = autoSuggestCartridges[j];
            
            if(cartridge['@type'] == "DimensionSearchAutoSuggestItem")
            {
                //find dim search result
                dimSearchResult = cartridge;
                break;
            }
        }
        
        if (dimSearchResult != null)
        {
            var slot = this.buildAuditDiv(data);
            var item = this.generateHtmlContent(dimSearchResult);
            return slot.append(item);
        }
        return null;
    };
    
    $j.EndecaSearchSuggestor.prototype.buildAuditDiv = function(data) {
        var $div = $j('<div/>');
        var auditInfo = null;
        if(data['contents'] && data['contents'].length > 0){
            auditInfo = data['contents'][0]['endeca:auditInfo'];
            auditInfo['ecr:name'] = data['contents'][0]['name'];
        }else if(data['endeca:auditInfo']){
            auditInfo = data['endeca:auditInfo'];
            auditInfo['ecr:name'] = data['name'];
        }

        if (auditInfo) {
            $div.attr('data-oc-audit-info', JSON.stringify(auditInfo));
        }
        return $div;
    };

    $j.EndecaSearchSuggestor.prototype.generateHtmlContent = function(dimSearchResult) 
    {
        var newContent = null;
        
        //Contains dimension search results
        if(dimSearchResult != null && dimSearchResult.dimensionSearchGroups.length > 0)
        {
            newContent = this.buildAuditDiv(dimSearchResult);
            
            //add title if it is not empty
            if(dimSearchResult.title && $j.trim(dimSearchResult.title) != "")
            {
                var encodedTitle = $j('<div/>').text(dimSearchResult.title).html();
                newContent.append('<div class="title">'+ encodedTitle + '</div>');
            }
            
            var dimSearchGroupList = dimSearchResult.dimensionSearchGroups;
            
            for(var i = 0; i < dimSearchGroupList.length; i++)
            {
                var dimResultGroup = dimSearchGroupList[i];

                //output dimension name here
                
                var displayName = dimResultGroup.displayName;
                
                newContent.append('<div class="dimRoots">' + displayName + '</div>');
                
                //output dim result of this group here
                for(var j = 0; j < dimResultGroup.dimensionSearchValues.length; j++)
                { 
                    var dimResult = dimResultGroup.dimensionSearchValues[j];
                    var action;
					if(dimResult.siteState.matchedUrlPattern != null)
						action = dimResult.siteState.matchedUrlPattern + dimResult.contentPath + dimResult.navigationState;
					else
						action = dimResult.contentPath + dimResult.navigationState;
                    var text = dimResult.label;
                    var ancestors = dimResult.ancestors;
                    var count = dimResult.count == null ? '' : '&nbsp;('+dimResult.count+')';
                    
                    var ancestorsStr = "";
                    if(ancestors != null && ancestors.length > 0)
                    {
                        for(var n = 0; n < ancestors.length; n++)
                        {
                            ancestorsStr += ancestors[n].label + " > ";
                        }
                    }
                    
                    if(dimSearchResult.displayImage)
                    {
                        var imageUrl = "";
						
                        if($j.trim(dimResult.properties.img_url_thumbnail) != '')
                        {
                            imageUrl = dimResult.properties.img_url_thumbnail;
                        }
                        else
                        {
                            imageUrl = this._options.defaultImage;
                        }
                        
                        //only show image when image is enabled and url is not null
                        newContent.append('<div class="dimResult"><table><tr><td width="36"><img src="' 
                                + imageUrl + '"/></td><td><div class="link"><a href="' + this._options.searchUrl + action + '">' 
                                + ancestorsStr + this.highlightMatched(text) + '</a>'+count+'<div></td></tr></table></div>'); 	 
                    }
                    else
                    {
                        newContent.append('<div class="dimResult"><div class="link"><a href="' + this._options.searchUrl + action + '">' 
                                + ancestorsStr + this.highlightMatched(text) + '</a>' +count+ '<div></div>');
                    }
                }
            }
        }
        
        //has result, return the generated html 
        if(newContent != null)
        {
            return newContent[0];
        }
        
        return null;
    };
    
    /**
     * Highlight the matched text in result item.
     */
    $j.EndecaSearchSuggestor.prototype.highlightMatched = function(text)
    {
        var inputText = $j.trim(this._element.val()).toLowerCase();
        var highlighted = text.toLowerCase();
        if(highlighted.indexOf(inputText) != -1)
        {
            var index = highlighted.indexOf(inputText);
            var prefix = text.substring(0, index);
            var suffix = text.substring(index + inputText.length);
            inputText = text.substr(index, inputText.length);
            highlighted = prefix + '<span>' + inputText + '</span>' + suffix;
        }
        return highlighted;
    };
    
    /**
     * Bind event handlers for the links and divs in the box
     */
    $j.EndecaSearchSuggestor.prototype.bindEventHandler = function()
    {
        var suggestor = this;
        
        //change CSS class when mouseover on result item
        $j(".dimResult", this._container).mouseover(
                function(e)
                {
                    $j(".dimResult", suggestor._container).removeClass("selected");
                    $j(this).addClass("selected");
                    suggestor._selectedIndex = $j(".dimResult", suggestor._container).index($j(this));
                }
        );
        
        //select the result item when user lick on it
        $j(".dimResult", this._container).click(
                function(e)
                {
                    suggestor.selectItem();
                }
        );
        
        //select the result item when user lick on it
        $j("a", $j(".dimResult", this._container)).click(
                function(e)
                {
                    e.preventDefault();
                    suggestor.selectItem();
                }
        );
        
        //Dim roots are not link, when click, move the focus back to input box
        $j(".dimRoots", this._container).click(
                function()
                {
                    clearTimeout(suggestor._hideTimeOutId);
                    suggestor._element.focus();
                }
        );
    };
    
    /**
     * Set the search suggestion box position
     */
    $j.EndecaSearchSuggestor.prototype.setPosition = function()
    {
        var offset = this._element.offset();
        this._container.css({
            top: offset.top + this._element.outerHeight(),
            left: offset.left,
            width: this._element.width()
        });
    };
    
    /**
     * Main function to enable the search suggestion to the selected element.
     */
    $j.fn.endecaSearchSuggest = function(options)
    {
        var opts = $j.extend({}, $j.fn.endecaSearchSuggest.defaults, options);
        this.each(
                function()
                {
                    var element = $j(this);
                    new $j.EndecaSearchSuggestor(element, opts);
                }
        ); 
    };
    
    /**
     * Default settings for the search suggestion.
     */
    $j.fn.endecaSearchSuggest.defaults = {
            minAutoSuggestInputLength: 3,
            displayImage: false,
            delay: 250,
            autoSuggestServiceUrl: '',
            contentPaths: [],
            templateTypes: [],
            templateIds: [],
            searchUrl: '',
            containerClass: 'dimSearchSuggContainer',
            defaultImage:'no_image.gif'
    };
} 
)(jQuery);
