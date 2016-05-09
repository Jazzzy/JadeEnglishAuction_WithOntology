package jade;


import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import model.Book;
import model.Buyer;
import ontology.*;
import viewController.BookBuyerGUI;
import viewController.Controller;

public class BookBuyerAgent extends Agent {

/*
    // The list of known seller agents
    private AID[] sellerAgents = null;
*/

    private Controller controller;

    private static BookBuyerGUI myGui;

    private Buyer buyer;

    private Codec codec = new SLCodec();
    //private Codec codec = new LEAPCodec();
    private Ontology ontology = EnglishAuctionOntology.getInstance();

    public Buyer getBuyer() {
        return buyer;
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    protected void setup() {

        this.buyer = new Buyer();

        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);


        myGui = new BookBuyerGUI();
        myGui.setBookSellerAgent(this);
        new Thread() {
            @Override
            public void run() {
                myGui.launchThis();
            }
        }.start();

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("book-buying");
        sd.setName("JADE-book-buying");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        addBehaviour(new respondToProposals());
        addBehaviour(new respondToWins());

    }

    private class respondToProposals extends CyclicBehaviour {

        @Override
        public void action() {
            //MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP).MatchLanguage(codec.getName()).MatchOntology(ontology.getName());


            MessageTemplate mt1 = MessageTemplate.MatchPerformative(ACLMessage.CFP);
            MessageTemplate mt2=MessageTemplate.MatchLanguage(codec.getName());
            MessageTemplate mt3=MessageTemplate.MatchOntology(ontology.getName());
            MessageTemplate mta1= MessageTemplate.and(mt1,mt2);
            MessageTemplate mtFinal= MessageTemplate.and(mta1,mt3);

            ACLMessage msg = myAgent.receive(mtFinal);

            if (msg != null) {
                String title = msg.getConversationId();

                ContentElement ce = null;

                Float price = null; //Float.parseFloat(msg.getContent());
                Book book = null; // = buyer.getBookByName(title);

                try {
                    ce = getContentManager().extractContent(msg);
                } catch (Codec.CodecException e) {
                    e.printStackTrace();
                } catch (OntologyException e) {
                    e.printStackTrace();
                }
                if (ce instanceof Action) {
                    ToOffer toOffer = (ToOffer) ((Action) ce).getAction();
                    price = toOffer.getAnOffer().getPrice();
                    book = buyer.getBookByName(toOffer.getAnOffer().getItem().getTitle());
                }


                if (book != null) {
                    if (price <= book.getMaxPriceToPay()) {
                        book.addToLog("We have accepted the price for this book: " + price);
                        ACLMessage reply = msg.createReply();
                        reply.setPerformative(ACLMessage.PROPOSE);

                        //ONTOLOGY CODE
                        reply.setLanguage(codec.getName());
                        reply.setOntology(ontology.getName());

                        ToBid ontToBid = new ToBid();
                        Offer ontOffer = new Offer();
                        ontology.Book ontBook = new ontology.Book();
                        ontBook.setTitle(book.getTitle());
                        ontOffer.setItem(ontBook);
                        ontOffer.setPrice(price);
                        ontToBid.setAnOffer(ontOffer);

                        try {
                            getContentManager().fillContent(reply, ontToBid);
                            myAgent.send(reply);
                        } catch (Codec.CodecException cep) {
                            cep.printStackTrace();
                        } catch (OntologyException oe) {
                            oe.printStackTrace();
                        }
                        //END ONTOLOGY CODE

                        //reply.setContent(String.valueOf(price));

                    }
                } else {
                    block();
                }

                controller.updateListOfBooksRemote();

            } else {
                block();
            }


        }
    }

    private class respondToWins extends CyclicBehaviour {

        @Override
        public void action() {
            //MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST).MatchLanguage(codec.getName()).MatchOntology(ontology.getName());
            MessageTemplate mt1 = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
            MessageTemplate mt2=MessageTemplate.MatchLanguage(codec.getName());
            MessageTemplate mt3=MessageTemplate.MatchOntology(ontology.getName());
            MessageTemplate mta1= MessageTemplate.and(mt1,mt2);
            MessageTemplate mtFinal= MessageTemplate.and(mta1,mt3);

            ACLMessage msg = myAgent.receive(mtFinal);
            if (msg != null) {
                String title = msg.getConversationId();

                ContentElement ce = null;

                Float price = null; //Float.parseFloat(msg.getContent());
                Book book = null; // = buyer.getBookByName(title);

                try {
                    ce = getContentManager().extractContent(msg);
                } catch (Codec.CodecException e) {
                    e.printStackTrace();
                } catch (OntologyException e) {
                    e.printStackTrace();
                }
                if (ce instanceof Action) {
                    ToInform toInform = (ToInform) ((Action) ce).getAction();
                    price = toInform.getBill().getPrice();
                    book = buyer.getBookByName(toInform.getBill().getItem().getTitle());
                    book.addToLog("Won the book for the price: " + price);
                }

                if (book != null) {
                    Controller.showWebInfo("You have won the book: " + book.getTitle() + " for the price: " + price + "\n" + book.getLog());
                    buyer.removeBookByTitle(book.getTitle());
                } else {
                    block();
                }
                controller.updateListOfBooksRemote();

            } else {
                block();
            }


        }
    }


    // Put agent clean-up operations here
    protected void takeDown() {

        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        myGui.dispose();
        System.out.println("Buyer-agent " + getAID().getName() + " terminating.");
    }

    public void addWantedBook(final String title, final float maxPrice) {
        addBehaviour(new OneShotBehaviour() {
            public void action() {
                buyer.addBook(new Book(title, maxPrice));
                System.out.println(title + " inserted into wanted books");
                getController().updateListOfBooksRemote();
            }
        });
    }
}

