package vanity.cms.search.reindexer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import vanity.article.Article
import vanity.cms.search.reindexer.impl.ReIndexer
import vanity.cms.search.reindexer.impl.ReIndexerFactory
import vanity.cms.search.reindexer.impl.ReIndexingCmd
import vanity.search.Index

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

@Component
class ReIndexingManager {

    private static final ConcurrentMap<Index, ReIndexer> RUNNING_REINDEXERS = new ConcurrentHashMap<>()

    @Autowired
    ReIndexerFactory reIndexerFactory

    @Async
    public void startReIndexingAsync(final ReIndexingCmd reIndexingCmd) {
        startReIndexing(reIndexingCmd)
    }

    public void startReIndexing(final ReIndexingCmd reIndexingCmd) {
        // create lazy re indexer
        ReIndexer reIndexer = reIndexerFactory.produce(reIndexingCmd)
        // try add it to running indexes and add trigger start only when has status initialized
        if (RUNNING_REINDEXERS.putIfAbsent(reIndexingCmd.target, reIndexer) == null) {
            try {
                Article.withNewSession {
                    reIndexer.start()
                }
            } finally {
                if (!RUNNING_REINDEXERS.remove(reIndexingCmd.target, reIndexer)) {
                    throw new IllegalStateException("Could not remove entry for ${reIndexingCmd.target}")
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
