package vanity.cms.tracking

import groovy.util.logging.Slf4j
import org.springframework.transaction.annotation.Transactional
import vanity.article.ArticleService
import vanity.article.TagService
import vanity.tracking.ClickService

@Slf4j
class ClicksAggregationService {

    TagService tagService

    ArticleService articleService

    ClickService clickService

    @Transactional
    public void execute() {
        new ArticlesClicksAggregationWorker(clickService, articleService).execute()
        new TagsClicksAggregationWorker(clickService, tagService).execute()
    }
}
