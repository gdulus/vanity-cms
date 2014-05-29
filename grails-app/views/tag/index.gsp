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

            <g:form class="form-inline">
                <input type="text" value="${query}" name="query" class="input-xxlarge" placeholder="${g.message(code: 'vanity.cms.tag.search')}">
                <g:submitButton name="${message(code: 'default.button.search.label')}" class="btn btn-success"/>
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
                        <td class="title"><g:link action="edit" id="${element.id}">${element.name}</g:link></td>
                        <td>
                            <g:if test="${element.root}">
                                <span class="label label-success"><g:message code="vanity.cms.tag.root"/></span>
                            </g:if>
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
                            <g:link action="delete" id="${element.id}" class="btn btn-danger confirm"><g:message
                                    code="vanity.cms.delete"/></g:link>
                            <g:link action="edit" id="${element.id}" class="btn btn-success"><g:message
                                    code="vanity.cms.edit"/></g:link>
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