<message:flashBased/>

<g:hasErrors bean="${article}">
    <div class="alert alert-error">
        <g:renderErrors bean="${article}" as="list"/>
    </div>
</g:hasErrors>

<g:form name="celebrityForm" url="[action: action]">

    <g:hiddenField name="id" value="${article.id}"/>

    <fieldset>
        <legend><g:message code="${formLegend}"/></legend>

        <label class="control-label" for="title"><g:message code="vanity.cms.article.title"/></label>
        <g:field class="input-xxlarge error" type="text" name="title" value="${article?.title}"/>

        <label class="control-label" for="body"><g:message code="vanity.cms.article.body"/></label>
        <g:textArea class="input-xxlarge" name="body" value="${article?.body}"/>

        <label class="control-label" for="tags"><g:message code="vanity.cms.article.tag"/></label>
        <g:select class="input-xxlarge" id="tags" name="tags" from="${tags}" value="${article?.tags*.id}" optionKey="id"
                  optionValue="name" multiple="true"/>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary"><g:message code="vanity.cms.save"/></button>
            <g:link action="index" class="btn"><g:message code="vanity.cms.cancel"/></g:link>
        </div>

    </fieldset>
</g:form>