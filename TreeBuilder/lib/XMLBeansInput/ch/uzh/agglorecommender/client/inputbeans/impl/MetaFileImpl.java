/*
 * XML Type:  MetaFile
 * Namespace: ch/uzh/agglorecommender/client/inputbeans
 * Java type: ch.uzh.agglorecommender.client.inputbeans.MetaFile
 *
 * Automatically generated - do not modify.
 */
package ch.uzh.agglorecommender.client.inputbeans.impl;
/**
 * An XML MetaFile(@ch/uzh/agglorecommender/client/inputbeans).
 *
 * This is a complex type.
 */
public class MetaFileImpl extends ch.uzh.agglorecommender.client.inputbeans.impl.FileImpl implements ch.uzh.agglorecommender.client.inputbeans.MetaFile
{
    private static final long serialVersionUID = 1L;
    
    public MetaFileImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName IDCOLUMNNUMBER$0 = 
        new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "IdColumnNumber");
    
    
    /**
     * Gets the "IdColumnNumber" element
     */
    public java.math.BigInteger getIdColumnNumber()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(IDCOLUMNNUMBER$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getBigIntegerValue();
        }
    }
    
    /**
     * Gets (as xml) the "IdColumnNumber" element
     */
    public org.apache.xmlbeans.XmlPositiveInteger xgetIdColumnNumber()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlPositiveInteger target = null;
            target = (org.apache.xmlbeans.XmlPositiveInteger)get_store().find_element_user(IDCOLUMNNUMBER$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "IdColumnNumber" element
     */
    public void setIdColumnNumber(java.math.BigInteger idColumnNumber)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(IDCOLUMNNUMBER$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(IDCOLUMNNUMBER$0);
            }
            target.setBigIntegerValue(idColumnNumber);
        }
    }
    
    /**
     * Sets (as xml) the "IdColumnNumber" element
     */
    public void xsetIdColumnNumber(org.apache.xmlbeans.XmlPositiveInteger idColumnNumber)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlPositiveInteger target = null;
            target = (org.apache.xmlbeans.XmlPositiveInteger)get_store().find_element_user(IDCOLUMNNUMBER$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlPositiveInteger)get_store().add_element_user(IDCOLUMNNUMBER$0);
            }
            target.set(idColumnNumber);
        }
    }
}
