package vanity.cms.search.reindexer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import vanity.article.Article
import vanity.cms.search.reindexer.impl.ReIndexer
import vanity.cms.search.reindexer.impl.ReIndexerFactory
import vanity.search.Index

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

@Component
class ReIndexingManager {

    private static final ConcurrentMap<Index, ReIndexer> RUNNING_REINDEXERS = new ConcurrentHashMap<>()

    @Autowired
    ReIndexerFactory reIndexerFactory

    @Async
    public void startReIndexing(final Index reIndexingTarget) {

        // create lazy re indexer
        ReIndexer reIndexer = reIndexerFactory.produce(reIndexingTarget)
        // try add it to running indexes and add trigger start only when has status initialized
        if (RUNNING_REINDEXERS.putIfAbsent(reIndexingTarget, reIndexer) == null) {
            try {
                Article.withNewSession {
                    reIndexer.start()
                }
            } finally {
                if (!RUNNING_REINDEXERS.remove(reIndexingTarget, reIndexer)) {
                    throw new IllegalStateException("Could not remove entry for ${reIndexingTarget}")
                }
            }
        }
    }

    public void stopReIndexing(final Index reIndexingTarget) {
        RUNNING_REINDEXERS[reIndexingTarget]?.stop()
    }

    public ReIndexingStatuses getReIndexingStatuses() {
        ReIndexingStatuses result = new ReIndexingStatuses()
        RUNNING_REINDEXERS.entrySet().each { result[it.key] = it.value.status }
        return result
    }

}
