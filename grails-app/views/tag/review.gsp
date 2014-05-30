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

        <g:form action="review" class="form-inline" method="GET">
            <input type="text" value="${query}" name="query" class="input-xxlarge" placeholder="${g.message(code: 'vanity.cms.tag.search')}">
            <button type="submit" class="btn btn-success"><g:message code="default.button.search.label"/></button>
            <g:link class="btn btn-success"><g:message code="default.button.clean.label"/></g:link>
        </g:form>

        <g:form action="confirmTagsReview">
            <g:hiddenField name="strategy" value=""/>
            <g:hiddenField name="serializedTagIds" value=""/>

            <table class="table table-striped">
                <tr>
                    <th width="10px"><g:checkBox name="tag.all" class="selection-master"/></th>
                    <th width="75%"><g:message code="vanity.cms.tag.name"/></th>
                    <th>
                        <g:submitButton id="ROOT" class="btn btn-danger confirm submit" name="${message(code: 'vanity.cms.tags.review.root')}"/>
                        <g:submitButton id="SPAM" class="btn btn-danger confirm submit" name="${message(code: 'vanity.cms.tags.review.spam')}"/>
                    </th>
                </tr>

                <g:each in="${paginationBean.elements}" var="element" status="i">
                    <tr>
                        <td>
                            <g:checkBox tag-id="${element.id}" name="tags.${element.id}" class="selection-slave"/>
                        </td>
                        <td class="name">
                            ${element.name}
                        </td>
                        <td class="data">
                            <g:link action="markAsRootTag" id="${element.id}" class="btn btn-danger confirm" params="[offset: params.offset, max: params.max]">
                                <g:message code="vanity.cms.tags.review.root"/>
                            </g:link>
                            <g:link action="markAsSpam" id="${element.id}" class="btn btn-danger confirm" params="[offset: params.offset, max: params.max]">
                                <g:message code="vanity.cms.tags.review.spam"/>
                            </g:link>
                            <g:link action="reviewMoreOptions" id="${element.id}" class="btn btn-success">
                                <g:message code="vanity.cms.tags.review.more"/>
                            </g:link>
                        </td>
                    </tr>
                </g:each>
            </table>
        </g:form>

        <div class="pagination">
            <g:paginate
                    next="&raquo;"
                    prev="&laquo;"
                    maxsteps="0"
                    params="${query ? [query: query] : null}"
                    max="${grailsApplication.config.cms.tag.pagination.max}"
                    total="${paginationBean.totalCount}"/>
        </div>
    </div>
</div>

</body>
</html>