<%@ include file="/WEB-INF/jsp/header.jsp" %>

<div id="searchResultsDiv" class="searchResults">

    <c:if test="${not empty cards}">
        Total Results: (${numResults})<br>
        
        <c:if test="${currentPage ne 1}">
            <span id="previousPageSpan_upper" class="previous">
                <a href="?page=previous">&lt;&lt;Previous Page</a>
            </span>
        </c:if> 
        <c:if test="${currentPage eq 1}">
            <span id="previousPageSpan_upper" class="previous">
                &lt;&lt;Previous Page
            </span>
        </c:if> 
        
        &nbsp;&nbsp; Page: ${currentPage} of ${numPages} &nbsp;&nbsp;
        
        <c:if test="${currentPage ne numPages}">
            <span id="nextPageSpan_upper" class="next">
                <a href="?page=next">Next Page&gt;&gt;</a>
            </span>
        </c:if>
        <c:if test="${currentPage eq numPages}">
            <span id="nextPageSpan_upper" class="next">
                Next Page&gt;&gt;
            </span>
        </c:if>
        
        <br><br>
    </c:if>

    <c:forEach var="aCard" items="${cards}">
        <c:set var="card" scope="request" value="${aCard}" />
        <jsp:include page="displayCard.jsp" />
    </c:forEach>
    
    

    <c:if test="${not empty cards}">
        <br><br>
        <c:if test="${currentPage ne 1}">
            <span id="previousPageSpan_lower" class="previous">
                <a href="?page=previous">&lt;&lt;Previous Page</a>
            </span>
        </c:if> 
        <c:if test="${currentPage eq 1}">
            <span id="previousPageSpan_lower" class="previous">
                &lt;&lt;Previous Page
            </span>
        </c:if> 
        
        &nbsp;&nbsp; Page: ${currentPage} of ${numPages} &nbsp;&nbsp;
        
        <c:if test="${currentPage ne numPages}">
            <span id="nextPageSpan_lower" class="next">
                <a href="?page=next">Next Page&gt;&gt;</a>
            </span>
        </c:if>
        <c:if test="${currentPage eq numPages}">
            <span id="nextPageSpan_lower" class="next">
                Next Page&gt;&gt;
            </span>
        </c:if>
        
    </c:if>
</div>

<!-- JQuery -->
<!-- <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script> -->
<script type="text/javascript" src="js/jQuery/jquery.min.js" ></script>
<!-- JQuery UI -->
<!-- <script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.0/jquery-ui.min.js"></script> -->
<script type="text/javascript" src="js/jQuery/jquery-ui.min.js" ></script>
<script type="text/javascript">

    jQuery( document ).ready( function() {
        //Wait a bit for the other JavaScript to execute
        setTimeout( function() {
            searchResults.normalizeCardTables();
        }, 250 );
        displayCard.setUpCards();
    });
    
</script>