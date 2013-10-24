package vanity.cms.celebrity

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import vanity.celebrity.Celebrity
import vanity.celebrity.CelebrityService
import vanity.cms.image.handler.HandlingSpecification
import vanity.cms.image.handler.ImageUploadHandler
import vanity.utils.ConfigUtils

class CelebrityAdminService {

    GrailsApplication grailsApplication

    CelebrityService celebrityService

    @Autowired
    ImageUploadHandler imageUploadHandler

    @Transactional
    Celebrity save(final Celebrity celebrity, final MultipartFile multipartFile) {
        Celebrity savedCelebrity = celebrityService.save(celebrity)

        if (!savedCelebrity) {
            return null
        }

        if (!multipartFile || multipartFile.isEmpty()) {
            return savedCelebrity
        }

        HandlingSpecification specification = new HandlingSpecification(
            uploadedImage: multipartFile,
            location: grailsApplication.config.files.celebrity.location,
            prefix: grailsApplication.config.files.celebrity.prefix,
            uniqueness: savedCelebrity.id,
            maxWidth: ConfigUtils.$as(grailsApplication.config.files.celebrity.maxWidth, Integer),
            maxHeight: ConfigUtils.$as(grailsApplication.config.files.celebrity.maxHeight, Integer)
        )

        savedCelebrity.avatar = imageUploadHandler.handle(specification)
        savedCelebrity.save()
    }

    @Transactional
    void delete(final Long id) {
        Celebrity savedCelebrity = celebrityService.get(id)

        if (!savedCelebrity) {
            return
        }

        savedCelebrity.delete()
    }
}
