<%@ include file="/WEB-INF/jsp/header.jsp" %>

<%-- Variable "card" required which is a Java MagicCard object. --%>
<%-- This file is dynamically included in displayCard.jsp --%>
<table>
    <tr>
        <th class="topRow">ID:</th>
        <td class="topRow">
            <a id="${card.multiverseId}_idLink" href="http://gatherer.wizards.com/Pages/Card/Details.aspx?multiverseid=${card.multiverseId}" target="_blank">${card.multiverseId}</a>
        </td>
    </tr>
    <tr>
        <th>Name:</th>
        <td>${card.name}</td>
    </tr>
    <tr>
        <th>Mana Cost:</th>
        <td>
            <c:forEach var="mana" items="${card.manaFilename}">
                <img src="images/${mana}" class="manaImg" alt="${mana}"/>
            </c:forEach>
        </td>
    </tr>
    <tr>
        <th>Types:</th>
        <td>${card.type}</td>
    </tr>
    <c:if test="${not empty card.text}">
        <tr>
            <th>Text:</th>
            <td>${card.text}</td>
        </tr>
    </c:if>
    <c:if test="${not empty card.flavorText}">
        <tr>
            <th>Flavor Text:</th>
            <td>${card.flavorText}</td>
        </tr>
    </c:if>
    <c:if test="${not empty card.power}">
        <tr>
            <th>Power/Toughness:</th>
            <td>${card.power}/${card.toughness}</td>
        </tr>
    </c:if>
    <c:if test="${not empty card.loyalty}">
        <tr>
            <th>Loyalty:</th>
            <td>${card.loyalty}</td>
        </tr>
    </c:if>
    <c:if test="${empty card.alternateVersions}">
        <tr>
            <th>Expansion:</th>
            <td id="${card.multiverseId}_expansion">${card.expansion}</td>
        </tr>
    </c:if>
    <c:if test="${not empty card.alternateVersions}">
        <tr>
            <th>Expansion:</th>
            <td>
                <select id="${card.multiverseId}_select" name="${card.multiverseId}_select">
                </select>
            </td>
        </tr>
    </c:if>
    <c:if test="${not empty card.rarity}">
        <tr>
            <th>Rarity:</th>
            <td>${card.rarity}</td>
        </tr>
    </c:if>
</table>