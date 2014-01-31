package vanity.cms.search

import groovy.util.logging.Slf4j
import pl.burningice.burningconfig.features.JobLastRun
import vanity.article.TagService
import vanity.article.TagStatus
import vanity.cms.search.reindexer.ReIndexingManager
import vanity.cms.search.reindexer.impl.ReIndexingCmd
import vanity.search.Index

@Slf4j
class ReIndexingTagsJob {

    static triggers = {
        cron name: 'ReIndexingTagsJob', cronExpression: '0 0/1 * * * ?'
    }

    def concurrent = false

    ReIndexingManager reIndexingManager

    TagService tagService

    @JobLastRun
    def execute(lastRun) {
        if (lastRun) {
            log.info('Starting job last run = {}', lastRun)
            Closure dataProvider = { tagService.findAllFromThePointOfTime((Date) lastRun, TagStatus.OPEN_STATUSES) }
            reIndexingManager.startReIndexing(new ReIndexingCmd(Index.TAG_UPDATE, dataProvider))
            log.info('Job finished')
        }
    }
}
