package jade;

/*****************************************************************
 * JADE - Java Agent DEvelopment Framework is a framework to develop
 * multi-agent systems in compliance with the FIPA specifications.
 * Copyright (C) 2000 CSELT S.p.A.
 * <p>
 * GNU Lesser General Public License
 * <p>
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 * <p>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 *****************************************************************/


import jade.content.lang.Codec;
import jade.content.lang.leap.LEAPCodec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import model.Auction;
import model.Book;
import model.Seller;
import ontology.*;
import viewController.BookSellerGUI;
import viewController.Controller;

import java.util.ArrayList;
import java.util.Hashtable;


public class BookSellerAgent extends Agent {
    // The catalogue of books for sale (maps the title of a book to its price)
    private Hashtable catalogue;
    // The GUI by means of which the user can add books in the catalogue
    private static BookSellerGUI myGui;

    private Controller controller;

    private Seller seller;

    private Codec codec = new SLCodec();
    //private Codec codec = new LEAPCodec();
    private Ontology ontology = EnglishAuctionOntology.getInstance();


    public Seller getSeller() {
        return seller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Controller getController() {
        return controller;
    }

    protected void setup() {

        this.seller = new Seller();

        catalogue = new Hashtable();

        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);


        myGui = new BookSellerGUI();
        myGui.setBookSellerAgent(this);
        new Thread() {
            @Override
            public void run() {
                myGui.launchThis();
            }
        }.start();


    }

