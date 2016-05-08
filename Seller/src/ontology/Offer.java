package ontology;



/**
* Protege name: Offer
* @author OntologyBeanGenerator v4.1
* @version 2016/05/9, 00:14:38
*/
public interface Offer extends jade.content.Concept {

   /**
   * Protege name: price
   */
   public void setPrice(float value);
   public float getPrice();

   /**
   * Protege name: item
   */
   public void setItem(Book value);
   public Book getItem();

}
