package vanity.cms.search

import org.springframework.beans.factory.annotation.Autowired
import vanity.cms.search.reindexer.ReIndexingManager
import vanity.cms.search.reindexer.impl.ReIndexingCmd
import vanity.search.Index

class SearchController {

    @Autowired
    ReIndexingManager reIndexingManager

    def index() {
        [
            supportedReIndexingTargets: [
                (Index.ARTICLE_ALL): true,
                (Index.TAG_ALL): true,
                (Index.ARTICLE_PARTIAL): false,
                (Index.TAG_PARTIAL): false
            ],
            state: reIndexingManager.getReIndexingStatuses()
        ]
    }

    def startReIndexing(ReIndexCmd cmd) {
        reIndexingManager.startReIndexing(new ReIndexingCmd(cmd.reIndexingTarget))
        redirect(action: 'index')
    }

    def stopReIndexing(ReIndexCmd cmd) {
        reIndexingManager.stopReIndexing(cmd.reIndexingTarget)
        redirect(action: 'index')
    }

}
