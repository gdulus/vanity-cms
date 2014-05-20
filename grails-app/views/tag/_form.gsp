<message:flashBased/>

<g:hasErrors bean="${tag}">
    <div class="alert alert-error">
        <g:renderErrors bean="${tag}" as="list"/>
    </div>
</g:hasErrors>

<g:form url="[action: action]">
    <g:if test="${tag?.id}">
        <g:hiddenField name="id" value="${tag.id}"/>
        <g:hiddenField name="root" value="${tag.root}"/>
    </g:if>

    <fieldset>
        <legend><g:message code="${formLegend}"/></legend>

        <label class="control-label" for="name"><g:message code="vanity.cms.tag.name"/></label>
        <g:field class="input-xxlarge" type="text" name="name" value="${tag?.name}"/>


        <label class="checkbox" for="root">
            <g:checkBox name="root" value="${tag.root}"/>
            <g:message code="vanity.cms.tag.root"/>
        </label>

        <g:if test="${rootTags}">
            <label class="control-label" for="parentTagsIds"><g:message code="vanity.cms.tag.parentTags"/></label>
            <g:select class="input-xxlarge" name="parentTagsIds" from="${rootTags}" value="${parentTagsIds}"
                      optionKey="id" optionValue="name" multiple="true"/>
        </g:if>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary"><g:message code="vanity.cms.save"/></button>
            <g:link action="index" class="btn"><g:message code="vanity.cms.cancel"/></g:link>
        </div>

    </fieldset>
</g:form>

<g:if test="${tag?.id}">
    <g:form>
        <fieldset>
            <legend><g:message code="vanity.cms.tag.children"/></legend>
            <g:field href="${g.createLink(controller: 'tagApi', action: 'children')}" class="input-xxlarge" type="text" name="search" placeholder="${g.message(code: 'vanity.cms.tag.searchChildren')}"/>

            <div id="tag-selection"></div>

            <g:if test="${tag?.hasChildren()}">
                <table class="table table-striped">
                    <tr>
                        <th style="width: 90%"><g:message code="vanity.cms.tag.name"/></th>
                        <th><g:message code="default.button.delete.label"/></th>
                    </tr>
                    <g:each in="${tag.childTags}">
                        <tr>
                            <td><g:link action="edit" id="${it.id}">${it.name}</g:link></td>
                            <td><g:checkBox name="tag_${it.id}" value=""/></td>
                        </tr>
                    </g:each>
                </table>
            </g:if>


            <div class="form-actions">
                <button type="submit" class="btn btn-primary"><g:message code="vanity.cms.save"/></button>
                <g:link action="index" class="btn"><g:message code="vanity.cms.cancel"/></g:link>
            </div>

        </fieldset>
    </g:form>
</g:if>