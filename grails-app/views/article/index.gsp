<html>
<head>
    <title></title>
    <meta name="layout" content="cms"/>
    <r:require module="articleList"/>
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
                    <th style="width:50%"><g:message code="vanity.cms.article.title"/></th>
                    <th><g:message code="vanity.cms.article.source"/></th>
                    <th><g:message code="vanity.cms.article.publicationDate"/></th>
                    <th><g:message code="vanity.cms.article.crawlingDate"/></th>
                    <th></th>
                </tr>

                <g:each in="${paginationBean.elements}" var="element" status="i">
                    <tr>
                        <td class="title">${element.title}</td>
                        <td class="source"><a href="${element.url}" target="_blank">${element.source.target}</a></td>
                        <td class="date-created"><g:formatDate date="${element.publicationDate}"
                                                               format="yyyy-MM-dd HH:mm"/></td>
                        <td class="date-crawled"><g:formatDate date="${element.dateCreated}"
                                                               format="yyyy-MM-dd HH:mm"/></td>
                        <td class="options">
                            <g:link action="delete" id="${element.id}" class="btn btn-danger delete"><g:message
                                    code="vanity.cms.delete"/></g:link>
                            <g:link action="edit" id="${element.id}" class="btn btn-success"><g:message
                                    code="vanity.cms.edit"/></g:link>
                            <div class="clearfix"></div>
                        </td>
                    </tr>
                </g:each>
            </table>

            <div class="pagination">
                <g:paginate next="&raquo;" prev="&laquo;" maxsteps="0" controller="article" action="index"
                            total="${paginationBean.totalCount}"/>
            </div>
        </g:else>
    </div>
</div>

</body>
</html>