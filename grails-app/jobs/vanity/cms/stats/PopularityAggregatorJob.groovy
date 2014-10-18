package vanity.cms.stats

import groovy.util.logging.Slf4j

@Slf4j
class PopularityAggregatorJob {

    static triggers = {
        cron name: 'PopularityAggregatorJob', cronExpression: '0 0 0/1 * * ?' // every 1 hour
    }

    PopularityAggregationService popularityAggregationService

    def execute() {
        log.info('Starting job')
        popularityAggregationService.execute()
        log.info('Job finished')
    }
}
