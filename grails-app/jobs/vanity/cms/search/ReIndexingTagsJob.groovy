package vanity.cms.search

import groovy.util.logging.Slf4j
import pl.burningice.burningconfig.features.JobLastRun
import vanity.cms.search.reindexer.ReIndexingManager
import vanity.cms.search.reindexer.impl.ReIndexingCmd
import vanity.search.Index

@Slf4j
class ReIndexingTagsJob {

    static triggers = {
        cron name: 'ReIndexingTagsJob', cronExpression: '0 0/15 * * * ?' // every 15 minutes
    }

    def concurrent = false

    ReIndexingManager reIndexingManager

    TagReIndexingService tagReIndexingService

    @JobLastRun
    def execute(lastRun) {
        if (lastRun) {
            log.info('Starting job last run = {}', lastRun)
            List<Long> entitiesIds = tagReIndexingService.findAllValidForReIndexing((Date) lastRun)
            reIndexingManager.startReIndexing(new ReIndexingCmd(Index.TAGS, entitiesIds))
            log.info('Job finished')
        }
    }
}
