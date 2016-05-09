package ontology.impl;


import ontology.*;

/**
* Protege name: ToInform
* @author OntologyBeanGenerator v4.1
* @version 2016/05/9, 02:34:12
*/
public class DefaultToInform implements ToInform {

  private static final long serialVersionUID = 3782210241590169965L;

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
