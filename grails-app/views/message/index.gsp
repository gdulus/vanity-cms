<html>
<head>
    <title></title>
    <meta name="layout" content="cms"/>
    <r:require module="base"/>
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
                <input type="text" value="${params.query}" name="query" class="input-xxlarge" placeholder="${g.message(code: 'default.search.placeholder')}">
                <button type="submit" class="btn btn-success"><g:message code="default.button.search.label"/></button>
                <g:link class="btn btn-success"><g:message code="default.button.clean.label"/></g:link>
            </g:form>

            <table class="table table-striped">
                <tr>
                    <th style="width:20%"><g:message code="vanity.cms.message.code"/></th>
                    <th style="width:40%"><g:message code="vanity.cms.message.text"/></th>
                    <th></th>
                </tr>

                <g:each in="${paginationBean.elements}" var="element" status="i">
                    <tr>
                        <td>${element.code}</td>
                        <td>${element.text}</td>
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
                <g:paginate next="&raquo;"
                            prev="&laquo;"
                            maxsteps="0"
                            action="index"
                            max="${grailsApplication.config.cms.celebrity.pagination.max}"
                            total="${paginationBean.totalCount}"/>
            </div>

        </g:else>
    </div>
</div>

</body>
</html>