<div>
    <g:form action="ajaxConfirmTagReview">
        <fieldset id="activate">
            <legend>${element.reviewedTag.name}</legend>
            <input type="hidden" id="tag-id" value="${element.reviewedTag.id}" />
            <g:if test="${element.hasSimilarities()}" >
                <div id="duplicates" class="tags">
                    <span class="help-block">Can you find duplicate?</span>
                    <g:each in="${element.similarTags}" var="similarTag">
                        <g:if test="${similarTag.shouldBeReviewed()}">
                            <span class="label label-warning">${similarTag.name}</span>
                        </g:if>
                        <g:else>
                            <span class="label label-success">${similarTag.name}</span>
                        </g:else>
                    </g:each>
                </div>
            </g:if>
            <g:if test="${element.hasParents()}">
                <div id="parents" class="tags">
                    <span class="help-block">Is it alias for any of this parent tasks?</span>
                    <g:each in="${element.parentTags}" var="parentTag">
                        <g:if test="${parentTag.shouldBeReviewed()}">
                            <span class="label label-warning">${parentTag.name}</span>
                        </g:if>
                        <g:else>
                            <span class="label label-success">${parentTag.name}</span>
                        </g:else>
                    </g:each>
                </div>
            </g:if>

            <span class="help-block">Is that a parent tag?</span>
            <label class="checkbox">
                <input id="parent-task" type="checkbox" value="true" name="parent-tag"/> yes, this is parent
            </label>

            <button type="submit" class="btn">Mark as reviewed</button>
      </fieldset>
    </g:form>
</div>