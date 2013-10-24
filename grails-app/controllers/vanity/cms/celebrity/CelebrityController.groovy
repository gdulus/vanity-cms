package vanity.cms.celebrity

import vanity.celebrity.Celebrity
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

        def celebrity = new Celebrity()
        bindData(celebrity, celebrityCmd.properties, [exclude: 'avatar'])

        try {
            def savedCelebrity = celebrityAdminService.save(celebrity, celebrityCmd.avatar)

            if (!savedCelebrity) {
                flash.error = 'vanity.cms.celebrity.savingDomainError'
                return render(view: 'create', model: [tags: tagService.getAllValidRootTags(), celebrity: celebrity])
            } else {
                flash.info = 'vanity.cms.celebrity.saved'
                return redirect(action: 'edit', id: savedCelebrity.id)
            }
        } catch (ImageHandlingException e) {
            flash.error = 'vanity.cms.celebrity.savingImageError'
            return render(view: 'create', model: [tags: tagService.getAllValidRootTags(), celebrity: celebrity])
        }
    }

    def edit(final Long id) {
        [tags: tagService.getAllValidRootTags(), celebrity: celebrityService.read(id)]
    }

    def update(final CelebrityCmd celebrityCmd) {
        def celebrity = celebrityService.get(celebrityCmd.id)
        bindData(celebrity, params, [exclude: 'avatar'])

        try {
            def savedCelebrity = celebrityAdminService.save(celebrity, celebrityCmd.avatar)

            if (!savedCelebrity) {
                flash.error = 'vanity.cms.celebrity.savingDomainError'
                return render(view: 'edit', model: [tags: tagService.getAllValidRootTags(), celebrity: celebrity])
            } else {
                flash.info = 'vanity.cms.celebrity.saved'
                return redirect(action: 'edit', id: savedCelebrity.id)
            }
        } catch (ImageHandlingException e) {
            flash.error = 'vanity.cms.celebrity.savingImageError'
            return render(view: 'edit', model: [tags: tagService.getAllValidRootTags(), celebrity: celebrity])
        }
    }

    def delete(final Long id) {
        celebrityAdminService.delete(id)
        flash.info = 'vanity.cms.celebrity.deleted'
        redirect(action: 'index')
    }

}
