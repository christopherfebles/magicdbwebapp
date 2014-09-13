(function(displayCard, $, undefined) {

    //Public Method
    displayCard.setUpCards = function() {
        displayCard.markOwnedCards();

        //Add click events to card count arrows
        jQuery( "span[id$='_incrementButton']" ).click( function() {
            var multiverseId = calculateMultiverseId( jQuery(this).attr( "id" ) );
            displayCard.incrementDisplayCount( multiverseId );
        });
        jQuery( "span[id$='_decrementButton']" ).click( function() {
            var multiverseId = calculateMultiverseId( jQuery(this).attr( "id" ) );
            displayCard.decrementDisplayCount( multiverseId );
        });

        //Add click events to card count update/reset buttons
        jQuery( "button[id$='_countUpdateButton']" ).click( function() {
            var multiverseId = calculateMultiverseId( jQuery(this).attr( "id" ) );

            //Update the owned count in the database to the current value of the input box
            var countDisplay = "#" + multiverseId + "_countDisplay";
            var currentCount = Number( jQuery( countDisplay ).val() );

            var databaseCount = updateNumberOfCardsOwned( multiverseId, currentCount );
            //Need some kind of success notification here
            if ( databaseCount === currentCount ) {
                //Success
                alert( 'Database updated.' );
            } else {
                //Fail
                alert( 'Database update failed.' );
            }
        });
        jQuery( "button[id$='_countResetButton']" ).click( function() {
            var multiverseId = calculateMultiverseId( jQuery(this).attr( "id" ) );

            //Reset the owned count to the value stored in the database
            var numCardsOwned = numberOfCardsOwned( multiverseId );
            var countDisplay = "#" + multiverseId + "_countDisplay";
            jQuery( countDisplay ).val( numCardsOwned );
        });
    };

    //Public Method
    displayCard.markOwnedCards = function() {
        //Mark all cards displayed on the page as owned
        var allImages = jQuery( "img[id$='_img']" );

        allImages.each( function() {
            //Image Id = "${card.multiverseId}_img"
            var linkId = jQuery(this).attr( "id" );

            var multiverseId = linkId.slice( 0, linkId.indexOf( "_" ) );
            updateCardOwnershipDisplay( multiverseId );
        });

        //When card changed, update ownership indicator
        //pseudoLink
        jQuery( ".pseudoLink" ).click( function() {
            //Calculate multiverseId
            var linkId = jQuery(this).attr( "id" );

            //linkId = "${card.multiverseId}_previous" or "${card.multiverseId}_next"
            var multiverseId = calculateMultiverseId( linkId );
            updateCardOwnershipDisplay( multiverseId );
        });
    };

    //Public Method
    displayCard.incrementDisplayCount = function( multiverseId ) {
        var countDisplay = "#" + multiverseId + "_countDisplay";
        var currentCount = jQuery( countDisplay ).val();

        currentCount++;
        jQuery( countDisplay ).val( currentCount );
    };

    //Public Method
    displayCard.decrementDisplayCount = function( multiverseId ) {
        var countDisplay = "#" + multiverseId + "_countDisplay";
        var currentCount = jQuery( countDisplay ).val();

        currentCount--;
        if ( currentCount < 0 ) {
            currentCount = 0;
        }
        jQuery( countDisplay ).val( currentCount );
    };

    // Private Method
    function calculateMultiverseId( idStr ) {
        //Extract the multiverseId from an HTML element's ID tag where the multiverseID is separated by an underscore
        var multiverseId = idStr.slice( 0, idStr.indexOf( "_" ) );
        return multiverseId;
    }

    // Private Method
    function updateCardOwnershipDisplay( multiverseId ) {
        //Update ownership indicator of the given Card
        var imgSelector = "#" + multiverseId + "_img";
        var imgParent = jQuery( imgSelector ).parent();

        var numCardsOwned = numberOfCardsOwned( multiverseId );
        if ( numCardsOwned !== 0 ) {
            //Update displayed count
            var countDisplay = "#" + multiverseId + "_countDisplay";
            jQuery( countDisplay ).val( numCardsOwned );

            //Show indicator
            imgParent.css({
                border: "5px solid red"
            });
        } else {
            //Hide Indicator
            imgParent.css({
                border: ""
            });
        }
    }

    // Private Method
    function updateNumberOfCardsOwned( multiverseId, numberOfOwnedCards ) {
        var numOwned = 0;
        jQuery.ajax({
            type : "POST",
            url : "setNumberOfCardsOwned.do",
            async : false,
            data: {
                id : multiverseId,
                number : numberOfOwnedCards
                }
        }).done( function( data ) {
            numOwned = Number( data );
            if ( numOwned === -1 ) {
                numOwned = 0;
            }
        });
        return numOwned;
    }

    // Private Method
    function numberOfCardsOwned( multiverseId ) {
        var numOwned = 0;
        jQuery.ajax({
            type : "GET",
            url : "numberOfCardsOwned.do",
            async : false,
            data: {
                id : multiverseId
                }
        }).done( function( data ) {
            numOwned = Number( data );
            if ( numOwned === -1 ) {
                numOwned = 0;
            }
        });
        return numOwned;
    }

    // Private Method
    function isCardOwned( multiverseId ) {
        var isOwned = false;
        jQuery.ajax({
            type : "GET",
            url : "isCardOwned.do",
            async : false,
            data: {
                id : multiverseId
                }
        }).done( function( data ) {
            isOwned = ( data === "true" );
        });
        return isOwned;
    }

}(window.displayCard = window.displayCard || {}, jQuery));