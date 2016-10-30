package vanity.cms.celebrity

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.transaction.annotation.Transactional
import vanity.celebrity.Celebrity

class CelebrityAdminService {

    GrailsApplication grailsApplication

    Celebrity update(final Long id, final Closure binder) {
        Celebrity celebrity = Celebrity.get(id)

        if (!celebrity) {
            return null
        }

        binder.call(celebrity)

        celebrity.save()
        return celebrity
    }

    @Transactional
    Celebrity save(final Closure binder) {
        Celebrity celebrity = new Celebrity()
        binder.call(celebrity)
        celebrity.save()
        return celebrity
    }

    @Transactional
    void delete(final Long id) {
        Celebrity celebrity = Celebrity.get(id)

        if (!celebrity) {
            return
        }

        celebrity.delete()
    }
}
