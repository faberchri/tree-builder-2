/*
 * An XML document type.
 * Localname: Input
 * Namespace: ch/uzh/agglorecommender/client/inputbeans
 * Java type: ch.uzh.agglorecommender.client.inputbeans.InputDocument
 *
 * Automatically generated - do not modify.
 */
package ch.uzh.agglorecommender.client.inputbeans;


/**
 * A document containing one Input(@ch/uzh/agglorecommender/client/inputbeans) element.
 *
 * This is a complex type.
 */
public interface InputDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(InputDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sB60B15B0AF9AA2D067FC4DD65B1CBDC2").resolveHandle("input517adoctype");
    
    /**
     * Gets the "Input" element
     */
    ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input getInput();
    
    /**
     * Sets the "Input" element
     */
    void setInput(ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input input);
    
    /**
     * Appends and returns a new empty "Input" element
     */
    ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input addNewInput();
    
    /**
     * An XML Input(@ch/uzh/agglorecommender/client/inputbeans).
     *
     * This is a complex type.
     */
    public interface Input extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Input.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sB60B15B0AF9AA2D067FC4DD65B1CBDC2").resolveHandle("input5e10elemtype");
        
        /**
         * Gets array of all "Rating" elements
         */
        ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.Rating[] getRatingArray();
        
        /**
         * Gets ith "Rating" element
         */
        ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.Rating getRatingArray(int i);
        
        /**
         * Returns number of "Rating" element
         */
        int sizeOfRatingArray();
        
        /**
         * Sets array of all "Rating" element
         */
        void setRatingArray(ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.Rating[] ratingArray);
        
        /**
         * Sets ith "Rating" element
         */
        void setRatingArray(int i, ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.Rating rating);
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "Rating" element
         */
        ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.Rating insertNewRating(int i);
        
        /**
         * Appends and returns a new empty value (as xml) as the last "Rating" element
         */
        ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.Rating addNewRating();
        
        /**
         * Removes the ith "Rating" element
         */
        void removeRating(int i);
        
        /**
         * Gets array of all "userNumericalAttribute" elements
         */
        ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNumericalAttribute[] getUserNumericalAttributeArray();
        
        /**
         * Gets ith "userNumericalAttribute" element
         */
        ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNumericalAttribute getUserNumericalAttributeArray(int i);
        
        /**
         * Returns number of "userNumericalAttribute" element
         */
        int sizeOfUserNumericalAttributeArray();
        
        /**
         * Sets array of all "userNumericalAttribute" element
         */
        void setUserNumericalAttributeArray(ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNumericalAttribute[] userNumericalAttributeArray);
        
        /**
         * Sets ith "userNumericalAttribute" element
         */
        void setUserNumericalAttributeArray(int i, ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNumericalAttribute userNumericalAttribute);
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "userNumericalAttribute" element
         */
        ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNumericalAttribute insertNewUserNumericalAttribute(int i);
        
        /**
         * Appends and returns a new empty value (as xml) as the last "userNumericalAttribute" element
         */
        ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNumericalAttribute addNewUserNumericalAttribute();
        
        /**
         * Removes the ith "userNumericalAttribute" element
         */
        void removeUserNumericalAttribute(int i);
        
        /**
         * Gets array of all "userNominalAttribute" elements
         */
        ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalAttribute[] getUserNominalAttributeArray();
        
        /**
         * Gets ith "userNominalAttribute" element
         */
        ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalAttribute getUserNominalAttributeArray(int i);
        
        /**
         * Returns number of "userNominalAttribute" element
         */
        int sizeOfUserNominalAttributeArray();
        
        /**
         * Sets array of all "userNominalAttribute" element
         */
        void setUserNominalAttributeArray(ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalAttribute[] userNominalAttributeArray);
        
        /**
         * Sets ith "userNominalAttribute" element
         */
        void setUserNominalAttributeArray(int i, ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalAttribute userNominalAttribute);
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "userNominalAttribute" element
         */
        ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalAttribute insertNewUserNominalAttribute(int i);
        
        /**
         * Appends and returns a new empty value (as xml) as the last "userNominalAttribute" element
         */
        ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalAttribute addNewUserNominalAttribute();
        
        /**
         * Removes the ith "userNominalAttribute" element
         */
        void removeUserNominalAttribute(int i);
        
        /**
         * Gets array of all "userNominalMultivaluedAttribute" elements
         */
        ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalMultivaluedAttribute[] getUserNominalMultivaluedAttributeArray();
        
        /**
         * Gets ith "userNominalMultivaluedAttribute" element
         */
        ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalMultivaluedAttribute getUserNominalMultivaluedAttributeArray(int i);
        
        /**
         * Returns number of "userNominalMultivaluedAttribute" element
         */
        int sizeOfUserNominalMultivaluedAttributeArray();
        
        /**
         * Sets array of all "userNominalMultivaluedAttribute" element
         */
        void setUserNominalMultivaluedAttributeArray(ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalMultivaluedAttribute[] userNominalMultivaluedAttributeArray);
        
        /**
         * Sets ith "userNominalMultivaluedAttribute" element
         */
        void setUserNominalMultivaluedAttributeArray(int i, ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalMultivaluedAttribute userNominalMultivaluedAttribute);
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "userNominalMultivaluedAttribute" element
         */
        ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalMultivaluedAttribute insertNewUserNominalMultivaluedAttribute(int i);
        
        /**
         * Appends and returns a new empty value (as xml) as the last "userNominalMultivaluedAttribute" element
         */
        ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalMultivaluedAttribute addNewUserNominalMultivaluedAttribute();
        
        /**
         * Removes the ith "userNominalMultivaluedAttribute" element
         */
        void removeUserNominalMultivaluedAttribute(int i);
        
        /**
         * Gets array of all "contentNumericalAttribute" elements
         */
        ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNumericalAttribute[] getContentNumericalAttributeArray();
        
        /**
         * Gets ith "contentNumericalAttribute" element
         */
        ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNumericalAttribute getContentNumericalAttributeArray(int i);
        
        /**
         * Returns number of "contentNumericalAttribute" element
         */
        int sizeOfContentNumericalAttributeArray();
        
        /**
         * Sets array of all "contentNumericalAttribute" element
         */
        void setContentNumericalAttributeArray(ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNumericalAttribute[] contentNumericalAttributeArray);
        
        /**
         * Sets ith "contentNumericalAttribute" element
         */
        void setContentNumericalAttributeArray(int i, ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNumericalAttribute contentNumericalAttribute);
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "contentNumericalAttribute" element
         */
        ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNumericalAttribute insertNewContentNumericalAttribute(int i);
        
        /**
         * Appends and returns a new empty value (as xml) as the last "contentNumericalAttribute" element
         */
        ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNumericalAttribute addNewContentNumericalAttribute();
        
        /**
         * Removes the ith "contentNumericalAttribute" element
         */
        void removeContentNumericalAttribute(int i);
        
        /**
         * Gets array of all "contentNominalAttribute" elements
         */
        ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalAttribute[] getContentNominalAttributeArray();
        
        /**
         * Gets ith "contentNominalAttribute" element
         */
        ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalAttribute getContentNominalAttributeArray(int i);
        
        /**
         * Returns number of "contentNominalAttribute" element
         */
        int sizeOfContentNominalAttributeArray();
        
        /**
         * Sets array of all "contentNominalAttribute" element
         */
        void setContentNominalAttributeArray(ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalAttribute[] contentNominalAttributeArray);
        
        /**
         * Sets ith "contentNominalAttribute" element
         */
        void setContentNominalAttributeArray(int i, ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalAttribute contentNominalAttribute);
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "contentNominalAttribute" element
         */
        ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalAttribute insertNewContentNominalAttribute(int i);
        
        /**
         * Appends and returns a new empty value (as xml) as the last "contentNominalAttribute" element
         */
        ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalAttribute addNewContentNominalAttribute();
        
        /**
         * Removes the ith "contentNominalAttribute" element
         */
        void removeContentNominalAttribute(int i);
        
        /**
         * Gets array of all "contentNominalMultivaluedAttribute" elements
         */
        ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalMultivaluedAttribute[] getContentNominalMultivaluedAttributeArray();
        
        /**
         * Gets ith "contentNominalMultivaluedAttribute" element
         */
        ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalMultivaluedAttribute getContentNominalMultivaluedAttributeArray(int i);
        
        /**
         * Returns number of "contentNominalMultivaluedAttribute" element
         */
        int sizeOfContentNominalMultivaluedAttributeArray();
        
        /**
         * Sets array of all "contentNominalMultivaluedAttribute" element
         */
        void setContentNominalMultivaluedAttributeArray(ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalMultivaluedAttribute[] contentNominalMultivaluedAttributeArray);
        
        /**
         * Sets ith "contentNominalMultivaluedAttribute" element
         */
        void setContentNominalMultivaluedAttributeArray(int i, ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalMultivaluedAttribute contentNominalMultivaluedAttribute);
        
        /**
         * Inserts and returns a new empty value (as xml) as the ith "contentNominalMultivaluedAttribute" element
         */
        ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalMultivaluedAttribute insertNewContentNominalMultivaluedAttribute(int i);
        
        /**
         * Appends and returns a new empty value (as xml) as the last "contentNominalMultivaluedAttribute" element
         */
        ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalMultivaluedAttribute addNewContentNominalMultivaluedAttribute();
        
        /**
         * Removes the ith "contentNominalMultivaluedAttribute" element
         */
        void removeContentNominalMultivaluedAttribute(int i);
        
        /**
         * An XML Rating(@ch/uzh/agglorecommender/client/inputbeans).
         *
         * This is a complex type.
         */
        public interface Rating extends org.apache.xmlbeans.XmlObject
        {
            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Rating.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sB60B15B0AF9AA2D067FC4DD65B1CBDC2").resolveHandle("rating6129elemtype");
            
            /**
             * Gets the "Attribute" element
             */
            ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute getAttribute();
            
            /**
             * Sets the "Attribute" element
             */
            void setAttribute(ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute attribute);
            
            /**
             * Appends and returns a new empty "Attribute" element
             */
            ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute addNewAttribute();
            
            /**
             * Gets array of all "File" elements
             */
            ch.uzh.agglorecommender.client.inputbeans.RatingFile[] getFileArray();
            
            /**
             * Gets ith "File" element
             */
            ch.uzh.agglorecommender.client.inputbeans.RatingFile getFileArray(int i);
            
            /**
             * Returns number of "File" element
             */
            int sizeOfFileArray();
            
            /**
             * Sets array of all "File" element
             */
            void setFileArray(ch.uzh.agglorecommender.client.inputbeans.RatingFile[] fileArray);
            
            /**
             * Sets ith "File" element
             */
            void setFileArray(int i, ch.uzh.agglorecommender.client.inputbeans.RatingFile file);
            
            /**
             * Inserts and returns a new empty value (as xml) as the ith "File" element
             */
            ch.uzh.agglorecommender.client.inputbeans.RatingFile insertNewFile(int i);
            
            /**
             * Appends and returns a new empty value (as xml) as the last "File" element
             */
            ch.uzh.agglorecommender.client.inputbeans.RatingFile addNewFile();
            
            /**
             * Removes the ith "File" element
             */
            void removeFile(int i);
            
            /**
             * A factory class with static methods for creating instances
             * of this type.
             */
            
            public static final class Factory
            {
                public static ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.Rating newInstance() {
                  return (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.Rating) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                
                public static ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.Rating newInstance(org.apache.xmlbeans.XmlOptions options) {
                  return (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.Rating) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                
                private Factory() { } // No instance of this class allowed
            }
        }
        
        /**
         * An XML userNumericalAttribute(@ch/uzh/agglorecommender/client/inputbeans).
         *
         * This is a complex type.
         */
        public interface UserNumericalAttribute extends org.apache.xmlbeans.XmlObject
        {
            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(UserNumericalAttribute.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sB60B15B0AF9AA2D067FC4DD65B1CBDC2").resolveHandle("usernumericalattributeb59belemtype");
            
            /**
             * Gets the "Attribute" element
             */
            ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute getAttribute();
            
            /**
             * Sets the "Attribute" element
             */
            void setAttribute(ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute attribute);
            
            /**
             * Appends and returns a new empty "Attribute" element
             */
            ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute addNewAttribute();
            
            /**
             * Gets array of all "File" elements
             */
            ch.uzh.agglorecommender.client.inputbeans.MetaFile[] getFileArray();
            
            /**
             * Gets ith "File" element
             */
            ch.uzh.agglorecommender.client.inputbeans.MetaFile getFileArray(int i);
            
            /**
             * Returns number of "File" element
             */
            int sizeOfFileArray();
            
            /**
             * Sets array of all "File" element
             */
            void setFileArray(ch.uzh.agglorecommender.client.inputbeans.MetaFile[] fileArray);
            
            /**
             * Sets ith "File" element
             */
            void setFileArray(int i, ch.uzh.agglorecommender.client.inputbeans.MetaFile file);
            
            /**
             * Inserts and returns a new empty value (as xml) as the ith "File" element
             */
            ch.uzh.agglorecommender.client.inputbeans.MetaFile insertNewFile(int i);
            
            /**
             * Appends and returns a new empty value (as xml) as the last "File" element
             */
            ch.uzh.agglorecommender.client.inputbeans.MetaFile addNewFile();
            
            /**
             * Removes the ith "File" element
             */
            void removeFile(int i);
            
            /**
             * A factory class with static methods for creating instances
             * of this type.
             */
            
            public static final class Factory
            {
                public static ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNumericalAttribute newInstance() {
                  return (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNumericalAttribute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                
                public static ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNumericalAttribute newInstance(org.apache.xmlbeans.XmlOptions options) {
                  return (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNumericalAttribute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                
                private Factory() { } // No instance of this class allowed
            }
        }
        
        /**
         * An XML userNominalAttribute(@ch/uzh/agglorecommender/client/inputbeans).
         *
         * This is a complex type.
         */
        public interface UserNominalAttribute extends org.apache.xmlbeans.XmlObject
        {
            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(UserNominalAttribute.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sB60B15B0AF9AA2D067FC4DD65B1CBDC2").resolveHandle("usernominalattribute6ef7elemtype");
            
            /**
             * Gets the "Attribute" element
             */
            ch.uzh.agglorecommender.client.inputbeans.NominalAttribute getAttribute();
            
            /**
             * Sets the "Attribute" element
             */
            void setAttribute(ch.uzh.agglorecommender.client.inputbeans.NominalAttribute attribute);
            
            /**
             * Appends and returns a new empty "Attribute" element
             */
            ch.uzh.agglorecommender.client.inputbeans.NominalAttribute addNewAttribute();
            
            /**
             * Gets array of all "File" elements
             */
            ch.uzh.agglorecommender.client.inputbeans.MetaFile[] getFileArray();
            
            /**
             * Gets ith "File" element
             */
            ch.uzh.agglorecommender.client.inputbeans.MetaFile getFileArray(int i);
            
            /**
             * Returns number of "File" element
             */
            int sizeOfFileArray();
            
            /**
             * Sets array of all "File" element
             */
            void setFileArray(ch.uzh.agglorecommender.client.inputbeans.MetaFile[] fileArray);
            
            /**
             * Sets ith "File" element
             */
            void setFileArray(int i, ch.uzh.agglorecommender.client.inputbeans.MetaFile file);
            
            /**
             * Inserts and returns a new empty value (as xml) as the ith "File" element
             */
            ch.uzh.agglorecommender.client.inputbeans.MetaFile insertNewFile(int i);
            
            /**
             * Appends and returns a new empty value (as xml) as the last "File" element
             */
            ch.uzh.agglorecommender.client.inputbeans.MetaFile addNewFile();
            
            /**
             * Removes the ith "File" element
             */
            void removeFile(int i);
            
            /**
             * A factory class with static methods for creating instances
             * of this type.
             */
            
            public static final class Factory
            {
                public static ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalAttribute newInstance() {
                  return (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalAttribute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                
                public static ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalAttribute newInstance(org.apache.xmlbeans.XmlOptions options) {
                  return (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalAttribute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                
                private Factory() { } // No instance of this class allowed
            }
        }
        
        /**
         * An XML userNominalMultivaluedAttribute(@ch/uzh/agglorecommender/client/inputbeans).
         *
         * This is a complex type.
         */
        public interface UserNominalMultivaluedAttribute extends org.apache.xmlbeans.XmlObject
        {
            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(UserNominalMultivaluedAttribute.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sB60B15B0AF9AA2D067FC4DD65B1CBDC2").resolveHandle("usernominalmultivaluedattribute78ddelemtype");
            
            /**
             * Gets the "Attribute" element
             */
            ch.uzh.agglorecommender.client.inputbeans.NominalMultivaluedAttribute getAttribute();
            
            /**
             * Sets the "Attribute" element
             */
            void setAttribute(ch.uzh.agglorecommender.client.inputbeans.NominalMultivaluedAttribute attribute);
            
            /**
             * Appends and returns a new empty "Attribute" element
             */
            ch.uzh.agglorecommender.client.inputbeans.NominalMultivaluedAttribute addNewAttribute();
            
            /**
             * Gets array of all "File" elements
             */
            ch.uzh.agglorecommender.client.inputbeans.MetaFile[] getFileArray();
            
            /**
             * Gets ith "File" element
             */
            ch.uzh.agglorecommender.client.inputbeans.MetaFile getFileArray(int i);
            
            /**
             * Returns number of "File" element
             */
            int sizeOfFileArray();
            
            /**
             * Sets array of all "File" element
             */
            void setFileArray(ch.uzh.agglorecommender.client.inputbeans.MetaFile[] fileArray);
            
            /**
             * Sets ith "File" element
             */
            void setFileArray(int i, ch.uzh.agglorecommender.client.inputbeans.MetaFile file);
            
            /**
             * Inserts and returns a new empty value (as xml) as the ith "File" element
             */
            ch.uzh.agglorecommender.client.inputbeans.MetaFile insertNewFile(int i);
            
            /**
             * Appends and returns a new empty value (as xml) as the last "File" element
             */
            ch.uzh.agglorecommender.client.inputbeans.MetaFile addNewFile();
            
            /**
             * Removes the ith "File" element
             */
            void removeFile(int i);
            
            /**
             * A factory class with static methods for creating instances
             * of this type.
             */
            
            public static final class Factory
            {
                public static ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalMultivaluedAttribute newInstance() {
                  return (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalMultivaluedAttribute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                
                public static ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalMultivaluedAttribute newInstance(org.apache.xmlbeans.XmlOptions options) {
                  return (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalMultivaluedAttribute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                
                private Factory() { } // No instance of this class allowed
            }
        }
        
        /**
         * An XML contentNumericalAttribute(@ch/uzh/agglorecommender/client/inputbeans).
         *
         * This is a complex type.
         */
        public interface ContentNumericalAttribute extends org.apache.xmlbeans.XmlObject
        {
            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ContentNumericalAttribute.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sB60B15B0AF9AA2D067FC4DD65B1CBDC2").resolveHandle("contentnumericalattributee119elemtype");
            
            /**
             * Gets the "Attribute" element
             */
            ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute getAttribute();
            
            /**
             * Sets the "Attribute" element
             */
            void setAttribute(ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute attribute);
            
            /**
             * Appends and returns a new empty "Attribute" element
             */
            ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute addNewAttribute();
            
            /**
             * Gets array of all "File" elements
             */
            ch.uzh.agglorecommender.client.inputbeans.MetaFile[] getFileArray();
            
            /**
             * Gets ith "File" element
             */
            ch.uzh.agglorecommender.client.inputbeans.MetaFile getFileArray(int i);
            
            /**
             * Returns number of "File" element
             */
            int sizeOfFileArray();
            
            /**
             * Sets array of all "File" element
             */
            void setFileArray(ch.uzh.agglorecommender.client.inputbeans.MetaFile[] fileArray);
            
            /**
             * Sets ith "File" element
             */
            void setFileArray(int i, ch.uzh.agglorecommender.client.inputbeans.MetaFile file);
            
            /**
             * Inserts and returns a new empty value (as xml) as the ith "File" element
             */
            ch.uzh.agglorecommender.client.inputbeans.MetaFile insertNewFile(int i);
            
            /**
             * Appends and returns a new empty value (as xml) as the last "File" element
             */
            ch.uzh.agglorecommender.client.inputbeans.MetaFile addNewFile();
            
            /**
             * Removes the ith "File" element
             */
            void removeFile(int i);
            
            /**
             * A factory class with static methods for creating instances
             * of this type.
             */
            
            public static final class Factory
            {
                public static ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNumericalAttribute newInstance() {
                  return (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNumericalAttribute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                
                public static ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNumericalAttribute newInstance(org.apache.xmlbeans.XmlOptions options) {
                  return (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNumericalAttribute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                
                private Factory() { } // No instance of this class allowed
            }
        }
        
        /**
         * An XML contentNominalAttribute(@ch/uzh/agglorecommender/client/inputbeans).
         *
         * This is a complex type.
         */
        public interface ContentNominalAttribute extends org.apache.xmlbeans.XmlObject
        {
            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ContentNominalAttribute.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sB60B15B0AF9AA2D067FC4DD65B1CBDC2").resolveHandle("contentnominalattribute61f5elemtype");
            
            /**
             * Gets the "Attribute" element
             */
            ch.uzh.agglorecommender.client.inputbeans.NominalAttribute getAttribute();
            
            /**
             * Sets the "Attribute" element
             */
            void setAttribute(ch.uzh.agglorecommender.client.inputbeans.NominalAttribute attribute);
            
            /**
             * Appends and returns a new empty "Attribute" element
             */
            ch.uzh.agglorecommender.client.inputbeans.NominalAttribute addNewAttribute();
            
            /**
             * Gets array of all "File" elements
             */
            ch.uzh.agglorecommender.client.inputbeans.MetaFile[] getFileArray();
            
            /**
             * Gets ith "File" element
             */
            ch.uzh.agglorecommender.client.inputbeans.MetaFile getFileArray(int i);
            
            /**
             * Returns number of "File" element
             */
            int sizeOfFileArray();
            
            /**
             * Sets array of all "File" element
             */
            void setFileArray(ch.uzh.agglorecommender.client.inputbeans.MetaFile[] fileArray);
            
            /**
             * Sets ith "File" element
             */
            void setFileArray(int i, ch.uzh.agglorecommender.client.inputbeans.MetaFile file);
            
            /**
             * Inserts and returns a new empty value (as xml) as the ith "File" element
             */
            ch.uzh.agglorecommender.client.inputbeans.MetaFile insertNewFile(int i);
            
            /**
             * Appends and returns a new empty value (as xml) as the last "File" element
             */
            ch.uzh.agglorecommender.client.inputbeans.MetaFile addNewFile();
            
            /**
             * Removes the ith "File" element
             */
            void removeFile(int i);
            
            /**
             * A factory class with static methods for creating instances
             * of this type.
             */
            
            public static final class Factory
            {
                public static ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalAttribute newInstance() {
                  return (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalAttribute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                
                public static ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalAttribute newInstance(org.apache.xmlbeans.XmlOptions options) {
                  return (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalAttribute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                
                private Factory() { } // No instance of this class allowed
            }
        }
        
        /**
         * An XML contentNominalMultivaluedAttribute(@ch/uzh/agglorecommender/client/inputbeans).
         *
         * This is a complex type.
         */
        public interface ContentNominalMultivaluedAttribute extends org.apache.xmlbeans.XmlObject
        {
            public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
                org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ContentNominalMultivaluedAttribute.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sB60B15B0AF9AA2D067FC4DD65B1CBDC2").resolveHandle("contentnominalmultivaluedattribute5b1felemtype");
            
            /**
             * Gets the "Attribute" element
             */
            ch.uzh.agglorecommender.client.inputbeans.NominalMultivaluedAttribute getAttribute();
            
            /**
             * Sets the "Attribute" element
             */
            void setAttribute(ch.uzh.agglorecommender.client.inputbeans.NominalMultivaluedAttribute attribute);
            
            /**
             * Appends and returns a new empty "Attribute" element
             */
            ch.uzh.agglorecommender.client.inputbeans.NominalMultivaluedAttribute addNewAttribute();
            
            /**
             * Gets array of all "File" elements
             */
            ch.uzh.agglorecommender.client.inputbeans.MetaFile[] getFileArray();
            
            /**
             * Gets ith "File" element
             */
            ch.uzh.agglorecommender.client.inputbeans.MetaFile getFileArray(int i);
            
            /**
             * Returns number of "File" element
             */
            int sizeOfFileArray();
            
            /**
             * Sets array of all "File" element
             */
            void setFileArray(ch.uzh.agglorecommender.client.inputbeans.MetaFile[] fileArray);
            
            /**
             * Sets ith "File" element
             */
            void setFileArray(int i, ch.uzh.agglorecommender.client.inputbeans.MetaFile file);
            
            /**
             * Inserts and returns a new empty value (as xml) as the ith "File" element
             */
            ch.uzh.agglorecommender.client.inputbeans.MetaFile insertNewFile(int i);
            
            /**
             * Appends and returns a new empty value (as xml) as the last "File" element
             */
            ch.uzh.agglorecommender.client.inputbeans.MetaFile addNewFile();
            
            /**
             * Removes the ith "File" element
             */
            void removeFile(int i);
            
            /**
             * A factory class with static methods for creating instances
             * of this type.
             */
            
            public static final class Factory
            {
                public static ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalMultivaluedAttribute newInstance() {
                  return (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalMultivaluedAttribute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
                
                public static ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalMultivaluedAttribute newInstance(org.apache.xmlbeans.XmlOptions options) {
                  return (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalMultivaluedAttribute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
                
                private Factory() { } // No instance of this class allowed
            }
        }
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input newInstance() {
              return (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static ch.uzh.agglorecommender.client.inputbeans.InputDocument newInstance() {
          return (ch.uzh.agglorecommender.client.inputbeans.InputDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.InputDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (ch.uzh.agglorecommender.client.inputbeans.InputDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static ch.uzh.agglorecommender.client.inputbeans.InputDocument parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (ch.uzh.agglorecommender.client.inputbeans.InputDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.InputDocument parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ch.uzh.agglorecommender.client.inputbeans.InputDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static ch.uzh.agglorecommender.client.inputbeans.InputDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.InputDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.InputDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.InputDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.InputDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.InputDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.InputDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.InputDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.InputDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.InputDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.InputDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.InputDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.InputDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.InputDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.InputDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.InputDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.InputDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (ch.uzh.agglorecommender.client.inputbeans.InputDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.InputDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ch.uzh.agglorecommender.client.inputbeans.InputDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.InputDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (ch.uzh.agglorecommender.client.inputbeans.InputDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.InputDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ch.uzh.agglorecommender.client.inputbeans.InputDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static ch.uzh.agglorecommender.client.inputbeans.InputDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (ch.uzh.agglorecommender.client.inputbeans.InputDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static ch.uzh.agglorecommender.client.inputbeans.InputDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (ch.uzh.agglorecommender.client.inputbeans.InputDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
