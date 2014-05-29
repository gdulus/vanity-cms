package vanity.cms.search

import grails.plugin.springsecurity.annotation.Secured
import org.springframework.beans.factory.annotation.Autowired
import vanity.cms.search.reindexer.ReIndexingManager
import vanity.cms.search.reindexer.impl.ReIndexingCmd
import vanity.cms.search.reindexer.impl.ReIndexingType
import vanity.search.Index
import vanity.user.Authority

@Secured([Authority.ROLE_ADMIN])
class SearchController {

    @Autowired
    ReIndexingManager reIndexingManager

    ArticleReIndexingService articleReIndexingService

    TagReIndexingService tagReIndexingService

    def index() {
        [
            supportedReIndexingTargets: [
                (Index.ARTICLES): true,
                (Index.TAGS): true
            ],
            state: reIndexingManager.getReIndexingStatuses()
        ]
    }

    def startReIndexing(ReIndexCmd cmd) {
        List<Long> entitiesIds = getReIndexingSource(cmd.reIndexingTarget)

        if (entitiesIds) {
            reIndexingManager.startReIndexingAsync(new ReIndexingCmd(cmd.reIndexingTarget, ReIndexingType.FULL, entitiesIds))
        }

        redirect(action: 'index')
    }

    private List<Long> getReIndexingSource(final Index target) {
        switch (target) {
            case Index.ARTICLES:
                return articleReIndexingService.findAllValidForReIndexing()
            case Index.TAGS:
                return tagReIndexingService.findAllValidForReIndexing()
            default:
                return null
        }
    }

    def stopReIndexing(ReIndexCmd cmd) {
        reIndexingManager.stopReIndexing(cmd.reIndexingTarget)
        redirect(action: 'index')
    }

}
