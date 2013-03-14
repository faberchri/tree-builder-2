/*
 * XML Type:  RatingFile
 * Namespace: ch/uzh/agglorecommender/client/inputbeans
 * Java type: ch.uzh.agglorecommender.client.inputbeans.RatingFile
 *
 * Automatically generated - do not modify.
 */
package ch.uzh.agglorecommender.client.inputbeans.impl;
/**
 * An XML RatingFile(@ch/uzh/agglorecommender/client/inputbeans).
 *
 * This is a complex type.
 */
public class RatingFileImpl extends ch.uzh.agglorecommender.client.inputbeans.impl.FileImpl implements ch.uzh.agglorecommender.client.inputbeans.RatingFile
{
    private static final long serialVersionUID = 1L;
    
    public RatingFileImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName USERIDCOLUMNNUMBER$0 = 
        new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "UserIdColumnNumber");
    private static final javax.xml.namespace.QName CONTENTIDCOLUMNNUMBER$2 = 
        new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "ContentIdColumnNumber");
    private static final javax.xml.namespace.QName USEFORTESTONLY$4 = 
        new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "useForTestOnly");
    
    
    /**
     * Gets the "UserIdColumnNumber" element
     */
    public java.math.BigInteger getUserIdColumnNumber()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USERIDCOLUMNNUMBER$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getBigIntegerValue();
        }
    }
    
    /**
     * Gets (as xml) the "UserIdColumnNumber" element
     */
    public org.apache.xmlbeans.XmlPositiveInteger xgetUserIdColumnNumber()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlPositiveInteger target = null;
            target = (org.apache.xmlbeans.XmlPositiveInteger)get_store().find_element_user(USERIDCOLUMNNUMBER$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "UserIdColumnNumber" element
     */
    public void setUserIdColumnNumber(java.math.BigInteger userIdColumnNumber)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USERIDCOLUMNNUMBER$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(USERIDCOLUMNNUMBER$0);
            }
            target.setBigIntegerValue(userIdColumnNumber);
        }
    }
    
    /**
     * Sets (as xml) the "UserIdColumnNumber" element
     */
    public void xsetUserIdColumnNumber(org.apache.xmlbeans.XmlPositiveInteger userIdColumnNumber)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlPositiveInteger target = null;
            target = (org.apache.xmlbeans.XmlPositiveInteger)get_store().find_element_user(USERIDCOLUMNNUMBER$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlPositiveInteger)get_store().add_element_user(USERIDCOLUMNNUMBER$0);
            }
            target.set(userIdColumnNumber);
        }
    }
    
    /**
     * Gets the "ContentIdColumnNumber" element
     */
    public java.math.BigInteger getContentIdColumnNumber()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CONTENTIDCOLUMNNUMBER$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getBigIntegerValue();
        }
    }
    
    /**
     * Gets (as xml) the "ContentIdColumnNumber" element
     */
    public org.apache.xmlbeans.XmlPositiveInteger xgetContentIdColumnNumber()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlPositiveInteger target = null;
            target = (org.apache.xmlbeans.XmlPositiveInteger)get_store().find_element_user(CONTENTIDCOLUMNNUMBER$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "ContentIdColumnNumber" element
     */
    public void setContentIdColumnNumber(java.math.BigInteger contentIdColumnNumber)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CONTENTIDCOLUMNNUMBER$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CONTENTIDCOLUMNNUMBER$2);
            }
            target.setBigIntegerValue(contentIdColumnNumber);
        }
    }
    
    /**
     * Sets (as xml) the "ContentIdColumnNumber" element
     */
    public void xsetContentIdColumnNumber(org.apache.xmlbeans.XmlPositiveInteger contentIdColumnNumber)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlPositiveInteger target = null;
            target = (org.apache.xmlbeans.XmlPositiveInteger)get_store().find_element_user(CONTENTIDCOLUMNNUMBER$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlPositiveInteger)get_store().add_element_user(CONTENTIDCOLUMNNUMBER$2);
            }
            target.set(contentIdColumnNumber);
        }
    }
    
    /**
     * Gets the "useForTestOnly" element
     */
    public boolean getUseForTestOnly()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USEFORTESTONLY$4, 0);
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "useForTestOnly" element
     */
    public org.apache.xmlbeans.XmlBoolean xgetUseForTestOnly()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(USEFORTESTONLY$4, 0);
            return target;
        }
    }
    
    /**
     * Sets the "useForTestOnly" element
     */
    public void setUseForTestOnly(boolean useForTestOnly)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USEFORTESTONLY$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(USEFORTESTONLY$4);
            }
            target.setBooleanValue(useForTestOnly);
        }
    }
    
    /**
     * Sets (as xml) the "useForTestOnly" element
     */
    public void xsetUseForTestOnly(org.apache.xmlbeans.XmlBoolean useForTestOnly)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(USEFORTESTONLY$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_element_user(USEFORTESTONLY$4);
            }
            target.set(useForTestOnly);
        }
    }
}
