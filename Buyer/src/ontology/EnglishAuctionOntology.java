// file: EnglishAuctionOntology.java generated by ontology bean generator.  DO NOT EDIT, UNLESS YOU ARE REALLY SURE WHAT YOU ARE DOING!
package ontology;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ConceptSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.TermSchema;
import jade.util.leap.HashMap;
import nl.uva.psy.swi.beangenerator.ProtegeIntrospector;
import nl.uva.psy.swi.beangenerator.ProtegeTools;
import nl.uva.psy.swi.beangenerator.SlotHolder;
import ontology.impl.*;

/**
 * file: EnglishAuctionOntology.java
 *
 * @author OntologyBeanGenerator v4.1
 * @version 2016/05/9, 02:34:12
 */
public class EnglishAuctionOntology extends jade.content.onto.Ontology implements ProtegeTools.ProtegeOntology {
    /**
     * These hashmap store a mapping from jade names to either protege names of SlotHolder
     * containing the protege names.  And vice versa
     */
    private HashMap jadeToProtege;

    private static final long serialVersionUID = 3782210241590169965L;

    //NAME
    public static final String ONTOLOGY_NAME = "englishAuction";
    private static ProtegeIntrospector introspect = new ProtegeIntrospector();
    // The singleton instance of this ontology
    private static Ontology theInstance = new EnglishAuctionOntology();

    public static Ontology getInstance() {
        return theInstance;
    }

    // ProtegeOntology methods
    public SlotHolder getSlotNameFromJADEName(SlotHolder jadeSlot) {
        return (SlotHolder) jadeToProtege.get(jadeSlot);
    }


    // storing the information
    private void storeSlotName(String jadeName, String javaClassName, String slotName) {
        jadeToProtege.put(new SlotHolder(javaClassName, jadeName), new SlotHolder(javaClassName, slotName));
    }


    // VOCABULARY
    public static final String TOBID_ANOFFER = "anOffer";
    public static final String TOBID = "ToBid";
    public static final String TOINFORM_BILL = "bill";
    public static final String TOINFORM = "ToInform";
    public static final String TOOFFER_ANOFFER = "anOffer";
    public static final String TOOFFER = "ToOffer";
    public static final String OFFER_ITEM = "item";
    public static final String OFFER_PRICE = "price";
    public static final String OFFER = "Offer";
    public static final String BOOK_TITLE = "title";
    public static final String BOOK = "Book";
    public static final String BUY_ITEM = "item";
    public static final String BUY_PRICE = "price";
    public static final String BUY = "Buy";

    /**
     * Constructor
     */
    private EnglishAuctionOntology() {
        super(ONTOLOGY_NAME, BasicOntology.getInstance());
        introspect.setOntology(this);
        jadeToProtege = new HashMap();
        try {

            // adding Concept(s)
            ConceptSchema buySchema = new ConceptSchema(BUY);
            add(buySchema, DefaultBuy.class);
            ConceptSchema bookSchema = new ConceptSchema(BOOK);
            add(bookSchema, DefaultBook.class);
            ConceptSchema offerSchema = new ConceptSchema(OFFER);
            add(offerSchema, DefaultOffer.class);

            // adding AgentAction(s)
            AgentActionSchema toOfferSchema = new AgentActionSchema(TOOFFER);
            add(toOfferSchema, DefaultToOffer.class);
            AgentActionSchema toInformSchema = new AgentActionSchema(TOINFORM);
            add(toInformSchema, DefaultToInform.class);
            AgentActionSchema toBidSchema = new AgentActionSchema(TOBID);
            add(toBidSchema, DefaultToBid.class);

            // adding AID(s)

            // adding Predicate(s)


            // adding fields
            buySchema.add(BUY_PRICE, (TermSchema) getSchema(BasicOntology.FLOAT), ObjectSchema.MANDATORY);
            buySchema.add(BUY_ITEM, bookSchema, ObjectSchema.MANDATORY);
            bookSchema.add(BOOK_TITLE, (TermSchema) getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
            offerSchema.add(OFFER_PRICE, (TermSchema) getSchema(BasicOntology.FLOAT), ObjectSchema.MANDATORY);
            offerSchema.add(OFFER_ITEM, bookSchema, ObjectSchema.MANDATORY);
            toOfferSchema.add(TOOFFER_ANOFFER, offerSchema, ObjectSchema.MANDATORY);
            toInformSchema.add(TOINFORM_BILL, buySchema, ObjectSchema.MANDATORY);
            toBidSchema.add(TOBID_ANOFFER, offerSchema, ObjectSchema.MANDATORY);

            // adding name mappings
            storeSlotName("price", "ontology.impl.DefaultBuy", "price");
            storeSlotName("item", "ontology.impl.DefaultBuy", "item");
            storeSlotName("title", "ontology.impl.DefaultBook", "title");
            storeSlotName("price", "ontology.impl.DefaultOffer", "price");
            storeSlotName("item", "ontology.impl.DefaultOffer", "item");
            storeSlotName("anOffer", "ontology.impl.DefaultToOffer", "anOffer");
            storeSlotName("bill", "ontology.impl.DefaultToInform", "bill");
            storeSlotName("anOffer", "ontology.impl.DefaultToBid", "anOffer");

            // adding inheritance

        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
    }
}
