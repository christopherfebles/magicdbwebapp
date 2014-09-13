<%@ include file="/WEB-INF/jsp/header.jsp" %>

<%-- Variable "card" required which is a Java MagicCard object. --%>

<div class="displayCardTable">
    <div class="displayCardImage">
        <jsp:include page="countWidget.jsp" />
        
        <img id="${card.multiverseId}_img" name="${card.multiverseId}_img" src="image.do?id=${card.multiverseId}"
            alt="${card.name}: ${card.multiverseId}" title="${card.name}: ${card.multiverseId}" class="cardImg" />
        
        <c:if test="${not empty card.alternateVersions}">
            <br>
            <%-- Insert Paging Controls for alternate views of this card --%>
            <div class="pageControlsDiv">
                <span id="${card.multiverseId}_previous" class="previous"> &lt;&lt;Previous <span class="helperText">(by
                        ID)</span>
                </span> <span id="${card.multiverseId}_next" class="next"> Next <span class="helperText">(by ID)</span>&gt;&gt;
                </span>
            </div>
        </c:if>
    </div>

    <div class="displayCardData">
        <jsp:include page="cardData.jsp" />
    </div>
</div>

<!-- JQuery -->
<!-- <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script> -->
<script type="text/javascript" src="js/jQuery/jquery.min.js" ></script>
<!-- JQuery UI -->
<!-- <script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.0/jquery-ui.min.js"></script> -->
<script type="text/javascript" src="js/jQuery/jquery-ui.min.js" ></script>
<script type="text/javascript">
    jQuery(document).ready(function() {
        cardNavigator.setUpPaging('${card.JSONString}');
    });
</script>
