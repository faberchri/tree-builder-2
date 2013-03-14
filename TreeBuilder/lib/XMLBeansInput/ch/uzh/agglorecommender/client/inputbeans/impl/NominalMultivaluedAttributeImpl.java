/*
 * XML Type:  NominalMultivaluedAttribute
 * Namespace: ch/uzh/agglorecommender/client/inputbeans
 * Java type: ch.uzh.agglorecommender.client.inputbeans.NominalMultivaluedAttribute
 *
 * Automatically generated - do not modify.
 */
package ch.uzh.agglorecommender.client.inputbeans.impl;
/**
 * An XML NominalMultivaluedAttribute(@ch/uzh/agglorecommender/client/inputbeans).
 *
 * This is a complex type.
 */
public class NominalMultivaluedAttributeImpl extends ch.uzh.agglorecommender.client.inputbeans.impl.NominalAttributeImpl implements ch.uzh.agglorecommender.client.inputbeans.NominalMultivaluedAttribute
{
    private static final long serialVersionUID = 1L;
    
    public NominalMultivaluedAttributeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName VALUESEPARATOR$0 = 
        new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "ValueSeparator");
    
    
    /**
     * Gets the "ValueSeparator" element
     */
    public java.lang.String getValueSeparator()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VALUESEPARATOR$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "ValueSeparator" element
     */
    public org.apache.xmlbeans.XmlString xgetValueSeparator()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(VALUESEPARATOR$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "ValueSeparator" element
     */
    public void setValueSeparator(java.lang.String valueSeparator)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VALUESEPARATOR$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(VALUESEPARATOR$0);
            }
            target.setStringValue(valueSeparator);
        }
    }
    
    /**
     * Sets (as xml) the "ValueSeparator" element
     */
    public void xsetValueSeparator(org.apache.xmlbeans.XmlString valueSeparator)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(VALUESEPARATOR$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(VALUESEPARATOR$0);
            }
            target.set(valueSeparator);
        }
    }
}
