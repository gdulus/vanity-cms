package vanity.cms.cleanup

import groovy.util.logging.Slf4j
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import vanity.article.Article
import vanity.article.ArticleService
import vanity.cms.article.ArticleAdminService

import javax.servlet.http.HttpServletResponse

@Slf4j
class RemoveNotExistingArticlesJob {

    private static final int DAYS_TO_OBSOLETE

    static triggers = {
        cron name: 'RemoveNotExistingArticlesJob', cronExpression: '0 0/1 * * * ?'
    }

    def concurrent = false

    ArticleService articleService

    ArticleAdminService articleAdminService

    def execute() {
        log.info('Starting job')
        Date obsoletionDate = new Date() - DAYS_TO_OBSOLETE
        List<Article> articles = articleService.findAllWithPublicationDateOlderThan(obsoletionDate)
        log.info('Found {} articles to process', articles.size())

        articles.each {
            if (!exists(it)) {
                log.info('Article {} not exists anymore - marking for deletion', it)
                articleAdminService.markAsDeleted(it.id)
            } else {
                log.info('Article {} still exists skip removal', it)
            }
        }

        log.info('Job finished')
    }

    private boolean exists(final Article article) {
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(new HttpGet(article.url))
        return response.statusCode = HttpServletResponse.SC_OK
    }
}
