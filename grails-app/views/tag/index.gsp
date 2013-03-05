<html>
<head>
    <title></title>
    <meta name="layout" content="cms"/>
    <r:require module="review"/>
</head>
<body>
    <div class="row-fluid">
        <div class="span3">
            <div class="well sidebar-nav">
                <g:render template="submenu" />
            </div>
        </div>
        <div class="span9">
            <table class="table table-striped">
                <tr>
                    <th width="50px">#</th>
                    <th width="250px"><g:message code="cms.tag.name" /></th>
                    <th></th>
                </tr>

                <g:each in="${elements}" var="element" status="i">
                    <tr class="to-review">
                        <td><%= i + 1 %></td>
                        <td class="name" href="${createLink(action: 'ajaxGetTagReviewForm', id:element.id)}">${element.name}</td>
                        <td class="data"></td>
                    </tr>
                </g:each>

            </table>
        </div>
    </div>

</body>
</html>