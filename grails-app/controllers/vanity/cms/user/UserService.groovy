package vanity.cms.user

import vanity.celebrity.Celebrity
import vanity.pagination.PaginationAware
import vanity.pagination.PaginationBean
import vanity.pagination.PaginationParams
import vanity.user.Authority
import vanity.user.Role
import vanity.user.User
import vanity.user.UserRole

class UserService implements PaginationAware<Celebrity> {

    PaginationBean<User> listWithPagination(final PaginationParams params) {
        if (params.queryParams.username) {
            Role role = Role.findByAuthority(Authority.ROLE_PORTAL_USER)
            String like = "%${params.queryParams.username}%"
            List<User> result = UserRole.executeQuery('''
                select
                    r.user
                from
                    UserRole as r
                where
                    lower(r.user.username) like :likeStatement
                    and r.role = :role
                order by
                    :sort
                ''',
                    [
                            likeStatement: like,
                            role         : role,
                            max          : params.max,
                            offset       : params.offset ?: 0,
                            sort         : params.sort
                    ]
            )

            int count = UserRole.executeQuery('''
                select
                     count(*)
                from
                    UserRole as r
                where
                    lower(r.user.username) like :likeStatement
                    and r.role = :role
                ''',
                    [
                            likeStatement: like,
                            role         : role
                    ]
            )[0]

            return new PaginationBean<>(result, count)
        } else {
            Role role = Role.findByAuthority(Authority.ROLE_PORTAL_USER)
            List<User> result = UserRole.executeQuery('''
                select
                    r.user
                from
                    UserRole as r
                where
                    r.role = :role
                order by
                    :sort
                ''',
                    [
                            role  : role,
                            max   : params.max,
                            offset: params.offset ?: 0,
                            sort  : params.sort
                    ]
            )

            int count = UserRole.executeQuery('''
                select
                     count(*)
                from
                    UserRole as r
                where
                    r.role = :role
                ''',
                    [
                            role: role
                    ]
            )[0]
            return new PaginationBean<>(result, count)
        }
    }

    public void block(final Long id) {
        User user = User.get(id)
        user.enabled = false
        user.save()
    }

    public void unblock(final Long id) {
        User user = User.get(id)
        user.enabled = true
        user.save()
    }
}
