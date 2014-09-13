<%@ include file="/WEB-INF/jsp/header.jsp" %>

<%-- Variable "card" required which is a Java MagicCard object. --%>
<%-- This page is embedded in displayCard.jsp, which should have the "card" variable included. --%>
<table class="countWidgetTable">
    <tr>
        <td>
            <span id="${card.multiverseId}_incrementButton" class="arrow-up"></span>
        </td>
        <td align="center" valign="middle" rowspan="3">
            <button id="${card.multiverseId}_countUpdateButton" name="${card.multiverseId}_countUpdateButton" value="Update" type="button" class="countButton">Update</button><br>
            <button id="${card.multiverseId}_countResetButton" name="${card.multiverseId}_countResetButton" value="Reset" type="button" class="countButton">Reset</button>
        </td>
    </tr>
    <tr>
        <td>
            <%-- Display current count here --%>
            <input id="${card.multiverseId}_countDisplay" name="${card.multiverseId}_countDisplay" type="text" value="0" class="countDisplayInput"/>
        </td>
    </tr>
    <tr>
        <td>
            <span id="${card.multiverseId}_decrementButton" class="arrow-down"></span>
        </td>
    </tr>
</table>