package ontology.impl;


import ontology.*;

/**
* Protege name: Buy
* @author OntologyBeanGenerator v4.1
* @version 2016/05/9, 00:14:38
*/
public class DefaultBuy implements Buy {

  private static final long serialVersionUID = -6366770272942530551L;

  private String _internalInstanceName = null;

  public DefaultBuy() {
    this._internalInstanceName = "";
  }

  public DefaultBuy(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: price
   */
   private float price;
   public void setPrice(float value) { 
    this.price=value;
   }
   public float getPrice() {
     return this.price;
   }

   /**
   * Protege name: item
   */
   private Book item;
   public void setItem(Book value) { 
    this.item=value;
   }
   public Book getItem() {
     return this.item;
   }

}
