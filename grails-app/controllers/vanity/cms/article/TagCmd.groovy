package vanity.cms.article

import grails.validation.Validateable

@Validateable
class TagCmd {

    Long id

    String name

    // only for validation purpose
    Boolean root = false

    List<String> parentTagsIds

    static constraints = {
        id(nullable: true)
        name(nullable: false, blank: false)
        parentTagsIds(validator: { val, obj -> obj.root || val.size() > 0 })
    }

    public List<Long> getParentTagsIds() {
        this.@parentTagsIds.collect { it.toLong() }
    }

}
