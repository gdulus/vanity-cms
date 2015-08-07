<html>
<head>
    <title></title>
    <meta name="layout" content="cms"/>
    <r:require module="celebrityList"/>
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

                    <th style="width:20%"><g:message code="vanity.cms.celebrity.name"/></th>
                    <th><g:message code="vanity.cms.celebrity.tag"/></th>
                    <th><g:message code="vanity.cms.celebrity.country"/></th>
                    <th><g:message code="vanity.cms.celebrity.job"/></th>
                    <th><g:message code="vanity.cms.celebrity.quotations"/></th>
                    <th></th>
                </tr>

                <g:each in="${paginationBean.elements}" var="element" status="i">
                    <tr>
                        <td class="name">${element.firstName} ${element.lastName}</td>
                        <td class="tag"><g:link controller="tag" action="edit" id="${element.tag.id}">${element.tag.name}</g:link></td>
                        <td>
                            <g:if test="${element.countries}">
                                <ul>
                                    <g:each in="${element.countries}" var="country">
                                        <li>${country.name}</li>
                                    </g:each>
                                </ul>
                            </g:if>
                        </td>
                        <td>
                            <g:if test="${element.jobs}">
                                <ul>
                                    <g:each in="${element.jobs}" var="job">
                                        <li>${job.name}</li>
                                    </g:each>
                                </ul>
                            </g:if>
                        </td>
                        <td>${element.quotations.size()}</td>
                        <td class="options">
                            <g:link action="delete" id="${element.id}" class="btn btn-danger confirm"><g:message code="vanity.cms.delete"/></g:link>
                            <g:link action="edit" id="${element.id}" class="btn btn-success"><g:message code="vanity.cms.edit"/></g:link>
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