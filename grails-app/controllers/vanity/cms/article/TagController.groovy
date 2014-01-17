package vanity.cms.article

import vanity.article.Tag
import vanity.article.TagService
import vanity.utils.AjaxUtils
import vanity.utils.ConfigUtils

class TagController {

    TagService tagService

    TagAdminService tagAdminService

    TagReviewService tagReviewService

    TagPromotionService tagPromotionService

    def index(final Long offset, final Long max) {
        Long maxValue = max ?: ConfigUtils.$as(grailsApplication.config.cms.tag.pagination.max, Long)
        [paginationBean: tagService.listWithPagination(maxValue, offset, "name")]
    }

    def create() {
        [rootTags: tagService.getAllValidRootTags()]
    }

    def save(final TagCmd tagCmd) {
        if (!tagCmd.validate()) {
            flash.error = 'vanity.cms.tag.savingDomainError'
            return render(view: 'create', model: [rootTags: tagService.getAllValidRootTags(), parentTagsIds: tagCmd.parentTagsIds, tag: tagCmd])
        }

        Tag tag = tagAdminService.save(tagCmd.name, tagCmd.parentTagsIds)

        if (tag.hasErrors()) {
            flash.error = 'vanity.cms.tag.savingDomainError'
            return render(view: 'create', model: [rootTags: tagService.getAllValidRootTags(), parentTagsIds: tagCmd.parentTagsIds, tag: tag])
        } else {
            flash.info = 'vanity.cms.tag.saved'
            return redirect(action: 'edit', id: tag.id)
        }
    }

    def edit(final Long id) {
        Tag tag = tagService.read(id)
        Map<String, ?> model = [tag: tag]

        if (!tag.root) {
            model.rootTags = tagService.getAllValidRootTags()
            model.parentTagsIds = tagService.getParentTags(id)*.id
        }

        return model
    }

    def delete(final Long id) {
        tagAdminService.delete(id)
        flash.info = 'vanity.cms.tag.deleted'
        redirect(action: 'index')
    }

    def review() {
        [elements: tagReviewService.getAllTagsForReview()]
    }

    def ajaxGetTagReviewForm(Long id) {
        [element: tagReviewService.getTagHint(id)]
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

    def ajaxConfirmTagReview(ConfirmTagReviewCmd reviewCmd) {
        if (!reviewCmd.validate()) {
            render AjaxUtils.renderErrors(reviewCmd.errors)
            return
        }

        try {
            if (performTagReviewAction(reviewCmd)) {
                flash.info = 'vanity.cms.tags.review.success'
                render AjaxUtils.Const.SUCCESS_RESPONSE
            } else {
                flash.error = 'vanity.cms.tags.review.error'
                render AjaxUtils.Const.ERROR_RESPONSE
            }
        } catch (IllegalArgumentException exc) {
            flash.error = 'vanity.cms.tags.review.error'
            render AjaxUtils.Const.ERROR_RESPONSE
        }
    }

    private boolean performTagReviewAction(ConfirmTagReviewCmd reviewCmd) {
        switch (reviewCmd.strategy) {
            case ConfirmTagReviewCmd.Strategy.DUPLICATE:
                return tagReviewService.markAsDuplicateTag(reviewCmd.id, reviewCmd.duplicatedTagId)
            case ConfirmTagReviewCmd.Strategy.ALIAS:
                return tagReviewService.markAsAlisTag(reviewCmd.id, reviewCmd.parentTagIds)
            case ConfirmTagReviewCmd.Strategy.PARENT:
                return tagReviewService.markAsParentTag(reviewCmd.id)
            default:
                return false
        }
    }

}
