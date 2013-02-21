<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title><g:layoutTitle default="Vanity cms"/></title>
      	<g:layoutHead/>
        <r:layoutResources />
        <style type="text/css">
            body {
                padding-top: 60px;
                padding-bottom: 40px;
            }
            .sidebar-nav {
                padding: 9px 0;
            }

            @media (max-width: 980px) {
                /* Enable use of floated navbar text */
                .navbar-text.pull-right {
                float: none;
                padding-left: 5px;
                padding-right: 5px;
                }
            }
        </style>
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
            <a class="brand" href="#">Vanity CMS</a>
            <div class="nav-collapse collapse">
                <p class="navbar-text pull-right">
                    Logged in as <a href="#" class="navbar-link">Username</a>
                </p>
                <ul class="nav">
                    <li class="active"><a href="#">Dashboard</a></li>
                    <li><a href="#about">Articles</a></li>
                    <li><a href="#contact">Tags</a></li>
                </ul>
            </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>

    <div class="container-fluid">
        <div class="row-fluid">
            <div class="span3">
                <div class="well sidebar-nav">
                    <ul class="nav nav-list">
                    <li class="nav-header">Tags</li>
                    <li class="active"><a href="#">Review stream</a></li>
                    <li><a href="#">List</a></li>
                    <li><a href="#">Add new</a></li>
                </ul>
            </div><!--/.well -->
        </div><!--/span-->
        <div class="span9">
            <g:layoutBody/>
        </div><!--/span-->
        </div><!--/row-->
    </div><!--/.fluid-container-->

    <r:layoutResources />
    </body>
</html>

