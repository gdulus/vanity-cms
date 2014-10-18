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
        V.CMS.ClickableTags.init(
            tagContainer,
            true,
            function (tag) {
                targetInputField.val(tag.attr('id'));
            },
            function () {
                targetInputField.val('');
            }
        );
    }

    // allows to bind quick search action for parent tags
    function activateMultipleTagCloud(tagContainer, targetInputField) {
        var serializeSelected = function (allSelected) {
            var listOfIds = allSelected.map(function () {
                return $(this).attr('id');
            }).get();
            var val = V.CMS.Ajax.serializeForSingleInputBinding(listOfIds);
            console.log(val);
            return val;
        }
        // prepare action on cloud
        V.CMS.ClickableTags.init(
            tagContainer,
            false,
            function (current, allSelected) {
                targetInputField.val(serializeSelected(allSelected));
            },
            function (current, allSelected) {
                targetInputField.val(serializeSelected(allSelected));
            }
        );
    }

    var form = $('#review-form');
    activateParentQuickSearch(form);
    activateSingleTagCloud(form.find('#duplicates'), form.find('#duplicated-tag-id'));
    activateMultipleTagCloud(form.find('#parents'), form.find('#parent-tag-ids'));
})();

(function () {
    $('.submit').click(function () {
        $('#strategy').val($(this).attr('id'));
        var ids = $('.selection-slave:checked').serialize();
        if (ids) {
            $('#serializedTagIds').val(ids.replace(/tags\./g, '').replace(/=on/g, '').replace(/&/g, ','));
        }
    })
})();