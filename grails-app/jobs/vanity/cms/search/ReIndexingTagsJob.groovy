package vanity.cms.search

import groovy.util.logging.Slf4j
import pl.burningice.burningconfig.features.JobLastRun
import vanity.cms.search.reindexer.ReIndexingManager
import vanity.cms.search.reindexer.impl.ReIndexingCmd
import vanity.search.Index

@Slf4j
class ReIndexingTagsJob {

    static triggers = {
        cron cronExpression: '0 0 0/1 * * ?'
    }

    def concurrent = false

    ReIndexingManager reIndexingManager

    @JobLastRun
    def execute(lastRun) {
        if (lastRun) {
            log.info('Starting job last run = {}', lastRun)
            reIndexingManager.startReIndexing(new ReIndexingCmd(Index.TAG_PARTIAL, (Date) lastRun))
            log.info('Job finished')
        }
    }
}
