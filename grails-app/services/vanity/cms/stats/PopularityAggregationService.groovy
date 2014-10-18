package vanity.cms.stats

import groovy.util.logging.Slf4j
import org.springframework.transaction.annotation.Transactional
import vanity.stats.PopularityService
import vanity.tracking.ClickService

@Slf4j
class PopularityAggregationService {

    PopularityService popularityService

    ClickService clickService

    @Transactional
    public void execute() {
        new ArticlesPopularityAggregationWorker(clickService, popularityService).execute()
        new TagsPopularityAggregationWorker(clickService, popularityService).execute()
    }
}
