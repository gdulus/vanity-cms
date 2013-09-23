package vanity.cms.search

import org.springframework.beans.factory.annotation.Autowired
import vanity.cms.search.reindexer.ReIndexingManager
import vanity.search.Index

class SearchController {

    @Autowired
    ReIndexingManager reIndexingManager

    def index() {
        [
            supportedReIndexingTargets: [Index.ARTICLE, Index.TAG],
            state: reIndexingManager.getReIndexingStatuses()
        ]
    }

    def startReIndexing(ReIndexCmd cmd) {
        reIndexingManager.startReIndexing(cmd.reIndexingTarget)
        redirect(action: 'index')
    }

    def stopReIndexing(ReIndexCmd cmd) {
        reIndexingManager.stopReIndexing(cmd.reIndexingTarget)
        redirect(action: 'index')
    }

}
