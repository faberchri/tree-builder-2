/*
 * XML Type:  Category
 * Namespace: ch/uzh/agglorecommender/client/inputbeans
 * Java type: ch.uzh.agglorecommender.client.inputbeans.Category
 *
 * Automatically generated - do not modify.
 */
package ch.uzh.agglorecommender.client.inputbeans.impl;
/**
 * An XML Category(@ch/uzh/agglorecommender/client/inputbeans).
 *
 * This is a complex type.
 */
public class CategoryImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.uzh.agglorecommender.client.inputbeans.Category
{
    private static final long serialVersionUID = 1L;
    
    public CategoryImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName CATEGORYTAG$0 = 
        new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "CategoryTag");
    private static final javax.xml.namespace.QName IDENTIFICATIONREGEX$2 = 
        new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "IdentificationRegex");
    
    
    /**
     * Gets the "CategoryTag" element
     */
    public java.lang.String getCategoryTag()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CATEGORYTAG$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "CategoryTag" element
     */
    public org.apache.xmlbeans.XmlString xgetCategoryTag()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CATEGORYTAG$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "CategoryTag" element
     */
    public void setCategoryTag(java.lang.String categoryTag)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CATEGORYTAG$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CATEGORYTAG$0);
            }
            target.setStringValue(categoryTag);
        }
    }
    
    /**
     * Sets (as xml) the "CategoryTag" element
     */
    public void xsetCategoryTag(org.apache.xmlbeans.XmlString categoryTag)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CATEGORYTAG$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(CATEGORYTAG$0);
            }
            target.set(categoryTag);
        }
    }
    
    /**
     * Gets the "IdentificationRegex" element
     */
    public java.lang.String getIdentificationRegex()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(IDENTIFICATIONREGEX$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "IdentificationRegex" element
     */
    public org.apache.xmlbeans.XmlString xgetIdentificationRegex()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(IDENTIFICATIONREGEX$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "IdentificationRegex" element
     */
    public void setIdentificationRegex(java.lang.String identificationRegex)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(IDENTIFICATIONREGEX$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(IDENTIFICATIONREGEX$2);
            }
            target.setStringValue(identificationRegex);
        }
    }
    
    /**
     * Sets (as xml) the "IdentificationRegex" element
     */
    public void xsetIdentificationRegex(org.apache.xmlbeans.XmlString identificationRegex)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(IDENTIFICATIONREGEX$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(IDENTIFICATIONREGEX$2);
            }
            target.set(identificationRegex);
        }
    }
}
