package vanity.cms.article

import groovy.util.logging.Slf4j
import org.springframework.transaction.annotation.Transactional
import vanity.article.ArticleClick
import vanity.article.ArticleClickService
import vanity.article.ArticleService

@Slf4j
class ArticleClicksAggregationService {

    ArticleService articleService

    ArticleClickService articleClickService

    @Transactional
    public void execute() {
        Set<Long> toBeRemoved = []

        aggregateClicks {final Long articleId, final Integer occurrence ->
            log.info('Updating rank of article {} by {}', articleId, occurrence)
            articleService.updateRank(articleId, occurrence)
            toBeRemoved << articleId
        }

        articleClickService.deleteAllByArticleIds(toBeRemoved)
    }

    private void aggregateClicks(final Closure worker) {
        List<Object[]> queryResult = (List<Object[]>) ArticleClick.executeQuery('''
            select
                article.id as articleId, count(*) as occurrence
            from
                ArticleClick a
            group by
                article.id
        ''')

        queryResult.each {
            worker.call((Long)it[0], (Integer)it[1])
        }
    }
}
