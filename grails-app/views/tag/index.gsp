<html>
<head>
    <title></title>
    <meta name="layout" content="cms"/>
    <r:require module="tagList"/>
</head>

<body>
<div class="row-fluid">
    <div class="span3">
        <div class="well sidebar-nav">
            <g:render template="submenu"/>
        </div>
    </div>

    <div class="span9">

        <message:flashBased/>

        <g:if test="${paginationBean.isEmpty()}">
            <message:nothingToList/>
        </g:if>
        <g:else>

            <g:form class="form-inline" method="GET">
                <input type="text" value="${params.query}" name="query" class="input-xxlarge" placeholder="${g.message(code: 'vanity.cms.tag.search')}">
                <button type="submit" class="btn btn-success"><g:message code="default.button.search.label"/></button>
                <g:link class="btn btn-success"><g:message code="default.button.clean.label"/></g:link>
            </g:form>

            <table class="table table-striped">
                <tr>
                    <th style="width:20%"><g:message code="vanity.cms.tag.name"/></th>
                    <th style="width:10%"><g:message code="vanity.cms.tag.status"/></th>
                    <th style="width:10%"><g:message code="vanity.cms.tag.children"/></th>
                    <th></th>
                </tr>

                <g:each in="${paginationBean.elements}" var="element" status="i">
                    <tr>
                        <td class="title">
                            <g:link url=" https://www.google.pl/#q=${element.name}" target="_blank"><i class="icon-search"></i> ${element.name}</g:link>
                        </td>
                        <td>
                            <g:if test="${element.root}">
                                <span class="label label-success"><g:message code="vanity.cms.tag.root"/></span>
                            </g:if>
                            <g:elseif test="${element.spam}">
                                <span class="label label-error"><g:message code="vanity.cms.tag.spam"/></span>
                            </g:elseif>
                            <g:else>
                                <span class="label label-info"><g:message code="vanity.cms.tag.child"/></span>
                            </g:else>
                        </td>
                        <td>
                            <g:if test="${element.childTags}">
                                <ul>
                                    <g:each in="${element.childTags}">
                                        <li><g:link action="edit" id="${it.id}">${it.name}</g:link></li>
                                    </g:each>
                                </ul>
                            </g:if>
                        </td>
                        <td class="options">
                            <g:if test="${!element.spam}">
                                <g:link action="spam" id="${element.id}" class="btn btn-danger confirm"><g:message code="vanity.cms.delete"/></g:link>
                                <g:link action="edit" id="${element.id}" class="btn btn-success"><g:message code="vanity.cms.edit"/></g:link>
                            </g:if>
                            <g:else>
                                <g:link action="unSpam" id="${element.id}" class="btn btn-danger confirm"><g:message code="vanity.cms.unspam"/></g:link>
                            </g:else>
                            <div class="clearfix"></div>
                        </td>
                    </tr>
                </g:each>
            </table>

            <div class="pagination">
                <g:paginate
                        next="&raquo;"
                        prev="&laquo;"
                        maxsteps="0"
                        action="index"
                        params="${query ? [query: query] : null}"
                        max="${grailsApplication.config.cms.tag.pagination.max}"
                        total="${paginationBean.totalCount}"/>
            </div>
        </g:else>
    </div>
</div>

</body>
</html>