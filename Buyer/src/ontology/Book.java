package ontology;



/**
* Protege name: Book
* @author OntologyBeanGenerator v4.1
* @version 2016/05/9, 02:34:12
*/
public class Book implements jade.content.Concept {

private static final long serialVersionUID = 3782210241590169965L;

private String _internalInstanceName = null;

public Book() {
        this._internalInstanceName = "";
        }

public Book(String instance_name) {
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
