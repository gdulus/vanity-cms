package vanity.cms.article

import grails.validation.Validateable

@Validateable
class ConfirmTagsReviewCmd {

    public enum Strategy {
        ROOT,
        SPAM
    }

    Strategy strategy

    String serializedTagIds

    static constraints = {
        strategy(nullable: false)
        serializedTagIds(nullable: false)
    }

    public List<Long> getTagIds() {
        return serializedTagIds.tokenize(',').collect({ it.toLong() })
    }
}
