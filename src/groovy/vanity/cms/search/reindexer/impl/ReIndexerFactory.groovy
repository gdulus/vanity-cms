package vanity.cms.search.reindexer.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import vanity.search.Index
import vanity.search.SearchEngineIndexer

@Component
final class ReIndexerFactory {

    @Autowired
    SearchEngineIndexer searchEngineIndexer

    @Value('${cms.search.reindex.batchSize}')
    Integer batchSize

    public final ReIndexer produce(final ReIndexingCmd cmd) {
        switch (true) {
            case cmd.target == Index.ARTICLES:
                return new ArticleReIndexer(batchSize, cmd.entitiesIds, searchEngineIndexer)
            case cmd.target == Index.TAGS:
                return new TagReIndexer(batchSize, cmd.entitiesIds, searchEngineIndexer)
        }

        throw new IllegalArgumentException("Not supported re indexing target ${cmd.target} and action ${cmd.type}")
    }


}
