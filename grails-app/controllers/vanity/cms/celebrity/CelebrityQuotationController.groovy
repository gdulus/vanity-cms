package vanity.cms.celebrity

import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import vanity.celebrity.CelebrityQuotationsService
import vanity.celebrity.Quotation
import vanity.user.Authority

@Secured([Authority.ROLE_ADMIN])
class CelebrityQuotationController {

    CelebrityQuotationsService celebrityQuotationsService

    def save(final Long celebrityId, final String content, final String source) {
        try {
            celebrityQuotationsService.save(celebrityId, content, source)
            flash.info = 'vanity.cms.celebrity.quotation.saved'
            return redirect(controller: 'celebrity', action: 'edit', id: celebrityId)
        } catch (ValidationException exp) {
            flash.error = 'vanity.cms.celebrity.quotation.savingDomainError'
            return redirect(controller: 'celebrity', action: 'edit', id: celebrityId)
        }
    }

    def update(final Long id, final String content, final String source) {
        try {
            Quotation quotation = celebrityQuotationsService.update(id, content, source)
            flash.info = 'vanity.cms.celebrity.quotation.saved'
            return redirect(controller: 'celebrity', action: 'edit', id: quotation.celebrity.id)
        } catch (ValidationException exp) {
            flash.error = 'vanity.cms.celebrity.quotation.savingDomainError'
            return redirect(controller: 'celebrity', action: 'edit', id: celebrityId)
        }
    }

    def delete(final Long celebrityId, final Long id) {
        celebrityQuotationsService.delete(id)
        flash.info = 'vanity.cms.celebrity.quotation.deleted'
        return redirect(controller: 'celebrity', action: 'edit', id: celebrityId)
    }
}
