package vanity.cms

class MessageTagLib {

    private static final String CSS_CLASS_ERROR = 'alert-error'

    private static final String CSS_CLASS_WARNING = 'alert-warning'

    private static final String CSS_CLASS_INFO = 'alert-info'

    static namespace = 'message'

    def flashBased = { attrs, body ->
        Message message = getMessageFromFlash()

        if (message){
            out << """<div class="alert ${message.cssClass}">${g.message(code: message.code)}</div>"""
        }
    }

    private Message getMessageFromFlash(){
        if (flash.info){
            return new Message(flash.info, CSS_CLASS_INFO)
        }

        if (flash.warning){
            return new Message(flash.warning, CSS_CLASS_WARNING)
        }

        if (flash.error){
            return new Message(flash.error, CSS_CLASS_ERROR)
        }

        return null
    }

    private static final class Message {

        final String code

        final String cssClass

        Message(final def code, final String cssClass){
            this.code = code
            this.cssClass = cssClass
        }

    }

}
