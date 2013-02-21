package vanity.cms.article

import vanity.cms.utils.AjaxUtils

class ReviewController {

    def reviewService

    def index() {
        [elements:reviewService.getAllTagsForReview()]
    }

    def ajaxGetTagReviewForm(){
        [element: reviewService.getTagHint(params.id)]
    }

    def ajaxConfirmTagReview(ConfirmTagReviewCmd reviewCmd){
        // data valid?
        if (!reviewCmd.validate()){
            render AjaxUtils.renderErrors(reviewCmd.errors)
            return
        }
        // iterate over strategies
        switch(reviewCmd.strategy){
            case ConfirmTagReviewCmd.Strategy.DUPLICATE:
                break;
            case ConfirmTagReviewCmd.Strategy.ALIAS:
                break;
            case ConfirmTagReviewCmd.Strategy.PARENT:
                reviewService.markAsParentTag(reviewCmd.id)
                render AjaxUtils.Const.SUCCESS_RESPONSE
                break;
            default:
                throw new IllegalStateException("Not supported ")
        }
    }

}
