package vanity.cms.article

import vanity.utils.AjaxUtils

class TagController {

    def reviewService

    def index() {
        [elements:reviewService.getAllTagsForReview()]
    }

    def ajaxGetTagReviewForm(Long id){
        [element: reviewService.getTagHint(id)]
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

    def promoted(){

    }

    def list(){

    }

}
