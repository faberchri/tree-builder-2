/*
 * XML Type:  File
 * Namespace: ch/uzh/agglorecommender/client/inputbeans
 * Java type: ch.uzh.agglorecommender.client.inputbeans.File
 *
 * Automatically generated - do not modify.
 */
package ch.uzh.agglorecommender.client.inputbeans.impl;
/**
 * An XML File(@ch/uzh/agglorecommender/client/inputbeans).
 *
 * This is a complex type.
 */
public class FileImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.uzh.agglorecommender.client.inputbeans.File
{
    private static final long serialVersionUID = 1L;
    
    public FileImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName COLUMNSEPARATOR$0 = 
        new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "ColumnSeparator");
    private static final javax.xml.namespace.QName LOCATION$2 = 
        new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "Location");
    private static final javax.xml.namespace.QName VALUECOLUMNNUMBER$4 = 
        new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "ValueColumnNumber");
    private static final javax.xml.namespace.QName STARTLINE$6 = 
        new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "StartLine");
    private static final javax.xml.namespace.QName ENDLINE$8 = 
        new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "EndLine");
    
    
    /**
     * Gets the "ColumnSeparator" element
     */
    public java.lang.String getColumnSeparator()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(COLUMNSEPARATOR$0, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "ColumnSeparator" element
     */
    public ch.uzh.agglorecommender.client.inputbeans.File.ColumnSeparator xgetColumnSeparator()
    {
        synchronized (monitor())
        {
            check_orphaned();
            ch.uzh.agglorecommender.client.inputbeans.File.ColumnSeparator target = null;
            target = (ch.uzh.agglorecommender.client.inputbeans.File.ColumnSeparator)get_store().find_element_user(COLUMNSEPARATOR$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "ColumnSeparator" element
     */
    public void setColumnSeparator(java.lang.String columnSeparator)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(COLUMNSEPARATOR$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(COLUMNSEPARATOR$0);
            }
            target.setStringValue(columnSeparator);
        }
    }
    
    /**
     * Sets (as xml) the "ColumnSeparator" element
     */
    public void xsetColumnSeparator(ch.uzh.agglorecommender.client.inputbeans.File.ColumnSeparator columnSeparator)
    {
        synchronized (monitor())
        {
            check_orphaned();
            ch.uzh.agglorecommender.client.inputbeans.File.ColumnSeparator target = null;
            target = (ch.uzh.agglorecommender.client.inputbeans.File.ColumnSeparator)get_store().find_element_user(COLUMNSEPARATOR$0, 0);
            if (target == null)
            {
                target = (ch.uzh.agglorecommender.client.inputbeans.File.ColumnSeparator)get_store().add_element_user(COLUMNSEPARATOR$0);
            }
            target.set(columnSeparator);
        }
    }
    
    /**
     * Gets the "Location" element
     */
    public java.lang.String getLocation()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(LOCATION$2, 0);
            if (target == null)
            {
                return null;
            }
            return target.getStringValue();
        }
    }
    
    /**
     * Gets (as xml) the "Location" element
     */
    public org.apache.xmlbeans.XmlString xgetLocation()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(LOCATION$2, 0);
            return target;
        }
    }
    
    /**
     * Sets the "Location" element
     */
    public void setLocation(java.lang.String location)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(LOCATION$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(LOCATION$2);
            }
            target.setStringValue(location);
        }
    }
    
    /**
     * Sets (as xml) the "Location" element
     */
    public void xsetLocation(org.apache.xmlbeans.XmlString location)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlString target = null;
            target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(LOCATION$2, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(LOCATION$2);
            }
            target.set(location);
        }
    }
    
    /**
     * Gets the "ValueColumnNumber" element
     */
    public java.math.BigInteger getValueColumnNumber()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VALUECOLUMNNUMBER$4, 0);
            if (target == null)
            {
                return null;
            }
            return target.getBigIntegerValue();
        }
    }
    
    /**
     * Gets (as xml) the "ValueColumnNumber" element
     */
    public org.apache.xmlbeans.XmlPositiveInteger xgetValueColumnNumber()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlPositiveInteger target = null;
            target = (org.apache.xmlbeans.XmlPositiveInteger)get_store().find_element_user(VALUECOLUMNNUMBER$4, 0);
            return target;
        }
    }
    
    /**
     * Sets the "ValueColumnNumber" element
     */
    public void setValueColumnNumber(java.math.BigInteger valueColumnNumber)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(VALUECOLUMNNUMBER$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(VALUECOLUMNNUMBER$4);
            }
            target.setBigIntegerValue(valueColumnNumber);
        }
    }
    
    /**
     * Sets (as xml) the "ValueColumnNumber" element
     */
    public void xsetValueColumnNumber(org.apache.xmlbeans.XmlPositiveInteger valueColumnNumber)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlPositiveInteger target = null;
            target = (org.apache.xmlbeans.XmlPositiveInteger)get_store().find_element_user(VALUECOLUMNNUMBER$4, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlPositiveInteger)get_store().add_element_user(VALUECOLUMNNUMBER$4);
            }
            target.set(valueColumnNumber);
        }
    }
    
    /**
     * Gets the "StartLine" element
     */
    public java.math.BigInteger getStartLine()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(STARTLINE$6, 0);
            if (target == null)
            {
                return null;
            }
            return target.getBigIntegerValue();
        }
    }
    
    /**
     * Gets (as xml) the "StartLine" element
     */
    public org.apache.xmlbeans.XmlPositiveInteger xgetStartLine()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlPositiveInteger target = null;
            target = (org.apache.xmlbeans.XmlPositiveInteger)get_store().find_element_user(STARTLINE$6, 0);
            return target;
        }
    }
    
    /**
     * True if has "StartLine" element
     */
    public boolean isSetStartLine()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(STARTLINE$6) != 0;
        }
    }
    
    /**
     * Sets the "StartLine" element
     */
    public void setStartLine(java.math.BigInteger startLine)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(STARTLINE$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(STARTLINE$6);
            }
            target.setBigIntegerValue(startLine);
        }
    }
    
    /**
     * Sets (as xml) the "StartLine" element
     */
    public void xsetStartLine(org.apache.xmlbeans.XmlPositiveInteger startLine)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlPositiveInteger target = null;
            target = (org.apache.xmlbeans.XmlPositiveInteger)get_store().find_element_user(STARTLINE$6, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlPositiveInteger)get_store().add_element_user(STARTLINE$6);
            }
            target.set(startLine);
        }
    }
    
    /**
     * Unsets the "StartLine" element
     */
    public void unsetStartLine()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(STARTLINE$6, 0);
        }
    }
    
    /**
     * Gets the "EndLine" element
     */
    public java.math.BigInteger getEndLine()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ENDLINE$8, 0);
            if (target == null)
            {
                return null;
            }
            return target.getBigIntegerValue();
        }
    }
    
    /**
     * Gets (as xml) the "EndLine" element
     */
    public org.apache.xmlbeans.XmlPositiveInteger xgetEndLine()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlPositiveInteger target = null;
            target = (org.apache.xmlbeans.XmlPositiveInteger)get_store().find_element_user(ENDLINE$8, 0);
            return target;
        }
    }
    
    /**
     * True if has "EndLine" element
     */
    public boolean isSetEndLine()
    {
        synchronized (monitor())
        {
            check_orphaned();
            return get_store().count_elements(ENDLINE$8) != 0;
        }
    }
    
    /**
     * Sets the "EndLine" element
     */
    public void setEndLine(java.math.BigInteger endLine)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(ENDLINE$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(ENDLINE$8);
            }
            target.setBigIntegerValue(endLine);
        }
    }
    
    /**
     * Sets (as xml) the "EndLine" element
     */
    public void xsetEndLine(org.apache.xmlbeans.XmlPositiveInteger endLine)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlPositiveInteger target = null;
            target = (org.apache.xmlbeans.XmlPositiveInteger)get_store().find_element_user(ENDLINE$8, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlPositiveInteger)get_store().add_element_user(ENDLINE$8);
            }
            target.set(endLine);
        }
    }
    
    /**
     * Unsets the "EndLine" element
     */
    public void unsetEndLine()
    {
        synchronized (monitor())
        {
            check_orphaned();
            get_store().remove_element(ENDLINE$8, 0);
        }
    }
    /**
     * An XML ColumnSeparator(@ch/uzh/agglorecommender/client/inputbeans).
     *
     * This is an atomic type that is a restriction of ch.uzh.agglorecommender.client.inputbeans.File$ColumnSeparator.
     */
    public static class ColumnSeparatorImpl extends org.apache.xmlbeans.impl.values.JavaStringHolderEx implements ch.uzh.agglorecommender.client.inputbeans.File.ColumnSeparator
    {
        private static final long serialVersionUID = 1L;
        
        public ColumnSeparatorImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType, false);
        }
        
        protected ColumnSeparatorImpl(org.apache.xmlbeans.SchemaType sType, boolean b)
        {
            super(sType, b);
        }
    }
}
