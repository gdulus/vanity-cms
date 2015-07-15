<message:flashBased/>

<g:hasErrors bean="${country}">
    <div class="alert alert-error">
        <g:renderErrors bean="${country}" field="name" as="list"/>
        <g:renderErrors bean="${country}" field="isoCode" as="list"/>
    </div>
</g:hasErrors>

<g:form name="celebrityForm" url="[action: action]" enctype="multipart/form-data">
    <g:if test="${country?.id}">
        <g:hiddenField name="id" value="${country.id}"/>
    </g:if>

    <fieldset>
        <legend><g:message code="${formLegend}"/></legend>

        <label class="control-label" for="name"><g:message code="vanity.cms.locations.country.name"/></label>
        <g:field class="input-xxlarge error" type="text" name="name" value="${country?.name}"/>

        <label class="control-label" for="isoCode"><g:message code="vanity.cms.locations.country.isoCode"/></label>
        <g:field class="input-xxlarge error" type="text" name="isoCode" value="${country?.isoCode}"/>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary"><g:message code="vanity.cms.save"/></button>
        <g:if test="${country?.id}">
            <g:link controller="message" action="index" params="${[query: "vanity.country." + country.getTranslationKey()]}" class="btn"><g:message code="vanity.cms.translations"/></g:link>
        </g:if>
            <g:link action="index" class="btn"><g:message code="vanity.cms.cancel"/></g:link>
        </div>

    </fieldset>
</g:form>