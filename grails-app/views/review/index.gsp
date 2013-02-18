<html>
<head>
    <title></title>
    <meta name="layout" content="cms"/>
    <r:require module="review"/>
</head>
<body>
    <table class="table table-striped">
        <tr>
            <th width="5%">#</th>
            <th width="30%"><g:message code="cms.tag.name" /></th>
            <th></th>
        </tr>

        <g:each in="${elements}" var="element" status="i">
            <tr class="to-review">
                <td><%= i + 1 %></td>
                <td class="name" href="${createLink(action: 'ajaxGetTagReviewForm', id:element.id)}"><span class="btn btn-primary">${element.name}</span></td>
                <td class="data"></td>
            </tr>
        </g:each>

    </table>
</body>
</html>