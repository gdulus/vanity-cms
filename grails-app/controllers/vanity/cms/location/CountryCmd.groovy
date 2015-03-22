package vanity.cms.location

import grails.validation.Validateable

@Validateable
class CountryCmd {

    Long id

    String name

    String isoCode

    static constraints = {
        id(nullable: true)
        name(nullable: false, blank: false)
        isoCode(nullable: false, blank: false)
    }

}
