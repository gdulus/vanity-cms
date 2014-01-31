package vanity.cms.article

import org.codehaus.groovy.grails.commons.GrailsApplication
import vanity.article.Article
import vanity.article.ArticleService
import vanity.article.TagService
import vanity.utils.ConfigUtils

class ArticleController {

    TagService tagService

    ArticleService articleService

    ArticleAdminService articleAdminService

    GrailsApplication grailsApplication

    def index(final Long offset, final Long max) {
        Long maxValue = max ?: ConfigUtils.$as(grailsApplication.config.cms.article.pagination.max, Long)
        [paginationBean: articleAdminService.listWithPagination(maxValue, offset, "dateCreated")]
    }

    def edit(final Long id) {
        [tags: tagService.findAll(), article: articleService.read(id)]
    }

    def update(final ArticleCmd articleCmd) {
        if (!articleCmd.validate()) {
            flash.error = 'vanity.cms.article.savingDomainError'
            return render(view: 'edit', model: [tags: tagService.findAll(), article: articleCmd])
        }

        Article article = articleAdminService.update(articleCmd.id) {
            bindData(it, articleCmd.properties)
        }

        if (!article) {
            flash.error = 'vanity.cms.entity.notFound'
            return redirect(action: 'index')
        }

        if (article.hasErrors()) {
            flash.error = 'vanity.cms.article.savingDomainError'
            return render(view: 'edit', model: [tags: tagService.findAll(), article: article])
        } else {
            flash.info = 'vanity.cms.article.saved'
            return redirect(action: 'edit', id: article.id)
        }
    }

    def delete(final Long id) {
        articleAdminService.markAsDeleted(id)
        flash.info = 'vanity.cms.article.deleted'
        redirect(action: 'index')
    }
}
