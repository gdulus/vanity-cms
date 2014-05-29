package vanity.cms.article

import grails.validation.Validateable

@Validateable
class CrateTagCmd {

    String name

    Boolean root

    static constraints = {
        name(nullable: false, blank: false)
    }

}
