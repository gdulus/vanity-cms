package vanity.cms.stats

import groovy.transform.PackageScope
import vanity.article.Article
import vanity.stats.PopularityService
import vanity.tracking.ArticleClick
import vanity.tracking.ClickService

@PackageScope
class ArticlesPopularityAggregationWorker extends AbstractPopularityAggregationWorker {

    ArticlesPopularityAggregationWorker(ClickService clickService, PopularityService popularityService) {
        super(clickService, popularityService)
    }

    @Override
    protected Set<AggregationResult> aggregateClicks() {
        List<Object[]> queryResult = (List<Object[]>) ArticleClick.executeQuery('''
           select
               article.id, day, count(*)
           from
               ArticleClick a
           group by
               article.id,
               day
       ''')

        queryResult.collect { new AggregationResult(it) } as Set
    }

    @Override
    protected void updateRank(final AggregationResult aggregationResult) {
        popularityService.update(Article.load(aggregationResult.id), aggregationResult.day, aggregationResult.count)
    }

    @Override
    protected void deleteClicks(final Set<Long> ids) {
        clickService.deleteAllByArticleIds(ids)
    }
}
