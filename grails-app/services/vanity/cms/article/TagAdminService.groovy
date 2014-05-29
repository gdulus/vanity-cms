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
        List<Tag> tags = Tag.findAllByStatusInList(TagStatus.OPEN_STATUSES, [sort: sort, max: max, offset: offset])
        return new PaginationBean<Tag>(tags, Tag.countByStatusInList(TagStatus.OPEN_STATUSES))
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
    Tag save(final String name, final Boolean root) {
        Tag tag = new Tag(name: name, status: TagStatus.PUBLISHED, root: root)
        tag.save()
        return tag
    }

    @Transactional
    Tag update(final Long id, final String name, final Boolean root) {
        Tag tag = Tag.get(id)

        if (!tag) {
            return null
        }

        if (!root) {
            tag.childTags.clear()
        }

        tag.name = name
        tag.root = root
        tag.save()
        return tag
    }

    @Transactional
    Tag updateRelations(final Long id, final List<Long> parentsToAdd, final List<Long> parentsToRemove, final List<Long> childrenToAdd, final List<Long> childrenToRemove) {
        Tag tag = Tag.get(id)

        if (!tag) {
            return null
        }

        parentsToAdd.each { Tag.get(it).addToChildTags(tag) }
        parentsToRemove.each { Tag.get(it).removeFromChildTags(tag) }
        childrenToAdd.each { tag.addToChildTags(Tag.load(it)) }
        childrenToRemove.each { tag.removeFromChildTags(Tag.load(it)) }

        tag.save()
        return tag
    }
}
