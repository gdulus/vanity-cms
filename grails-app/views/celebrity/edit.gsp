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
        <g:render template="form"
                  model="${[celebrity: celebrity, tags: tags, action: 'update', formLegend: 'vanity.cms.celebrity.formEditLegend']}"/>

        <h3><g:message code="vanity.cms.celebrity.quotation"/></h3>

        <g:if test="${quotations}">
            <table class="table table-striped">
                <tr>
                    <th style="width:20%"><g:message code="vanity.cms.celebrity.quotation"/></th>
                    <th><g:message code="vanity.cms.celebrity.quotation.source"/></th>
                    <th></th>
                </tr>
                <g:each in="${quotations}" var="quotation">
                    <tr>
                        <td>${quotations.content}</td>
                        <td><g:message code="vanity.cms.celebrity.quotation.source"/></td>
                        <td></td>
                    </tr>
                </g:each>
            </table>
        </g:if>
        <g:else>
            <message:nothingToList/>
        </g:else>
        <g:form method="post" name="celebrityForm" controller="celebrityQuotation" action="save">
            <g:hiddenField name="celebrityId" value="${celebrity?.id}"/>

            <label class="control-label" for="content"><g:message code="vanity.cms.celebrity.quotation"/></label>
            <g:textArea class="input-xxlarge" name="content"/>

            <label class="control-label" for="source"><g:message code="vanity.cms.celebrity.quotation.source"/></label>
            <g:textField class="input-xxlarge" name="source"/>

            <div class="form-actions">
                <button type="submit" class="btn btn-primary"><g:message code="vanity.cms.save"/></button>
                <g:link action="index" class="btn"><g:message code="vanity.cms.cancel"/></g:link>
            </div>
        </g:form>

    </div>

</div>

</body>
</html>