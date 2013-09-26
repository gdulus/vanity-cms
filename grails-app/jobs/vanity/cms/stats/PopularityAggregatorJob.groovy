package vanity.cms.stats

import groovy.util.logging.Slf4j

@Slf4j
class PopularityAggregatorJob {

    static triggers = {
        simple startDelay: 10000, repeatInterval: 10000
    }

    PopularityAggregationService popularityAggregationService

    def execute() {
        log.info('Starting job')
        popularityAggregationService.execute()
        log.info('Job finished')
    }
}
