/*
 * XML Type:  NumericalAttribute
 * Namespace: ch/uzh/agglorecommender/client/inputbeans
 * Java type: ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute
 *
 * Automatically generated - do not modify.
 */
package ch.uzh.agglorecommender.client.inputbeans.impl;
/**
 * An XML NumericalAttribute(@ch/uzh/agglorecommender/client/inputbeans).
 *
 * This is a complex type.
 */
public class NumericalAttributeImpl extends ch.uzh.agglorecommender.client.inputbeans.impl.AttributeImpl implements ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute
{
    private static final long serialVersionUID = 1L;
    
    public NumericalAttributeImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName MINVALUE$0 = 
        new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "minValue");
    private static final javax.xml.namespace.QName MAXVALUE$2 = 
        new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "maxValue");
    
    
    /**
     * Gets the "minValue" element
     */
    public double getMinValue()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MINVALUE$0, 0);
            if (target == null)
            {
                return 0.0;
            }
            return target.getDoubleValue();
        }
    }
    
    /**
     * Gets (as xml) the "minValue" element
     */
    public org.apache.xmlbeans.XmlDouble xgetMinValue()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(MINVALUE$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "minValue" element
     */
    public void setMinValue(double minValue)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MINVALUE$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(MINVALUE$0);
            }
            target.setDoubleValue(minValue);
        }
    }
    
    /**
     * Sets (as xml) the "minValue" element
     */
    public void xsetMinValue(org.apache.xmlbeans.XmlDouble minValue)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(MINVALUE$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlDouble)get_store().add_element_user(MINVALUE$0);
            }
            target.set(minValue);
        }
    }
    
    /**
     * Gets the "maxValue" element
     */
    public double getMaxValue()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MAXVALUE$2, 0);
            if (target == null)
            {
                return 0.0;
            }
            return target.getDoubleValue();
        }
    }
    
    /**
     * Gets (as xml) the "maxValue" element
     */
    public org.apache.xmlbeans.XmlDouble xgetMaxValue()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(MAXVALUE$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "maxValue" element
     */
    public void setMaxValue(double maxValue)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MAXVALUE$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(MAXVALUE$2);
            }
            target.setDoubleValue(maxValue);
        }
    }
    
    /**
     * Sets (as xml) the "maxValue" element
     */
    public void xsetMaxValue(org.apache.xmlbeans.XmlDouble maxValue)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlDouble target = null;
            target = (org.apache.xmlbeans.XmlDouble)get_store().find_element_user(MAXVALUE$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlDouble)get_store().add_element_user(MAXVALUE$2);
            }
            target.set(maxValue);
        }
    }
}
