package vanity.cms.search

import groovy.util.logging.Slf4j
import pl.burningice.burningconfig.features.JobLastRun
import vanity.article.ArticleService
import vanity.article.ArticleStatus
import vanity.cms.search.reindexer.ReIndexingManager
import vanity.cms.search.reindexer.impl.ReIndexingCmd
import vanity.cms.search.reindexer.impl.ReIndexingType
import vanity.search.Index

@Slf4j
class ReIndexingArticlesJob {

    static triggers = {
        cron name: 'ReIndexingArticlesJob', cronExpression: '0 0 0/1 * * ?' // every 1 hours
    }

    def concurrent = false

    ReIndexingManager reIndexingManager

    ArticleService articleService

    @JobLastRun
    def execute(lastRun) {
        if (lastRun) {
            log.info('Starting job last run = {}', lastRun)
            List<Long> ids = articleService.findAllIdsFromThePointOfTimeWithStatus((Date) lastRun, [ArticleStatus.ACTIVE])
            reIndexingManager.startReIndexing(new ReIndexingCmd(Index.ARTICLES, ReIndexingType.FULL, ids))
            log.info('Job finished')
        }
    }
}
