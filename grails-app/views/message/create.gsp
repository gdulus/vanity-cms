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
            <g:render template="submenu"/>
        </div>
    </div>

    <div class="span9">
        <g:render template="form" model="${[i18nMessage: i18nMessage, action: 'save', formLegend: 'vanity.cms.message.formCreateLegend']}"/>
    </div>
</div>

</body>
</html>