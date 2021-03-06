package ontology;



/**
* Protege name: Buy
* @author OntologyBeanGenerator v4.1
* @version 2016/05/9, 02:34:12
*/
public class Buy implements jade.content.Concept {

   private static final long serialVersionUID = 3782210241590169965L;

   private String _internalInstanceName = null;

   public Buy() {
      this._internalInstanceName = "";
   }

   public Buy(String instance_name) {
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
