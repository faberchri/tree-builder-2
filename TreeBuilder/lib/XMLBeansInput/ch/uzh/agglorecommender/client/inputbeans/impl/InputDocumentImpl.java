/*
 * An XML document type.
 * Localname: Input
 * Namespace: ch/uzh/agglorecommender/client/inputbeans
 * Java type: ch.uzh.agglorecommender.client.inputbeans.InputDocument
 *
 * Automatically generated - do not modify.
 */
package ch.uzh.agglorecommender.client.inputbeans.impl;
/**
 * A document containing one Input(@ch/uzh/agglorecommender/client/inputbeans) element.
 *
 * This is a complex type.
 */
public class InputDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.uzh.agglorecommender.client.inputbeans.InputDocument
{
    private static final long serialVersionUID = 1L;
    
    public InputDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName INPUT$0 = 
        new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "Input");
    
    
    /**
     * Gets the "Input" element
     */
    public ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input getInput()
    {
        synchronized (monitor())
        {
            check_orphaned();
            ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input target = null;
            target = (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input)get_store().find_element_user(INPUT$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "Input" element
     */
    public void setInput(ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input input)
    {
        generatedSetterHelperImpl(input, INPUT$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "Input" element
     */
    public ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input addNewInput()
    {
        synchronized (monitor())
        {
            check_orphaned();
            ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input target = null;
            target = (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input)get_store().add_element_user(INPUT$0);
            return target;
        }
    }
    /**
     * An XML Input(@ch/uzh/agglorecommender/client/inputbeans).
     *
     * This is a complex type.
     */
    public static class InputImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input
    {
        private static final long serialVersionUID = 1L;
        
        public InputImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName RATING$0 = 
            new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "Rating");
        private static final javax.xml.namespace.QName USERNUMERICALATTRIBUTE$2 = 
            new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "userNumericalAttribute");
        private static final javax.xml.namespace.QName USERNOMINALATTRIBUTE$4 = 
            new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "userNominalAttribute");
        private static final javax.xml.namespace.QName USERNOMINALMULTIVALUEDATTRIBUTE$6 = 
            new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "userNominalMultivaluedAttribute");
        private static final javax.xml.namespace.QName CONTENTNUMERICALATTRIBUTE$8 = 
            new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "contentNumericalAttribute");
        private static final javax.xml.namespace.QName CONTENTNOMINALATTRIBUTE$10 = 
            new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "contentNominalAttribute");
        private static final javax.xml.namespace.QName CONTENTNOMINALMULTIVALUEDATTRIBUTE$12 = 
            new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "contentNominalMultivaluedAttribute");
        
        
        /**
         * Gets array of all "Rating" elements
         */
        public ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.Rating[] getRatingArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(RATING$0, targetList);
                ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.Rating[] result = new ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.Rating[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "Rating" element
         */
        public ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.Rating getRatingArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.Rating target = null;
                target = (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.Rating)get_store().find_element_user(RATING$0, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "Rating" element
         */
        public int sizeOfRatingArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(RATING$0);
            }
        }
        
        /**
         * Sets array of all "Rating" element  WARNING: This method is not atomicaly synchronized.
         */
        public void setRatingArray(ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.Rating[] ratingArray)
        {
            check_orphaned();
            arraySetterHelper(ratingArray, RATING$0);
        }
        
        /**
         * Sets ith "Rating" element
         */
        public void setRatingArray(int i, ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.Rating rating)
        {
            generatedSetterHelperImpl(rating, RATING$0, i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "Rating" element
         */
        public ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.Rating insertNewRating(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.Rating target = null;
                target = (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.Rating)get_store().insert_element_user(RATING$0, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "Rating" element
         */
        public ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.Rating addNewRating()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.Rating target = null;
                target = (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.Rating)get_store().add_element_user(RATING$0);
                return target;
            }
        }
        
        /**
         * Removes the ith "Rating" element
         */
        public void removeRating(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(RATING$0, i);
            }
        }
        
        /**
         * Gets array of all "userNumericalAttribute" elements
         */
        public ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNumericalAttribute[] getUserNumericalAttributeArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(USERNUMERICALATTRIBUTE$2, targetList);
                ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNumericalAttribute[] result = new ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNumericalAttribute[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "userNumericalAttribute" element
         */
        public ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNumericalAttribute getUserNumericalAttributeArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNumericalAttribute target = null;
                target = (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNumericalAttribute)get_store().find_element_user(USERNUMERICALATTRIBUTE$2, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "userNumericalAttribute" element
         */
        public int sizeOfUserNumericalAttributeArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(USERNUMERICALATTRIBUTE$2);
            }
        }
        
        /**
         * Sets array of all "userNumericalAttribute" element  WARNING: This method is not atomicaly synchronized.
         */
        public void setUserNumericalAttributeArray(ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNumericalAttribute[] userNumericalAttributeArray)
        {
            check_orphaned();
            arraySetterHelper(userNumericalAttributeArray, USERNUMERICALATTRIBUTE$2);
        }
        
        /**
         * Sets ith "userNumericalAttribute" element
         */
        public void setUserNumericalAttributeArray(int i, ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNumericalAttribute userNumericalAttribute)
        {
            generatedSetterHelperImpl(userNumericalAttribute, USERNUMERICALATTRIBUTE$2, i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "userNumericalAttribute" element
         */
        public ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNumericalAttribute insertNewUserNumericalAttribute(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNumericalAttribute target = null;
                target = (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNumericalAttribute)get_store().insert_element_user(USERNUMERICALATTRIBUTE$2, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "userNumericalAttribute" element
         */
        public ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNumericalAttribute addNewUserNumericalAttribute()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNumericalAttribute target = null;
                target = (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNumericalAttribute)get_store().add_element_user(USERNUMERICALATTRIBUTE$2);
                return target;
            }
        }
        
        /**
         * Removes the ith "userNumericalAttribute" element
         */
        public void removeUserNumericalAttribute(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(USERNUMERICALATTRIBUTE$2, i);
            }
        }
        
        /**
         * Gets array of all "userNominalAttribute" elements
         */
        public ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalAttribute[] getUserNominalAttributeArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(USERNOMINALATTRIBUTE$4, targetList);
                ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalAttribute[] result = new ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalAttribute[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "userNominalAttribute" element
         */
        public ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalAttribute getUserNominalAttributeArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalAttribute target = null;
                target = (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalAttribute)get_store().find_element_user(USERNOMINALATTRIBUTE$4, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "userNominalAttribute" element
         */
        public int sizeOfUserNominalAttributeArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(USERNOMINALATTRIBUTE$4);
            }
        }
        
        /**
         * Sets array of all "userNominalAttribute" element  WARNING: This method is not atomicaly synchronized.
         */
        public void setUserNominalAttributeArray(ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalAttribute[] userNominalAttributeArray)
        {
            check_orphaned();
            arraySetterHelper(userNominalAttributeArray, USERNOMINALATTRIBUTE$4);
        }
        
        /**
         * Sets ith "userNominalAttribute" element
         */
        public void setUserNominalAttributeArray(int i, ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalAttribute userNominalAttribute)
        {
            generatedSetterHelperImpl(userNominalAttribute, USERNOMINALATTRIBUTE$4, i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "userNominalAttribute" element
         */
        public ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalAttribute insertNewUserNominalAttribute(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalAttribute target = null;
                target = (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalAttribute)get_store().insert_element_user(USERNOMINALATTRIBUTE$4, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "userNominalAttribute" element
         */
        public ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalAttribute addNewUserNominalAttribute()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalAttribute target = null;
                target = (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalAttribute)get_store().add_element_user(USERNOMINALATTRIBUTE$4);
                return target;
            }
        }
        
        /**
         * Removes the ith "userNominalAttribute" element
         */
        public void removeUserNominalAttribute(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(USERNOMINALATTRIBUTE$4, i);
            }
        }
        
        /**
         * Gets array of all "userNominalMultivaluedAttribute" elements
         */
        public ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalMultivaluedAttribute[] getUserNominalMultivaluedAttributeArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(USERNOMINALMULTIVALUEDATTRIBUTE$6, targetList);
                ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalMultivaluedAttribute[] result = new ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalMultivaluedAttribute[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "userNominalMultivaluedAttribute" element
         */
        public ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalMultivaluedAttribute getUserNominalMultivaluedAttributeArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalMultivaluedAttribute target = null;
                target = (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalMultivaluedAttribute)get_store().find_element_user(USERNOMINALMULTIVALUEDATTRIBUTE$6, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "userNominalMultivaluedAttribute" element
         */
        public int sizeOfUserNominalMultivaluedAttributeArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(USERNOMINALMULTIVALUEDATTRIBUTE$6);
            }
        }
        
        /**
         * Sets array of all "userNominalMultivaluedAttribute" element  WARNING: This method is not atomicaly synchronized.
         */
        public void setUserNominalMultivaluedAttributeArray(ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalMultivaluedAttribute[] userNominalMultivaluedAttributeArray)
        {
            check_orphaned();
            arraySetterHelper(userNominalMultivaluedAttributeArray, USERNOMINALMULTIVALUEDATTRIBUTE$6);
        }
        
        /**
         * Sets ith "userNominalMultivaluedAttribute" element
         */
        public void setUserNominalMultivaluedAttributeArray(int i, ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalMultivaluedAttribute userNominalMultivaluedAttribute)
        {
            generatedSetterHelperImpl(userNominalMultivaluedAttribute, USERNOMINALMULTIVALUEDATTRIBUTE$6, i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "userNominalMultivaluedAttribute" element
         */
        public ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalMultivaluedAttribute insertNewUserNominalMultivaluedAttribute(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalMultivaluedAttribute target = null;
                target = (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalMultivaluedAttribute)get_store().insert_element_user(USERNOMINALMULTIVALUEDATTRIBUTE$6, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "userNominalMultivaluedAttribute" element
         */
        public ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalMultivaluedAttribute addNewUserNominalMultivaluedAttribute()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalMultivaluedAttribute target = null;
                target = (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalMultivaluedAttribute)get_store().add_element_user(USERNOMINALMULTIVALUEDATTRIBUTE$6);
                return target;
            }
        }
        
        /**
         * Removes the ith "userNominalMultivaluedAttribute" element
         */
        public void removeUserNominalMultivaluedAttribute(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(USERNOMINALMULTIVALUEDATTRIBUTE$6, i);
            }
        }
        
        /**
         * Gets array of all "contentNumericalAttribute" elements
         */
        public ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNumericalAttribute[] getContentNumericalAttributeArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(CONTENTNUMERICALATTRIBUTE$8, targetList);
                ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNumericalAttribute[] result = new ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNumericalAttribute[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "contentNumericalAttribute" element
         */
        public ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNumericalAttribute getContentNumericalAttributeArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNumericalAttribute target = null;
                target = (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNumericalAttribute)get_store().find_element_user(CONTENTNUMERICALATTRIBUTE$8, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "contentNumericalAttribute" element
         */
        public int sizeOfContentNumericalAttributeArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(CONTENTNUMERICALATTRIBUTE$8);
            }
        }
        
        /**
         * Sets array of all "contentNumericalAttribute" element  WARNING: This method is not atomicaly synchronized.
         */
        public void setContentNumericalAttributeArray(ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNumericalAttribute[] contentNumericalAttributeArray)
        {
            check_orphaned();
            arraySetterHelper(contentNumericalAttributeArray, CONTENTNUMERICALATTRIBUTE$8);
        }
        
        /**
         * Sets ith "contentNumericalAttribute" element
         */
        public void setContentNumericalAttributeArray(int i, ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNumericalAttribute contentNumericalAttribute)
        {
            generatedSetterHelperImpl(contentNumericalAttribute, CONTENTNUMERICALATTRIBUTE$8, i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "contentNumericalAttribute" element
         */
        public ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNumericalAttribute insertNewContentNumericalAttribute(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNumericalAttribute target = null;
                target = (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNumericalAttribute)get_store().insert_element_user(CONTENTNUMERICALATTRIBUTE$8, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "contentNumericalAttribute" element
         */
        public ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNumericalAttribute addNewContentNumericalAttribute()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNumericalAttribute target = null;
                target = (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNumericalAttribute)get_store().add_element_user(CONTENTNUMERICALATTRIBUTE$8);
                return target;
            }
        }
        
        /**
         * Removes the ith "contentNumericalAttribute" element
         */
        public void removeContentNumericalAttribute(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(CONTENTNUMERICALATTRIBUTE$8, i);
            }
        }
        
        /**
         * Gets array of all "contentNominalAttribute" elements
         */
        public ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalAttribute[] getContentNominalAttributeArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(CONTENTNOMINALATTRIBUTE$10, targetList);
                ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalAttribute[] result = new ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalAttribute[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "contentNominalAttribute" element
         */
        public ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalAttribute getContentNominalAttributeArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalAttribute target = null;
                target = (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalAttribute)get_store().find_element_user(CONTENTNOMINALATTRIBUTE$10, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "contentNominalAttribute" element
         */
        public int sizeOfContentNominalAttributeArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(CONTENTNOMINALATTRIBUTE$10);
            }
        }
        
        /**
         * Sets array of all "contentNominalAttribute" element  WARNING: This method is not atomicaly synchronized.
         */
        public void setContentNominalAttributeArray(ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalAttribute[] contentNominalAttributeArray)
        {
            check_orphaned();
            arraySetterHelper(contentNominalAttributeArray, CONTENTNOMINALATTRIBUTE$10);
        }
        
        /**
         * Sets ith "contentNominalAttribute" element
         */
        public void setContentNominalAttributeArray(int i, ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalAttribute contentNominalAttribute)
        {
            generatedSetterHelperImpl(contentNominalAttribute, CONTENTNOMINALATTRIBUTE$10, i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "contentNominalAttribute" element
         */
        public ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalAttribute insertNewContentNominalAttribute(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalAttribute target = null;
                target = (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalAttribute)get_store().insert_element_user(CONTENTNOMINALATTRIBUTE$10, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "contentNominalAttribute" element
         */
        public ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalAttribute addNewContentNominalAttribute()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalAttribute target = null;
                target = (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalAttribute)get_store().add_element_user(CONTENTNOMINALATTRIBUTE$10);
                return target;
            }
        }
        
        /**
         * Removes the ith "contentNominalAttribute" element
         */
        public void removeContentNominalAttribute(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(CONTENTNOMINALATTRIBUTE$10, i);
            }
        }
        
        /**
         * Gets array of all "contentNominalMultivaluedAttribute" elements
         */
        public ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalMultivaluedAttribute[] getContentNominalMultivaluedAttributeArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                java.util.List targetList = new java.util.ArrayList();
                get_store().find_all_element_users(CONTENTNOMINALMULTIVALUEDATTRIBUTE$12, targetList);
                ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalMultivaluedAttribute[] result = new ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalMultivaluedAttribute[targetList.size()];
                targetList.toArray(result);
                return result;
            }
        }
        
        /**
         * Gets ith "contentNominalMultivaluedAttribute" element
         */
        public ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalMultivaluedAttribute getContentNominalMultivaluedAttributeArray(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalMultivaluedAttribute target = null;
                target = (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalMultivaluedAttribute)get_store().find_element_user(CONTENTNOMINALMULTIVALUEDATTRIBUTE$12, i);
                if (target == null)
                {
                    throw new IndexOutOfBoundsException();
                }
                return target;
            }
        }
        
        /**
         * Returns number of "contentNominalMultivaluedAttribute" element
         */
        public int sizeOfContentNominalMultivaluedAttributeArray()
        {
            synchronized (monitor())
            {
                check_orphaned();
                return get_store().count_elements(CONTENTNOMINALMULTIVALUEDATTRIBUTE$12);
            }
        }
        
        /**
         * Sets array of all "contentNominalMultivaluedAttribute" element  WARNING: This method is not atomicaly synchronized.
         */
        public void setContentNominalMultivaluedAttributeArray(ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalMultivaluedAttribute[] contentNominalMultivaluedAttributeArray)
        {
            check_orphaned();
            arraySetterHelper(contentNominalMultivaluedAttributeArray, CONTENTNOMINALMULTIVALUEDATTRIBUTE$12);
        }
        
        /**
         * Sets ith "contentNominalMultivaluedAttribute" element
         */
        public void setContentNominalMultivaluedAttributeArray(int i, ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalMultivaluedAttribute contentNominalMultivaluedAttribute)
        {
            generatedSetterHelperImpl(contentNominalMultivaluedAttribute, CONTENTNOMINALMULTIVALUEDATTRIBUTE$12, i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
        }
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "contentNominalMultivaluedAttribute" element
         */
        public ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalMultivaluedAttribute insertNewContentNominalMultivaluedAttribute(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalMultivaluedAttribute target = null;
                target = (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalMultivaluedAttribute)get_store().insert_element_user(CONTENTNOMINALMULTIVALUEDATTRIBUTE$12, i);
                return target;
            }
        }
        
        /**
         * Appends and returns a new empty value (as xml) as the last "contentNominalMultivaluedAttribute" element
         */
        public ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalMultivaluedAttribute addNewContentNominalMultivaluedAttribute()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalMultivaluedAttribute target = null;
                target = (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalMultivaluedAttribute)get_store().add_element_user(CONTENTNOMINALMULTIVALUEDATTRIBUTE$12);
                return target;
            }
        }
        
        /**
         * Removes the ith "contentNominalMultivaluedAttribute" element
         */
        public void removeContentNominalMultivaluedAttribute(int i)
        {
            synchronized (monitor())
            {
                check_orphaned();
                get_store().remove_element(CONTENTNOMINALMULTIVALUEDATTRIBUTE$12, i);
            }
        }
        /**
         * An XML Rating(@ch/uzh/agglorecommender/client/inputbeans).
         *
         * This is a complex type.
         */
        public static class RatingImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.Rating
        {
            private static final long serialVersionUID = 1L;
            
            public RatingImpl(org.apache.xmlbeans.SchemaType sType)
            {
                super(sType);
            }
            
            private static final javax.xml.namespace.QName ATTRIBUTE$0 = 
                new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "Attribute");
            private static final javax.xml.namespace.QName FILE$2 = 
                new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "File");
            
            
            /**
             * Gets the "Attribute" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute getAttribute()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute)get_store().find_element_user(ATTRIBUTE$0, 0);
                    if (target == null)
                    {
                      return null;
                    }
                    return target;
                }
            }
            
            /**
             * Sets the "Attribute" element
             */
            public void setAttribute(ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute attribute)
            {
                generatedSetterHelperImpl(attribute, ATTRIBUTE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
            }
            
            /**
             * Appends and returns a new empty "Attribute" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute addNewAttribute()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute)get_store().add_element_user(ATTRIBUTE$0);
                    return target;
                }
            }
            
            /**
             * Gets array of all "File" elements
             */
            public ch.uzh.agglorecommender.client.inputbeans.RatingFile[] getFileArray()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    java.util.List targetList = new java.util.ArrayList();
                    get_store().find_all_element_users(FILE$2, targetList);
                    ch.uzh.agglorecommender.client.inputbeans.RatingFile[] result = new ch.uzh.agglorecommender.client.inputbeans.RatingFile[targetList.size()];
                    targetList.toArray(result);
                    return result;
                }
            }
            
            /**
             * Gets ith "File" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.RatingFile getFileArray(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.RatingFile target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.RatingFile)get_store().find_element_user(FILE$2, i);
                    if (target == null)
                    {
                      throw new IndexOutOfBoundsException();
                    }
                    return target;
                }
            }
            
            /**
             * Returns number of "File" element
             */
            public int sizeOfFileArray()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    return get_store().count_elements(FILE$2);
                }
            }
            
            /**
             * Sets array of all "File" element  WARNING: This method is not atomicaly synchronized.
             */
            public void setFileArray(ch.uzh.agglorecommender.client.inputbeans.RatingFile[] fileArray)
            {
                check_orphaned();
                arraySetterHelper(fileArray, FILE$2);
            }
            
            /**
             * Sets ith "File" element
             */
            public void setFileArray(int i, ch.uzh.agglorecommender.client.inputbeans.RatingFile file)
            {
                generatedSetterHelperImpl(file, FILE$2, i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
            }
            
            /**
             * Inserts and returns a new empty value (as xml) as the ith "File" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.RatingFile insertNewFile(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.RatingFile target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.RatingFile)get_store().insert_element_user(FILE$2, i);
                    return target;
                }
            }
            
            /**
             * Appends and returns a new empty value (as xml) as the last "File" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.RatingFile addNewFile()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.RatingFile target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.RatingFile)get_store().add_element_user(FILE$2);
                    return target;
                }
            }
            
            /**
             * Removes the ith "File" element
             */
            public void removeFile(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    get_store().remove_element(FILE$2, i);
                }
            }
        }
        /**
         * An XML userNumericalAttribute(@ch/uzh/agglorecommender/client/inputbeans).
         *
         * This is a complex type.
         */
        public static class UserNumericalAttributeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNumericalAttribute
        {
            private static final long serialVersionUID = 1L;
            
            public UserNumericalAttributeImpl(org.apache.xmlbeans.SchemaType sType)
            {
                super(sType);
            }
            
            private static final javax.xml.namespace.QName ATTRIBUTE$0 = 
                new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "Attribute");
            private static final javax.xml.namespace.QName FILE$2 = 
                new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "File");
            
            
            /**
             * Gets the "Attribute" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute getAttribute()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute)get_store().find_element_user(ATTRIBUTE$0, 0);
                    if (target == null)
                    {
                      return null;
                    }
                    return target;
                }
            }
            
            /**
             * Sets the "Attribute" element
             */
            public void setAttribute(ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute attribute)
            {
                generatedSetterHelperImpl(attribute, ATTRIBUTE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
            }
            
            /**
             * Appends and returns a new empty "Attribute" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute addNewAttribute()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute)get_store().add_element_user(ATTRIBUTE$0);
                    return target;
                }
            }
            
            /**
             * Gets array of all "File" elements
             */
            public ch.uzh.agglorecommender.client.inputbeans.MetaFile[] getFileArray()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    java.util.List targetList = new java.util.ArrayList();
                    get_store().find_all_element_users(FILE$2, targetList);
                    ch.uzh.agglorecommender.client.inputbeans.MetaFile[] result = new ch.uzh.agglorecommender.client.inputbeans.MetaFile[targetList.size()];
                    targetList.toArray(result);
                    return result;
                }
            }
            
            /**
             * Gets ith "File" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.MetaFile getFileArray(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.MetaFile target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.MetaFile)get_store().find_element_user(FILE$2, i);
                    if (target == null)
                    {
                      throw new IndexOutOfBoundsException();
                    }
                    return target;
                }
            }
            
            /**
             * Returns number of "File" element
             */
            public int sizeOfFileArray()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    return get_store().count_elements(FILE$2);
                }
            }
            
            /**
             * Sets array of all "File" element  WARNING: This method is not atomicaly synchronized.
             */
            public void setFileArray(ch.uzh.agglorecommender.client.inputbeans.MetaFile[] fileArray)
            {
                check_orphaned();
                arraySetterHelper(fileArray, FILE$2);
            }
            
            /**
             * Sets ith "File" element
             */
            public void setFileArray(int i, ch.uzh.agglorecommender.client.inputbeans.MetaFile file)
            {
                generatedSetterHelperImpl(file, FILE$2, i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
            }
            
            /**
             * Inserts and returns a new empty value (as xml) as the ith "File" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.MetaFile insertNewFile(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.MetaFile target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.MetaFile)get_store().insert_element_user(FILE$2, i);
                    return target;
                }
            }
            
            /**
             * Appends and returns a new empty value (as xml) as the last "File" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.MetaFile addNewFile()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.MetaFile target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.MetaFile)get_store().add_element_user(FILE$2);
                    return target;
                }
            }
            
            /**
             * Removes the ith "File" element
             */
            public void removeFile(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    get_store().remove_element(FILE$2, i);
                }
            }
        }
        /**
         * An XML userNominalAttribute(@ch/uzh/agglorecommender/client/inputbeans).
         *
         * This is a complex type.
         */
        public static class UserNominalAttributeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalAttribute
        {
            private static final long serialVersionUID = 1L;
            
            public UserNominalAttributeImpl(org.apache.xmlbeans.SchemaType sType)
            {
                super(sType);
            }
            
            private static final javax.xml.namespace.QName ATTRIBUTE$0 = 
                new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "Attribute");
            private static final javax.xml.namespace.QName FILE$2 = 
                new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "File");
            
            
            /**
             * Gets the "Attribute" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.NominalAttribute getAttribute()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.NominalAttribute target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.NominalAttribute)get_store().find_element_user(ATTRIBUTE$0, 0);
                    if (target == null)
                    {
                      return null;
                    }
                    return target;
                }
            }
            
            /**
             * Sets the "Attribute" element
             */
            public void setAttribute(ch.uzh.agglorecommender.client.inputbeans.NominalAttribute attribute)
            {
                generatedSetterHelperImpl(attribute, ATTRIBUTE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
            }
            
            /**
             * Appends and returns a new empty "Attribute" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.NominalAttribute addNewAttribute()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.NominalAttribute target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.NominalAttribute)get_store().add_element_user(ATTRIBUTE$0);
                    return target;
                }
            }
            
            /**
             * Gets array of all "File" elements
             */
            public ch.uzh.agglorecommender.client.inputbeans.MetaFile[] getFileArray()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    java.util.List targetList = new java.util.ArrayList();
                    get_store().find_all_element_users(FILE$2, targetList);
                    ch.uzh.agglorecommender.client.inputbeans.MetaFile[] result = new ch.uzh.agglorecommender.client.inputbeans.MetaFile[targetList.size()];
                    targetList.toArray(result);
                    return result;
                }
            }
            
            /**
             * Gets ith "File" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.MetaFile getFileArray(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.MetaFile target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.MetaFile)get_store().find_element_user(FILE$2, i);
                    if (target == null)
                    {
                      throw new IndexOutOfBoundsException();
                    }
                    return target;
                }
            }
            
            /**
             * Returns number of "File" element
             */
            public int sizeOfFileArray()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    return get_store().count_elements(FILE$2);
                }
            }
            
            /**
             * Sets array of all "File" element  WARNING: This method is not atomicaly synchronized.
             */
            public void setFileArray(ch.uzh.agglorecommender.client.inputbeans.MetaFile[] fileArray)
            {
                check_orphaned();
                arraySetterHelper(fileArray, FILE$2);
            }
            
            /**
             * Sets ith "File" element
             */
            public void setFileArray(int i, ch.uzh.agglorecommender.client.inputbeans.MetaFile file)
            {
                generatedSetterHelperImpl(file, FILE$2, i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
            }
            
            /**
             * Inserts and returns a new empty value (as xml) as the ith "File" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.MetaFile insertNewFile(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.MetaFile target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.MetaFile)get_store().insert_element_user(FILE$2, i);
                    return target;
                }
            }
            
            /**
             * Appends and returns a new empty value (as xml) as the last "File" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.MetaFile addNewFile()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.MetaFile target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.MetaFile)get_store().add_element_user(FILE$2);
                    return target;
                }
            }
            
            /**
             * Removes the ith "File" element
             */
            public void removeFile(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    get_store().remove_element(FILE$2, i);
                }
            }
        }
        /**
         * An XML userNominalMultivaluedAttribute(@ch/uzh/agglorecommender/client/inputbeans).
         *
         * This is a complex type.
         */
        public static class UserNominalMultivaluedAttributeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalMultivaluedAttribute
        {
            private static final long serialVersionUID = 1L;
            
            public UserNominalMultivaluedAttributeImpl(org.apache.xmlbeans.SchemaType sType)
            {
                super(sType);
            }
            
            private static final javax.xml.namespace.QName ATTRIBUTE$0 = 
                new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "Attribute");
            private static final javax.xml.namespace.QName FILE$2 = 
                new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "File");
            
            
            /**
             * Gets the "Attribute" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.NominalMultivaluedAttribute getAttribute()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.NominalMultivaluedAttribute target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.NominalMultivaluedAttribute)get_store().find_element_user(ATTRIBUTE$0, 0);
                    if (target == null)
                    {
                      return null;
                    }
                    return target;
                }
            }
            
            /**
             * Sets the "Attribute" element
             */
            public void setAttribute(ch.uzh.agglorecommender.client.inputbeans.NominalMultivaluedAttribute attribute)
            {
                generatedSetterHelperImpl(attribute, ATTRIBUTE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
            }
            
            /**
             * Appends and returns a new empty "Attribute" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.NominalMultivaluedAttribute addNewAttribute()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.NominalMultivaluedAttribute target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.NominalMultivaluedAttribute)get_store().add_element_user(ATTRIBUTE$0);
                    return target;
                }
            }
            
            /**
             * Gets array of all "File" elements
             */
            public ch.uzh.agglorecommender.client.inputbeans.MetaFile[] getFileArray()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    java.util.List targetList = new java.util.ArrayList();
                    get_store().find_all_element_users(FILE$2, targetList);
                    ch.uzh.agglorecommender.client.inputbeans.MetaFile[] result = new ch.uzh.agglorecommender.client.inputbeans.MetaFile[targetList.size()];
                    targetList.toArray(result);
                    return result;
                }
            }
            
            /**
             * Gets ith "File" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.MetaFile getFileArray(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.MetaFile target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.MetaFile)get_store().find_element_user(FILE$2, i);
                    if (target == null)
                    {
                      throw new IndexOutOfBoundsException();
                    }
                    return target;
                }
            }
            
            /**
             * Returns number of "File" element
             */
            public int sizeOfFileArray()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    return get_store().count_elements(FILE$2);
                }
            }
            
            /**
             * Sets array of all "File" element  WARNING: This method is not atomicaly synchronized.
             */
            public void setFileArray(ch.uzh.agglorecommender.client.inputbeans.MetaFile[] fileArray)
            {
                check_orphaned();
                arraySetterHelper(fileArray, FILE$2);
            }
            
            /**
             * Sets ith "File" element
             */
            public void setFileArray(int i, ch.uzh.agglorecommender.client.inputbeans.MetaFile file)
            {
                generatedSetterHelperImpl(file, FILE$2, i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
            }
            
            /**
             * Inserts and returns a new empty value (as xml) as the ith "File" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.MetaFile insertNewFile(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.MetaFile target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.MetaFile)get_store().insert_element_user(FILE$2, i);
                    return target;
                }
            }
            
            /**
             * Appends and returns a new empty value (as xml) as the last "File" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.MetaFile addNewFile()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.MetaFile target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.MetaFile)get_store().add_element_user(FILE$2);
                    return target;
                }
            }
            
            /**
             * Removes the ith "File" element
             */
            public void removeFile(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    get_store().remove_element(FILE$2, i);
                }
            }
        }
        /**
         * An XML contentNumericalAttribute(@ch/uzh/agglorecommender/client/inputbeans).
         *
         * This is a complex type.
         */
        public static class ContentNumericalAttributeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNumericalAttribute
        {
            private static final long serialVersionUID = 1L;
            
            public ContentNumericalAttributeImpl(org.apache.xmlbeans.SchemaType sType)
            {
                super(sType);
            }
            
            private static final javax.xml.namespace.QName ATTRIBUTE$0 = 
                new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "Attribute");
            private static final javax.xml.namespace.QName FILE$2 = 
                new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "File");
            
            
            /**
             * Gets the "Attribute" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute getAttribute()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute)get_store().find_element_user(ATTRIBUTE$0, 0);
                    if (target == null)
                    {
                      return null;
                    }
                    return target;
                }
            }
            
            /**
             * Sets the "Attribute" element
             */
            public void setAttribute(ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute attribute)
            {
                generatedSetterHelperImpl(attribute, ATTRIBUTE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
            }
            
            /**
             * Appends and returns a new empty "Attribute" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute addNewAttribute()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute)get_store().add_element_user(ATTRIBUTE$0);
                    return target;
                }
            }
            
            /**
             * Gets array of all "File" elements
             */
            public ch.uzh.agglorecommender.client.inputbeans.MetaFile[] getFileArray()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    java.util.List targetList = new java.util.ArrayList();
                    get_store().find_all_element_users(FILE$2, targetList);
                    ch.uzh.agglorecommender.client.inputbeans.MetaFile[] result = new ch.uzh.agglorecommender.client.inputbeans.MetaFile[targetList.size()];
                    targetList.toArray(result);
                    return result;
                }
            }
            
            /**
             * Gets ith "File" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.MetaFile getFileArray(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.MetaFile target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.MetaFile)get_store().find_element_user(FILE$2, i);
                    if (target == null)
                    {
                      throw new IndexOutOfBoundsException();
                    }
                    return target;
                }
            }
            
            /**
             * Returns number of "File" element
             */
            public int sizeOfFileArray()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    return get_store().count_elements(FILE$2);
                }
            }
            
            /**
             * Sets array of all "File" element  WARNING: This method is not atomicaly synchronized.
             */
            public void setFileArray(ch.uzh.agglorecommender.client.inputbeans.MetaFile[] fileArray)
            {
                check_orphaned();
                arraySetterHelper(fileArray, FILE$2);
            }
            
            /**
             * Sets ith "File" element
             */
            public void setFileArray(int i, ch.uzh.agglorecommender.client.inputbeans.MetaFile file)
            {
                generatedSetterHelperImpl(file, FILE$2, i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
            }
            
            /**
             * Inserts and returns a new empty value (as xml) as the ith "File" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.MetaFile insertNewFile(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.MetaFile target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.MetaFile)get_store().insert_element_user(FILE$2, i);
                    return target;
                }
            }
            
            /**
             * Appends and returns a new empty value (as xml) as the last "File" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.MetaFile addNewFile()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.MetaFile target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.MetaFile)get_store().add_element_user(FILE$2);
                    return target;
                }
            }
            
            /**
             * Removes the ith "File" element
             */
            public void removeFile(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    get_store().remove_element(FILE$2, i);
                }
            }
        }
        /**
         * An XML contentNominalAttribute(@ch/uzh/agglorecommender/client/inputbeans).
         *
         * This is a complex type.
         */
        public static class ContentNominalAttributeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalAttribute
        {
            private static final long serialVersionUID = 1L;
            
            public ContentNominalAttributeImpl(org.apache.xmlbeans.SchemaType sType)
            {
                super(sType);
            }
            
            private static final javax.xml.namespace.QName ATTRIBUTE$0 = 
                new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "Attribute");
            private static final javax.xml.namespace.QName FILE$2 = 
                new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "File");
            
            
            /**
             * Gets the "Attribute" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.NominalAttribute getAttribute()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.NominalAttribute target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.NominalAttribute)get_store().find_element_user(ATTRIBUTE$0, 0);
                    if (target == null)
                    {
                      return null;
                    }
                    return target;
                }
            }
            
            /**
             * Sets the "Attribute" element
             */
            public void setAttribute(ch.uzh.agglorecommender.client.inputbeans.NominalAttribute attribute)
            {
                generatedSetterHelperImpl(attribute, ATTRIBUTE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
            }
            
            /**
             * Appends and returns a new empty "Attribute" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.NominalAttribute addNewAttribute()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.NominalAttribute target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.NominalAttribute)get_store().add_element_user(ATTRIBUTE$0);
                    return target;
                }
            }
            
            /**
             * Gets array of all "File" elements
             */
            public ch.uzh.agglorecommender.client.inputbeans.MetaFile[] getFileArray()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    java.util.List targetList = new java.util.ArrayList();
                    get_store().find_all_element_users(FILE$2, targetList);
                    ch.uzh.agglorecommender.client.inputbeans.MetaFile[] result = new ch.uzh.agglorecommender.client.inputbeans.MetaFile[targetList.size()];
                    targetList.toArray(result);
                    return result;
                }
            }
            
            /**
             * Gets ith "File" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.MetaFile getFileArray(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.MetaFile target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.MetaFile)get_store().find_element_user(FILE$2, i);
                    if (target == null)
                    {
                      throw new IndexOutOfBoundsException();
                    }
                    return target;
                }
            }
            
            /**
             * Returns number of "File" element
             */
            public int sizeOfFileArray()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    return get_store().count_elements(FILE$2);
                }
            }
            
            /**
             * Sets array of all "File" element  WARNING: This method is not atomicaly synchronized.
             */
            public void setFileArray(ch.uzh.agglorecommender.client.inputbeans.MetaFile[] fileArray)
            {
                check_orphaned();
                arraySetterHelper(fileArray, FILE$2);
            }
            
            /**
             * Sets ith "File" element
             */
            public void setFileArray(int i, ch.uzh.agglorecommender.client.inputbeans.MetaFile file)
            {
                generatedSetterHelperImpl(file, FILE$2, i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
            }
            
            /**
             * Inserts and returns a new empty value (as xml) as the ith "File" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.MetaFile insertNewFile(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.MetaFile target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.MetaFile)get_store().insert_element_user(FILE$2, i);
                    return target;
                }
            }
            
            /**
             * Appends and returns a new empty value (as xml) as the last "File" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.MetaFile addNewFile()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.MetaFile target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.MetaFile)get_store().add_element_user(FILE$2);
                    return target;
                }
            }
            
            /**
             * Removes the ith "File" element
             */
            public void removeFile(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    get_store().remove_element(FILE$2, i);
                }
            }
        }
        /**
         * An XML contentNominalMultivaluedAttribute(@ch/uzh/agglorecommender/client/inputbeans).
         *
         * This is a complex type.
         */
        public static class ContentNominalMultivaluedAttributeImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalMultivaluedAttribute
        {
            private static final long serialVersionUID = 1L;
            
            public ContentNominalMultivaluedAttributeImpl(org.apache.xmlbeans.SchemaType sType)
            {
                super(sType);
            }
            
            private static final javax.xml.namespace.QName ATTRIBUTE$0 = 
                new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "Attribute");
            private static final javax.xml.namespace.QName FILE$2 = 
                new javax.xml.namespace.QName("ch/uzh/agglorecommender/client/inputbeans", "File");
            
            
            /**
             * Gets the "Attribute" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.NominalMultivaluedAttribute getAttribute()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.NominalMultivaluedAttribute target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.NominalMultivaluedAttribute)get_store().find_element_user(ATTRIBUTE$0, 0);
                    if (target == null)
                    {
                      return null;
                    }
                    return target;
                }
            }
            
            /**
             * Sets the "Attribute" element
             */
            public void setAttribute(ch.uzh.agglorecommender.client.inputbeans.NominalMultivaluedAttribute attribute)
            {
                generatedSetterHelperImpl(attribute, ATTRIBUTE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
            }
            
            /**
             * Appends and returns a new empty "Attribute" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.NominalMultivaluedAttribute addNewAttribute()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.NominalMultivaluedAttribute target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.NominalMultivaluedAttribute)get_store().add_element_user(ATTRIBUTE$0);
                    return target;
                }
            }
            
            /**
             * Gets array of all "File" elements
             */
            public ch.uzh.agglorecommender.client.inputbeans.MetaFile[] getFileArray()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    java.util.List targetList = new java.util.ArrayList();
                    get_store().find_all_element_users(FILE$2, targetList);
                    ch.uzh.agglorecommender.client.inputbeans.MetaFile[] result = new ch.uzh.agglorecommender.client.inputbeans.MetaFile[targetList.size()];
                    targetList.toArray(result);
                    return result;
                }
            }
            
            /**
             * Gets ith "File" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.MetaFile getFileArray(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.MetaFile target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.MetaFile)get_store().find_element_user(FILE$2, i);
                    if (target == null)
                    {
                      throw new IndexOutOfBoundsException();
                    }
                    return target;
                }
            }
            
            /**
             * Returns number of "File" element
             */
            public int sizeOfFileArray()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    return get_store().count_elements(FILE$2);
                }
            }
            
            /**
             * Sets array of all "File" element  WARNING: This method is not atomicaly synchronized.
             */
            public void setFileArray(ch.uzh.agglorecommender.client.inputbeans.MetaFile[] fileArray)
            {
                check_orphaned();
                arraySetterHelper(fileArray, FILE$2);
            }
            
            /**
             * Sets ith "File" element
             */
            public void setFileArray(int i, ch.uzh.agglorecommender.client.inputbeans.MetaFile file)
            {
                generatedSetterHelperImpl(file, FILE$2, i, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_ARRAYITEM);
            }
            
            /**
             * Inserts and returns a new empty value (as xml) as the ith "File" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.MetaFile insertNewFile(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.MetaFile target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.MetaFile)get_store().insert_element_user(FILE$2, i);
                    return target;
                }
            }
            
            /**
             * Appends and returns a new empty value (as xml) as the last "File" element
             */
            public ch.uzh.agglorecommender.client.inputbeans.MetaFile addNewFile()
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    ch.uzh.agglorecommender.client.inputbeans.MetaFile target = null;
                    target = (ch.uzh.agglorecommender.client.inputbeans.MetaFile)get_store().add_element_user(FILE$2);
                    return target;
                }
            }
            
            /**
             * Removes the ith "File" element
             */
            public void removeFile(int i)
            {
                synchronized (monitor())
                {
                    check_orphaned();
                    get_store().remove_element(FILE$2, i);
                }
            }
        }
    }
}
