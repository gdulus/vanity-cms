package vanity.cms.tracking

import groovy.transform.PackageScope
import vanity.article.TagService
import vanity.tracking.ArticleClick
import vanity.tracking.ClickService

@PackageScope
class TagsClicksAggregationWorker extends AbstractClicksAggregationWorker<TagService> {

    TagsClicksAggregationWorker(ClickService clickService, TagService updateRankService) {
        super(clickService, updateRankService)
    }

    @Override
    protected Set<AggregationResult> aggregateClicks() {
        List<Object[]> queryResult = (List<Object[]>) ArticleClick.executeQuery('''
           select
               tag.id, count(*)
           from
               TagClick a
           group by
               tag.id
       ''')

        queryResult.collect { new AggregationResult((Long) it[0], (Integer) it[1]) } as Set
    }

    @Override
    protected void updateRank(final AggregationResult aggregationResult) {
        updateRankService.updateRank(aggregationResult.id, aggregationResult.count)
    }

    @Override
    protected void deleteClicks(final Set<Long> ids) {
        clickService.deleteAllByTagIds(ids)
    }
}
