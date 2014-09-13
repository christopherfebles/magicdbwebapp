(function(combobox, $, undefined) {

    // Private Property
    var comboboxListIdStr = "";
    var inputIdStr = "";

    // Public Method
    combobox.createWithSelectElement = function( selectIdStr ) {
        var selectElement = jQuery( "#" + selectIdStr );
        var spanWrapper = jQuery( "<span>" )
                            .addClass( "combobox-span" )
                            .attr( "id", selectIdStr + "_span" )
                            .insertAfter( selectElement );

        inputIdStr = selectIdStr + "_input";
        //TODO: Add arrow key listeners to select options in the dropdown
        var input = jQuery( "<input>" )
                    .attr( "id", inputIdStr )
                    .keyup( function() {
                        combobox.filterList( jQuery(this) );
                    })
                    .appendTo( spanWrapper );

        //Create button
        jQuery( "<span>" )
            .attr( "id", selectIdStr + "_button" )
            .addClass( "arrow-down" )
            .addClass( "combobox-arrow" )
            .appendTo( spanWrapper );
        jQuery( "#" + selectIdStr + "_button" ).click( function(){
            //Show all list items
            jQuery( "#" + comboboxListIdStr + " > li" ).each(function() {
                jQuery(this).show();
            });
            //Toggle show/hide of list
            toggleList();
        });

        comboboxListIdStr = selectIdStr + "_ul" ;
        var optionList = jQuery( "<ul>" )
                            .attr( "id", comboboxListIdStr )
                            .addClass( "combobox-list" )
                            .hide()
                            .appendTo( spanWrapper );

        //Loop through each item in the Select and add it to the list
        jQuery( "#" + selectIdStr + " option" ).each( function() {
            if ( jQuery(this).val().trim() !== '' ) {

                var li = jQuery('<li/>')
                        .appendTo( optionList );

                //onClick, listItems should populate the input field and hide the list
                //Create listItem
                jQuery('<a>')
                    .text( jQuery(this).val() )
                    .click( function(){
                        combobox.listItemSelected( jQuery(this) );
                    })
                    .addClass( "combobox-listItem" )
                    .appendTo( li );
            }
        });

        //Set default value
        input.val( selectElement.val() );
        //Delete existing dropdown
        selectElement.remove();
        //Replace with new input field
        input.attr( "id", selectIdStr );
        input.attr( "name", selectIdStr );
        inputIdStr = selectIdStr;

        //Move list under input field
        optionList.position({
            my:        "left top",
            at:        "left bottom",
            of:        input,
            collision: "none"
        });

        //Comment out the following line to hide list by default
        //toggleList();
    };

    //Public Method
    combobox.listItemSelected = function( item ) {
        //populate the input field and hide the list
        var input = jQuery( "#" + inputIdStr );

        input.val( item.text() );

        toggleList();
    };

    //Public Method
    //See: http://stackoverflow.com/questions/1772035/filtering-a-list-as-you-type-with-jquery
    combobox.filterList = function( input ) {
        var text = input.val();
        var list = jQuery( "#" + comboboxListIdStr );

        var someItemsShowing = false;
        if ( text.trim() !== '' ) {
            jQuery( "#" + comboboxListIdStr + " > li" ).each(function() {
                //If a value in the dropdown Starts With the entered text, display
                if ( jQuery(this).text().toLowerCase().indexOf(text.toLowerCase()) === 0 ) {
                    jQuery(this).show();
                    someItemsShowing = true;
                }
                else {
                    jQuery(this).hide();
                }
            });

            if ( !list.is(':visible') ) {
                toggleList();
            }
        } else {
            //If no text in the box, hide the dropdown
            if ( list.is(':visible') ) {
                toggleList();
            }
        }
        //If no list items are visible, hide the dropdown
        if ( list.is(':visible') && !someItemsShowing ) {
            toggleList();
        }

    };

    // Private Method
    function toggleList() {
        var list = jQuery( "#" + comboboxListIdStr );
        if ( list.is(':visible') ) {
            //Hide
            list.slideUp( "slow" );
            jQuery( ".combobox-arrow.arrow-up" ).addClass( "arrow-down" );
            jQuery( ".combobox-arrow.arrow-down" ).removeClass( "arrow-up" );
        } else {
            //Show
            list.slideDown( "slow" );
            jQuery( ".combobox-arrow.arrow-down" ).addClass( "arrow-up" );
            jQuery( ".combobox-arrow.arrow-up" ).removeClass( "arrow-down" );
        }
    }

}(window.combobox = window.combobox || {}, jQuery));