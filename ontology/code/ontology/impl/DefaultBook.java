package ontology.impl;


import ontology.*;

/**
* Protege name: Book
* @author OntologyBeanGenerator v4.1
* @version 2016/05/9, 00:14:38
*/
public class DefaultBook implements Book {

  private static final long serialVersionUID = -6366770272942530551L;

  private String _internalInstanceName = null;

  public DefaultBook() {
    this._internalInstanceName = "";
  }

  public DefaultBook(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * The title of the book
   * Protege name: title
   */
   private String title;
   public void setTitle(String value) { 
    this.title=value;
   }
   public String getTitle() {
     return this.title;
   }

}
