package vanity.cms.article

import org.springframework.transaction.annotation.Transactional
import vanity.article.Article
import vanity.article.ArticleStatus
import vanity.pagination.PaginationAware
import vanity.pagination.PaginationBean
import vanity.pagination.PaginationParams

class ArticleAdminService implements PaginationAware<Article> {

    @Transactional(readOnly = true)
    PaginationBean<Article> listWithPagination(final PaginationParams params) {
        List<Article> articles = Article.findAllByStatus(ArticleStatus.ACTIVE, [max: params.max, offset: params.offset, sort: params.sort])
        int count = Article.countByStatus(ArticleStatus.ACTIVE)
        return new PaginationBean<Article>(articles, count)
    }

    @Transactional
    void markAsDeleted(final Long id) {
        update(id) { Article it -> it.status = ArticleStatus.DELETED }
    }

    @Transactional
    Article update(final Long id, final Closure binder) {
        Article article = Article.get(id)

        if (!article) {
            return null
        }

        binder.call(article)
        return article.save()
    }
}
