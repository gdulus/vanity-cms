<message:flashBased/>

<g:hasErrors bean="${celebrityJob}">
    <div class="alert alert-error">
        <g:renderErrors bean="${celebrityJob}" field="name" as="list"/>
    </div>
</g:hasErrors>

<g:form name="celebrityForm" url="[action: action]" enctype="multipart/form-data">
    <g:if test="${celebrityJob?.id}">
        <g:hiddenField name="id" value="${celebrityJob.id}"/>
    </g:if>

    <fieldset>
        <legend><g:message code="${formLegend}"/></legend>

        <label class="control-label" for="name"><g:message code="vanity.cms.celebrity.job.name"/></label>
        <g:field class="input-xxlarge error" type="text" name="name" value="${celebrityJob?.name}"/>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary"><g:message code="vanity.cms.save"/></button>
        <g:if test="${celebrityJob?.id}">
            <g:link controller="message" action="index" params="${[query: "." + celebrityJob.getTranslationKey()]}" class="btn"><g:message code="vanity.cms.translations"/></g:link>
        </g:if>
            <g:link action="index" class="btn"><g:message code="vanity.cms.cancel"/></g:link>
        </div>

    </fieldset>
</g:form>