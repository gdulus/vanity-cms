package vanity.cms.tracking

import groovy.transform.EqualsAndHashCode
import groovy.transform.PackageScope
import vanity.tracking.ClickService

@PackageScope
abstract class AbstractClicksAggregationWorker<T> {

    protected final ClickService clickService

    protected final T updateRankService

    AbstractClicksAggregationWorker(ClickService clickService, T updateRankService) {
        this.clickService = clickService
        this.updateRankService = updateRankService
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

        final Integer count

        AggregationResult(Long id, Integer count) {
            this.id = id
            this.count = count
        }
    }

}
