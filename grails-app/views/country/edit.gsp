<html>
<head>
    <title></title>
    <meta name="layout" content="cms"/>
    <r:require module="countryForm"/>
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
                  model="${[country: country, action: 'update', formLegend: 'vanity.cms.celebrity.job.formEditLegend']}"/>
    </div>
</div>

</body>
</html>