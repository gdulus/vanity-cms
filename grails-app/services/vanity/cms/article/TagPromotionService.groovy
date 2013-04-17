package vanity.cms.article

import org.springframework.transaction.annotation.Transactional
import vanity.article.Status
import vanity.article.Tag
import vanity.article.TagService

class TagPromotionService {

    static transactional = false

    TagService tagService

    public List<Tag> getTagsValidForPromotion() {
        return tagService.getAllValidTags()
    }

    @Transactional
    public boolean promoteTag(final Long tagId){
        tagService.changeTagStatus(tagId, Status.Tag.PROMOTED)
    }

    @Transactional
    public boolean unPromoteTag(final Long tagId){
        tagService.changeTagStatus(tagId, Status.Tag.PUBLISHED)
    }
}
