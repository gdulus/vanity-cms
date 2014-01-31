package vanity.cms.search

import groovy.util.logging.Slf4j
import pl.burningice.burningconfig.features.JobLastRun
import vanity.article.ArticleService
import vanity.article.ArticleStatus
import vanity.cms.search.reindexer.ReIndexingManager
import vanity.cms.search.reindexer.impl.ReIndexingCmd
import vanity.search.Index

@Slf4j
class CleanUpArticlesJob {

    static triggers = {
        cron name: 'CleanUpArticlesJob', cronExpression: '0 0/1 * * * ?'
    }

    def concurrent = false

    ReIndexingManager reIndexingManager

    ArticleService articleService

    @JobLastRun
    def execute(lastRun) {
        if (lastRun) {
            log.info('Starting job last run = {}', lastRun)
            Closure dataProvider = { articleService.findAllFromThePointOfTimeWithStatus((Date) lastRun, [ArticleStatus.DELETED]) }
            reIndexingManager.startReIndexing(new ReIndexingCmd(Index.ARTICLE_REMOVE, dataProvider))
            log.info('Job finished')
        }
    }
}
