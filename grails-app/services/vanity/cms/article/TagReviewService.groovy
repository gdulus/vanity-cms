package vanity.cms.article

import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.Validate
import org.springframework.transaction.annotation.Transactional
import vanity.article.*
import vanity.cms.article.review.TagReveiewHint

class TagReviewService {

    static transactional = false

    TagService tagService

    ArticleService articleService

    @Transactional(readOnly = true)
    public List<Tag> getAllTagsForReview(){
        return Tag.findAllByStatus(Status.Tag.TO_BE_REVIEWED, [sort:'name'])
    }

    @Transactional(readOnly = true)
    public TagReveiewHint getTagHint(final Long id){
        // validate input
        Validate.notNull(id, 'Provide not null and not empty tag id')
        Tag tag = Tag.get(id)
        Validate.notNull(tag, "There is no Tag with id ${id}")
        // get all reviewed parent tags
        List<Tag> parentTags = tagService.getAllValidTags()
        // find similar tags
        List<Tag> similarTags = tagService.getAllTags().findAll({
            (it != tag
                && (it.name.toLowerCase().contains(tag.name.toLowerCase())
                    || tag.name.toLowerCase().contains(it.name.toLowerCase())
                    || StringUtils.getLevenshteinDistance(tag.name, it.name) <= 5))

        })
        // prepare hint bean and return it
        return new TagReveiewHint(tag, similarTags, parentTags)
    }

    @Transactional
    public boolean markAsParentTag(final Long id){
        // validate input
        Validate.notNull(id, 'Provide not null and not empty tag id')
        Tag tag = Tag.get(id)
        Validate.notNull(tag, "There is no Tag with id ${id}")
        // mark tag as root one and mark it as published
        tag.root = true
        tag.status = Status.PUBLISHED
        return tag.save() != null
    }

    @Transactional
    public boolean markAsDuplicateTag(final Long reviewedTagId, final Long duplicatedTagId){
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
        List<Article> relatedArticles = articleService.getByTag(reviewedTag)
        // remove tag that is a duplication and replace it by duplicated tag
        relatedArticles.each {Article it ->
            it.removeFromTags(reviewedTag)
            it.addToTags(duplicatedTag)
            it.save(flush: true)
        }
        // delete duplicated tag
        reviewedTag.delete()
    }

    @Transactional
    public boolean markAsAlisTag(final Long reviewedTagId, final List<Long> parentTagIds){
        // validate input
        Validate.notNull(reviewedTagId, 'Provide not null and not empty reviewed tag id')
        Validate.isTrue((parentTagIds && parentTagIds.size() > 0), 'Provide not null and not empty list of parent tag ids')
        // search for related tags
        Tag reviewedTag = Tag.get(reviewedTagId)
        List<Tag> parentTags = parentTagIds.collect({Tag.get(it)})
        // validate that tags exists
        Validate.notNull(reviewedTag, "There is no Tag with id ${reviewedTagId}")
        Validate.isTrue(!parentTags.isEmpty(), "There is no Tags for ids ${parentTagIds}")
        // create relation between child tag and parent tag
        parentTags.each {Tag parentTag ->
            parentTag.addToChildTags(reviewedTag)
            parentTag.save()
        }
        // mark child tag as published
        reviewedTag.status = Status.PUBLISHED
        reviewedTag.save() != null
    }

}
