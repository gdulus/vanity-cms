package vanity.cms.celebrity

import vanity.cms.image.handler.ImageHandlingException

class CelebrityController {

    def tagService

    def celebrityService

    def celebrityAdminService

    def index() {
        [paginationBean: celebrityService.listWithPagination(params)]
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
            def celebrity = celebrityAdminService.save(celebrityCmd.avatar) {
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
            def celebrity = celebrityAdminService.update(celebrityCmd.id, celebrityCmd.deleteAvatar, celebrityCmd.avatar) {
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
