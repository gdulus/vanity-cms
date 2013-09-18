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

    private ReIndexingPhase phase

    private Integer processed

    private Integer toProcess

    private volatile Boolean stop

    public ArticleReIndexer(Integer partitionSize, Closure articlesProvider, SearchEngineIndexer searchEngineIndexer) {
        this.partitionSize = partitionSize
        this.articlesProvider = articlesProvider
        this.searchEngineIndexer = searchEngineIndexer
        this.processed = 0
        this.phase = ReIndexingPhase.INITIALIZED
        this.stop = false
    }

    @Override
    void start() {
        for (List<Article> articlesPartition : prepare().collate(partitionSize)) {
            if (stop){
                break
            }

            Set<ArticleDocument> articleDocuments = convert(articlesPartition)
            clear(articleDocuments)
            index(articleDocuments)
            processed += articlesPartition.size()
        }
    }

    private List<Article> prepare() {
        phase = ReIndexingPhase.PREPARING
        List<Article> articles = (List<Article>) articlesProvider.call()
        toProcess = articles.size()
        log.info('Got {} articles to process', toProcess)
        return articles
    }

    private Set<ArticleDocument> convert(final List<Article> articlesPartition) {
        log.info('Converting batch')
        phase = ReIndexingPhase.CONVERTING
        return ConversionUtils.asArticleDocuments(articlesPartition)
    }

    private void clear(Set<ArticleDocument> articleDocuments) {
        log.info('Clearing index')
        phase = ReIndexingPhase.CLEARING
        searchEngineIndexer.deleteArticles(articleDocuments)
    }

    private void index(Set<ArticleDocument> articleDocuments) {
        log.info('Re-indexing')
        phase = ReIndexingPhase.INDEXING
        searchEngineIndexer.indexArticles(articleDocuments)
    }

    @Override
    void stop() {
        stop = true
    }

    @Override
    ReIndexingStatus getStatus() {
        int percent = processed == 0 ? 0 : (processed / toProcess) * 100
        new ReIndexingStatus(percent, phase)
    }
}
