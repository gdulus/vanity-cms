package vanity.cms.celebrity

import grails.validation.Validateable
import vanity.article.Tag
import vanity.celebrity.Job
import vanity.location.Country
import vanity.user.Gender

@Validateable
class CelebrityCmd {

    Long id

    Integer height

    Gender gender

    String firstName

    String lastName

    String nickName

    String description

    Date birthDate

    String birthLocation

    Boolean dead

    Date deathDate

    String deathLocation

    Tag tag

    String jobs

    String countries

    static constraints = {
        tag(nullable: false, unique: true, validator: { it?.root })
    }

    List<Job> getJobs() {
        return this.@jobs?.split(',')?.collect { Job.load(it.toLong()) }
    }

    List<Country> getCountries() {
        return this.@countries?.split(',')?.collect { Country.load(it.toLong()) }
    }
}
