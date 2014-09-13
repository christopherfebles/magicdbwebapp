<%@ include file="/WEB-INF/jsp/header.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Magic the Gathering Database</title>
        <%-- JQuery UI --%>
        <!-- <link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.0/themes/smoothness/jquery-ui.css" /> -->
        <link rel="stylesheet" href="css/jquery-ui.css" />
        <%-- Add local CSS --%>
        <link rel="stylesheet" href="css/style.css" />
    </head>
    <body>
            
        <form:form method="POST" modelAttribute="searchObj" action="search.do" cssClass="searchForm">
        
            <button id="resetButton" name="resetButton" value="Reset" type="reset">Reset Form</button>
            <!-- <br><br> -->
            
            <table id="searchElementsTable" class="searchElementsTable">
                <tr>
                    <td>
                        <form:label path="cardName">Card Name</form:label>
                        <form:input path="cardName" />
                        <form:select path="nameSearch">
                            <form:option value="contains">Contains</form:option>
                            <form:option value="exact">Exact</form:option>
                            <form:option value="startsWith">Starts With</form:option>
                        </form:select>
                    </td>
                    <td>
                        <form:checkbox path="andColor" label="and"></form:checkbox>
                        &nbsp;&nbsp;
                        
                        <form:label path="color">Color</form:label>
                        <form:select path="color">
                            <form:option value="">&nbsp;</form:option>
                            <form:options items="${colors}" itemLabel="displayableName" />
                        </form:select>
                    </td>
                </tr>
    
                <tr>
                    <td>
                        <form:checkbox path="andType" label="and"></form:checkbox>
                        &nbsp;&nbsp;
                        
                        <form:label path="type">Type</form:label>
                        <form:select path="type">
                            <form:option value="">&nbsp;</form:option>
                            <form:options items="${types}" itemLabel="displayableName" />
                        </form:select>
                    </td>
                    <td>
                        <form:checkbox path="andSubType" label="and"></form:checkbox>
                        &nbsp;&nbsp;
                        
                        <form:label path="subType">SubType</form:label>
                        <form:select path="subType" cssStyle="display: none;">
                            <form:option value="">&nbsp;</form:option>
                            <form:options items="${subTypes}" itemLabel="displayableName" />
                        </form:select>
                    </td>
                </tr>
                
                <tr>
                    <td>
                        <form:checkbox path="andExpansion" label="and"></form:checkbox>
                        &nbsp;&nbsp;
                        
                        <form:label path="expansion">Expansion</form:label>
                        <form:select path="expansion">
                            <form:option value="">&nbsp;</form:option>
                            <form:options items="${expansions}" />
                        </form:select>
                    </td>
                    <td>
                        <form:checkbox path="ownedCardsOnly" label="Only Owned Cards"></form:checkbox>
                    </td>
                </tr>
                <!-- <br><br> -->
            </table>
            
            <button id="submitButton" name="submitButton" value="Search" type="submit">Search</button>
        
        </form:form>
        
        <br><br>
        <jsp:include page="searchResults.jsp" />
        
        <!-- JQuery -->
        <!-- <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script> -->
        <script type="text/javascript" src="js/jQuery/jquery.min.js" ></script>
        <!-- JQuery UI -->
        <!-- <script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.0/jquery-ui.min.js"></script> -->
        <script type="text/javascript" src="js/jQuery/jquery-ui.min.js" ></script>
        <!-- Add local JavaScript -->
        <!-- <script type="text/javascript" src="js/displayCard.js" ></script>
        <script type="text/javascript" src="js/cardNavigator.js" ></script>
        <script type="text/javascript" src="js/combobox.js" ></script>
        <script type="text/javascript" src="js/searchResults.js" ></script> -->
        <script type="text/javascript" src="js/script.js" ></script>
        
        <script type="text/javascript">
        
            jQuery( document ).ready( function() {
                combobox.createWithSelectElement( "subType" );
            });
            
        </script>
    </body>
</html>