(function(undefined){
    // allows to bind quick search action for parent tags
    function activateParentQuickSearch(form){
        // search for element
        console.log("bind quick search");
        var input = form.find('#parent-quick-search');
         // prepare trigger function
        var triggerSearch = function(){
            var value = input.val().toLowerCase();
            var parentTags = form.find('#parents .tags span');
            if (!!value){
                parentTags.each(function(){
                    var tag = $(this);
                    tag.text().toLowerCase().match(value) ? tag.show() : tag.hide();
                });
            } else {
                parentTags.show();
            }
        };
        // prepare timeout for search
        input.data('timeout', null).keyup(function(){
            clearTimeout(input.data('timeout'));
            input.data('timeout', setTimeout(triggerSearch, 500));
        });
    }
    // allows to bind quick search action for parent tags
    function activateSingleTagCloud(tagContainer, targetInputField){
        tagContainer.find('.tags span').hover(
            function(){
                var that = $(this);
                if (!that.hasClass('label-success')){
                    that.removeClass('label-info').addClass('label-warning');
                }
            },
            function(){
                var that = $(this);
                if (!that.hasClass('label-success')){
                    that.removeClass('label-warning').addClass('label-info');
                }
            }
        ).click(function(){
            var that = $(this);
            if (!that.hasClass('label-success')){
                var markedTag = tagContainer.find('.tags span.label-success');
                if (markedTag){
                    markedTag.removeClass('label-success').addClass('label-info');
                }
                targetInputField.val(that.attr('id'));
                that.removeClass('label-info').removeClass('label-warning').addClass('label-success');
            } else {
                targetInputField.val('');
                that.removeClass('label-success').addClass('label-warning');
            }
        });
    }
    // allows to bind quick search action for parent tags
    function activateMultipleTagCloud(tagContainer, targetInputField){
        var serializeSelected = function(){
            var listOfIds = tagContainer.find('.tags span.label-success').map(function() {
                return $(this).attr('id');
            }).get();
            var val = V.CMS.Ajax.serializeForSingleInputBinding(listOfIds);
            console.log(val);
            return val;
        }
        // prepare action on cloud
        tagContainer.find('.tags span').hover(
            function(){
                var that = $(this);
                if (!that.hasClass('label-success')){
                    that.removeClass('label-info').addClass('label-warning');
                }
            },
            function(){
                var that = $(this);
                if (!that.hasClass('label-success')){
                    that.removeClass('label-warning').addClass('label-info');
                }
            }
        ).click(function(){
            var that = $(this);
            if (!that.hasClass('label-success')){
                that.removeClass('label-info').removeClass('label-warning').addClass('label-success');
                targetInputField.val(serializeSelected());
            } else {
                that.removeClass('label-success').addClass('label-warning');
                targetInputField.val(serializeSelected());
            }
        });
    }
    // validate
    function validate(form){
        return !!form.find('input[name=strategy]:checked').val()
    }
    // bind hover on a row
    $('.to-review').hover(
        // on mouse over
        function () {
            // find basic elements
            var row = $(this);
            // mark row with color
            row.addClass('selected');
            // bind click on a row
            row.bind('click', function(){
                // remove click - we want to load form once and add loader class
                row.unbind('click');
                row.addClass('loader');
                // execute ajax call
                $.get(row.find('.name').attr('href'), function(data) {
                    // remove loader and show form
                    var container = row.removeClass('loader').find('.data').html(data).find('#container');
                    var form = container.find('form');
                    activateParentQuickSearch(form);
                    activateSingleTagCloud(form.find('#duplicates'), form.find('#duplicated-tag-id'));
                    activateMultipleTagCloud(form.find('#parents'), form.find('#parent-tag-ids'));
                    form.submit(function(){
                        // find all elements
                        var form = $(this);
                        var submitButton = form.find('button[type=submit]');
                        // validate
                        if (!validate(form)){
                            container.find('#error').text(V.CMS.I18n.get('vanity.cms.tags.review.selectOneStrategy')).show();
                            return false;
                        }
                        // hide submit button
                        submitButton.attr("disabled", "disabled");
                        // prepare post data
                        var postData = form.serialize();
                        console.log(postData);
                        // execute ajax call
                        $.post(form.attr('action'), postData, function(data) {
                            // we reload page - error message is set up in flash
                            if (V.CMS.Ajax.isSuccessResponse(data)){
                                location.reload();
                            } else {
                                container.find('#error').empty().html(V.CMS.Ajax.deserializeErrors(data)).show();
                            }
                        }, 'json');
                        return false;
                    });
                });
            });
        },
        // on mouse out
        function () {
            // unbind click, remove all classes and remove form
            $(this).removeClass('selected loader').find('.data').empty();
            $(this).find('.name span').unbind('click');
        }
    );
})();