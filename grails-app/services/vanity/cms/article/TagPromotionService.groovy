package vanity.cms.article

import org.springframework.transaction.annotation.Transactional
import vanity.article.Status
import vanity.article.Tag
import vanity.article.TagService

class TagPromotionService {

    TagService tagService

    public List<Tag> getTagsValidForPromotion() {
        return tagService.getAllValidRootTags()
    }

    @Transactional
    public boolean promoteTag(final Long tagId) {
        tagService.changeTagStatus(tagId, Status.Tag.PROMOTED)
    }

    @Transactional
    public boolean unPromoteTag(final Long tagId) {
        tagService.changeTagStatus(tagId, Status.Tag.PUBLISHED)
    }
}
