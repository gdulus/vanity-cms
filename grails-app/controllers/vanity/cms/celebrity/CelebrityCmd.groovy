package vanity.cms.celebrity

import grails.validation.Validateable
import org.apache.commons.lang.StringUtils
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.web.multipart.MultipartFile
import vanity.article.Tag
import vanity.image.gorm.ImageContainer

@Validateable
class CelebrityCmd implements ImageContainer {

    Long id

    String firstName

    String lastName

    String description

    MultipartFile avatar

    Tag tag

    static constraints = {
        id(nullable: true)
        firstName(nullable: true, blank: true)
        lastName(nullable: true, blank: true)
        description(nullable: true, blank: true)
        tag(nullable: false, unique: true, validator: {
            return it?.root
        })
        avatar(nullable: true, empty: false)
    }

    @Override
    String getImagePath(GrailsApplication grailsApplication) {
        return StringUtils.EMPTY
    }

    @Override
    boolean hasImage() {
        return false
    }
}
