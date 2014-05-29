package vanity.cms.search

import groovy.util.logging.Slf4j
import pl.burningice.burningconfig.features.JobLastRun
import vanity.article.TagService
import vanity.cms.search.reindexer.ReIndexingManager
import vanity.cms.search.reindexer.impl.ReIndexingCmd
import vanity.cms.search.reindexer.impl.ReIndexingType
import vanity.search.Index

@Slf4j
class ReIndexingTagsJob {

    static triggers = {
        cron name: 'ReIndexingTagsJob', cronExpression: '0 0/15 * * * ?' // every 15 minutes
    }

    def concurrent = false

    ReIndexingManager reIndexingManager

    TagService tagService

    @JobLastRun
    def execute(lastRun) {
        if (lastRun) {
            log.info('Starting job last run = {}', lastRun)
            List<Long> entitiesIds = tagService.findAllValidRootTagsIds((Date) lastRun)
            reIndexingManager.startReIndexing(new ReIndexingCmd(Index.TAGS, ReIndexingType.FULL, entitiesIds))
            log.info('Job finished')
        }
    }
}
