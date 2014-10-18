package vanity.cms.article

import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.Validate
import org.springframework.transaction.annotation.Transactional
import vanity.Touple
import vanity.article.ArticleService
import vanity.article.Tag
import vanity.article.TagService
import vanity.article.TagStatus
import vanity.cms.article.review.TagReviewHint
import vanity.pagination.PaginationAware
import vanity.pagination.PaginationBean

class TagReviewService implements PaginationAware<Tag> {

    TagService tagService

    ArticleService articleService

    @Transactional(readOnly = true)
    public PaginationBean<Touple<Tag, Long>> listWithPagination(final Long max, final Long offset, final String sort, final String query) {

        Map<String, ?> dataParams = [
            status: TagStatus.TO_BE_REVIEWED,
            max: max,
            offset: offset ?: 0
        ]

        Map<String, ?> countParams = [
            status: TagStatus.TO_BE_REVIEWED,
        ]

        String hqlDataQuery = """
            select
                t.id,
                count(*) as articles
            from
                Article a
            left join
                a.tags as t
            where
                t.status = :status
        """

        String hqlCountQuery = """
            select
                count(*)
            from
                Tag t
            where
                t.status = :status
            """

        if (query) {
            hqlDataQuery += ' and lower(t.name) like :query '
            hqlCountQuery += ' and lower(t.name) like :query '
            dataParams['query'] = "%${query?.toLowerCase()}%"
            countParams['query'] = "%${query?.toLowerCase()}%"
        }

        hqlDataQuery += '''
            group by
                t
            order by
                articles desc,
                t.name
        '''

        List<Touple<Tag, Long>> tags = Tag.executeQuery(hqlDataQuery, dataParams).collect {
            return new Touple<>(Tag.read(it[0]), it[1])
        }

        int count = Tag.executeQuery(hqlCountQuery, countParams)[0]
        return new PaginationBean<Touple<Tag, Long>>(tags, count)
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
    public boolean markAllAsRoot(final List<Long> ids) {
        boolean result = true
        ids.each {
            if (!markAsRoot(it)) {
                result = false
            }
        }
        return result
    }

    @Transactional
    public boolean markAsRoot(final Long id) {
        // validate input
        Validate.notNull(id, 'Provide not null and not empty tag id')
        Tag tag = Tag.get(id)
        tag.root = true
        tag.status = TagStatus.PUBLISHED
        // bump last update for all related articles to reindex them
        articleService.updateLastUpdatedForAllByTag(tag)
        return tag.save() != null
    }

    @Transactional
    public boolean markAllAsSpam(final List<Long> ids) {
        boolean result = true
        ids.each {
            if (!markAsSpam(it)) {
                result = false
            }
        }
        return result
    }

    @Transactional
    public boolean markAsSpam(final Long id) {
        Validate.notNull(id, 'Provide not null and not empty tag id')
        Tag tag = Tag.get(id)
        articleService.updateLastUpdatedForAllByTag(tag)
        return tagService.setStatus(tag, TagStatus.SPAM)
    }

    @Transactional
    public boolean markAsNotSpam(final Long id) {
        Validate.notNull(id, 'Provide not null and not empty tag id')
        Tag tag = Tag.get(id)
        articleService.updateLastUpdatedForAllByTag(tag)
        return tagService.setStatus(tag, TagStatus.PUBLISHED)
    }

    @Transactional
    public boolean markAsDuplicate(final Long reviewedTagId, final Long duplicatedTagId) {
        markAsAlis(reviewedTagId, [duplicatedTagId])
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
            // bump last update for all related articles to reindex them
            articleService.updateLastUpdatedForAllByTag(parentTag)
        }
        // mark child tag as published
        reviewedTag.status = TagStatus.PUBLISHED
        reviewedTag.save() != null
    }

}
