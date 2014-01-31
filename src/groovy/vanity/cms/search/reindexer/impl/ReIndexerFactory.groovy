package vanity.cms.search.reindexer.impl

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import vanity.search.Index
import vanity.search.SearchEngineIndexer

@Component
final class ReIndexerFactory {

    @Autowired
    SearchEngineIndexer searchEngineIndexer

    @Autowired
    GrailsApplication grailsApplication

    public final ReIndexer produce(final ReIndexingCmd reIndexingCmd) {
        switch (reIndexingCmd.target) {
            case Index.ARTICLE_UPDATE:
                return new ArticleReIndexer(batchSize, reIndexingCmd.dataProvider, searchEngineIndexer)
            case Index.ARTICLE_REMOVE:
                return new ArticleRemover(batchSize, reIndexingCmd.dataProvider, searchEngineIndexer)
            case Index.TAG_UPDATE:
                return new TagReIndexer(batchSize, reIndexingCmd.dataProvider, searchEngineIndexer)
        }

        throw new IllegalArgumentException("Not supported re indexing target ${reIndexingCmd.target}")
    }

    private Integer getBatchSize() {
        return Integer.valueOf(grailsApplication.config.cms.search.reindex.batchSize)
    }

}
