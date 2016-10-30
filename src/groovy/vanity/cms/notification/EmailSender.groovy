package vanity.cms.notification

import grails.gsp.PageRenderer
import grails.plugin.mail.MailService
import org.apache.commons.lang.Validate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import vanity.celebrity.CelebrityImage
import vanity.i18n.Message

@Component
class EmailSender {

    private static final Locale PL_LOCALE = new Locale('pl', 'PL')

    @Autowired
    MessageSource messageSource

    @Autowired
    PageRenderer groovyPageRenderer

    @Autowired
    MailService mailService

    public void sendImageApproved(final CelebrityImage image) {
        Validate.notNull(image?.author?.profile?.email)

        mailService.sendMail {
            async true
            to image.author.profile.email
            subject getText('email.user.imageapproved.title', [image.author.username])
            html getText('email.user.imageapproved.body', [image.author.username])
        }
    }

    private String getText(final String code, final List<?> params) {
        String template = Message.findByCodeAndLocale(code, PL_LOCALE).text
        params.eachWithIndex { int index, Object value ->
            template = template.replaceAll("{${index}", value.toString())
        }
        return template
    }

}