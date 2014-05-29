package vanity.cms.search

import org.springframework.transaction.annotation.Transactional
import vanity.article.Article
import vanity.article.ArticleService
import vanity.article.ArticleStatus

class ArticleReIndexingService {

    ArticleService articleService

    @Transactional(readOnly = true)
    public List<Long> findAllValidForReIndexing() {
        return articleService.findAllIds()
    }

    @Transactional(readOnly = true)
    public List<Long> findAllValidForReIndexing(final Date point, final List<ArticleStatus> articleStatuses) {
        return Article.executeQuery('''
                    select
                        id
                    from
                        Article
                    where
                        (dateCreated >= :point or lastUpdated >= :point)
                        and status in :statuses
                    ''',
            [
                point: point,
                statuses: articleStatuses
            ]
        ) as List<Long>
    }

}
