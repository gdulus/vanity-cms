<%@ page import="vanity.article.Article" %>
<html>
<head>
    <title></title>
    <meta name="layout" content="cms"/>
    <r:require module="base"/>
</head>
<body>
    <div class="row-fluid">
        <div class="span3">
            <div class="well sidebar-nav">
                <g:render template="submenu" />
            </div>
        </div>
        <div class="span9">
            <table class="table">
                <tr>
                    <th style="width: 200px;">re-indexer name</th>
                    <th style="width: 100px;">status</th>
                    <th>progress</th>
                    <th style="width: 50px;"></th>
                </tr>
                <tr>
                    <td>Article re-indexer</td>
                    <td>${state[Article] ? state[Article].reIndexingPhase : '-'}</td>
                    <td>${state[Article] ? state[Article].percent + ' %' : '-'}</td>
                    <td><g:link action="startArticleIndexing" class="btn btn-success">start</g:link></td>
                </tr>
            </table>
        </div>
    </div>

</body>
</html>