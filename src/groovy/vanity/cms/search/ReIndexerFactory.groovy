package vanity.cms.search

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import vanity.article.Article
import vanity.article.ArticleService
import vanity.search.SearchEngineIndexer

@Component
final class ReIndexerFactory {

    @Autowired
    SearchEngineIndexer searchEngineIndexer

    @Autowired
    ArticleService articleService

    public final produce(final Class reIndexingTarget){
        switch(reIndexingTarget){
            case Article:
                return new ArticleReIndexer(50, {articleService.list()}, searchEngineIndexer)
        }

        throw new IllegalArgumentException("Not supported re indexing target ${reIndexingTarget}")
    }

}
