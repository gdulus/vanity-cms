package vanity.cms.search.reindexer.impl

import groovy.transform.PackageScope
import groovy.util.logging.Slf4j
import vanity.cms.search.reindexer.ReIndexingPhase
import vanity.cms.search.reindexer.ReIndexingStatus
import vanity.search.SearchEngineIndexer

@Slf4j
@PackageScope
abstract class AbstractReIndexer<I, O> implements ReIndexer {

    protected final SearchEngineIndexer searchEngineIndexer

    private final int partitionSize

    private final List<Long> entitiesIds

    private ReIndexingPhase phase

    private Integer processed

    private Integer toProcess

    private volatile Boolean stop

    public AbstractReIndexer(Integer partitionSize, List<Long> entitiesIds, SearchEngineIndexer searchEngineIndexer) {
        this.partitionSize = partitionSize
        this.entitiesIds = entitiesIds
        this.searchEngineIndexer = searchEngineIndexer
        this.processed = 0
        this.phase = ReIndexingPhase.INITIALIZED
        this.stop = false
    }

    @Override
    final void start() {
        for (List<Long> partition : prepare().collate(partitionSize)) {
            if (stop) {
                break
            }

            clear(partition)
            index(partition)
        }
    }

    protected List<Long> prepare() {
        phase = ReIndexingPhase.PREPARING
        toProcess = entitiesIds.size()
        log.info('Got {} elements to process', toProcess)
        return entitiesIds
    }

    protected void clear(final List<Long> partition) {
        log.info('Clearing index')
        phase = ReIndexingPhase.CONVERTING
        Set<O> documents = getForClearing(partition)

        if (documents) {
            phase = ReIndexingPhase.CLEARING
            doClear(documents)
        }
    }

    protected abstract Set<O> getForClearing(List<Long> partition)

    protected abstract void doClear(Set<O> documents)

    protected void index(final List<Long> partition) {
        log.info('Re-indexing')
        phase = ReIndexingPhase.CONVERTING
        Set<O> documents = getForIndexing(partition)

        if (documents) {
            phase = ReIndexingPhase.INDEXING
            doIndex(documents)
        }
    }

    protected abstract Set<O> getForIndexing(List<Long> partition)

    protected abstract void doIndex(Set<O> documents)

    @Override
    void stop() {
        stop = true
    }

    @Override
    ReIndexingStatus getStatus() {
        int percent = processed == 0 ? 0 : (processed / toProcess) * 100
        new ReIndexingStatus(percent, phase)
    }
}
