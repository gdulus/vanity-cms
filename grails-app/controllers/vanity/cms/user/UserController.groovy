package vanity.cms.user

import grails.plugin.springsecurity.annotation.Secured
import org.springframework.beans.factory.annotation.Value
import vanity.pagination.PaginationParams
import vanity.user.Authority

@Secured([Authority.ROLE_ADMIN])
class UserController {

    UserService userService

    @Value('${cms.pagination.max}')
    Long defaultMax

    def index(final Long offset, final Long max, final String username) {
        Long maxValue = max ?: defaultMax
        PaginationParams paginationParams = new PaginationParams(maxValue, offset, 'username', [username: username])
        [paginationBean: userService.listWithPagination(paginationParams),]
    }

    def block(final Long id) {
        userService.block(id)
        flash.info = 'vanity.cms.user.blocked'
        return redirect(action: 'index')
    }

    def unblock(final Long id) {
        userService.unblock(id)
        flash.info = 'vanity.cms.user.unblocked'
        return redirect(action: 'index')
    }
}

