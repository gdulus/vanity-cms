<%@ page import="vanity.cms.search.reindexer.ReIndexingPhase" %>
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
        <table class="table">
            <tr>
                <th style="width: 200px;"><g:message code="search.reindex.target"/></th>
                <th style="width: 100px;"><g:message code="search.reindex.state"/></th>
                <th><g:message code="search.reindex.progress"/></th>
                <th style="width: 50px;"></th>
            </tr>
            <g:each in="${supportedReIndexingTargets}" var="reIndexingTarget">
                <tr>
                    <td>${reIndexingTarget}</td>
                    <g:if test="${state[reIndexingTarget].phase == ReIndexingPhase.NONE}">
                        <td>-</td>
                        <td>-</td>
                        <td>
                            <g:link action="startReIndexing"
                                    class="btn btn-success"
                                    params="[reIndexingTarget: reIndexingTarget]"
                                    onclick="return confirm('${g.message(code: 'search.reindex.start.confirm')}')">
                                <g:message code="search.reindex.start"/>
                            </g:link>
                        </td>
                    </g:if>
                    <g:else>
                        <td>${state[reIndexingTarget].phase}</td>
                        <td>
                            <div class="progress progress-striped">
                                <div class="bar" style="width:  ${state[reIndexingTarget].percent}%;"></div>
                            </div>
                        </td>
                        <td>
                            <g:link action="stopReIndexing"
                                    class="btn btn-danger"
                                    params="[reIndexingTarget: reIndexingTarget]"
                                    onclick="return confirm('${g.message(code: 'search.reindex.stop.confirm')}')">
                                <g:message code="search.reindex.stop"/>
                            </g:link>
                        </td>
                    </g:else>
                </tr>
            </g:each>
        </table>
    </div>
</div>

</body>
</html>