package vanity.cms.celebrity

import org.springframework.web.multipart.MultipartFile
import vanity.celebrity.Celebrity

class CelebrityController {

    def tagService

    def celebrityService

    def index(){
        [paginationBean:celebrityService.listWithPagination(params)]
    }

    def create(){
        [tags:tagService.getAllValidRootTags()]
    }

    def save(final CelebrityCmd celebrityCmd){
        def celebrity = new Celebrity(params)
        def image = params.image ? params.image as MultipartFile : null

        if (!celebrityService.save(celebrity, image)){
            render(view: 'create', model: [tags:tagService.getAllValidRootTags(), celebrity:celebrity])
        } else {
            flash.info = 'vanity.cms.celebrity.saved'
            redirect(action: 'edit', id: celebrity.id)
        }
    }

    def edit(final Long id){
        [tags:tagService.getAllValidRootTags(), celebrity:celebrityService.read(id)]
    }

    def update(final Long id){
        def celebrity = celebrityService.get(id)
        celebrity.properties = params

        if (!celebrityService.save(celebrity)){
            render(view: 'edit', model: [tags:tagService.getAllValidRootTags(), celebrity:celebrity])
        } else {
            flash.info = 'vanity.cms.celebrity.saved'
            redirect(action: 'edit', id: celebrity.id)
        }
    }

}
