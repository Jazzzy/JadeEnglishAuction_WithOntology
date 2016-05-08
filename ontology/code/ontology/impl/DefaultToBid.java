package ontology.impl;


import ontology.*;

/**
* Protege name: ToBid
* @author OntologyBeanGenerator v4.1
* @version 2016/05/9, 00:14:38
*/
public class DefaultToBid implements ToBid {

  private static final long serialVersionUID = -6366770272942530551L;

  private String _internalInstanceName = null;

  public DefaultToBid() {
    this._internalInstanceName = "";
  }

  public DefaultToBid(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: anOffer
   */
   private Offer anOffer;
   public void setAnOffer(Offer value) { 
    this.anOffer=value;
   }
   public Offer getAnOffer() {
     return this.anOffer;
   }

}
