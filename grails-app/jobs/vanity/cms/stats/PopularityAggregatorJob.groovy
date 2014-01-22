package vanity.cms.stats

import groovy.util.logging.Slf4j
import pl.burningice.burningconfig.features.JobLastRun

@Slf4j
class PopularityAggregatorJob {

    static triggers = {
        simple startDelay: 10000, repeatInterval: 10000
    }

    PopularityAggregationService popularityAggregationService

    @JobLastRun
    def execute(lastRun) {
        log.info('Starting job last run = {}', lastRun)
        popularityAggregationService.execute()
        log.info('Job finished')
    }
}
