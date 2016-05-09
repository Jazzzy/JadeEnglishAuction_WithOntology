package ontology;



/**
* Protege name: Buy
* @author OntologyBeanGenerator v4.1
* @version 2016/05/9, 02:34:12
*/
public interface Buy extends jade.content.Concept {

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
