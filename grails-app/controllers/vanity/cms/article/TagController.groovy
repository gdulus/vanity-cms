package vanity.cms.article

import grails.plugin.springsecurity.annotation.Secured
import vanity.article.Tag
import vanity.article.TagService
import vanity.user.Authority
import vanity.utils.ConfigUtils

@Secured(['ROLE_ADMIN'])
class TagController {

    TagService tagService

    TagAdminService tagAdminService

    TagReviewService tagReviewService

    TagPromotionService tagPromotionService

    def index(final Long offset, final Long max) {
        Long maxValue = max ?: ConfigUtils.$as(grailsApplication.config.cms.tag.pagination.max, Long)
        [paginationBean: tagAdminService.listWithPagination(maxValue, offset, "name")]
    }

    def create() {
        [rootTags: tagService.findAllValidRootTags()]
    }

    def save(final TagCmd tagCmd) {
        if (!tagCmd.validate()) {
            flash.error = 'vanity.cms.tag.savingDomainError'
            return render(view: 'create', model: [rootTags: tagService.findAllValidRootTags(), parentTagsIds: tagCmd.parentTagsIds, tag: tagCmd])
        }

        Tag tag = tagAdminService.save(tagCmd.name, tagCmd.parentTagsIds)

        if (tag.hasErrors()) {
            flash.error = 'vanity.cms.tag.savingDomainError'
            return render(view: 'create', model: [rootTags: tagService.findAllValidRootTags(), parentTagsIds: tagCmd.parentTagsIds, tag: tag])
        } else {
            flash.info = 'vanity.cms.tag.saved'
            return redirect(action: 'edit', id: tag.id)
        }
    }

    def edit(final Long id) {
        Tag tag = tagService.read(id)

        if (!tag) {
            flash.error = 'vanity.cms.entity.notFound'
            return redirect(action: 'index')
        }

        Map<String, Object> model = [tag: tag]

        if (!tag.root) {
            model.parentTags = tagService.findAllByParentTags(id)
        }

        return model
    }

    def update(final TagCmd tagCmd) {
        if (!tagCmd.validate()) {
            flash.error = 'vanity.cms.tag.savingDomainError'
            return render(view: 'edit', model: [rootTags: tagService.findAllValidRootTags(), parentTagsIds: tagCmd.parentTagsIds, tag: tagCmd])
        }

        Tag tag = tagAdminService.update(tagCmd.id, tagCmd.name, tagCmd.parentTagsIds)

        if (!tag) {
            flash.error = 'vanity.cms.entity.notFound'
            return redirect(action: 'index')
        }

        if (tag.hasErrors()) {
            flash.error = 'vanity.cms.tag.savingDomainError'
            return render(view: 'edit', model: [rootTags: tagService.findAllValidRootTags(), parentTagsIds: tagCmd.parentTagsIds, tag: tag])
        } else {
            flash.info = 'vanity.cms.tag.saved'
            return redirect(action: 'edit', id: tag.id)
        }

    }

    def delete(final Long id) {
        tagAdminService.delete(id)
        flash.info = 'vanity.cms.tag.deleted'
        redirect(action: 'index')
    }

    def promoted() {
        [elements: tagPromotionService.getTagsValidForPromotion()]
    }

    def promoteTag(Long id) {
        if (tagPromotionService.promoteTag(id)) {
            flash.info = 'vanity.cms.tags.promote.success'
        } else {
            flash.error = 'vanity.cms.tags.promote.error'
        }
        redirect(action: 'promoted')
    }

    def unPromoteTag(Long id) {
        if (tagPromotionService.unPromoteTag(id)) {
            flash.info = 'vanity.cms.tags.unPromote.success'
        } else {
            flash.error = 'vanity.cms.tags.unPromote.error'
        }
        redirect(action: 'promoted')
    }

