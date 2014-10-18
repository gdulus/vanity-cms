<%@ page import="vanity.user.Authority" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title><g:layoutTitle default="${g.message(code: 'vanity.cms.title')}"/></title>
    <g:layoutHead/>
    <r:layoutResources/>
</head>

<body>
<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container-fluid">
            <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="brand" href="#"><g:message code="vanity.cms.title"/></a>

            <div class="nav-collapse collapse">
                <p class="navbar-text pull-right">
                    <g:link controller="logout" class="btn btn-small btn-danger btn-logout"><i class="icon-off icon-white"></i></g:link>
                </p>
                <ul class="nav">
                    <nav:mainMenu controller="article" code="vanity.cms.mainMenu.articles" roles="[Authority.ROLE_ADMIN]"/>
                    <nav:mainMenu controller="tag" code="vanity.cms.mainMenu.tags" roles="[Authority.ROLE_ADMIN, Authority.ROLE_REVIEWER]"/>
                    <nav:mainMenu controller="celebrity" code="vanity.cms.mainMenu.celebrities" roles="[Authority.ROLE_ADMIN]"/>
                    <nav:mainMenu controller="search" code="vanity.cms.mainMenu.search" roles="[Authority.ROLE_ADMIN]"/>
                </ul>
            </div><!--/.nav-collapse -->
        </div>
    </div>
</div>

<div class="container-fluid">
    <div class="row-fluid">
        <g:layoutBody/>
    </div>
</div>

<r:layoutResources/>
</body>
</html>

