package vanity.cms.search.reindexer.impl

import groovy.transform.PackageScope
import vanity.article.Article
import vanity.search.Document
import vanity.search.SearchEngineIndexer

@PackageScope
class ArticleReIndexer extends AbstractReIndexer<Article, Document.ArticleDocument> {

    ArticleReIndexer(Integer partitionSize, List<Long> entitiesIds, SearchEngineIndexer searchEngineIndexer) {
        super(partitionSize, entitiesIds, searchEngineIndexer)
    }

    @Override
    protected Set<Document.ArticleDocument> doConvert(final List<Long> partition) {
        Document.asArticleDocuments(partition.collect { Article.read(it) })
    }

    @Override
    protected void doClear(final Set<Document.ArticleDocument> documents) {
        searchEngineIndexer.deleteArticles(documents)
    }

    @Override
    protected void doIndex(final Set<Document.ArticleDocument> documents) {
        searchEngineIndexer.indexArticles(documents)
    }
}
