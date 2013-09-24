package vanity.cms.tracking

import groovy.transform.PackageScope
import vanity.article.ArticleService
import vanity.tracking.ArticleClick
import vanity.tracking.ClickService

@PackageScope
class ArticlesClicksAggregationWorker extends AbstractClicksAggregationWorker<ArticleService> {

    ArticlesClicksAggregationWorker(ClickService clickService, ArticleService updateRankService) {
        super(clickService, updateRankService)
    }

    @Override
    protected Set<AggregationResult> aggregateClicks() {
        List<Object[]> queryResult = (List<Object[]>) ArticleClick.executeQuery('''
           select
               article.id, count(*)
           from
               ArticleClick a
           group by
               article.id
       ''')

        queryResult.collect { new AggregationResult((Long) it[0], (Integer) it[1]) } as Set
    }

    @Override
    protected void updateRank(final AggregationResult aggregationResult) {
        updateRankService.updateRank(aggregationResult.id, aggregationResult.count)
    }

    @Override
    protected void deleteClicks(final Set<Long> ids) {
        clickService.deleteAllByArticleIds(ids)
    }
}
