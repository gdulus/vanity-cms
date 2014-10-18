var V = V || {};
V.CMS = V.CMS || {};

V.CMS.ClickableTags = (function () {
    return {
        getAllActive: function (tagContainer) {
            return tagContainer.find('span.label-success');
        },
        init: function (tagContainer, unique, onSelect, onDeselect) {
            tagContainer.find('span').hover(
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
                        if (unique) {
                            var markedTag = tagContainer.find('span.label-success');
                            if (markedTag) {
                                markedTag.removeClass('label-success').addClass('label-info');
                            }
                        }

                        that.removeClass('label-info').removeClass('label-warning').addClass('label-success');

                        if (onSelect) {
                            onSelect(that, tagContainer.find('span.label-success'));
                        }
                    } else {
                        that.removeClass('label-success').addClass('label-warning');

                        if (onDeselect) {
                            onDeselect(that, tagContainer.find('span.label-success'));
                        }
                    }
                }
            );
        }
    }
}());

V.CMS.Ajax = {
    serializeForSingleInputBinding: function (elements) {
        if (!(elements && $.isArray(elements))) {
            return '';
        }

        return elements.join(' ');
    },

    isSuccessResponse: function (data) {
        return data.status == 'success'
    },

    deserializeErrors: function (data) {
        if (this.isSuccessResponse(data) || !data.errors) {
            return '';
        }

        var result = [V.CMS.I18n.get('vanity.cms.save.error')];
        $.each(data.errors, function (key, value) {
            result.push(key + " " + V.CMS.I18n.get(value));
        });
        return result.join('<br />');
    }
};

V.CMS.I18n = (function () {

    var messages = {
        'vanity.cms.save.error': 'Errors while saving data',
        'vanity.cms.confirm': 'Are you sure?',
        'vanity.cms.tags.review.selectOneStrategy': 'Please select at last one strategy'
    }

    return {
        get: function (code) {
            return messages[code] || code
        }
    }
})();

V.CMS.Prompt = {
    init: function () {
        $('.btn.btn-danger.confirm').click(function (event) {
            if (!confirm(V.CMS.I18n.get('vanity.cms.confirm'))) {
                event.stopPropagation();
                event.preventDefault();
            }
        });
    }
};

V.CMS.SelectAll = {
    init: function () {
        $('.selection-master').change(function () {
            $('.selection-slave').prop('checked', $(this).is(':checked'));
        });
    }
};

$(document).ready(function () {
    V.CMS.Prompt.init();
    V.CMS.SelectAll.init();
});