    @Secured([Authority.ROLE_ADMIN, Authority.ROLE_REVIEWER])
    def review(final Long offset, final Long max) {
        Long maxValue = max ?: ConfigUtils.$as(grailsApplication.config.cms.tag.pagination.max, Long)
        [paginationBean: tagReviewService.listWithPagination(maxValue, offset, 'name')]
    }

    @Secured([Authority.ROLE_ADMIN, Authority.ROLE_REVIEWER])
    def reviewMoreOptions(final Long id) {
        [element: tagReviewService.getTagHint(id)]
    }

    @Secured([Authority.ROLE_ADMIN, Authority.ROLE_REVIEWER])
    def markAsRootTag(final Long id) {
        tagReviewService.markAsRoot(id)
        flash.info = 'vanity.cms.tags.review.success'
        redirect(action: 'review', params: [offset: params.offset, max: params.max])
    }

    @Secured([Authority.ROLE_ADMIN, Authority.ROLE_REVIEWER])
    def markAsSpam(final Long id) {
        tagReviewService.markAsSpam(id)
        flash.info = 'vanity.cms.tags.review.success'
        redirect(action: 'review', params: [offset: params.offset, max: params.max])
    }

    @Secured([Authority.ROLE_ADMIN, Authority.ROLE_REVIEWER])
    def confirmTagsReview(ConfirmTagsReviewCmd reviewCmd) {
        if (!reviewCmd.validate()) {
            flash.error = 'vanity.cms.tags.review.error.validate'
            return redirect(action: 'review')
        }

        try {
            if (performTagsReviewAction(reviewCmd)) {
                flash.info = 'vanity.cms.tags.review.success'
                redirect(action: 'review')
            } else {
                flash.error = 'vanity.cms.tags.review.error'
                redirect(action: 'review')
            }
        } catch (IllegalArgumentException exc) {
            flash.error = 'vanity.cms.tags.review.error'
            redirect(action: 'review')
        }
    }

    private boolean performTagsReviewAction(ConfirmTagsReviewCmd reviewCmd) {
        switch (reviewCmd.strategy) {
            case ConfirmTagsReviewCmd.Strategy.ROOT:
                return tagReviewService.markAllAsRoot(reviewCmd.tagIds)
            case ConfirmTagsReviewCmd.Strategy.SPAM:
                return tagReviewService.markAllAsSpam(reviewCmd.tagIds)
            default:
                return false
        }
    }

    @Secured([Authority.ROLE_ADMIN, Authority.ROLE_REVIEWER])
    def confirmTagReview(ConfirmTagReviewCmd reviewCmd) {
        if (!reviewCmd.validate()) {
            flash.error = 'vanity.cms.tags.review.error.validate'
            return render(view: 'reviewMoreOptions', model: [element: tagReviewService.getTagHint(reviewCmd.id)])
        }

        try {
            if (performTagReviewAction(reviewCmd)) {
                flash.info = 'vanity.cms.tags.review.success'
                redirect(action: 'review')
            } else {
                flash.error = 'vanity.cms.tags.review.error'
                redirect(action: 'review')
            }
        } catch (IllegalArgumentException exc) {
            flash.error = 'vanity.cms.tags.review.error'
            redirect(action: 'review')
        }
    }

    private boolean performTagReviewAction(ConfirmTagReviewCmd reviewCmd) {
        switch (reviewCmd.strategy) {
            case ConfirmTagReviewCmd.Strategy.DUPLICATE:
                return tagReviewService.markAsDuplicate(reviewCmd.id, reviewCmd.duplicatedTagId)
            case ConfirmTagReviewCmd.Strategy.ALIAS:
                return tagReviewService.markAsAlis(reviewCmd.id, reviewCmd.parentTagIds)
            case ConfirmTagReviewCmd.Strategy.ROOT:
                return tagReviewService.markAsRoot(reviewCmd.id)
            case ConfirmTagReviewCmd.Strategy.SPAM:
                return tagReviewService.markAsSpam(reviewCmd.id)
            default:
                return false
        }
    }

}
