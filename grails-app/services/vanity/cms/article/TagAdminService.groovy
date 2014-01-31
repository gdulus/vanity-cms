package vanity.cms.article

import org.springframework.transaction.annotation.Transactional
import vanity.article.ArticleService
import vanity.article.Tag
import vanity.article.TagStatus
import vanity.pagination.PaginationAware
import vanity.pagination.PaginationBean

class TagAdminService implements PaginationAware<Tag> {

    ArticleService articleService

    @Transactional(readOnly = true)
    public PaginationBean<Tag> listWithPagination(final Long max, final Long offset, final String sort) {
        return new PaginationBean<Tag>(Tag.list(max: max, offset: offset, sort: sort), Tag.count())
    }

    @Transactional
    void delete(final Long id) {
        Tag tag = Tag.get(id)

        if (!tag) {
            return
        }

        articleService.findAllByTag(tag)*.removeFromTags(tag)
        tag.delete()
    }

    @Transactional
    Tag save(final String name, final List<Long> parentTagIds) {
        Tag tag = new Tag(
            name: name,
            status: TagStatus.PUBLISHED,
            root: false
        )

        if (!tag.save()) {
            return tag
        }

        List<Tag> parentTags = parentTagIds.collect { Tag.load(it) }
        parentTags*.addToChildTags(tag)
        return tag
    }

    @Transactional
    Tag update(final Long id, final String name, final List<Long> parentTagIds) {
        Tag tag = Tag.get(id)

        if (!tag) {
            return null
        }

        tag.name = name

        if (!tag.save()) {
            return tag
        }

        List<Tag> parentTags = parentTagIds.collect { Tag.load(it) }
        parentTags*.addToChildTags(tag)
        return tag
    }
}
