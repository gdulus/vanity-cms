package vanity.cms.article

import grails.validation.Validateable

@Validateable
class ConfirmTagReviewCmd {

    public enum Strategy {
        DUPLICATE,
        ALIAS,
        PARENT
    }

    Strategy strategy

    String id

    String parentTagId

    String duplicatedTagId

    static constraints = {
        id(nullable: false)
        strategy(nullable: false)
        parentTagId(nullable: true, validator: {val, obj ->
            if(obj.strategy != Strategy.ALIAS || (obj.strategy == Strategy.ALIAS && val)){
                return true
            } else {
                return 'confirmTagReviewCmd.alias.noSelection'
            }
        })
        duplicatedTagId(validator: {val, obj ->
            if(obj.strategy != Strategy.DUPLICATE || (obj.strategy == Strategy.DUPLICATE && val)){
                return true
            } else {
                return 'confirmTagReviewCmd.duplicate.noSelection'
            }
        })
    }
}
