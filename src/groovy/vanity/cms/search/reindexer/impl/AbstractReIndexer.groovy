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

            Set<O> documents = convert(partition)

            if (documents) {
                processPartition(documents)
                processed += partition.size()
            }
        }
    }

    protected void processPartition(final Set<O> documents) {
        clear(documents)
        index(documents)
    }

    protected List<Long> prepare() {
        phase = ReIndexingPhase.PREPARING
        toProcess = entitiesIds.size()
        log.info('Got {} elements to process', toProcess)
        return entitiesIds
    }

    protected abstract Set<O> doConvert(List<Long> partition)

    private Set<O> convert(final List<Long> partition) {
        log.info('Converting batch')
        phase = ReIndexingPhase.CONVERTING
        return doConvert(partition)
    }

    protected abstract void doClear(Set<O> documents)

    protected void clear(final Set<O> documents) {
        log.info('Clearing index')
        phase = ReIndexingPhase.CLEARING
        doClear(documents)
    }

    protected abstract void doIndex(Set<O> documents)

    protected void index(final Set<O> documents) {
        log.info('Re-indexing')
        phase = ReIndexingPhase.INDEXING
        doIndex(documents)
    }

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
