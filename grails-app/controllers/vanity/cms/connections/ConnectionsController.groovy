package vanity.cms.connections

import grails.plugin.springsecurity.annotation.Secured
import vanity.user.Authority

@Secured([Authority.ROLE_ADMIN])
class ConnectionsController {

    def index() {
    }
}
