package vanity.cms.article

import grails.converters.JSON

class ReviewController {

    def reviewService

    def index() {
        [elements:reviewService.getAllTagsForReview()]
    }

    def ajaxGetTagReviewForm(){
        [element: reviewService.getTagHint(params.id)]
    }

    def ajaxConfirmTagReview(ConfirmTagReviewCmd reviewCmd){

        if (reviewCmd.parentTag){
            reviewService.markAsParentTag(reviewCmd.id)
            render([succes:true] as JSON)
        } else {
            render([succes:false] as JSON)
        }
    }
}
