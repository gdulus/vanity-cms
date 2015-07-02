package vanity.cms.i18n

import grails.plugin.springsecurity.annotation.Secured
import org.springframework.beans.factory.annotation.Value
import vanity.i18n.Message
import vanity.i18n.MessageService
import vanity.pagination.PaginationParams
import vanity.user.Authority

@Secured([Authority.ROLE_ADMIN])
class MessageController {

    private static final Locale PL_LOCALE = new Locale('pl', 'PL')

    MessageService messageService

    @Value('${cms.celebrity.messages.pagination.max}')
    Long defaultMaxMessages

    def index(final Long offset, final Long max) {
        Long maxValue = max ?: defaultMaxMessages
        PaginationParams paginationParams = new PaginationParams(maxValue, offset, 'code')
        [paginationBean: messageService.listWithPagination(paginationParams)]
    }

    def create() {}

    def save(final String code, final String text) {
        Message message = messageService.save(code, PL_LOCALE, text)

        if (message.hasErrors()) {
            flash.error = 'vanity.cms.message.savingDomainError'
            return render(view: 'create', model: [i18nMessage: message])
        } else {
            flash.info = 'vanity.cms.message.saved'
            return redirect(action: 'edit', id: message.id)
        }
    }

    def edit(final Long id) {
        [i18nMessage: messageService.read(id)]
    }

    def update(final Long id, final String code, final String text) {
        Message message = messageService.update(id, code, PL_LOCALE, text)

        if (!message) {
            flash.error = 'vanity.cms.entity.notFound'
            return redirect(action: 'index')
        }

        if (message.hasErrors()) {
            flash.error = 'vanity.cms.message.savingDomainError'
            return render(view: 'edit', model: [i18nMessage: message])
        } else {
            flash.info = 'vanity.cms.message.saved'
            return redirect(action: 'edit', id: message.id)
        }
    }

    def delete(final Long id) {
        messageService.delete(id)
        flash.info = 'vanity.cms.message.deleted'
        redirect(action: 'index')
    }

}
