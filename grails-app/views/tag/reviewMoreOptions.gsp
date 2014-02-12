<html>
<head>
    <title></title>
    <meta name="layout" content="cms"/>
    <r:require module="reviewTag"/>
</head>

<body>
<div class="row-fluid">
    <div class="span3">
        <div class="well sidebar-nav">
            <g:render template="submenu"/>
        </div>
    </div>

    <div class="span9">

        <message:flashBased/>

        <g:form action="confirmTagReview" name="review-form">
            <fieldset id="activate">
                <legend>${element.reviewedTag.name}</legend>
                <input type="hidden" id="tag-id" name="id" value="${element.reviewedTag.id}"/>
                <input type="hidden" id="parent-tag-ids" name="serializedParentTagIds"/>
                <input type="hidden" id="duplicated-tag-id" name="duplicatedTagId"/>

                <g:if test="${element.hasSimilarities()}">
                    <div id="duplicates">
                        <label class="radio">
                            <input type="radio" name="strategy" id="strategyDuplicate"
                                   value="${vanity.cms.article.ConfirmTagReviewCmd.Strategy.DUPLICATE}"/>
                            <g:message code="vanity.cms.tags.review.isThisDuplicate"/>
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
                            <input type="radio" name="strategy" id="strategyAlias"
                                   value="${vanity.cms.article.ConfirmTagReviewCmd.Strategy.ALIAS}"/>
                            <g:message code="vanity.cms.tags.review.isThisAlias"/>
                        </label>
                        <input type="text" id="parent-quick-search" placeholder="search for parent..."/>

                        <div class="tags">
                            <g:each in="${element.parentTags}" var="parentTag">
                                <span id="${parentTag.id}" class="label label-info">${parentTag.name}</span>
                            </g:each>
                        </div>
                    </div>
                </g:if>

                <label class="radio">
                    <input type="radio" name="strategy" id="strategyParent"
                           value="${vanity.cms.article.ConfirmTagReviewCmd.Strategy.ROOT}"/>
                    <g:message code="vanity.cms.tags.review.isThisParentTag"/>
                </label>

                <label class="radio">
                    <input type="radio" name="strategy" id="strategySpam"
                           value="${vanity.cms.article.ConfirmTagReviewCmd.Strategy.SPAM}"/>
                    <g:message code="vanity.cms.tags.review.isThisSpam"/>
                </label>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary"><g:message code="vanity.cms.save"/></button>
                    <g:link action="review" class="btn"><g:message code="vanity.cms.cancel"/></g:link>
                </div>

            </fieldset>
        </g:form>
    </div>
</div>

</body>
</html>