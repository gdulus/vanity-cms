<message:flashBased/>

<g:hasErrors bean="${tag}">
    <div class="alert alert-error">
        <g:renderErrors bean="${tag}" as="list"/>
    </div>
</g:hasErrors>

<g:form name="celebrityForm" url="[action: action]">
    <g:if test="${tag?.id}">
        <g:hiddenField name="id" value="${tag.id}"/>
        <g:hiddenField name="root" value="${tag.root}"/>
    </g:if>

    <fieldset>
        <legend><g:message code="${formLegend}"/></legend>

        <label class="control-label" for="name"><g:message code="vanity.cms.tag.name"/></label>
        <g:field class="input-xxlarge" type="text" name="name" value="${tag?.name}"/>

        <g:if test="${rootTags}">
            <label class="control-label" for="parentTagsIds"><g:message code="vanity.cms.tag.parentTags"/></label>
            <g:select class="input-xxlarge" name="parentTagsIds" from="${rootTags}" value="${parentTagsIds}"
                      optionKey="id" optionValue="name" multiple="true"/>
        </g:if>

        <g:if test="${tag?.root}">
            <p><g:message code="vanity.cms.tag.root"/></p>
        </g:if>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary"><g:message code="vanity.cms.save"/></button>
            <g:link action="index" class="btn"><g:message code="vanity.cms.cancel"/></g:link>
        </div>

    </fieldset>
</g:form>