(function (undefined) {
    var initTagsSelection = function () {
        V.CMS.ClickableTags.init(
            $('#tag-selection'),
            false,
            function (tag) {
            },
            function (tag) {
            }
        );
    };

    $('#search').keyup(function () {
        $(this).doTimeout('search-execution', 250, function () {
            var query = this.val();
            if (query.length > 2) {
                console.log('Searching for ' + query);
                var $tagSelection = $('#tag-selection');
                $tagSelection.empty();
                $.get(this.attr('href'), {query: this.val()}).done(function (data) {
                    var tags = '';
                    $.each(data, function (index, value) {
                        tags += '<span id="' + value.id + '" class="label label-info tag">' + value.name + '</span>';
                    });
                    $tagSelection.append(tags);
                    initTagsSelection();
                });
            }
        });
    });
})();