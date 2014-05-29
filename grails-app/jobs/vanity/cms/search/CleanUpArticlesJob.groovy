package vanity.cms.search

import groovy.util.logging.Slf4j
import pl.burningice.burningconfig.features.JobLastRun
import vanity.article.ArticleStatus
import vanity.cms.search.reindexer.ReIndexingManager
import vanity.cms.search.reindexer.impl.ReIndexingCmd
import vanity.cms.search.reindexer.impl.ReIndexingType
import vanity.search.Index

@Slf4j
class CleanUpArticlesJob {

    static triggers = {
        cron name: 'CleanUpArticlesJob', cronExpression: '0 0 3 * * ?' // 03:00 every day
    }

    def concurrent = false

    ReIndexingManager reIndexingManager

    ArticleReIndexingService articleReIndexingService

    @JobLastRun
    def execute(lastRun) {
        if (lastRun) {
            log.info('Starting job last run = {}', lastRun)
            List<Long> ids = articleReIndexingService.findAllValidForReIndexing((Date) lastRun, [ArticleStatus.DELETED])
            reIndexingManager.startReIndexing(new ReIndexingCmd(Index.ARTICLES, ReIndexingType.DELETE, ids))
            log.info('Job finished')
        }
    }
}
