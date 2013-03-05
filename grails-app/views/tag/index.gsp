<html>
<head>
    <title></title>
    <meta name="layout" content="cms"/>
    <r:require module="reviewTag"/>
</head>
<body>
    <div class="row-fluid">
        <div class="span3">
            <div class="well sidebar-nav">
                <g:render template="submenu" />
            </div>
        </div>
        <div class="span9">
            <g:if test="${flash.message}" >
                <div class="alert alert-success">
                    <g:message code="${flash.message}" />
                </div>
            </g:if>
            <g:if test="${flash.error}" >
                <div class="alert alert-error">
                    <g:message code="${flash.error}" />
                </div>
            </g:if>
            <table class="table table-striped">
                <tr>
                    <th width="50px">#</th>
                    <th width="350px"><g:message code="vanity.cms.tag.name" /></th>
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