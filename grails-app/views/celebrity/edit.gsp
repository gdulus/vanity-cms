<html>
<head>
    <title></title>
    <meta name="layout" content="cms"/>
    <r:require module="celebrityForm"/>
</head>

<body>
<div class="row-fluid">
    <div class="span3">
        <div class="well sidebar-nav">
            <g:render template="submenu"/>
        </div>
    </div>

    <div class="span9">
        <%--
            Main form
        --%>
        <g:render template="form" model="${[celebrity: celebrity, tags: tags, action: 'update', formLegend: 'vanity.cms.celebrity.formEditLegend']}"/>

        <%--
            Quotations edition
        --%>
        <legend><g:message code="vanity.cms.celebrity.quotations"/></legend>

        <g:if test="${quotations}">
            <table class="table table-striped">
                <tr>
                    <th style="width:70%"><g:message code="vanity.cms.celebrity.quotation"/></th>
                    <th><g:message code="vanity.cms.celebrity.quotation.source"/></th>
                    <th></th>
                </tr>
                <g:each in="${quotations}" var="it">
                    <tr>
                        <td>${it.content}</td>
                        <td>${it.source}</td>
                        <td class="options">
                            <g:link controller="celebrityQuotation" action="delete" params="${[celebrityId: celebrity?.id, id: it.id]}" class="btn btn-danger confirm"><g:message code="vanity.cms.delete"/></g:link>
                            <g:link action="edit" params="${[id: celebrity?.id, qId: it.id]}" class="btn btn-success"><g:message code="vanity.cms.edit"/></g:link>
                            <div class="clearfix"></div>
                        </td>
                    </tr>
                </g:each>
            </table>
        </g:if>
        <g:else>
            <message:nothingToList/>
        </g:else>

        <g:if test="${quotation}">
            <g:set var="targetAction" value="update"/>
            <g:set var="hiddenFiledName" value="id"/>
            <g:set var="hiddenFiledValue" value="${quotation.id}"/>
        </g:if>
        <g:else>
            <g:set var="targetAction" value="save"/>
            <g:set var="hiddenFiledName" value="celebrityId"/>
            <g:set var="hiddenFiledValue" value="${celebrity?.id}"/>
        </g:else>

        <g:form method="post" name="celebrityForm" controller="celebrityQuotation" action="${targetAction}">
            <g:hiddenField name="${hiddenFiledName}" value="${hiddenFiledValue}"/>

            <label class="control-label" for="content"><g:message code="vanity.cms.celebrity.quotation"/></label>
            <g:textArea class="input-xxlarge" name="content" value="${quotation?.content}"/>

            <label class="control-label" for="source"><g:message code="vanity.cms.celebrity.quotation.source"/></label>
            <g:textField class="input-xxlarge" name="source" value="${quotation?.source}"/>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary"><g:message code="vanity.cms.save"/></button>
                <g:link action="index" class="btn"><g:message code="vanity.cms.cancel"/></g:link>
            </div>
        </g:form>

    </div>

</div>

</body>
</html>