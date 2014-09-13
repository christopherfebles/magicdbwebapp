package com.christopherfebles.magic.form.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.powermock.reflect.Whitebox;

import com.christopherfebles.magic.dao.SearchDAO;
import com.christopherfebles.magic.dao.parameter.SearchParameter;
import com.christopherfebles.magic.dao.parameter.SearchParameter.FieldName;
import com.christopherfebles.magic.enums.Color;
import com.christopherfebles.magic.enums.SubType;
import com.christopherfebles.magic.enums.Type;
import com.christopherfebles.magic.form.model.DisplayMagicCard;
import com.christopherfebles.magic.form.model.SearchFormModel;
import com.christopherfebles.magic.model.MagicCard;
import com.christopherfebles.magic.service.MagicCardSearchService;
import com.christopherfebles.magic.testsupport.DAOTester;
import com.christopherfebles.magic.testsupport.UnitTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( locations = { "classpath:/applicationContext-test.xml" } )
@Category( UnitTest.class )
public class MagicCardSearchServiceTest extends DAOTester {
    
    private static final Logger LOG = LoggerFactory.getLogger( MagicCardSearchServiceTest.class );
    
    @Autowired
    private MagicCardSearchService service;
    
    @Autowired
    private SearchDAO searchDAO;
    
    @Test
    public void testWhiteHumans() throws Exception {
        
        SearchFormModel searchForm = this.getSearchForm();
        
        List<DisplayMagicCard> searchResults = service.searchDatabase( searchForm );
        
        assertNotNull( searchResults );
        assertFalse( searchResults.isEmpty() );
    }
    
    @Test
    public void testSearchDatabase() throws Exception {
        
        SearchFormModel searchForm = this.getSearchForm();
        searchForm.setCardName( "Paladin" );
        searchForm.setNameSearch( SearchFormModel.NAME_SEARCH_CONTAINS );
        
        List<SearchParameter> searchParms = this.callGenerateSearchParameters( searchForm );
        int expectedNumberOfResults = searchDAO.numberOfResultsWithSearchParameters( searchParms );
        
        List<DisplayMagicCard> actualResults = service.searchDatabase( searchForm );
        assertNotNull( actualResults );
        
        //Count actual number of MagicCards returned
        int actualNumberOfResults = 0;
        for( DisplayMagicCard displayCard : actualResults ) {
            actualNumberOfResults++;
            actualNumberOfResults += displayCard.getAlternateVersions().size();
        }
        assertEquals( expectedNumberOfResults, actualNumberOfResults );
        
        MagicCard testCard = new MagicCard();
        testCard.setMultiverseId( 370786 ); //This should be the LOWEST Multiverse Id of an English "%Paladin%" currently in the database.
        testCard.setName( "Fiendslayer Paladin" );
        DisplayMagicCard testDMCard = new DisplayMagicCard( testCard );
        
        assertTrue( actualResults.contains( testDMCard ) );
    }
    
    @Test
    public void testGenerateSearchParameters() throws Exception {
        
        SearchParameter nameParameter = new SearchParameter( FieldName.NAME, "Fireball" );;
        SearchParameter colorParameter = new SearchParameter( FieldName.COLOR, Color.WHITE );
        SearchParameter typeParameter = new SearchParameter( FieldName.TYPE, Type.CREATURE );
        SearchParameter subTypeParameter = new SearchParameter( FieldName.SUBTYPE, new SubType( "Human" ) );
        
        SearchFormModel searchForm = this.getSearchForm();
        List<SearchParameter> searchParms = this.callGenerateSearchParameters( searchForm );
        
        assertFalse( searchParms.contains( nameParameter ) );
        assertTrue( searchParms.contains( colorParameter ) );
        assertTrue( searchParms.contains( typeParameter ) );
        assertTrue( searchParms.contains( subTypeParameter ) );
    }
    
    private SearchFormModel getSearchForm() {
        
        SearchFormModel searchForm = new SearchFormModel();
        searchForm.setColor( Color.WHITE );
        searchForm.setType( Type.CREATURE );
        searchForm.setSubType( new SubType( "Human" ) );
        
        return searchForm;        
    }
    
    private List<SearchParameter> callGenerateSearchParameters( SearchFormModel searchForm ) throws Exception {
        return Whitebox.invokeMethod( service, "generateSearchParameters", searchForm );
    }
    
    protected static boolean searchServiceTesterInitializationComplete = false;
    @Override
    public void additionalSetUp() {
        
        if ( !searchServiceTesterInitializationComplete ) {
            LOG.trace( "Initializing embedded database for SearchService tests." );
            
            //Add Islands
            super.addAllIslands();
            
            //Add Paladins
            //20 total, 12 unique names
            super.addPaladins();
            
            //Add some white cards
            super.addWhiteCards();
            
            //Add Planeswalkers to database
            super.addPlaneswalkersNamedChandra();
            
            //Create multiple pages of Fireball for page testing
            super.addFireballs();

            searchServiceTesterInitializationComplete = true;
        }
    }
}
