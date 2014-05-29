(function (undefined) {
    var initTagsSelection = function (target) {
        V.CMS.ClickableTags.init(
            target,
            false,
            function (tag, allActive) {
                console.log('Selected ' + tag.text());
            },
            function (tag, allActive) {
                console.log('Deselected ' + tag.text());
            }
        );
    };

    var initSearch = function (searchInputId, searchResultTargetId) {
        $(searchInputId).keyup(function () {
            $(this).doTimeout('search-execution' + searchInputId, 250, function () {
                var query = this.val();
                if (query.length > 2) {
                    console.log('Searching for ' + query);
                    var $tagSelection = $(searchResultTargetId);
                    $tagSelection.empty();
                    $.get(this.attr('href'), {query: this.val()}).done(function (data) {
                        var tags = '';
                        $.each(data, function (index, value) {
                            tags += '<span id="' + value.id + '" class="label label-info tag">' + value.name + '</span>';
                        });
                        $tagSelection.append(tags);
                        initTagsSelection($tagSelection);
                    });
                }
            });
        });
    };

    initSearch('#search-parents', '#parent-tag-selection');
    initSearch('#search-children', '#children-tag-selection');

    var serialized = false;

    $('#update-relations').submit(function (event) {
        if (!serialized) {
            event.preventDefault();

            $('#add-parents').val($.map(V.CMS.ClickableTags.getAllActive($('#parent-tag-selection')),function (val, i) {
                return val.id
            }).join(','));
            $('#add-children').val($.map(V.CMS.ClickableTags.getAllActive($('#children-tag-selection')),function (val, i) {
                return val.id
            }).join(','));
            $('#delete-parents').val($.map($('#current-parents input:checkbox:checked'),function (val, it) {
                return val.id.split('_').pop()
            }).join(','));
            $('#delete-children').val($.map($('#current-children input:checkbox:checked'),function (val, it) {
                return val.id.split('_').pop()
            }).join(','));

            serialized = true;
            $(this).submit();
        }

    });

})();