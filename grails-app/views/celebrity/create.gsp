<html>
<head>
    <title></title>
    <meta name="layout" content="cms"/>
    <r:require module="celebrityForm"/>
</head>
<body>
    <div class="row-fluid">
        <div class="span3">
            <div class="well sidebar-nav">
                <g:render template="submenu" />
            </div>
        </div>
        <div class="span9">
            <g:render template="form" model="${[celebrity:celebrity, tags:tags, action:'save', formLegend:'vanity.cms.celebrity.formCreateLegend']}" />
        </div>
    </div>

</body>
</html>