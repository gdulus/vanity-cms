package vanity.cms.search.reindexer.impl

import groovy.transform.PackageScope
import vanity.article.Tag
import vanity.article.TagService
import vanity.search.Document
import vanity.search.SearchEngineIndexer

@PackageScope
class TagReIndexer extends AbstractReIndexer<Tag, Document.TagDocument> {

    TagReIndexer(Integer partitionSize, List<Long> entitiesIds, SearchEngineIndexer searchEngineIndexer, TagService tagService) {
        super(partitionSize, entitiesIds, searchEngineIndexer, tagService)
    }

    @Override
    protected Set<Document.TagDocument> getForIndexing(final List<Long> partition) {
        return partition.collect { Tag.read(it) }.findAll { it.searchable() }.collect { Tag tag ->
            Set<Tag> tags = tag.flatChildrenSet().findAll { Tag childTag -> childTag.searchable() }
            Document.asTagDocument(tag, tags)
        }
    }

    @Override
    protected Set<Document.TagDocument> getForClearing(final List<Long> partition) {
        return partition.collect { Tag.read(it) }.collect { Tag tag ->
            Set<Tag> tags = tag.flatChildrenSet().findAll { Tag childTag -> childTag.searchable() }
            Document.asTagDocument(tag, tags)
        }
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
