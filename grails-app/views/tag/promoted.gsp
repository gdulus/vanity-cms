<html>
<head>
    <title></title>
    <meta name="layout" content="cms"/>
    <r:require module="promoteTag"/>
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

        <g:form class="form-inline" method="GET" action="promoted">
            <input type="text" value="${params.query}" name="query" class="input-xxlarge" placeholder="${g.message(code: 'vanity.cms.tag.search')}">
            <label class="checkbox"><input type="checkbox" name="promoted" value="true" ${params.promoted ? 'checked' : ''}><g:message code="vanity.cms.promoted"/></label>
            <button type="submit" class="btn btn-success"><g:message code="default.button.search.label"/></button>
            <g:link class="btn btn-success" action="promoted"><g:message code="default.button.clean.label"/></g:link>
        </g:form>

        <table class="table table-striped">
            <tr>
                <th style="width: 20px">#</th>
                <th style="width: 500px"><g:message code="vanity.cms.tag.name"/></th>
                <th><g:message code="vanity.cms.tag.status"/></th>
                <th style="width: 200px"></th>
            </tr>

            <g:each in="${paginationBean.elements}" var="element" status="i">
                <tr class="to-promote">
                    <td><%=i + 1%></td>
                    <g:if test="${element.isPromoted()}">
                        <td class="name" href="${createLink(action: 'ajaxPromoteTag', id: element.id)}">${element.name}</td>
                    </g:if>
                    <g:else>
                        <td class="name" href="${createLink(action: 'ajaxUnpromoteTag', id: element.id)}">${element.name}</td>
                    </g:else>
                    <td>${element.status}</td>
                    <g:if test="${element.isPromoted()}">
                        <td><a href="${createLink(action: 'unPromoteTag', id: element.id)}" class="btn btn-danger">cancel promotion</a></td>
                    </g:if>
                    <g:else>
                        <td><a href="${createLink(action: 'promoteTag', id: element.id)}" class="btn btn-success">promote</a></td>
                    </g:else>
                </tr>
            </g:each>
        </table>

        <div class="pagination">
            <g:paginate
                    next="&raquo;"
                    prev="&laquo;"
                    maxsteps="0"
                    action="promoted"
                    max="${grailsApplication.config.cms.tag.pagination.max}"
                    total="${paginationBean.totalCount}"/>
        </div>
    </div>
</div>
</body>
</html>