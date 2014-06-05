package vanity.cms.search.reindexer.impl

import groovy.transform.PackageScope
import vanity.article.Article
import vanity.article.Tag
import vanity.article.TagService
import vanity.search.Document
import vanity.search.SearchEngineIndexer

@PackageScope
class ArticleReIndexer extends AbstractReIndexer<Article, Document.ArticleDocument> {

    ArticleReIndexer(Integer partitionSize, List<Long> entitiesIds, SearchEngineIndexer searchEngineIndexer, TagService tagService) {
        super(partitionSize, entitiesIds, searchEngineIndexer, tagService)
    }

    @Override
    protected Set<Document.ArticleDocument> getForIndexing(final List<Long> partition) {
        partition.collect { Article.read(it) }.findAll { it.searchable() }.collect { Article article ->
            Set<Tag> tags = article.tags.sum { Tag tag -> tag.flatChildrenSet() }.findAll { Tag tag -> tag.searchable() }
            Document.asArticleDocument(article, tags)
        }
    }

    @Override
    protected Set<Document.ArticleDocument> getForClearing(final List<Long> partition) {
        partition.collect { Article.read(it) }.collect { Article article ->
            Set<Tag> tags = article.tags.sum { Tag tag -> tag.flatChildrenSet() }.findAll { Tag tag -> tag.searchable() }
            Document.asArticleDocument(article, tags)
        }
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
