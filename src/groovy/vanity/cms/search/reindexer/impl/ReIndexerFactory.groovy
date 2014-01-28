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

    public final ReIndexer produce(final ReIndexingCmd reIndexingCmd) {
        switch (reIndexingCmd.target) {
            case Index.ARTICLE_ALL:
                return new ArticleReIndexer(batchSize, { articleService.list() }, searchEngineIndexer)
            case Index.TAG_ALL:
                return new TagReIndexer(batchSize, { tagService.getAllValidRootTags() }, searchEngineIndexer)
            case Index.ARTICLE_PARTIAL:
                return new ArticleReIndexer(batchSize, { articleService.findAllFromThePointOfTime(reIndexingCmd.startFrom) }, searchEngineIndexer)
            case Index.TAG_PARTIAL:
                return new TagReIndexer(batchSize, { tagService.findAllFromThePointOfTime(reIndexingCmd.startFrom) }, searchEngineIndexer)
        }

        throw new IllegalArgumentException("Not supported re indexing target ${reIndexingCmd.target}")
    }

    private Integer getBatchSize() {
        return Integer.valueOf(grailsApplication.config.cms.search.reindex.batchSize)
    }

}
