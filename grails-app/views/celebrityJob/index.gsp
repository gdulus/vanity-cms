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

        <g:if test="${paginationBean.isEmpty()}">
            <message:nothingToList/>
        </g:if>
        <g:else>
            <table class="table table-striped">
                <tr>

                    <th style="width:70%"><g:message code="vanity.cms.celebrity.job.name"/></th>
                    <th></th>
                </tr>

                <g:each in="${paginationBean.elements}" var="element" status="i">
                    <tr>
                        <td>${element.name}</td>

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