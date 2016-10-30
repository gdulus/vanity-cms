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
            <g:select class="input-xxlarge" value="${params.celebrityId}" noSelection="${[0: '']}" optionKey="id" optionValue="fullName" name="celebrityId" from="${celebrities}"/>
            <button type="submit" class="btn btn-success"><g:message code="default.button.search.label"/></button>
            <g:link class="btn btn-success"><g:message code="default.button.clean.label"/></g:link>
        </g:form>

        <g:if test="${paginationBean.isEmpty()}">
            <message:nothingToList/>
        </g:if>
        <g:else>
            <table class="table table-striped">
                <tr>
                    <th style="width:20%"><g:message code="vanity.cms.celebrity.name"/></th>
                    <th style="width:20%"><g:message code="vanity.cms.celebrity.user.name"/></th>
                    <th style="width:30%"><g:message code="vanity.cms.celebrity.image"/></th>
                    <th></th>
                </tr>

                <g:each in="${paginationBean.elements}" var="element" status="i">
                    <tr>
                        <td>
                            <g:link controller="celebrity" action="edit" id="${element.celebrity.id}">${element.celebrity.fullName}</g:link>
                        </td>
                        <td>
                            <g:link controller="user" params="${[username: element.author.username]}">${element.author.username}</g:link>
                        </td>
                        <td><image:celebrity src="${element}"/></td>
                        <td class="options">
                            <g:link action="delete" id="${element.id}" class="btn btn-danger confirm"><g:message code="vanity.cms.delete"/></g:link>
                            <g:if test="${element.state != vanity.celebrity.CelebrityImageStatus.REVIEWED}">
                                <g:link action="approve" id="${element.id}" class="btn btn-warning confirm"><g:message code="vanity.cms.approve"/></g:link>
                            </g:if>
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