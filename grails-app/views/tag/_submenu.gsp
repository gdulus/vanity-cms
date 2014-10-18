<%@ page import="vanity.user.Authority" %>
<ul class="nav nav-list">
    <li class="nav-header"><g:message code="vanity.cms.mainMenu.tags"/></li>
    <nav:subMenu action="index" match="['index', 'edit']" code="vanity.cms.subMenu.tags.list" roles="[Authority.ROLE_ADMIN]"/>
    <nav:subMenu action="review" match="['review', 'reviewMoreOptions', 'confirmTagReview']" code="vanity.cms.subMenu.tags.reviewStream" roles="[Authority.ROLE_ADMIN, Authority.ROLE_REVIEWER]"/>
    <nav:subMenu action="promoted" match="['promoted']" code="vanity.cms.subMenu.tags.promoted" roles="[Authority.ROLE_ADMIN]"/>
    <nav:subMenu action="create" match="['create', 'save']" code="vanity.cms.subMenu.tags.addNew" roles="[Authority.ROLE_ADMIN]"/>
</ul>