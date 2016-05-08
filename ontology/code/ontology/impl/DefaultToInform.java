package ontology.impl;


import ontology.*;

/**
* Protege name: ToInform
* @author OntologyBeanGenerator v4.1
* @version 2016/05/9, 00:14:38
*/
public class DefaultToInform implements ToInform {

  private static final long serialVersionUID = -6366770272942530551L;

  private String _internalInstanceName = null;

  public DefaultToInform() {
    this._internalInstanceName = "";
  }

  public DefaultToInform(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: bill
   */
   private Buy bill;
   public void setBill(Buy value) { 
    this.bill=value;
   }
   public Buy getBill() {
     return this.bill;
   }

}
