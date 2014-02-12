(function (undefined) {
    // allows to bind quick search action for parent tags
    function activateParentQuickSearch(form) {
        // search for element
        console.log("bind quick search");
        var input = form.find('#parent-quick-search');
        // prepare trigger function
        var triggerSearch = function () {
            var value = input.val().toLowerCase();
            var parentTags = form.find('#parents .tags span');
            if (!!value) {
                parentTags.each(function () {
                    var tag = $(this);
                    tag.text().toLowerCase().match(value) ? tag.show() : tag.hide();
                });
            } else {
                parentTags.show();
            }
        };
        // prepare timeout for search
        input.data('timeout', null).keyup(function () {
            clearTimeout(input.data('timeout'));
            input.data('timeout', setTimeout(triggerSearch, 500));
        });
    }

    // allows to bind quick search action for parent tags
    function activateSingleTagCloud(tagContainer, targetInputField) {
        tagContainer.find('.tags span').hover(
            function () {
                var that = $(this);
                if (!that.hasClass('label-success')) {
                    that.removeClass('label-info').addClass('label-warning');
                }
            },
            function () {
                var that = $(this);
                if (!that.hasClass('label-success')) {
                    that.removeClass('label-warning').addClass('label-info');
                }
            }
        ).click(function () {
                var that = $(this);
                if (!that.hasClass('label-success')) {
                    var markedTag = tagContainer.find('.tags span.label-success');
                    if (markedTag) {
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
    function activateMultipleTagCloud(tagContainer, targetInputField) {
        var serializeSelected = function () {
            var listOfIds = tagContainer.find('.tags span.label-success').map(function () {
                return $(this).attr('id');
            }).get();
            var val = V.CMS.Ajax.serializeForSingleInputBinding(listOfIds);
            console.log(val);
            return val;
        }
        // prepare action on cloud
        tagContainer.find('.tags span').hover(
            function () {
                var that = $(this);
                if (!that.hasClass('label-success')) {
                    that.removeClass('label-info').addClass('label-warning');
                }
            },
            function () {
                var that = $(this);
                if (!that.hasClass('label-success')) {
                    that.removeClass('label-warning').addClass('label-info');
                }
            }
        ).click(function () {
                var that = $(this);
                if (!that.hasClass('label-success')) {
                    that.removeClass('label-info').removeClass('label-warning').addClass('label-success');
                    targetInputField.val(serializeSelected());
                } else {
                    that.removeClass('label-success').addClass('label-warning');
                    targetInputField.val(serializeSelected());
                }
            });
    }

    var form = $('#review-form');
    activateParentQuickSearch(form);
    activateSingleTagCloud(form.find('#duplicates'), form.find('#duplicated-tag-id'));
    activateMultipleTagCloud(form.find('#parents'), form.find('#parent-tag-ids'));
})();