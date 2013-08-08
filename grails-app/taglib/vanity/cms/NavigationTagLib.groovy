package vanity.cms

import org.apache.commons.lang.StringUtils

class NavigationTagLib {

    private static final String CSS_CLASS_ACTIVE = 'class="active"'

    static namespace = 'nav'

    def mainMenu = { attrs, body ->
        def link = g.createLink(controller: attrs.controller)
        def name = g.message(code: attrs.code)
        def cssClass = controllerName == attrs.controller ? CSS_CLASS_ACTIVE : StringUtils.EMPTY
        out << """<li ${cssClass}><a href="${link}">${name}</a></li>"""
    }

    def subMenu = { attrs, body ->
        def link = g.createLink(action: attrs.action)
        def name = g.message(code: attrs.code)
        def cssClass = attrs.match.contains(actionName)  ? CSS_CLASS_ACTIVE : StringUtils.EMPTY
        out << """<li ${cssClass}><a href="${link}">${name}</a></li>"""
    }
}
