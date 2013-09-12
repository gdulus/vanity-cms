package vanity.cms.article

import groovy.util.logging.Slf4j

@Slf4j
class ArticleClicksAggregatorJob {

    static triggers = {
        simple startDelay: 10000, repeatInterval: 10000
    }

    ArticleClicksAggregationService articleClicksAggregationService

    def execute() {
        log.info('Starting job')
        articleClicksAggregationService.execute()
        log.info('Job finished')
    }
}
