package ontology;



/**
* Protege name: ToOffer
* @author OntologyBeanGenerator v4.1
* @version 2016/05/9, 02:34:12
*/
public class ToOffer implements jade.content.AgentAction {

   private static final long serialVersionUID = 3782210241590169965L;

   private String _internalInstanceName = null;

   public ToOffer() {
      this._internalInstanceName = "";
   }

   public ToOffer(String instance_name) {
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
