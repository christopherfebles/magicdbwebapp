(function(cardNavigator, $, undefined) {

    // Private Property
    var cardList = {};
    var hyperlinkCSSClass = "pseudoLink";

    // Public Method
    cardNavigator.setUpPaging = function( currentCardJSONString ) {

        var currentCard = jQuery.parseJSON( currentCardJSONString );
        //Store data
        cardList[currentCard.multiverseId] = currentCard;

        setupExpansionSelector( currentCard );

        resetNavigation( currentCard.parentMultiverseId, currentCard.multiverseId );
    };

    // Private method
    /**
     * Check if the given option exists in the list
     */
    function doesOptionExistInList( option, optionList ) {

        return optionList.filter(function() {
            return jQuery(this).text() === option;
        }).length === 0;

    }

    //Private method
    function setupExpansionSelector( currentCard ) {

        //Setup selector
        if ( currentCard.alternateVersions !== undefined ) {
            var expansionSelector = jQuery( "#" + currentCard.multiverseId + "_select" );

            //Add parent option
            jQuery( "<option>" )
                .attr( "id", currentCard.multiverseId + "_option" )
                .text( currentCard.expansion )
                .val( currentCard.multiverseId )
                .prop( "selected", true )
                .appendTo( expansionSelector );

            var selectOptions = jQuery( "#" + currentCard.multiverseId + "_select option");
            //Add alternate options
            for( var x = 0; x < currentCard.alternateVersions.length; x++ ) {
                var alternateCard = currentCard.alternateVersions[x];
                //Filter out expansions already present in the selector
                if ( doesOptionExistInList( alternateCard.expansion, selectOptions ) ) {
                    //This option hasn't been created yet
                    jQuery( "<option>" )
                        .text( alternateCard.expansion )
                        .val( alternateCard.multiverseId )
                        .appendTo( expansionSelector );
                }
                //Update the contents of selectOptions
                selectOptions = jQuery( "#" + currentCard.multiverseId + "_select option");
            }

            //Sort selector
            selectOptions.sort(function(a, b) {
                if (a.text > b.text) {
                    return 1;
                }
                else if (a.text < b.text) {
                    return -1;
                }
                else {
                    return 0;
                }
            });
            expansionSelector.empty().append(selectOptions);
            var selectedOption = jQuery( "#" + currentCard.multiverseId + "_option");
            selectedOption.prop( "selected", true );

            //Add onChange event handler
            expansionSelector.change( function() {
                var newMultiverseId = parseInt( jQuery( this ).val() );
                var newCard = cardList[ newMultiverseId ];

                if ( newCard === undefined ) {
                    //Search for the new card
                    for( var x = 0; x < currentCard.alternateVersions.length; x++ ) {
                        var alternateCard = currentCard.alternateVersions[x];
                        cardList[ alternateCard.multiverseId ] = alternateCard;
                        if ( alternateCard.multiverseId === newMultiverseId ) {
                            newCard = alternateCard;
                            break;
                        }
                    }
                }
                redrawCard( newCard );
            });
        }
    }

    // Private Method
    function redrawCard( newCard ) {
        //Reset all the card values in the table to the given card
        //All ID fields are prefixed with the parent card's multiverse id
        var parentId = newCard.parentMultiverseId;
        var cardId = newCard.multiverseId;

        //Reset card image
        var img = jQuery( "#" + parentId + "_img" );
        img.fadeTo( "slow" , 0.1, function() {
            //Once original card has faded out, swap in new card image
            img.attr( "src", "image.do?id=" + cardId );
            img.attr( "alt", newCard.name + ": " + cardId );
            img.attr( "title", newCard.name + ": " + cardId );
            img.fadeTo( "slow", 1 );

        });

        var idLink = jQuery( "#" + parentId + "_idLink" );
        idLink.attr( "href", "http://gatherer.wizards.com/Pages/Card/Details.aspx?multiverseid=" + cardId );
        idLink.text( cardId );

        var expansion = jQuery( "#" + parentId + "_expansion" );
        expansion.text( newCard.expansion );

        //Reset dropdown selector
        var selectOptions = jQuery( "#" + parentId + "_select option");
        selectOptions.filter(function() {
            return jQuery(this).text() === newCard.expansion;
        }).prop('selected', true);

        resetNavigation( parentId, cardId );
    }

    //Private Method
    function resetNavigation( parentMultiverseId, currentMultiverseId ) {
        //Reset the nav links based on the given Id

        //Set up Previous/Next links
        var nextSpanObj = jQuery( "#" + parentMultiverseId +  "_next");
        var previousSpanObj = jQuery( "#" + parentMultiverseId +  "_previous");

        nextSpanObj.unbind( "click" );
        nextSpanObj.removeClass( hyperlinkCSSClass );

        previousSpanObj.unbind( "click" );
        previousSpanObj.removeClass( hyperlinkCSSClass );

        var nextCard = getNextCard( currentMultiverseId );
        var previousCard = getPreviousCard( currentMultiverseId );

        if ( nextCard !== null ) {
            //Enable next link
            nextSpanObj.addClass( hyperlinkCSSClass );
            nextSpanObj.click( function() {
                redrawCard( nextCard );
            });
        }

        if ( previousCard !== null ) {
            //Enable previous link
            previousSpanObj.addClass( hyperlinkCSSClass );
            previousSpanObj.click( function() {
                redrawCard( previousCard );
            });
        }

        //If both disabled, hide both
        if ( previousCard === null && nextCard === null ) {
            previousSpanObj.hide();
            nextSpanObj.hide();
        } else {
            previousSpanObj.show();
            nextSpanObj.show();
        }
    }

    // Private Method
    function getNextCard( multiverseId ) {
        var currentCard = cardList[ multiverseId ];
        var parentCard = cardList[ currentCard.parentMultiverseId ];
        var nextCard = null;

        if ( parentCard !== undefined && parentCard.alternateVersions !== undefined ) {
            //Loop through alternateVersions and get the next card in the list after the current one, if any
            var nextCardId = -1;
            if ( parentCard.multiverseId === currentCard.multiverseId ) {
                //This card is the parent
                nextCardId = 0;
            } else {
                for( var x = 0; x < parentCard.alternateVersions.length; x++ ) {
                    if ( parentCard.alternateVersions[x].multiverseId === currentCard.multiverseId ) {
                        nextCardId = x + 1;
                        break;
                    }
                }
            }
            if ( nextCardId !== -1 && nextCardId < parentCard.alternateVersions.length ) {
                nextCard = parentCard.alternateVersions[ nextCardId ];
                //Store data
                cardList[nextCard.multiverseId] = nextCard;
            }
        }

        return nextCard;
    }

    // Private Method
    function getPreviousCard( multiverseId ) {
        var currentCard = cardList[ multiverseId ];
        var parentCard = cardList[ currentCard.parentMultiverseId ];
        var previousCard = null;

        if ( parentCard !== undefined && parentCard.alternateVersions !== undefined ) {
            //Loop through alternateVersions and get the next card in the list after the current one, if any
            var previousCardId = -1;
            if ( parentCard.multiverseId !== currentCard.multiverseId ) {
                //This card is NOT the parent
                for( var x = 0; x < parentCard.alternateVersions.length; x++ ) {
                    if ( parentCard.alternateVersions[x].multiverseId === currentCard.multiverseId ) {
                        previousCardId = x - 1;
                        if ( previousCardId === -1 ) {
                            //Set previous card to parent
                            previousCard = parentCard;
                        }
                        break;
                    }
                }
            }
            if ( previousCardId !== -1 && previousCardId < parentCard.alternateVersions.length ) {
                previousCard = parentCard.alternateVersions[ previousCardId ];
                //Store data
                cardList[previousCard.multiverseId] = previousCard;
            }
        }

        return previousCard;
    }

}(window.cardNavigator = window.cardNavigator || {}, jQuery));