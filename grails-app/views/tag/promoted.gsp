<html>
<head>
    <title></title>
    <meta name="layout" content="cms"/>
    <r:require module="promoteTag"/>
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
                    <th style="width: 50px">#</th>
                    <th style="width: 250px"><g:message code="vanity.cms.tag.name" /></th>
                    <th><g:message code="vanity.cms.tag.status" /></th>
                </tr>

                <g:each in="${elements}" var="element" status="i">
                    <tr class="to-promote">
                        <td><%= i + 1 %></td>
                        <g:if test="${element.isPromoted()}">
                            <td class="name" href="${createLink(action: 'ajaxPromoteTag', id:element.id)}">${element.name}</td>
                        </g:if>
                        <g:else>
                            <td class="name" href="${createLink(action: 'ajaxUnpromoteTag', id:element.id)}">${element.name}</td>
                        </g:else>
                        <td>${element.status}</td>
                    </tr>
                </g:each>
            </table>
        </div>
    </div>
</body>
</html>