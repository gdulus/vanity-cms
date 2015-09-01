package vanity.cms.celebrity

import grails.validation.Validateable
import org.apache.commons.lang.StringUtils
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.web.multipart.MultipartFile
import vanity.article.Tag
import vanity.celebrity.Job
import vanity.image.gorm.ImageContainer
import vanity.location.Country
import vanity.user.Gender

@Validateable
class CelebrityCmd implements ImageContainer {

    Long id

    Integer height

    Gender gender

    String firstName

    String lastName

    String description

    Date birthDate

    Boolean alive

    Date deathDate

    MultipartFile avatar

    Tag tag

    String jobs

    String countries

    Boolean deleteAvatar

    static constraints = {
        id(nullable: true)
        firstName(nullable: true, blank: true)
        lastName(nullable: true, blank: true)
        description(nullable: true, blank: true)
        tag(nullable: false, unique: true, validator: { it?.root })
        avatar(nullable: true, empty: false)
        birth(validator: { it.location && it.date })
        death(validator: { val, obj -> obj.alive || (!obj.alive && val.location && val.date) })
    }

    @Override
    String getImagePath(GrailsApplication grailsApplication) {
        return StringUtils.EMPTY
    }

    @Override
    boolean hasImage() {
        return false
    }

    List<Job> getJobs() {
        return this.@jobs?.split(',')?.collect { Job.load(it.toLong()) }
    }

    List<Country> getCountries() {
        return this.@countries?.split(',')?.collect { Country.load(it.toLong()) }
    }
}
