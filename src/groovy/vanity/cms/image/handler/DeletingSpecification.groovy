package vanity.cms.image.handler

import grails.validation.Validateable

@Validateable
final class DeletingSpecification {

    String location

    String name

    static constraints = {
        location(nullable: false, blank: false)
        name(nullable: false, blank: false)
    }

    public String getPath() {
        return "${location}/${name}"
    }
}
