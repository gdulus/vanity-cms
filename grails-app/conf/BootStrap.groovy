import vanity.utils.BootstrapUtils

class BootStrap {

    BootstrapUtils bootstrapUtils

    def init = { servletContext ->
        bootstrapUtils.init()
    }
    def destroy = {
    }
}
