package vanity.cms.celebrity

import org.codehaus.groovy.grails.commons.GrailsApplication
import vanity.article.TagService
import vanity.celebrity.Celebrity
import vanity.celebrity.CelebrityService
import vanity.cms.image.handler.ImageHandlingException
import vanity.utils.ConfigUtils

class CelebrityController {

    TagService tagService

    CelebrityService celebrityService

    CelebrityAdminService celebrityAdminService

    GrailsApplication grailsApplication

    def index(Long offset, Long max) {
        max = params.max ?: ConfigUtils.$as(grailsApplication.config.cms.celebrity.pagination.max, Long)
        [paginationBean: celebrityService.listWithPagination(max, offset, "lastName")]
    }

    def create() {
        [tags: tagService.getAllValidRootTags()]
    }

    def save(final CelebrityCmd celebrityCmd) {
        if (!celebrityCmd.validate()) {
            flash.error = 'vanity.cms.celebrity.savingDomainError'
            return render(view: 'create', model: [tags: tagService.getAllValidRootTags(), celebrity: celebrityCmd])
        }

        try {
            Celebrity celebrity = celebrityAdminService.save(celebrityCmd.avatar) {
                bindData(it, celebrityCmd.properties, [exclude: 'avatar'])
            }

            if (celebrity.hasErrors()) {
                flash.error = 'vanity.cms.celebrity.savingDomainError'
                return render(view: 'create', model: [tags: tagService.getAllValidRootTags(), celebrity: celebrity])
            } else {
                flash.info = 'vanity.cms.celebrity.saved'
                return redirect(action: 'edit', id: celebrity.id)
            }
        } catch (ImageHandlingException e) {
            flash.error = 'vanity.cms.celebrity.savingImageError'
            return render(view: 'create', model: [tags: tagService.getAllValidRootTags(), celebrity: celebrityCmd])
        }
    }

    def edit(final Long id) {
        [tags: tagService.getAllValidRootTags(), celebrity: celebrityService.read(id)]
    }

    def update(final CelebrityCmd celebrityCmd) {
        try {
            Celebrity celebrity = celebrityAdminService.update(celebrityCmd.id, celebrityCmd.deleteAvatar, celebrityCmd.avatar) {
                bindData(it, params, [exclude: 'avatar'])
            }

            if (!celebrity) {
                flash.error = 'vanity.cms.entity.notFound'
                return redirect(action: 'index')
            }

            if (celebrity.hasErrors()) {
                flash.error = 'vanity.cms.celebrity.savingDomainError'
                return render(view: 'edit', model: [tags: tagService.getAllValidRootTags(), celebrity: celebrity])
            } else {
                flash.info = 'vanity.cms.celebrity.saved'
                return redirect(action: 'edit', id: celebrity.id)
            }
        } catch (ImageHandlingException e) {
            flash.error = 'vanity.cms.celebrity.savingImageError'
            log.error(e)
            return render(view: 'edit', model: [tags: tagService.getAllValidRootTags(), celebrity: celebrityCmd])
        }
    }

    def delete(final Long id) {
        celebrityAdminService.delete(id)
        flash.info = 'vanity.cms.celebrity.deleted'
        redirect(action: 'index')
    }

}
