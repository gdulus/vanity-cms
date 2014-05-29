package vanity.cms.search

import org.springframework.transaction.annotation.Transactional
import vanity.article.Article

class ArticleReIndexingService {

    @Transactional(readOnly = true)
    public List<Long> findAllValidForReIndexing() {
        return Article.executeQuery('''
               select
                   id
               from
                   Article
                   ''',
            [:]
        ) as List<Long>
    }

    @Transactional(readOnly = true)
    public List<Long> findAllValidForReIndexing(final Date point) {
        return Article.executeQuery('''
                    select
                        id
                    from
                        Article
                    where
                        (dateCreated >= :point or lastUpdated >= :point)
                    ''',
            [
                point: point
            ]
        ) as List<Long>
    }

}
