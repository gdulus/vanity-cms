package vanity.cms.article

import org.springframework.transaction.annotation.Transactional
import vanity.article.Tag
import vanity.article.TagService
import vanity.article.TagStatus

class TagPromotionService {

    TagService tagService

    public List<Tag> getTagsValidForPromotion() {
        return tagService.findAllValidRootTags()
    }

    @Transactional
    public boolean promoteTag(final Long tagId) {
        tagService.updateTagStatus(tagId, TagStatus.PROMOTED)
    }

    @Transactional
    public boolean unPromoteTag(final Long tagId) {
        tagService.updateTagStatus(tagId, TagStatus.PUBLISHED)
    }
}
