package vanity.cms.tracking

import groovy.util.logging.Slf4j

@Slf4j
class ClicksAggregatorJob {

    static triggers = {
        simple startDelay: 10000, repeatInterval: 10000
    }

    ClicksAggregationService clicksAggregationService

    def execute() {
        log.info('Starting job')
        clicksAggregationService.execute()
        log.info('Job finished')
    }
}
