package vanity.cms.article

import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.Validate
import vanity.article.Tag
import vanity.article.TagService
import vanity.article.review.TagReveiewHint

class ReviewService {

    static transactional = false

    TagService tagService

    public List<Tag> getAllTagsForReview(){
        return tagService.getAllForReview()
    }

    public TagReveiewHint getTagHint(final String id){
        // validate input
        Validate.notEmpty(id, 'Provide not null and not empty tag id')
        // get tag and validate not nullity of it
        Tag tag = Tag.get(id)
        Validate.notNull(tag, "There is no Tag with id ${id}")
        // get all reviewed parent tags
        List<Tag> parentTags = tagService.getAllReviewedParentTags()
        // find similar tags
        List<Tag> similarTags = tagService.getAllTags().findAll({
            it != tag && StringUtils.getLevenshteinDistance(tag.name, it.name) <= 5
        })
        // prepare hint bean and return it
        return new TagReveiewHint(tag, similarTags, parentTags)
    }

    public boolean markAsParentTag(final String id){
        if (!tagService.markTagAsParentTag(id)){
            throw new IllegalArgumentException('Cant perform update for specified id')
        }
    }

}
