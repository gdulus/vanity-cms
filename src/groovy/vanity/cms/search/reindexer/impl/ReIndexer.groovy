package vanity.cms.search.reindexer.impl

import vanity.cms.search.reindexer.ReIndexingStatus

public interface ReIndexer {

    public void start()

    public void stop()

    public ReIndexingStatus getStatus()

}