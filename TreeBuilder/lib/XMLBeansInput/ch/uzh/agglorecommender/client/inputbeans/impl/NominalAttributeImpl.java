/*
 * XML Type:  NominalAttribute
 * Namespace: ch/uzh/agglorecommender/client/inputbeans
 * Java type: ch.uzh.agglorecommender.client.inputbeans.NominalAttribute
 *
 * Automatically generated - do not modify.
 */
package ch.uzh.agglorecommender.client.inputbeans.impl;
/**
 * An XML NominalAttribute(@ch/uzh/agglorecommender/client/inputbeans).
 *
 * This is a complex type.
 */
public class NominalAttributeImpl extends ch.uzh.agglorecommender.client.inputbeans.impl.AttributeImpl implements ch.uzh.agglorecommender.client.inputbeans.NominalAttribute
{
    private static final long serialVersionUID = 1L;
    
    public NominalAttributeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CATEGORY$0 = 
        new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "Category");
    
    
    /**
     * Gets array of all "Category" elements
     */
    public ch.uzh.agglorecommender.client.inputbeans.Category[] getCategoryArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            java.util.List targetList = new java.util.ArrayList();
            get_store().find_all_element_users(CATEGORY$0, targetList);
            ch.uzh.agglorecommender.client.inputbeans.Category[] result = new ch.uzh.agglorecommender.client.inputbeans.Category[targetList.size()];
            targetList.toArray(result);
            return result;
        }
    }
    
    /**
     * Gets ith "Category" element
     */
    public ch.uzh.agglorecommender.client.inputbeans.Category getCategoryArray(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            ch.uzh.agglorecommender.client.inputbeans.Category target = null;
            target = (ch.uzh.agglorecommender.client.inputbeans.Category)get_store().find_element_user(CATEGORY$0, i);
            if (target == null)
            {
                throw new IndexOutOfBoundsException();
            }
            return target;
        }
    }
    
    /**
     * Returns number of "Category" element
     */
    public int sizeOfCategoryArray()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(CATEGORY$0);
        }
    }
    
    /**
     * Sets array of all "Category" element  WARNING: This method is not atomicaly synchronized.
     */
    public void setCategoryArray(ch.uzh.agglorecommender.client.inputbeans.Category[] categoryArray)
    {
        check_orphaned();
        arraySetterHelper(categoryArray, CATEGORY$0);
    }
    
    /**
     * Sets ith "Category" element
     */
    public void setCategoryArray(int i, ch.uzh.agglorecommender.client.inputbeans.Category category)
    {
        generatedSetterHelperImpl(category, CATEGORY$0, i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
    }
    
    /**
     * Inserts and returns a new empty value (as xml) as the ith "Category" element
     */
    public ch.uzh.agglorecommender.client.inputbeans.Category insertNewCategory(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            ch.uzh.agglorecommender.client.inputbeans.Category target = null;
            target = (ch.uzh.agglorecommender.client.inputbeans.Category)get_store().insert_element_user(CATEGORY$0, i);
            return target;
        }
    }
    
    /**
     * Appends and returns a new empty value (as xml) as the last "Category" element
     */
    public ch.uzh.agglorecommender.client.inputbeans.Category addNewCategory()
    {
        synchronized (monitor())
        {
            check_orphaned();
            ch.uzh.agglorecommender.client.inputbeans.Category target = null;
            target = (ch.uzh.agglorecommender.client.inputbeans.Category)get_store().add_element_user(CATEGORY$0);
            return target;
        }
    }
    
    /**
     * Removes the ith "Category" element
     */
    public void removeCategory(int i)
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(CATEGORY$0, i);
        }
    }
}
