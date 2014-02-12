package vanity.cms.article

import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.Validate
import org.springframework.transaction.annotation.Transactional
import vanity.article.*
import vanity.cms.article.review.TagReviewHint
import vanity.pagination.PaginationAware
import vanity.pagination.PaginationBean

class TagReviewService implements PaginationAware<Tag> {

    TagService tagService

    ArticleService articleService

    @Transactional(readOnly = true)
    public PaginationBean<Tag> listWithPagination(final Long max, final Long offset, final String sort) {
        List<Tag> tags = Tag.findAllByStatus(TagStatus.TO_BE_REVIEWED, [sort: sort, max: max, offset: offset])
        return new PaginationBean<Tag>(tags, Tag.countByStatus(TagStatus.TO_BE_REVIEWED))
    }

    @Transactional(readOnly = true)
    public TagReviewHint getTagHint(final Long id) {
        // validate input
        Validate.notNull(id, 'Provide not null and not empty tag id')
        Tag tag = Tag.get(id)
        Validate.notNull(tag, "There is no Tag with id ${id}")
        // get all reviewed parent tags
        List<Tag> parentTags = tagService.findAllValidRootTags()
        // find similar tags
        List<Tag> similarTags = tagService.findAll().findAll({
            (it != tag
                && (it.name.toLowerCase().contains(tag.name.toLowerCase())
                || tag.name.toLowerCase().contains(it.name.toLowerCase())
                || StringUtils.getLevenshteinDistance(tag.name, it.name) <= 5))

        })
        // prepare hint bean and return it
        return new TagReviewHint(tag, similarTags, parentTags)
    }

    @Transactional
    public boolean markAsRoot(final Long id) {
        // validate input
        Validate.notNull(id, 'Provide not null and not empty tag id')
        Tag tag = Tag.get(id)
        tag.root = true
        tag.status = TagStatus.PUBLISHED
        return tag.save() != null
    }

    @Transactional
    public boolean markAsSpam(final Long id) {
        // validate input
        Validate.notNull(id, 'Provide not null and not empty tag id')
        Tag tag = Tag.get(id)
        tag.status = TagStatus.SPAM
        return tag.save() != null
    }

    @Transactional
    public boolean markAsDuplicate(final Long reviewedTagId, final Long duplicatedTagId) {
        // validate input
        Validate.notNull(reviewedTagId, 'Provide not null and not empty reviewed tag id')
        Validate.notNull(duplicatedTagId, 'Provide not null and not empty dulicated tag id')
        // search for related tags
        Tag reviewedTag = Tag.get(reviewedTagId)
        Tag duplicatedTag = Tag.get(duplicatedTagId)
        // validate that tags exists
        Validate.notNull(reviewedTag, "There is no Tag with id ${reviewedTagId}")
        Validate.notNull(duplicatedTag, "There is no Tag with id ${duplicatedTagId}")
        // search for all articles that contain tag that is a duplication
        List<Article> relatedArticles = articleService.findAllByTag(reviewedTag)
        // remove tag that is a duplication and replace it by duplicated tag
        relatedArticles.each { Article it ->
            it.removeFromTags(reviewedTag)
            it.addToTags(duplicatedTag)
            it.save(flush: true)
        }
        // delete duplicated tag
        reviewedTag.delete()
        return true
    }

    @Transactional
    public boolean markAsAlis(final Long reviewedTagId, final List<Long> parentTagIds) {
        // validate input
        Validate.notNull(reviewedTagId, 'Provide not null and not empty reviewed tag id')
        Validate.isTrue((parentTagIds && parentTagIds.size() > 0), 'Provide not null and not empty list of parent tag ids')
        // search for related tags
        Tag reviewedTag = Tag.get(reviewedTagId)
        List<Tag> parentTags = parentTagIds.collect({ Tag.get(it) })
        // validate that tags exists
        Validate.notNull(reviewedTag, "There is no Tag with id ${reviewedTagId}")
        Validate.isTrue(!parentTags.isEmpty(), "There is no Tags for ids ${parentTagIds}")
        // create relation between child tag and parent tag
        parentTags.each { Tag parentTag ->
            parentTag.addToChildTags(reviewedTag)
            parentTag.save()
        }
        // mark child tag as published
        reviewedTag.status = TagStatus.PUBLISHED
        reviewedTag.save() != null
    }

}
