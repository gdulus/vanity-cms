<div id="container">
    <div id="error" class="alert alert-error">
    </div>
    <g:form action="ajaxConfirmTagReview">
        <fieldset id="activate">
            <legend>${element.reviewedTag.name}</legend>
            <input type="hidden" id="tag-id" name="id" value="${element.reviewedTag.id}" />
            <input type="hidden" id="parent-tag-id" name="parentTagId" />
            <input type="hidden" id="duplicated-tag-id" name="duplicatedTagId" />

            <g:if test="${element.hasSimilarities()}" >
                <div id="duplicates">
                    <label class="radio">
                        <input type="radio" name="strategy" id="strategyDuplicate" value="${vanity.cms.article.ConfirmTagReviewCmd.Strategy.DUPLICATE}" />
                        Is this a duplicate?
                    </label>
                    <div class="tags">
                        <g:each in="${element.similarTags}" var="similarTag">
                            <span id="${similarTag.id}" class="label label-info">${similarTag.name}</span>
                        </g:each>
                    </div>
                </div>
            </g:if>

            <g:if test="${element.hasParents()}">
                <div id="parents">
                    <label class="radio">
                        <input type="radio" name="strategy" id="strategyAlias" value="${vanity.cms.article.ConfirmTagReviewCmd.Strategy.ALIAS}" />
                        Is this an alias for any of the parent tasks?
                    </label>
                    <input type="text" id="parent-quick-search" placeholder="search for parent..." />
                    <div class="tags">
                    <g:each in="${element.parentTags}" var="parentTag">
                        <span id="${parentTag.id}" class="label label-info">${parentTag.name}</span>
                    </g:each>
                    </div>
                </div>
            </g:if>

            <label class="radio">
                <input type="radio" name="strategy" id="strategyParent" value="${vanity.cms.article.ConfirmTagReviewCmd.Strategy.PARENT}" />
                Is this a parent tag?
            </label>

            <button type="submit" class="btn">Mark as reviewed</button>
      </fieldset>
    </g:form>
</div>