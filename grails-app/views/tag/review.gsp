<html>
<head>
    <title></title>
    <meta name="layout" content="cms"/>
    <r:require module="reviewTag"/>
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

        <table class="table table-striped">
            <tr>
                <th width="10px">#</th>
                <th width="75%"><g:message code="vanity.cms.tag.name"/></th>
                <th></th>
            </tr>

            <g:each in="${paginationBean.elements}" var="element" status="i">
                <tr>
                    <td><%=i + 1%></td>
                    <td class="name"
                        href="${createLink(action: 'ajaxGetTagReviewForm', id: element.id)}">${element.name}</td>
                    <td class="data">
                        <g:link action="markAsRootTag" id="${element.id}" class="btn btn-danger confirm">
                            <g:message code="vanity.cms.tags.review.root"/>
                        </g:link>
                        <g:link action="markAsSpam" id="${element.id}" class="btn btn-danger confirm">
                            <g:message code="vanity.cms.tags.review.spam"/>
                        </g:link>
                        <g:link action="reviewMoreOptions" id="${element.id}" class="btn btn-success">
                            <g:message code="vanity.cms.tags.review.more"/>
                        </g:link>
                    </td>
                </tr>
            </g:each>
        </table>

        <div class="pagination">
            <g:paginate
                    next="&raquo;"
                    prev="&laquo;"
                    maxsteps="0"
                    max="${grailsApplication.config.cms.tag.pagination.max}"
                    total="${paginationBean.totalCount}"/>
        </div>
    </div>
</div>

</body>
</html>