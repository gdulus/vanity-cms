<message:flashBased/>

<g:hasErrors bean="${i18nMessage}">
    <div class="alert alert-error">
        <g:renderErrors bean="${i18nMessage}" as="list"/>
    </div>
</g:hasErrors>

<g:form name="messageForm" url="[action: action]">
    <g:if test="${i18nMessage?.id}">
        <g:hiddenField name="id" value="${i18nMessage?.id}"/>
    </g:if>

    <fieldset>
        <legend><g:message code="${formLegend}"/></legend>

        <label class="control-label" for="code"><g:message code="vanity.cms.message.code"/></label>
        <g:field class="input-xxlarge" type="text" name="code" value="${i18nMessage?.code}"/>

        <label class="control-label" for="code"><g:message code="vanity.cms.message.code"/></label>
        <g:textArea class="input-xxlarge" name="text" value="${i18nMessage?.text}"/>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary"><g:message code="vanity.cms.save"/></button>
            <g:link action="index" class="btn"><g:message code="vanity.cms.cancel"/></g:link>
        </div>

    </fieldset>
</g:form>