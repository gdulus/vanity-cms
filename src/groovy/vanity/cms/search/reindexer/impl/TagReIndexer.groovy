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
    protected Set<Document.TagDocument> doConvert(final List<Long> partition) {
        return Document.asTagDocuments(partition.collect { Tag.read(it) })
    }

    @Override
    protected void doClear(final Set<Document.TagDocument> documents) {
        searchEngineIndexer.deleteTags(documents)
    }

    @Override
    protected void doIndex(final Set<Document.TagDocument> documents) {
        searchEngineIndexer.indexTags(documents.findAll { it.root } as Set)
    }
}
