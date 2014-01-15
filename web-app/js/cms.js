var V = V || {};
V.CMS = V.CMS || {};

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
        'vanity.cms.delete.confirm': 'Are you sure?',
        'vanity.cms.tags.review.selectOneStrategy': 'Please select at last one strategy'
    }

    return {
        get: function (code) {
            return messages[code] || code
        }
    }
})();

V.CMS.DeleteButton = {
    init: function () {
        $('.btn.btn-danger.delete').click(function (event) {
            if (!confirm(V.CMS.I18n.get('vanity.cms.delete.confirm'))) {
                event.stopPropagation();
                event.preventDefault();
            }
        });
    }
}

$(document).ready(function () {
    V.CMS.DeleteButton.init();
});