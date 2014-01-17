<html>
<head>
    <title></title>
    <meta name="layout" content="cms"/>
    <r:require module="tagForm"/>
</head>

<body>
<div class="row-fluid">
    <div class="span3">
        <div class="well sidebar-nav">
            <g:render template="submenu"/>
        </div>
    </div>

    <div class="span9">
        <g:render template="form" model="${[tag: tag,
                rootTags: rootTags,
                parentTagsIds: parentTagsIds,
                action: 'save',
                formLegend: 'vanity.cms.tag.formCreateLegend']}"/>
    </div>
</div>

</body>
</html>