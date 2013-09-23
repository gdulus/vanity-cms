package vanity.cms.search.reindexer.impl

import groovy.transform.PackageScope
import vanity.article.Tag
import vanity.search.Document
import vanity.search.SearchEngineIndexer

@PackageScope
class TagReIndexer extends AbstractReIndexer<Tag, Document.TagDocument> {

    TagReIndexer(Integer partitionSize, Closure dataProvider, SearchEngineIndexer searchEngineIndexer) {
        super(partitionSize, dataProvider, searchEngineIndexer)
    }

    @Override
    protected Set<Document.TagDocument> doConvert(final List<Tag> partition) {
        return Document.asTagDocuments(partition)
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
