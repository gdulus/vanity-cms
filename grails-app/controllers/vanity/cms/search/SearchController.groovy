package vanity.cms.search

import org.springframework.beans.factory.annotation.Autowired
import vanity.article.ArticleService
import vanity.article.TagService
import vanity.cms.search.reindexer.ReIndexingManager
import vanity.cms.search.reindexer.impl.ReIndexingCmd
import vanity.search.Index

class SearchController {

    @Autowired
    ReIndexingManager reIndexingManager

    ArticleService articleService

    TagService tagService

    def index() {
        [
            supportedReIndexingTargets: [
                (Index.ARTICLE_UPDATE): true,
                (Index.TAG_UPDATE): true,
                (Index.ARTICLE_PARTIAL): false,
                (Index.TAG_PARTIAL): false
            ],
            state: reIndexingManager.getReIndexingStatuses()
        ]
    }

    def startReIndexing(ReIndexCmd cmd) {
        Closure dataProvider = getReIndexingDataProvider(cmd.reIndexingTarget)

        if (dataProvider) {
            reIndexingManager.startReIndexingAsync(new ReIndexingCmd(cmd.reIndexingTarget, dataProvider))
        }

        redirect(action: 'index')
    }

    private Closure getReIndexingDataProvider(final Index target) {
        switch (target) {
            case Index.ARTICLE_UPDATE:
                return { articleService.findAll() }
            case Index.TAG_UPDATE:
                return { tagService.findAllValidRootTags() }
            default:
                return null
        }
    }

    def stopReIndexing(ReIndexCmd cmd) {
        reIndexingManager.stopReIndexing(cmd.reIndexingTarget)
        redirect(action: 'index')
    }

}
