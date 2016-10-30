package vanity.cms.celebrity

import grails.plugin.springsecurity.annotation.Secured
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.beans.factory.annotation.Value
import vanity.article.TagService
import vanity.celebrity.Celebrity
import vanity.celebrity.CelebrityJobService
import vanity.celebrity.CelebrityQuotationsService
import vanity.celebrity.CelebrityService
import vanity.cms.image.handler.ImageHandlingException
import vanity.location.CountryService
import vanity.pagination.PaginationParams
import vanity.user.Authority

@Secured([Authority.ROLE_ADMIN])
class CelebrityController {

    TagService tagService

    CelebrityService celebrityService

    CelebrityAdminService celebrityAdminService

    GrailsApplication grailsApplication

    CelebrityJobService celebrityJobService

    CountryService countryService

    CelebrityQuotationsService celebrityQuotationsService

    @Value('${cms.celebrity.pagination.max}')
    Long defaultMaxCelebrities

    def index(final String query, final Long offset, final Long max) {
        Long maxValue = max ?: defaultMaxCelebrities
        PaginationParams paginationParams = new PaginationParams(maxValue, offset, 'firstName', [query: query])
        [paginationBean: celebrityService.listWithPagination(paginationParams)]
    }

    def create() {
        [
            tags: tagService.findAllValidRootTags(),
            jobs: celebrityJobService.listAll(),
            countries: countryService.listAll()
        ]
    }

    def save(final CelebrityCmd cmd) {
        if (!cmd.validate()) {
            flash.error = 'vanity.cms.celebrity.savingDomainError'
            return render(view: 'create', model: [
                tags: tagService.findAllValidRootTags(),
                jobs: celebrityJobService.listAll(),
                countries: countryService.listAll(),
                celebrity: cmd
            ])
        }

        try {
            Celebrity celebrity = celebrityAdminService.save { Celebrity celebrity ->
                bindData(celebrity, cmd.properties, [exclude: ['class', 'avatar', 'jobs', 'countries']])
                cmd.jobs.each { celebrity.addToJobs(it) }
                cmd.countries.each { celebrity.addToCountries(it) }
            }

            if (celebrity.hasErrors()) {
                flash.error = 'vanity.cms.celebrity.savingDomainError'
                return render(view: 'create', model: [
                    tags: tagService.findAllValidRootTags(),
                    jobs: celebrityJobService.listAll(),
                    countries: countryService.listAll(),
                    celebrity: celebrity
                ])
            } else {
                flash.info = 'vanity.cms.celebrity.saved'
                return redirect(action: 'edit', id: celebrity.id)
            }
        } catch (ImageHandlingException e) {
            flash.error = 'vanity.cms.celebrity.savingImageError'
            return render(view: 'create', model: [
                tags: tagService.findAllValidRootTags(),
                jobs: celebrityJobService.listAll(),
                countries: countryService.listAll(),
                celebrity: cmd
            ])
        }
    }

    def edit(final Long id, final Long qId) {
        Celebrity celebrity = celebrityService.read(id)

        return [
            tags: tagService.findAllValidRootTags(),
            jobs: celebrityJobService.listAll(),
            countries: countryService.listAll(),
            celebrity: celebrity,
            quotations: celebrity.quotations,
            quotation: (qId ? celebrityQuotationsService.read(qId) : null)
        ]
    }

    def update(final CelebrityCmd cmd) {
        try {
            Celebrity celebrity = celebrityAdminService.update(cmd.id) { Celebrity celebrity ->
                bindData(celebrity, cmd.properties, [exclude: ['class', 'avatar', 'jobs', 'countries']])
                celebrity.jobs?.clear()
                cmd.jobs.each { celebrity.addToJobs(it) }
                celebrity.countries?.clear()
                cmd.countries.each { celebrity.addToCountries(it) }
            }

            if (!celebrity) {
                flash.error = 'vanity.cms.entity.notFound'
                return redirect(action: 'index')
            }

            if (celebrity.hasErrors()) {
                flash.error = 'vanity.cms.celebrity.savingDomainError'
                return render(view: 'edit', model: [
                    tags: tagService.findAllValidRootTags(),
                    jobs: celebrityJobService.listAll(),
                    countries: countryService.listAll(),
                    celebrity: celebrity,
                    quotations: celebrity.quotations
                ])
            } else {
                flash.info = 'vanity.cms.celebrity.saved'
                return redirect(action: 'edit', id: celebrity.id)
            }
        } catch (ImageHandlingException e) {
            flash.error = 'vanity.cms.celebrity.savingImageError'
            log.error(e)
            return render(view: 'edit', model: [
                tags: tagService.findAllValidRootTags(),
                jobs: celebrityJobService.listAll(),
                countries: countryService.listAll(),
                celebrity: cmd,
                quotations: []
            ])
        }
    }

    def delete(final Long id) {
        celebrityAdminService.delete(id)
        flash.info = 'vanity.cms.celebrity.deleted'
        redirect(action: 'index')
    }

}
