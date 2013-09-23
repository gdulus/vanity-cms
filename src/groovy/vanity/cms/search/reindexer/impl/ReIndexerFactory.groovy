package vanity.cms.search.reindexer.impl

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import vanity.article.ArticleService
import vanity.article.TagService
import vanity.search.Index
import vanity.search.SearchEngineIndexer

@Component
final class ReIndexerFactory {

    @Autowired
    SearchEngineIndexer searchEngineIndexer

    @Autowired
    ArticleService articleService

    @Autowired
    TagService tagService

    @Autowired
    GrailsApplication grailsApplication

    public final ReIndexer produce(final Index reIndexingTarget) {
        switch (reIndexingTarget) {
            case Index.ARTICLE:
                return new ArticleReIndexer(batchSize, { articleService.list() }, searchEngineIndexer)
            case Index.TAG:
                return new TagReIndexer(batchSize, { tagService.getAllValidRootTags() }, searchEngineIndexer)
        }

        throw new IllegalArgumentException("Not supported re indexing target ${reIndexingTarget}")
    }

    private Integer getBatchSize() {
        return Integer.valueOf(grailsApplication.config.cms.search.reindex.batchSize)
    }

}
