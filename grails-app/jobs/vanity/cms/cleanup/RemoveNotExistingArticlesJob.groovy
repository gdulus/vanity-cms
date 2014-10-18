package vanity.cms.cleanup

import groovy.util.logging.Slf4j
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.util.EntityUtils
import vanity.article.Article
import vanity.article.ArticleService
import vanity.cms.article.ArticleAdminService

import javax.servlet.http.HttpServletResponse

import static groovyx.gpars.GParsPool.withPool

@Slf4j
class RemoveNotExistingArticlesJob {

    private static final int DAYS_TO_OBSOLETE = 30

    private static final int COLLATION_SIZE = 100

    private static final int THREADS_POOL_SIZE = 10

    static triggers = {
        cron name: 'RemoveNotExistingArticlesJob', cronExpression: '0 0 0 * * ?' // 24:00 every day
    }

    def concurrent = false

    ArticleService articleService

    ArticleAdminService articleAdminService

    HttpClient httpClient

    def execute() {
        log.info('Starting job')

        withPool(THREADS_POOL_SIZE) {
            obsoleteArticles.collate(COLLATION_SIZE).eachParallel { List<Article> partition ->
                partition.each {
                    if (!exists(it)) {
                        log.info('Article {} not exists anymore - marking for deletion', it)
                        articleAdminService.markAsDeleted(it.id)
                    } else {
                        log.info('Article {} still exists skip removal', it)
                    }
                }
            }
        }

        log.info('Job finished')
    }

    private List<Article> getObsoleteArticles() {
        Date obsolescenceDate = new Date() - DAYS_TO_OBSOLETE
        List<Article> articles = articleService.findAllWithPublicationDateOlderThan(obsolescenceDate)
        log.info('Found {} articles to process', articles.size())
        return articles
    }

    private boolean exists(final Article article) {
        HttpResponse response = httpClient.execute(new HttpGet(article.url))
        EntityUtils.consume(response.getEntity())
        return response.statusCode = HttpServletResponse.SC_OK
    }
}
