<message:flashBased/>

<g:hasErrors bean="${tag}">
    <div class="alert alert-error">
        <g:renderErrors bean="${tag}" as="list"/>
    </div>
</g:hasErrors>

<g:form url="[action: action]">
    <g:if test="${action == 'update'}">
        <g:hiddenField name="tag.id" value="${tag.id}"/>
    </g:if>

    <fieldset>
        <legend><g:message code="${formLegend}"/></legend>

        <label class="control-label" for="name"><g:message code="vanity.cms.tag.name"/></label>
        <g:field class="input-xxlarge" type="text" name="name" value="${tag?.name}"/>


        <label class="checkbox" for="root">
        <g:checkBox name="root" value="${tag?.root}"/>
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

<g:if test="${action == 'update'}">

    <g:form action="updateRelations" id="${tag.id}" name="update-relations">
        <g:hiddenField name="parentsToAddSerialized" id="add-parents"/>
        <g:hiddenField name="childrenToAddSerialized" id="add-children"/>
        <g:hiddenField name="parentsToDeleteSerialized" id="delete-parents"/>
        <g:hiddenField name="childrenToDeleteSerialized" id="delete-children"/>

        <fieldset>

            <g:if test="${!tag.root}">
                <legend><g:message code="vanity.cms.tag.parentTags"/></legend>
                <g:field href="${g.createLink(controller: 'tagApi', action: 'parents')}" class="input-xxlarge" type="text" name="search-parents" placeholder="${g.message(code: 'vanity.cms.tag.searchParents')}"/>

                <div id="parent-tag-selection" class="tags"></div>

                <g:if test="${parentTags}">
                    <table class="table table-striped" id="current-parents">
                        <tr>
                            <th style="width: 90%"><g:message code="vanity.cms.tag.name"/></th>
                            <th><g:message code="default.button.delete.label"/></th>
                        </tr>
                        <g:each in="${parentTags}">
                            <tr>
                                <td><g:link action="edit" id="${it.id}">${it.name}</g:link></td>
                                <td><g:checkBox name="parent_${it.id}" value=""/></td>
                            </tr>
                        </g:each>
                    </table>
                </g:if>
            </g:if>

            <legend><g:message code="vanity.cms.tag.children"/></legend>
            <g:field href="${g.createLink(controller: 'tagApi', action: 'children')}" class="input-xxlarge" type="text" name="search-children" placeholder="${g.message(code: 'vanity.cms.tag.searchChildren')}"/>

            <div id="children-tag-selection" class="tags"></div>

            <g:if test="${tag?.hasChildren()}">
                <table class="table table-striped" id="current-children">
                    <tr>
                        <th style="width: 90%"><g:message code="vanity.cms.tag.name"/></th>
                        <th><g:message code="default.button.delete.label"/></th>
                    </tr>
                    <g:each in="${tag.childTags}">
                        <tr>
                            <td><g:link action="edit" id="${it.id}">${it.name}</g:link></td>
                            <td><g:checkBox name="child_${it.id}" value=""/></td>
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