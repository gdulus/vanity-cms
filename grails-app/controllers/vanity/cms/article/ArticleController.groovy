package vanity.cms.article

import grails.plugin.springsecurity.annotation.Secured
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.beans.factory.annotation.Value
import vanity.article.Article
import vanity.article.ArticleService
import vanity.article.TagService
import vanity.pagination.PaginationParams
import vanity.user.Authority

@Secured([Authority.ROLE_ADMIN])
class ArticleController {

    TagService tagService

    ArticleService articleService

    ArticleAdminService articleAdminService

    GrailsApplication grailsApplication

    @Value('${cms.article.pagination.max}')
    Long defaultMaxArticles

    def index(final Long offset, final Long max) {
        Long maxValue = max ?: defaultMaxArticles
        PaginationParams paginationParams = new PaginationParams(maxValue, offset, "dateCreated")
        [paginationBean: articleAdminService.listWithPagination(paginationParams)]
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
