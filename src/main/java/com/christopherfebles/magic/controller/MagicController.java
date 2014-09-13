package com.christopherfebles.magic.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.christopherfebles.magic.dao.ExpansionDAO;
import com.christopherfebles.magic.dao.MagicCardDAO;
import com.christopherfebles.magic.dao.SubTypeDAO;
import com.christopherfebles.magic.enums.Color;
import com.christopherfebles.magic.enums.SubType;
import com.christopherfebles.magic.enums.Type;
import com.christopherfebles.magic.form.model.DisplayMagicCard;
import com.christopherfebles.magic.form.model.SearchFormModel;
import com.christopherfebles.magic.service.MagicCardSearchService;

@Controller
public class MagicController {

    private static final Logger LOG = LoggerFactory.getLogger( MagicController.class );

    @Autowired
    private MagicCardSearchService searchService;
    
    @Autowired
    private MagicCardDAO cardDAO;
    
    @Autowired
    private SubTypeDAO subTypeDAO;
    
    @Autowired
    private ExpansionDAO expansionDAO;
    
    //Data caching
    private static List<SubType> subTypeList;

    /**
     * Get the list of colors for display in the UI
     * 
     * @return    A list of displayable colors for a dropdown
     */
    private List<Color> getCardColors() {
        List<Color> colorList = new ArrayList<>( Arrays.asList( Color.values() ) );
        colorList.remove( Color.VARIABLE_COLORLESS );
        return colorList;
    }
    
    /**
     * Get a list of subtypes for display in the UI
     * 
     * @return    A list of displayable SubTypes for a dropdown
     */
    private List<SubType> getSubTypes() {
        if ( subTypeList == null ) {
            subTypeList = subTypeDAO.getAllSubTypes();
        }
        return subTypeList;
    }

    @RequestMapping( value = "", method = RequestMethod.GET )
    public String main() {
        LOG.trace( "Redirecting from main url to search form." );
        return "redirect:search.form";
    }

    @RequestMapping( value = "/search.form", method = RequestMethod.GET )
    public String searchForm( Model model, HttpServletRequest request ) {
        LOG.trace( "Search form loaded" );
        request.getSession().invalidate();

        return this.prepareSearchModel( model );
    }

    @RequestMapping( value = "/search.do", method = { RequestMethod.GET, RequestMethod.POST } )
    public String searchDo( @ModelAttribute( "searchObj" ) SearchFormModel searchFormParm, Model model,
                            @RequestParam( value = "page", required = false ) String page,
                            HttpServletRequest request ) {
        LOG.trace( "Search form loaded." );
        
        SearchFormModel searchForm = searchFormParm;
        if ( searchForm.isEmpty() ) {
            searchForm = ( SearchFormModel ) request.getSession().getAttribute( "MagicController_searchForm" );
            if ( searchForm == null || searchForm.isEmpty() ) {
                return "redirect:search.form";
            }
        }
        
        List<DisplayMagicCard> resultList = null;
        Integer pageNumber = ( Integer ) request.getSession().getAttribute( "MagicController_pageNumber" );
        
        if ( StringUtils.isEmpty( page ) ) {

            request.getSession().setAttribute( "MagicController_searchForm", searchForm );
            pageNumber = 1;
            
            Integer numPages = searchService.numberOfPagesForSearch( searchForm );
            request.getSession().setAttribute( "MagicController_numPages", numPages );
            
            Integer numResults = searchService.numberOfResultsForSearch( searchForm );
            request.getSession().setAttribute( "MagicController_numResults", numResults );
        } else {
            switch (page) {
                case "next":
                    pageNumber++;
                    break;
                case "previous":
                    pageNumber--;
                    break;
                default:
                    try {
                        pageNumber = Integer.parseInt(page);
                    } catch (NumberFormatException e) {
                        //Unknown page value
                        LOG.error("Unknown value for page: {}", page);
                    }
                    break;
            }
        }
        resultList = searchService.searchDatabaseByPage( searchForm, pageNumber );
        request.getSession().setAttribute( "MagicController_pageNumber", pageNumber );
        
        model.addAttribute( "cards", resultList );
        model.addAttribute( "searchObj", searchForm );
        model.addAttribute( "currentPage", pageNumber );
        model.addAttribute( "numPages", request.getSession().getAttribute( "MagicController_numPages" ) );
        model.addAttribute( "numResults", request.getSession().getAttribute( "MagicController_numResults" ) );
        
        return this.prepareSearchModel( model );
    }
    
    private String prepareSearchModel( Model model ) {

        if ( !model.containsAttribute( "searchObj" ) ) {
            model.addAttribute( "searchObj", new SearchFormModel() );
        }
        
        if ( !model.containsAttribute( "colors" ) ) {
            model.addAttribute( "colors", getCardColors() );
        }

        if ( !model.containsAttribute( "types" ) ) {
            model.addAttribute( "types", Type.values() );
        }

        if ( !model.containsAttribute( "subTypes" ) ) {
            model.addAttribute( "subTypes", getSubTypes() );
        }

        if ( !model.containsAttribute( "expansions" ) ) {
            model.addAttribute( "expansions", expansionDAO.getAllExpansions() );
        }
        return "search";
    }

    @RequestMapping( value = "/image.do", method = RequestMethod.GET, produces = "image/jpeg" )
    @ResponseBody
    public byte[] getCardImage( @RequestParam( "id" ) String id ) {
        LOG.trace( "Loading image for multiverse id {}", id );

        return cardDAO.getCardImageById( Integer.parseInt( id ) );
    }

    @RequestMapping( value = "/isCardOwned.do", method = RequestMethod.GET )
    @ResponseBody
    public String isOwned( @RequestParam( "id" ) String id ) {
        LOG.trace( "Checking if card owned for multiverse id {}", id );

        return String.valueOf( cardDAO.isCardOwned( Integer.parseInt( id ) ) );
    }

    @RequestMapping( value = "/numberOfCardsOwned.do", method = RequestMethod.GET )
    @ResponseBody
    public String numberOwned( @RequestParam( "id" ) String id ) {
        LOG.trace( "Loading number of cards owned for multiverse id {}", id );

        return String.valueOf( cardDAO.numberOfOwnedCard( Integer.parseInt( id ) ) );
    }

    @RequestMapping( value = "/setNumberOfCardsOwned.do", method = RequestMethod.POST )
    @ResponseBody
    public String updateNumberOwned( @RequestParam( "id" ) String id, @RequestParam( "number" ) String number ) {
        LOG.trace( "Updating number of cards owned to {} for multiverse id {}", number, id );

        int idInt = Integer.parseInt( id );
        int numberInt = Integer.parseInt( number );
        
        cardDAO.updateOwnedCardCount( idInt, numberInt );
        
        return String.valueOf( cardDAO.numberOfOwnedCard( idInt ) );
    }
}