package vanity.cms.stats

import groovy.transform.PackageScope
import vanity.article.Tag
import vanity.stats.PopularityService
import vanity.tracking.ClickService
import vanity.tracking.TagClick

@PackageScope
class TagsPopularityAggregationWorker extends AbstractPopularityAggregationWorker {

    TagsPopularityAggregationWorker(ClickService clickService, PopularityService popularityService) {
        super(clickService, popularityService)
    }

    @Override
    protected Set<AggregationResult> aggregateClicks() {
        List<Object[]> queryResult = (List<Object[]>) TagClick.executeQuery('''
           select
               tag.id, day, count(*)
           from
               TagClick a
           group by
               tag.id,
               day
       ''')

        queryResult.collect { new AggregationResult(it) } as Set
    }

    @Override
    protected void updateRank(final AggregationResult aggregationResult) {
        popularityService.update(Tag.load(aggregationResult.id), aggregationResult.day, aggregationResult.count)
    }

    @Override
    protected void deleteClicks(final Set<Long> ids) {
        clickService.deleteAllByTagIds(ids)
    }
}
