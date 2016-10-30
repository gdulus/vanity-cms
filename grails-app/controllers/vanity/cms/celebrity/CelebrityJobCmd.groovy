package vanity.cms.celebrity

import grails.validation.Validateable

@Validateable
class CelebrityJobCmd {

    Long id

    String name

    static constraints = {
        id(nullable: true)
        name(nullable: false, blank: false)
    }
}
