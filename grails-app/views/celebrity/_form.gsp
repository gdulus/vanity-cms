<message:flashBased/>

<g:hasErrors bean="${celebrity}">
    <div class="alert alert-error">
        <g:renderErrors bean="${celebrity}" as="list"/>
    </div>
</g:hasErrors>

<g:form name="celebrityForm" url="[action: action]" enctype="multipart/form-data">
    <g:if test="${celebrity?.id}">
        <g:hiddenField name="id" value="${celebrity.id}"/>
    </g:if>

    <fieldset>
        <legend><g:message code="${formLegend}"/></legend>

        <label class="control-label" for="firstName"><g:message code="vanity.cms.celebrity.firstName"/></label>
        <g:field class="input-xxlarge error" type="text" name="firstName" value="${celebrity?.firstName}"/>

        <label class="control-label" for="lastName"><g:message code="vanity.cms.celebrity.lastName"/></label>
        <g:field class="input-xxlarge" type="text" name="lastName" value="${celebrity?.lastName}"/>

        <label class="control-label" for="nickName"><g:message code="vanity.cms.celebrity.nickName"/></label>
        <g:field class="input-xxlarge" type="text" name="nickName" value="${celebrity?.nickName}"/>

        <label class="control-label" for="height"><g:message code="vanity.cms.celebrity.height"/></label>
        <g:field class="input-xxlarge" type="text" name="height" value="${celebrity?.height}"/>

        <label class="control-label" for="gender"><g:message code="vanity.cms.celebrity.gender"/></label>
        <g:select class="input-xxlarge" name="gender" from="${vanity.user.Gender.values()}" value="${celebrity?.gender}" noSelection="['': '']"/>

        <label class="control-label" for="birthDate"><g:message code="vanity.cms.celebrity.birthDate"/></label>
        <g:datePicker class="datepicker" name="birthDate" value="${celebrity?.birthDate}" precision="day" relativeYears="[-200..0]" default="none" noSelection="['': '']"/>

        <label class="control-label" for="birthLocation"><g:message code="vanity.cms.celebrity.birthPlace"/></label>
        <g:field class="input-xxlarge" type="text" name="birthLocation" value="${celebrity?.birthLocation}"/>

        <label class="control-label" for="dead"><g:message code="vanity.cms.celebrity.dead"/></label>
        <g:checkBox name="dead" value="${celebrity?.dead}"/>

        <label class="control-label" for="deathDate"><g:message code="vanity.cms.celebrity.deathDate"/></label>
        <g:datePicker name="deathDate" value="${celebrity?.deathDate}" precision="day" relativeYears="[-200..0]" default="none" noSelection="['': '']"/>

        <label class="control-label" for="deathLocation"><g:message code="vanity.cms.celebrity.deathPlace"/></label>
        <g:field class="input-xxlarge" type="text" name="deathLocation" value="${celebrity?.deathLocation}"/>

        <label class="control-label" for="tag.id"><g:message code="vanity.cms.celebrity.tag"/></label>
        <g:select class="input-xxlarge" name="tag.id" from="${tags}" value="${celebrity?.tag?.id}" optionKey="id" optionValue="name" noSelection="['': '']"/>

        <label class="control-label" for="jobs"><g:message code="vanity.cms.celebrity.job"/></label>
        <g:select multiple="true" class="input-xxlarge" name="jobs" from="${jobs}" value="${celebrity?.jobs*.id}" optionKey="id" optionValue="name"/>

        <label class="control-label" for="countries"><g:message code="vanity.cms.celebrity.country"/></label>
        <g:select multiple="true" class="input-xxlarge" name="countries" from="${countries}" value="${celebrity?.countries*.id}" optionKey="id" optionValue="name"/>

        <label class="control-label" for="description"><g:message code="vanity.cms.celebrity.description"/></label>
        <g:textArea class="input-xxlarge" name="description" value="${celebrity?.description}"/>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary"><g:message code="vanity.cms.save"/></button>
            <g:link action="index" class="btn"><g:message code="vanity.cms.cancel"/></g:link>
        </div>

    </fieldset>
</g:form>