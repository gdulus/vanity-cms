package vanity.cms.api

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import vanity.article.TagService

@Secured(['ROLE_ADMIN'])
class TagApiController {

    TagService tagService

    def children(final String query) {
        if (!query) {
            return render([:] as JSON)
        }

        render(tagService.findAllActiveByQuery(query, false).collect { [id: it.id, name: it.name] } as JSON)
    }

    def parents(final String query) {
        if (!query) {
            return render([:] as JSON)
        }

        render(tagService.findAllActiveByQuery(query, true).collect { [id: it.id, name: it.name] } as JSON)
    }
}
