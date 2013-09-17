package vanity.cms.search

import groovy.util.logging.Slf4j
import vanity.article.Article
import vanity.search.ArticleDocument
import vanity.search.SearchEngineIndexer
import vanity.search.utils.ConversionUtils

@Slf4j
class ArticleReIndexer implements ReIndexer {

    private final int partitionSize

    private final SearchEngineIndexer searchEngineIndexer

    private final Closure articlesProvider

    private volatile ReIndexingPhase phase

    private int processed

    private int toProcess

    public ArticleReIndexer(int partitionSize, Closure articlesProvider, SearchEngineIndexer searchEngineIndexer) {
        this.partitionSize = partitionSize
        this.articlesProvider = articlesProvider
        this.searchEngineIndexer = searchEngineIndexer
        this.processed = 0
        this.phase = ReIndexingPhase.INITIALIZED
    }

    void start() {
        phase = ReIndexingPhase.PREPARING
        List<Article> articles = articlesProvider.call()
        toProcess = articles.size()
        log.info('Got {} articles to process', toProcess)

        articles.collate(partitionSize).each { List<Article> articlesPartition ->
            log.info('Clearing first batch')
            phase = ReIndexingPhase.CLEARING
            Set<ArticleDocument> articleDocuments = ConversionUtils.asArticleDocuments(articlesPartition)
            searchEngineIndexer.deleteArticles(articleDocuments)

            log.info('Re-indexing first batch')
            phase = ReIndexingPhase.INDEXING
            searchEngineIndexer.indexArticles(articleDocuments)

            processed += articleDocuments.size()
        }
    }

    ReIndexingStatus getStatus() {
        int percent = processed == 0 ? 0 : (processed / toProcess) * 100
        new ReIndexingStatus(percent, phase)
    }
}
