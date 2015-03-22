package vanity.cms.location

import grails.plugin.springsecurity.annotation.Secured
import org.springframework.beans.factory.annotation.Value
import vanity.location.Country
import vanity.location.CountryService
import vanity.pagination.PaginationParams
import vanity.user.Authority

@Secured([Authority.ROLE_ADMIN])
class CountryController {

    CountryService countryService

    @Value('${cms.location.country.pagination.max}')
    Long defaultMaxCountries

    def index(final Long offset, final Long max) {
        Long maxValue = max ?: defaultMaxCountries
        PaginationParams paginationParams = new PaginationParams(maxValue, offset, 'name')
        [paginationBean: countryService.listWithPagination(paginationParams)]
    }

    def create() {}

    def save(final CountryCmd cmd) {
        if (!cmd.validate()) {
            flash.error = 'vanity.cms.location.country.savingDomainError'
            return render(view: 'create', model: [country: cmd])
        }

        Country country = countryService.save(cmd.name, cmd.isoCode)

        if (!country.hasErrors()) {
            flash.info = 'vanity.cms.location.country.saved'
            return redirect(action: 'edit', id: country.id)
        } else {
            flash.error = 'vanity.cms.location.country.savingDomainError'
            return render(view: 'create', model: [country: country])
        }
    }

    def edit(final Long id) {
        [country: countryService.read(id)]
    }

    def update(final CountryCmd cmd) {
        if (!cmd.validate()) {
            flash.error = 'vanity.cms.location.country.savingDomainError'
            return render(view: 'edit', model: [country: cmd])
        }

        Country country = countryService.update(cmd.id, cmd.name, cmd.isoCode)

        if (!country) {
            flash.error = 'vanity.cms.entity.notFound'
            return redirect(action: 'index')
        }

        if (country.hasErrors()) {
            flash.error = 'vanity.cms.location.country.savingDomainError'
            return render(view: 'edit', model: [country: country])
        } else {
            flash.info = 'vanity.cms.location.country.saved'
            return redirect(action: 'edit', id: country.id)
        }
    }

    def delete(final Long id) {
        countryService.delete(id)
        flash.info = 'vanity.cms.location.country.deleted'
        redirect(action: 'index')
    }

}
