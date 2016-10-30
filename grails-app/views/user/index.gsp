<html>
<head>
    <title></title>
    <meta name="layout" content="cms"/>
    <r:require module="celebrityJobList"/>
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

        <g:form class="form-inline" method="GET">
            <input type="text" value="${params.username}" name="username" class="input-xxlarge" placeholder="${g.message(code: 'vanity.cms.search')}">
            <button type="submit" class="btn btn-success"><g:message code="default.button.search.label"/></button>
            <g:link class="btn btn-success"><g:message code="default.button.clean.label"/></g:link>
        </g:form>

        <g:if test="${paginationBean.isEmpty()}">
            <message:nothingToList/>
        </g:if>
        <g:else>
            <table class="table table-striped">
                <tr>
                    <th style="width:20%"><g:message code="vanity.cms.user.name"/></th>
                    <th style="width:20%"><g:message code="vanity.cms.user.email"/></th>
                    <th style="width:20%"><g:message code="vanity.cms.user.enabled"/></th>
                    <th></th>
                </tr>

                <g:each in="${paginationBean.elements}" var="element" status="i">
                    <tr>
                        <td>${element.username}</td>
                        <td>${element.profile?.email}</td>
                        <td><g:message code="vanity.yes.no.${element.enabled}"/></td>
                        <td class="options">
                            <g:if test="${element.enabled}">
                                <g:link action="block" id="${element.id}" class="btn btn-danger confirm"><g:message code="vanity.cms.block"/></g:link>
                            </g:if>
                            <g:else>
                                <g:link action="unblock" id="${element.id}" class="btn btn-danger confirm"><g:message code="vanity.cms.unblock"/></g:link>
                            </g:else>
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
                            params="${params}"
                            max="${grailsApplication.config.cms.celebrity.pagination.max}"
                            total="${paginationBean.totalCount}"/>
            </div>

        </g:else>
    </div>
</div>

</body>
</html>