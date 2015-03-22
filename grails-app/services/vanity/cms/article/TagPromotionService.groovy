package vanity.cms.article

import org.springframework.transaction.annotation.Transactional
import vanity.article.Tag
import vanity.article.TagService
import vanity.article.TagStatus
import vanity.pagination.PaginationAware
import vanity.pagination.PaginationBean
import vanity.pagination.PaginationParams

class TagPromotionService implements PaginationAware<Tag> {

    TagService tagService

    @Override
    PaginationBean<Tag> listWithPagination(final PaginationParams params) {
        Map<String, ?> queryParams = [:]
        String query = 'from Tag where status in :statuses '
        queryParams['statuses'] = (params?.queryParams?.promoted ? [TagStatus.PROMOTED] : TagStatus.OPEN_STATUSES)

        if (params?.queryParams?.query) {
            query += ' and normalizedName like :query'
            queryParams['query'] = "%${params.queryParams.query.toString().encodeAsPrettyUrl()}%"
        }

        List<Tag> tags = Tag.executeQuery(query, queryParams, [max: params.max, offset: params.offset])
        Integer count = Tag.executeQuery('select count(id) ' + query, queryParams).first()
        return new PaginationBean<Tag>(tags, count)

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
