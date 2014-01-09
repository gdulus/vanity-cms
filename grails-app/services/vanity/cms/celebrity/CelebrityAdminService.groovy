package vanity.cms.celebrity

import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import vanity.celebrity.Celebrity
import vanity.cms.image.handler.DeletingSpecification
import vanity.cms.image.handler.ImageResourceHandler
import vanity.cms.image.handler.SavingSpecification
import vanity.image.gorm.Image
import vanity.utils.ConfigUtils

class CelebrityAdminService {

    GrailsApplication grailsApplication

    @Autowired
    ImageResourceHandler imageUploadHandler

    Celebrity update(final Long id, final Boolean deleteAvatar, final MultipartFile multipartFile, final Closure binder) {
        Celebrity celebrity = Celebrity.get(id)

        if (!celebrity) {
            return null
        }

        binder.call(celebrity)

        if (celebrity.save()) {
            if (deleteAvatar && celebrity.hasImage()) {
                deleteImage(celebrity.avatar.name)
                celebrity.avatar = null
                celebrity.save()
            } else if (multipartFile && !multipartFile.isEmpty()) {
                celebrity.avatar = saveImage(celebrity.id, multipartFile)
                celebrity.save()
            }
        }

        return celebrity
    }

    @Transactional
    Celebrity save(final MultipartFile multipartFile, final Closure binder) {
        Celebrity celebrity = new Celebrity()
        binder.call(celebrity)

        if (!celebrity.save() || !multipartFile || multipartFile.isEmpty()) {
            return celebrity
        }

        celebrity.avatar = saveImage(celebrity.id, multipartFile)
        celebrity.save()
        return celebrity
    }

    @Transactional
    void delete(final Long id) {
        Celebrity celebrity = Celebrity.get(id)

        if (!celebrity) {
            return
        }

        if (celebrity.hasImage()) {
            deleteImage(celebrity.avatar.name)
        }

        celebrity.delete()
    }

    private Image saveImage(final Long uniqueness, final MultipartFile multipartFile) {
        SavingSpecification specification = new SavingSpecification(
            uploadedImage: multipartFile,
            location: grailsApplication.config.files.celebrity.location,
            prefix: grailsApplication.config.files.celebrity.prefix,
            uniqueness: uniqueness,
            maxWidth: ConfigUtils.$as(grailsApplication.config.files.celebrity.maxWidth, Integer),
            maxHeight: ConfigUtils.$as(grailsApplication.config.files.celebrity.maxHeight, Integer)
        )

        return imageUploadHandler.save(specification)
    }

    private void deleteImage(final String name) {
        DeletingSpecification specification = new DeletingSpecification(
            location: grailsApplication.config.files.celebrity.location,
            name: name
        )

        imageUploadHandler.delete(specification)
    }
}
