package vanity.cms.article

import grails.validation.Validateable
import vanity.article.Tag

@Validateable
class UpdateTagCmd {

    Tag tag

    String name

    Boolean root

    static constraints = {
        tag(nullable: false)
        name(nullable: false, blank: false)
    }

    public boolean hasChildren() {
        tag.hasChildren()
    }

    public boolean getChildTags() {
        tag.childTags
    }

    public Long getId() {
        tag.id
    }

}
