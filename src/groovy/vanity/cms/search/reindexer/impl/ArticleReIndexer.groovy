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
    protected Set<Document.ArticleDocument> getForIndexing(final List<Long> partition) {
        List<Article> articles = partition.collect { Article.read(it) }.findAll { it.searchable() }
        Document.asArticleDocuments(articles)
    }

    @Override
    protected Set<Document.ArticleDocument> getForClearing(final List<Long> partition) {
        List<Article> articles = partition.collect { Article.read(it) }
        Document.asArticleDocuments(articles)
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
