<message:flashBased />

<g:hasErrors bean="${celebrity}">
    <div class="alert alert-error">
        <g:renderErrors bean="${celebrity}" field="tag" as="list" />
    </div>
</g:hasErrors>

<g:form name="celebrityForm" url="[action:action]" enctype="multipart/form-data">
    <g:if test="${celebrity?.ident()}">
        <g:hiddenField name="id" value="${celebrity.ident()}" />
    </g:if>

    <fieldset>
        <legend><g:message code="${formLegend}" /></legend>

        <label class="control-label" for="firstName"><g:message code="vanity.cms.celebrity.firstName" /></label>
        <g:field class="input-xxlarge error" type="text" name="firstName" value="${celebrity?.firstName}" />

        <label class="control-label" for="lastName"><g:message code="vanity.cms.celebrity.lastName" /></label>
        <g:field class="input-xxlarge" type="text" name="lastName" value="${celebrity?.lastName}"/>

        <label class="control-label" for="tag.id"><g:message code="vanity.cms.celebrity.tag" /></label>
        <g:select class="input-xxlarge" name="tag.id" from="${tags}" value="${celebrity?.tag?.id}" optionKey="id" optionValue="name" noSelection="['':'']" />

        <label class="control-label" for="description"><g:message code="vanity.cms.celebrity.description" /></label>
        <g:textArea class="input-xxlarge" name="description" value="${celebrity?.description}"/>

        <label class="control-label" for="avatar"><g:message code="vanity.cms.celebrity.image" /></label>
        <g:field class="input-xxlarge" type="file" name="avatar" value=""/>

        <div class="form-actions">
          <button type="submit" class="btn btn-primary"><g:message code="vanity.cms.save" /></button>
          <g:link action="index" class="btn"><g:message code="vanity.cms.cancel" /></g:link>
        </div>

    </fieldset>
</g:form>