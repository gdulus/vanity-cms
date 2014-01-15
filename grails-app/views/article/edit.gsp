<html>
<head>
    <title></title>
    <meta name="layout" content="cms"/>
    <r:require module="articleForm"/>
</head>

<body>
<div class="row-fluid">
    <div class="span3">
        <div class="well sidebar-nav">
            <g:render template="submenu"/>
        </div>
    </div>

    <div class="span9">
        <g:render template="form"
                  model="${[article: article, tags: tags, action: 'update', formLegend: 'vanity.cms.article.formEditLegend']}"/>
    </div>
</div>

</body>
</html>