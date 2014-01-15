package vanity.cms.article

import org.codehaus.groovy.grails.commons.GrailsApplication
import vanity.article.ArticleService
import vanity.utils.ConfigUtils

class ArticleController {

    ArticleService articleService

    ArticleAdminService articleAdminService

    GrailsApplication grailsApplication

    def index(Long offset, Long max) {
        max = max ?: ConfigUtils.$as(grailsApplication.config.cms.article.pagination.max, Long)
        [paginationBean: articleService.listWithPagination(max, offset, "dateCreated")]
    }

    def delete(final Long id) {
        articleAdminService.delete(id)
        flash.info = 'vanity.cms.article.deleted'
        redirect(action: 'index')
    }
}
