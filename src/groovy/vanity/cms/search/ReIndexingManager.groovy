package vanity.cms.search

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

@Component
class ReIndexingManager {

    private static final ConcurrentMap<Class, ReIndexer> RUNNING_REINDEXERS = new ConcurrentHashMap<>()

    @Autowired
    ReIndexerFactory reIndexerFactory

    public void startReIndexing(final Class reIndexingTarget) {
        // create lazy re indexer
        ReIndexer reIndexer = reIndexerFactory.produce(reIndexingTarget)
        // try add it to running indexes and add trigger start only when has status initialized
        if (RUNNING_REINDEXERS.putIfAbsent(reIndexingTarget, reIndexer) == null) {
            try {
                reIndexingTarget.withNewSession { reIndexer.start() }
            } finally {
                RUNNING_REINDEXERS.remove(reIndexer)
            }
        }
    }

    public Map<Class, ReIndexingStatus> getReIndexingStatuses() {
        Map<Class, ReIndexingStatus> result = [:]
        synchronized (RUNNING_REINDEXERS) {
            RUNNING_REINDEXERS.each { result[it.key] = it.value.status }
        }
        return result
    }

}
