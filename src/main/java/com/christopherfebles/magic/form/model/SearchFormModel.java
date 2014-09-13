package com.christopherfebles.magic.form.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.christopherfebles.magic.enums.Color;
import com.christopherfebles.magic.enums.SubType;
import com.christopherfebles.magic.enums.Type;

public class SearchFormModel {
    
    //Default to AND search for fewer results
    private static final boolean AND_DEFAULT = true;
    
    public static final String NAME_SEARCH_STARTS_WITH = "startsWith";
    public static final String NAME_SEARCH_CONTAINS = "contains";
    public static final String NAME_SEARCH_EXACT = "exact";
    
    private String cardName;
    private String nameSearch = NAME_SEARCH_EXACT;
    
    private Color color;
    private boolean andColor = AND_DEFAULT;
    
    private Type type;
    private boolean andType = AND_DEFAULT;
    
    private SubType subType;
    private boolean andSubType = AND_DEFAULT;
    
    private String expansion;
    private boolean andExpansion = AND_DEFAULT;
    
    private boolean ownedCardsOnly = false;
    
    public boolean isEmpty() {
        return StringUtils.isEmpty( getCardName() ) &&
                getColor() == null &&
                getType() == null &&
                getSubType() == null &&
                getExpansion() == null;
    }
    
    public String getCardName() {
        return cardName;
    }

    public void setCardName( String cardName ) {
        this.cardName = cardName;
    }

    public Color getColor() {
        return color;
    }

    public void setColor( Color color ) {
        this.color = color;
    }
    
    public Type getType() {
        return type;
    }

    public void setType( Type type ) {
        this.type = type;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString( this, ToStringStyle.MULTI_LINE_STYLE );
    }

    public boolean isNameSearchContains() {
        return nameSearch.equals( NAME_SEARCH_CONTAINS );
    }

    public boolean isNameSearchExact() {
        return nameSearch.equals( NAME_SEARCH_EXACT );
    }

    public boolean isNameSearchStartsWith() {
        return nameSearch.equals( NAME_SEARCH_STARTS_WITH );
    }

    public String getNameSearch() {
        return this.nameSearch;
    }

    public void setNameSearch( String nameSearch ) {
        Validate.isTrue( nameSearch.equals( NAME_SEARCH_CONTAINS ) ||
                         nameSearch.equals( NAME_SEARCH_EXACT ) ||
                         nameSearch.equals( NAME_SEARCH_STARTS_WITH ) );
        this.nameSearch = nameSearch;
    }

    public boolean isAndColor() {
        return andColor;
    }

    public boolean getAndColor() {
        return this.isAndColor();
    }

    public void setAndColor( boolean andColor ) {
        this.andColor = andColor;
    }

    public boolean isAndType() {
        return andType;
    }

    public boolean getAndType() {
        return this.isAndType();
    }

    public void setAndType( boolean andType ) {
        this.andType = andType;
    }

    public SubType getSubType() {
        return subType;
    }

    public void setSubType( SubType subType ) {
        this.subType = subType;
        
        if ( StringUtils.isEmpty( this.subType.getType() ) ) {
            this.subType = null;
        }
    }

    public void setSubType( String subType ) {
        if ( StringUtils.isNotEmpty( subType ) ) {
            this.subType = new SubType( subType );
        }
    }

    public boolean isAndSubType() {
        return andSubType;
    }

    public boolean getAndSubType() {
        return isAndSubType();
    }

    public void setAndSubType( boolean andSubType ) {
        this.andSubType = andSubType;
    }

    public boolean isAndExpansion() {
        return andExpansion;
    }

    public boolean getAndExpansion() {
        return this.isAndExpansion();
    }

    public void setAndExpansion( boolean andExpansion ) {
        this.andExpansion = andExpansion;
    }

    public String getExpansion() {
        return expansion;
    }

    public void setExpansion( String expansion ) {
        this.expansion = expansion;
    }

    public boolean isOwnedCardsOnly() {
        return ownedCardsOnly;
    }

    public boolean getOwnedCardsOnly() {
        return this.isOwnedCardsOnly();
    }

    public void setOwnedCardsOnly( boolean ownedCardsOnly ) {
        this.ownedCardsOnly = ownedCardsOnly;
    }

}
