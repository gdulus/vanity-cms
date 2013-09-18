package vanity.cms.search

import org.springframework.beans.factory.annotation.Autowired
import vanity.search.Index

class SearchController {

    @Autowired
    ReIndexingManager reIndexingManager

    def index() {
        [
            supportedReIndexingTargets: [Index.ARTICLE],
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
