/*
 * XML Type:  Attribute
 * Namespace: ch/uzh/agglorecommender/client/inputbeans
 * Java type: ch.uzh.agglorecommender.client.inputbeans.Attribute
 *
 * Automatically generated - do not modify.
 */
package ch.uzh.agglorecommender.client.inputbeans.impl;
/**
 * An XML Attribute(@ch/uzh/agglorecommender/client/inputbeans).
 *
 * This is a complex type.
 */
public class AttributeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.uzh.agglorecommender.client.inputbeans.Attribute
{
    private static final long serialVersionUID = 1L;
    
    public AttributeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName TAG$0 = 
        new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "Tag");
    private static final javax.xml.namespace.QName USEFORCLUSTERING$2 = 
        new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "useForClustering");
    private static final javax.xml.namespace.QName PREPROCESSINGREGEX$4 = 
        new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "PreProcessingRegex");
    
    
    /**
     * Gets the "Tag" element
     */
    public java.lang.String getTag()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TAG$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "Tag" element
     */
    public org.apache.xmlbeans.XmlString xgetTag()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(TAG$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "Tag" element
     */
    public void setTag(java.lang.String tag)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TAG$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(TAG$0);
            }
            target.setStringValue(tag);
        }
    }
    
    /**
     * Sets (as xml) the "Tag" element
     */
    public void xsetTag(org.apache.xmlbeans.XmlString tag)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(TAG$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(TAG$0);
            }
            target.set(tag);
        }
    }
    
    /**
     * Gets the "useForClustering" element
     */
    public boolean getUseForClustering()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USEFORCLUSTERING$2, 0);
            if (target == null)
            {
                return false;
            }
            return target.getBooleanValue();
        }
    }
    
    /**
     * Gets (as xml) the "useForClustering" element
     */
    public org.apache.xmlbeans.XmlBoolean xgetUseForClustering()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(USEFORCLUSTERING$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "useForClustering" element
     */
    public void setUseForClustering(boolean useForClustering)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(USEFORCLUSTERING$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(USEFORCLUSTERING$2);
            }
            target.setBooleanValue(useForClustering);
        }
    }
    
    /**
     * Sets (as xml) the "useForClustering" element
     */
    public void xsetUseForClustering(org.apache.xmlbeans.XmlBoolean useForClustering)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlBoolean target = null;
            target = (org.apache.xmlbeans.XmlBoolean)get_store().find_element_user(USEFORCLUSTERING$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlBoolean)get_store().add_element_user(USEFORCLUSTERING$2);
            }
            target.set(useForClustering);
        }
    }
    
    /**
     * Gets the "PreProcessingRegex" element
     */
    public java.lang.String getPreProcessingRegex()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PREPROCESSINGREGEX$4, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "PreProcessingRegex" element
     */
    public org.apache.xmlbeans.XmlString xgetPreProcessingRegex()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PREPROCESSINGREGEX$4, 0);
            return target;
        }
    }
    
    /**
     * True if has "PreProcessingRegex" element
     */
    public boolean isSetPreProcessingRegex()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(PREPROCESSINGREGEX$4) != 0;
        }
    }
    
    /**
     * Sets the "PreProcessingRegex" element
     */
    public void setPreProcessingRegex(java.lang.String preProcessingRegex)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(PREPROCESSINGREGEX$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(PREPROCESSINGREGEX$4);
            }
            target.setStringValue(preProcessingRegex);
        }
    }
    
    /**
     * Sets (as xml) the "PreProcessingRegex" element
     */
    public void xsetPreProcessingRegex(org.apache.xmlbeans.XmlString preProcessingRegex)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(PREPROCESSINGREGEX$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(PREPROCESSINGREGEX$4);
            }
            target.set(preProcessingRegex);
        }
    }
    
    /**
     * Unsets the "PreProcessingRegex" element
     */
    public void unsetPreProcessingRegex()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(PREPROCESSINGREGEX$4, 0);
        }
    }
}
