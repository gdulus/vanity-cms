(function(undefined){
    // bind hover on a row
    $('.to-review').hover(
        // on mouse over
        function () {
            // find basic elements
            var that = $(this);
            var button = that.find('.name span')
            // mark row with color
            that.addClass('selected');
            // bind button click
            button.bind('click', function(){
                // remove click - we want to load form once and add loader class
                button.unbind('click');
                that.addClass('loader');
                // execute ajax call
                $.get(that.find('.name').attr('href'), function(data) {
                    // remove loader and show form
                    that.removeClass('loader').find('.data').html(data).find('form').submit(function(){
                        // find all elements
                        var form = $(this);
                        var submitButton = form.find('button[type=submit]');
                        // hide submit button
                        submitButton.hide();
                        // prepare post data
                        var postData = {
                            'id':form.find('#tag-id').val(),
                            'parentTag':form.find('#parent-task').is(':checked')
                        };
                        // execute ajax call
                        $.post(form.attr('action'), postData, function(data) {
                            location.reload();
                            submitButton.show();
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