(function(searchResults, $, undefined) {

    //Public Method
    searchResults.normalizeCardTables = function() {
        //Dynamically resize cards so they are aligned
        //All cards are displayed within a table with css class displayCardTable

        //Get the largest height and reset all to the same height
        var maxHeight = 0;
        var cardTable = jQuery( ".displayCardTable" );
        cardTable.each( function() {
            var height = jQuery(this).height();
            if ( height > maxHeight ) {
                maxHeight = height;
            }
        });
        cardTable.height( maxHeight );

        //Recenter tables
        cardTable.css({
            textAlign : "center",
            verticalAlign: "middle"
        });
    };

}(window.searchResults = window.searchResults || {}, jQuery));