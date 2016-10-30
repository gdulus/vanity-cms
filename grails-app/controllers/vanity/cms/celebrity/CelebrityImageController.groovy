package vanity.cms.celebrity

import grails.plugin.springsecurity.annotation.Secured
import org.springframework.beans.factory.annotation.Value
import vanity.celebrity.CelebrityService
import vanity.pagination.PaginationParams
import vanity.user.Authority

@Secured([Authority.ROLE_ADMIN])
class CelebrityImageController {

    CelebrityImageService celebrityImageService

    CelebrityService celebrityService

    @Value('${cms.pagination.max}')
    Long defaultMax

    def index(final Long offset, final Long max, final Long celebrityId) {
        Long maxValue = max ?: defaultMax
        PaginationParams paginationParams = new PaginationParams(maxValue, offset, 'dateCreated', [celebrityId: celebrityId])
        [
                paginationBean: celebrityImageService.listWithPagination(paginationParams),
                celebrities   : celebrityService.listAll()
        ]
    }

    def create() {}

    def approve(final Long id) {
        if (celebrityImageService.approve(id)) {
            flash.info = 'vanity.cms.celebrity.image.saved'
        } else {
            flash.error = 'vanity.cms.celebrity.image.error'
        }

        return redirect(action: 'index')
    }

    def delete(final Long id) {
        if (celebrityImageService.delete(id)) {
            flash.info = 'vanity.cms.celebrity.image.saved'
        } else {
            flash.error = 'vanity.cms.celebrity.image.error'
        }

        return redirect(action: 'index')
    }
}

