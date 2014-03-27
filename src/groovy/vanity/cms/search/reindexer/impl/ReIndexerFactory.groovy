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
            case (cmd.target == Index.ARTICLES && cmd.type == ReIndexingType.FULL):
                return new ArticleReIndexer(batchSize, cmd.dataProvider, searchEngineIndexer)
            case (cmd.target == Index.ARTICLES && cmd.type == ReIndexingType.DELETE):
                return new ArticleRemover(batchSize, cmd.dataProvider, searchEngineIndexer)
            case (cmd.target == Index.TAGS && cmd.type == ReIndexingType.FULL):
                return new TagReIndexer(batchSize, cmd.dataProvider, searchEngineIndexer)
        }

        throw new IllegalArgumentException("Not supported re indexing target ${cmd.target} and action ${cmd.type}")
    }


}
