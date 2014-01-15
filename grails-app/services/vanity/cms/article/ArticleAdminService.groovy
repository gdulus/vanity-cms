package vanity.cms.article

import org.springframework.transaction.annotation.Transactional
import vanity.article.Article

class ArticleAdminService {

    @Transactional
    void delete(final Long id) {
        Article article = Article.get(id)

        if (!article) {
            return
        }

        article.delete()
    }
}