    public ArrayList<AID> getAllBuyers() {
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("book-buying");
        template.addServices(sd);
        try {

            DFAgentDescription[] result = DFService.search(this, template);
            ArrayList<AID> buyers = new ArrayList<>();
            for (int i = 0; i < result.length; ++i) {
                buyers.add(result[i].getName());
            }
            return buyers;
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        return null;
    }


    protected void takeDown() {

        myGui.dispose();

        System.out.println("Seller-agent " + getAID().getName() + " terminating.");
    }


    public void addBookToCatalog(final String title) {
        addBehaviour(new OneShotBehaviour() {
            public void action() {
                seller.addBook(new Book(title));
                System.out.println(title + " inserted into catalogue");
                getController().updateListOfBooksRemote();
            }
        });
    }

    public void addBookToAuction(final String title, float reservePrice, float increment, float startingPrice) {
        addBehaviour(new OneShotBehaviour() {
            public void action() {
                Book book = seller.getBookByName(title);
                if (book == null) {
                    Controller.showError("Not such book with that name registered, you need to add it first");
                    return;
                }
                if (book.getStock() <= 0) {
                    Controller.showError("Not enough stock for that book");
                    return;
                }
                if (seller.addAuction(book, reservePrice, increment, startingPrice)) { //ADDED AUCTIONS


                    getController().updateListOfAuctionsRemote();
                    myAgent.addBehaviour(new Auctioning(myAgent, seller.getCurrentAuctionByTitle(book.getTitle())));


                    ArrayList<AID> listOfBuyers = getAllBuyers();
                    ACLMessage cfp = new ACLMessage(ACLMessage.CFP);

                    //ONTOLOGY CODE
                    cfp.setLanguage(codec.getName());
                    cfp.setOntology(ontology.getName());

                    ToOffer ontToOffer = new ToOffer();
                    Offer ontOffer = new Offer();
                    ontology.Book ontBook = new ontology.Book();
                    ontBook.setTitle(book.getTitle());
                    ontOffer.setItem(ontBook);
                    ontOffer.setPrice(seller.getCurrentAuctionByTitle(book.getTitle()).getCurrentPrice());
                    ontToOffer.setAnOffer(ontOffer);

                    try {
                        Action action = new Action(myAgent.getAID(),ontToOffer);
                        getContentManager().fillContent(cfp, action);
                        //myAgent.send(cfp);
                    } catch (Codec.CodecException ce) {
                        ce.printStackTrace();
                    } catch (OntologyException oe) {
                        oe.printStackTrace();
                    }
                    //END ONTOLOGY CODE

                    //This should be an ontology now
                    //cfp.setContent(String.valueOf( seller.getAuctionByTitle(book.getTitle()).getCurrentPrice()));

                    cfp.setConversationId(seller.getCurrentAuctionByTitle(book.getTitle()).getItem().getTitle());

                    for (AID aid : listOfBuyers) {
                        cfp.addReceiver(aid);
                    }
                    myAgent.send(cfp);
                    controller.updateListOfAuctionsRemote();


                }else{
                    System.err.print("Error when adding auction");

                }

                return;
            }
        });
    }


    private class Auctioning extends TickerBehaviour {

        private Auction auction;

        private AID winner = null;


        public Auctioning(Agent agent, Auction auction) {
            super(agent, 10000);
            this.auction = auction;
        }

        public void finishAuction() {

            if (winner == null) {
                this.auction.addToLog("On tick " + this.getTickCount() + " we have finished the auction with no winner");
                auction.endAuctionFail();
                this.stop();
                return;
            }

            if (auction.getCurrentPrice() < auction.getReservePrice()) {
                this.auction.addToLog("On tick " + this.getTickCount() + " we have finished the auction with no winner because we didn't surpass the reserve price");
                auction.endAuctionFail();
                this.stop();
                return;
            }

            ACLMessage informWin = new ACLMessage(ACLMessage.REQUEST);
            informWin.addReceiver(this.winner);
            informWin.setConversationId(auction.getItem().getTitle());

            //ONTOLOGY CODE
            informWin.setLanguage(codec.getName());
            informWin.setOntology(ontology.getName());

            ToInform ontToInform = new ToInform();
            Buy ontBuy = new Buy();
            ontology.Book ontBook = new ontology.Book();

            ontBook.setTitle(auction.getItem().getTitle());
            ontBuy.setItem(ontBook);
            ontBuy.setPrice((auction.getCurrentPrice()));
            ontToInform.setBill(ontBuy);

            try {
                Action action = new Action(myAgent.getAID(),ontToInform);
                getContentManager().fillContent(informWin, action);
                //myAgent.send(informWin);
            } catch (Codec.CodecException ce) {
                ce.printStackTrace();
            } catch (OntologyException oe) {
                oe.printStackTrace();
            }
            //END ONTOLOGY CODE

            //informWin.setContent(String.valueOf(auction.getCurrentPrice() - auction.getIncrement()));
            myAgent.send(informWin);

            this.auction.addToLog("On tick " + this.getTickCount() + " we have finished the auction with [" + this.winner.getLocalName() + "] as a winner and he paid " + (auction.getCurrentPrice()));
            auction.endAuctionSuccess();
            getController().updateListOfBooksRemote();
            getController().updateListOfAuctionsRemote();
            this.stop();
            return;

        }


        public void onTick() {

            MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE),MessageTemplate.MatchConversationId(auction.getItem().getTitle()));

            ACLMessage msg = myAgent.receive(mt);
            if (msg == null) { //No proposes for this auction
                if (winner == null) {
                    this.auction.addToLog("On tick " + this.getTickCount() + " we didn't have any replies and no winners, we dont end the auction");
                } else {
                    auction.setCurrentPrice(auction.getCurrentPrice()-auction.getIncrement());
                    finishAuction();
                }

            } else {

                this.winner = msg.getSender();//We pick the first and take it as the current winner

//                ACLMessage reply = msg.createReply();
//                reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
//                reply.setContent(String.valueOf(auction.getCurrentPrice()));
//                myAgent.send(reply); //We tell him he is the current winner

                this.auction.addToLog("On tick " + this.getTickCount() + " we have selected [" + this.winner.getLocalName() + "] as the current winner since he sent the first proposal");

                msg = myAgent.receive(mt); //We keep getting all the other proposes
                if (msg == null) { //if we only had one propose, this will be the winner and we finish the auction
                    this.auction.addToLog("On tick " + this.getTickCount() + " we only had one reply");
                    if (auction.getCurrentPrice() >= auction.getReservePrice()) { //Coment this if if we dont want to end it till we have reservation price.
                        finishAuction();
                        return;
                    }
                } else {
                    while (msg != null) { //And responding telling them that they are not in the first place
//                        reply = msg.createReply();
//                        reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
//                        reply.setContent(String.valueOf(auction.getCurrentPrice()));
//                        myAgent.send(reply);
                        this.auction.addToLog("On tick " + this.getTickCount() + " we have received a proposal from [" + msg.getSender().getLocalName() + "] but it was not the first one");
                        msg = myAgent.receive(mt);
                    }

                }
                this.auction.makeIncrement();
            }

            //Now we send the next cfp to all the current buyers
            ArrayList<AID> listOfBuyers = getAllBuyers();
            ACLMessage cfp = new ACLMessage(ACLMessage.CFP);

            //ONTOLOGY CODE
            cfp.setLanguage(codec.getName());
            cfp.setOntology(ontology.getName());

            ToOffer ontToOffer = new ToOffer();
            Offer ontOffer = new Offer();
            ontology.Book ontBook = new ontology.Book();
            ontBook.setTitle(auction.getItem().getTitle());
            ontOffer.setItem(ontBook);
            ontOffer.setPrice(seller.getCurrentAuctionByTitle(auction.getItem().getTitle()).getCurrentPrice());
            ontToOffer.setAnOffer(ontOffer);

            try {
                Action action = new Action(myAgent.getAID(),ontToOffer);
                getContentManager().fillContent(cfp, action);

            } catch (Codec.CodecException ce) {
                ce.printStackTrace();
            } catch (OntologyException oe) {
                oe.printStackTrace();
            }
            //END ONTOLOGY CODE

            cfp.setConversationId(auction.getItem().getTitle());
            for (AID aid : listOfBuyers) {
                cfp.addReceiver(aid);
            }
            myAgent.send(cfp);
            controller.updateListOfAuctionsRemote();

        }
    }

}
