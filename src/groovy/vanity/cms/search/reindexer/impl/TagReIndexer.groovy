package vanity.cms.search.reindexer.impl

import groovy.transform.PackageScope
import vanity.article.Tag
import vanity.search.Document
import vanity.search.SearchEngineIndexer

@PackageScope
class TagReIndexer extends AbstractReIndexer<Tag, Document.TagDocument> {

    TagReIndexer(Integer partitionSize, List<Long> entitiesIds, SearchEngineIndexer searchEngineIndexer) {
        super(partitionSize, entitiesIds, searchEngineIndexer)
    }


    @Override
    protected Set<Document.TagDocument> getForIndexing(final List<Long> partition) {
        List<Tag> tags = partition.collect { Tag.read(it) }.findAll { it.searchable() }
        Document.asTagDocuments(tags)
    }

    @Override
    protected Set<Document.TagDocument> getForClearing(final List<Long> partition) {
        List<Tag> tags = partition.collect { Tag.read(it) }
        Document.asTagDocuments(tags)
    }

    @Override
    protected void doClear(final Set<Document.TagDocument> documents) {
        searchEngineIndexer.deleteTags(documents)
    }

    @Override
    protected void doIndex(final Set<Document.TagDocument> documents) {
        searchEngineIndexer.indexTags(documents)
    }
}
