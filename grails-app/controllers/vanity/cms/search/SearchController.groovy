package vanity.cms.search

import grails.converters.JSON
import org.springframework.beans.factory.annotation.Autowired
import vanity.article.Article

class SearchController {

    @Autowired
    ReIndexingManager reIndexingManager

    def index(){
        [state:reIndexingManager.getReIndexingStatuses()]
    }

    def startArticleIndexing() {
        reIndexingManager.startReIndexing(Article)
        redirect(action: 'index')
    }

    def ajaxArticleIndexingStatus() {
        render(reIndexingManager.getReIndexingStatuses() as JSON)
    }
}
