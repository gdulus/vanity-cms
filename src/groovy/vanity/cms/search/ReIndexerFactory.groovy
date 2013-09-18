package vanity.cms.search

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import vanity.article.ArticleService
import vanity.search.Index
import vanity.search.SearchEngineIndexer

@Component
final class ReIndexerFactory {

    @Autowired
    SearchEngineIndexer searchEngineIndexer

    @Autowired
    ArticleService articleService

    @Autowired
    GrailsApplication grailsApplication

    public final produce(final Index reIndexingTarget) {
        switch (reIndexingTarget) {
            case Index.ARTICLE:
                Integer batchSize = Integer.valueOf(grailsApplication.config.cms.search.reindex.batchSize)
                return new ArticleReIndexer(batchSize, { articleService.list() }, searchEngineIndexer)
        }

        throw new IllegalArgumentException("Not supported re indexing target ${reIndexingTarget}")
    }

}
