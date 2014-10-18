package vanity.cms.stats

import groovy.transform.EqualsAndHashCode
import groovy.transform.PackageScope
import vanity.stats.PopularityService
import vanity.tracking.ClickService

@PackageScope
abstract class AbstractPopularityAggregationWorker {

    protected final ClickService clickService

    protected final PopularityService popularityService

    AbstractPopularityAggregationWorker(ClickService clickService, PopularityService popularityService) {
        this.clickService = clickService
        this.popularityService = popularityService
    }

    public void execute() {
        Set<Long> toBeRemoved = []

        aggregateClicks().each { final AggregationResult aggregationResult ->
            updateRank(aggregationResult)
            toBeRemoved << aggregationResult.id
        }

        deleteClicks(toBeRemoved)
    }

    protected abstract Set<AggregationResult> aggregateClicks()

    protected abstract void updateRank(AggregationResult aggregationResult)

    protected abstract void deleteClicks(Set<Long> ids)

    @EqualsAndHashCode(includes = ['id'])
    protected static final class AggregationResult {

        final Long id

        final Date day

        final Integer count

        AggregationResult(Object[] queryResult) {
            this.id = (Long) queryResult[0]
            this.day = new Date((Long) queryResult[1])
            this.count = (Integer) queryResult[2]
        }
    }

}
