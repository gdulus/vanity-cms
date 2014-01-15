package vanity.cms.article

import grails.validation.Validateable
import vanity.article.Tag

@Validateable
class ArticleCmd {

    Long id

    String title

    String body

    List<Long> tags

    static constraints = {
        id(nullable: false)
        title(nullable: false, blank: false)
        body(nullable: false, blank: false)
        tags(minSize: 1)
    }

    List<Tag> getTags() {
        return tags.collect { Tag.read(it) }
    }
}
