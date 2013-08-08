package vanity.cms.article

import vanity.utils.AjaxUtils

class TagController {

    def tagReviewService

    def tagPromotionService

    def index(){
        redirect(action: 'review')
    }

    def edit(){

    }

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

    def promoteTag(Long id){
        if(tagPromotionService.promoteTag(id)){
            flash.info = 'vanity.cms.tags.promote.success'
        } else {
            flash.error = 'vanity.cms.tags.promote.error'
        }
        redirect(action: 'promoted')
    }

    def unPromoteTag(Long id){
        if(tagPromotionService.unPromoteTag(id)){
            flash.info = 'vanity.cms.tags.unPromote.success'
        } else {
            flash.error = 'vanity.cms.tags.unPromote.error'
        }
        redirect(action: 'promoted')
    }

    def list(){
    }

}
