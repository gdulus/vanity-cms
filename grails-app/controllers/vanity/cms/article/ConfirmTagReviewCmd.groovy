package vanity.cms.article

import grails.validation.Validateable

@Validateable
class ConfirmTagReviewCmd {

    public enum Strategy {
        DUPLICATE,
        ALIAS,
        PARENT
    }

    Long id

    Long duplicatedTagId

    String serializedParentTagIds

    Strategy strategy

    static constraints = {
        id(nullable: false)
        strategy(nullable: false)
        serializedParentTagIds(nullable: true, validator: {val, obj ->
            if(obj.strategy == Strategy.ALIAS && (!val || !obj.parentTagIds)){
                return 'confirmTagReviewCmd.alias.noSelection'
            } else {
                return true
            }
        })
        duplicatedTagId(nullable: true, validator: {val, obj ->
            if(obj.strategy == Strategy.DUPLICATE && !val){
                return 'confirmTagReviewCmd.duplicate.noSelection'
            } else {
                return true
            }
        })
    }

    public List<Long> getParentTagIds(){
        return serializedParentTagIds.tokenize().collect({it.toLong()})
    }
}
