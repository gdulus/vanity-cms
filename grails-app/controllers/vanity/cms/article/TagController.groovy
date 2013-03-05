package vanity.cms.article

import vanity.utils.AjaxUtils

class TagController {

    static defaultAction = 'review'

    def tagReviewService

    def tagPromotionService

    def review() {
        [elements:tagReviewService.getAllTagsForReview()]
    }

    def ajaxGetTagReviewForm(Long id){
        [element: tagReviewService.getTagHint(id)]
    }

    def ajaxConfirmTagReview(ConfirmTagReviewCmd reviewCmd){
        if (!reviewCmd.validate()){
            render AjaxUtils.renderErrors(reviewCmd.errors)
            return
        }

        try {
            if (performTagReviewAction(reviewCmd)){
                flash.message = 'vanity.cms.tags.review.success'
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

    private boolean performTagReviewAction(ConfirmTagReviewCmd reviewCmd){
        switch(reviewCmd.strategy){
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

    def promoted(){
        [elements:tagPromotionService.getTagsValidForPromotion()]
    }

    def ajaxPromoteTag(Long id){
        if(tagPromotionService.promoteTag(id)){
            render AjaxUtils.Const.SUCCESS_RESPONSE
        } else {
            render AjaxUtils.Const.ERROR_RESPONSE
        }
    }

    def ajaxUnpromoteTag(Long id){
        if(tagPromotionService.unpromoteTag(id)){
            render AjaxUtils.Const.SUCCESS_RESPONSE
        } else {
            render AjaxUtils.Const.ERROR_RESPONSE
        }
    }

    def list(){
    }

}
