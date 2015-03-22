package vanity.cms.article

import org.springframework.transaction.annotation.Transactional
import vanity.article.Tag
import vanity.article.TagStatus
import vanity.pagination.PaginationAware
import vanity.pagination.PaginationBean
import vanity.pagination.PaginationParams

class TagAdminService implements PaginationAware<Tag> {

    @Transactional(readOnly = true)
    public PaginationBean<Tag> listWithPagination(final PaginationParams params) {
        List<TagStatus> statuses = TagStatus.OPEN_STATUSES + [TagStatus.SPAM]

        if (!params.queryParams.query) {
            List<Tag> tags = Tag.findAllByStatusInList(statuses, [sort: params.sort, max: params.max, offset: params.offset])
            int count = Tag.countByStatusInList(statuses)
            return new PaginationBean<Tag>(tags, count)
        }

        String likeStatement = "%${params.queryParams.query.toString().encodeAsPrettyUrl()}%"

        List<Tag> tags = Tag.executeQuery("""
                select
                    id
                from
                    Tag t
                where
                    normalizedName like :query
                    and status in (:openStatuses)
                order by
                    :sort
            """,
            [
                query: likeStatement,
                openStatuses: statuses,
                max: params.max,
                offset: params.offset ?: 0,
                sort: params.sort
            ]
        ).collect { Long it -> Tag.read(it) }

        int count = Tag.executeQuery("""
                select
                    count(*)
                from
                    Tag t
                where
                    lower(name) like :query
                    and status in (:openStatuses)
            """,
            [
                query: likeStatement,
                openStatuses: statuses

            ]
        )[0]

        return new PaginationBean<Tag>(tags, count)

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
