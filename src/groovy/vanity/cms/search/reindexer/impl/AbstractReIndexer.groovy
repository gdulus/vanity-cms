package vanity.cms.search.reindexer.impl

import groovy.transform.PackageScope
import groovy.util.logging.Slf4j
import vanity.cms.search.reindexer.ReIndexingPhase
import vanity.cms.search.reindexer.ReIndexingStatus
import vanity.search.SearchEngineIndexer

@Slf4j
@PackageScope
abstract class AbstractReIndexer<I, O> implements ReIndexer {

    private final int partitionSize

    private final Closure dataProvider

    protected final SearchEngineIndexer searchEngineIndexer

    private ReIndexingPhase phase

    private Integer processed

    private Integer toProcess

    private volatile Boolean stop

    public AbstractReIndexer(Integer partitionSize, Closure dataProvider, SearchEngineIndexer searchEngineIndexer) {
        this.partitionSize = partitionSize
        this.dataProvider = dataProvider
        this.searchEngineIndexer = searchEngineIndexer
        this.processed = 0
        this.phase = ReIndexingPhase.INITIALIZED
        this.stop = false
    }

    @Override
    void start() {
        for (List<I> partition : prepare().collate(partitionSize)) {
            if (stop) {
                break
            }

            Set<O> documents = convert(partition)
            clear(documents)
            index(documents)
            processed += partition.size()
        }
    }

    private List<I> prepare() {
        phase = ReIndexingPhase.PREPARING
        List<I> documents = (List<I>) dataProvider.call()
        toProcess = documents.size()
        log.info('Got {} elements to process', toProcess)
        return documents
    }

    protected abstract Set<O> doConvert(List<I> partition)

    private Set<O> convert(final List<I> partition) {
        log.info('Converting batch')
        phase = ReIndexingPhase.CONVERTING
        return doConvert(partition)
    }

    protected abstract void doClear(Set<O> documents)

    private void clear(final Set<O> documents) {
        log.info('Clearing index')
        phase = ReIndexingPhase.CLEARING
        doClear(documents)
    }

    protected abstract void doIndex(Set<O> documents)

    private void index(final Set<O> documents) {
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
