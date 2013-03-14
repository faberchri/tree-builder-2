/*
 * XML Type:  File
 * Namespace: ch/uzh/agglorecommender/client/inputbeans
 * Java type: ch.uzh.agglorecommender.client.inputbeans.File
 *
 * Automatically generated - do not modify.
 */
package ch.uzh.agglorecommender.client.inputbeans;


/**
 * An XML File(@ch/uzh/agglorecommender/client/inputbeans).
 *
 * This is a complex type.
 */
public interface File extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(File.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sB60B15B0AF9AA2D067FC4DD65B1CBDC2").resolveHandle("file852etype");
    
    /**
     * Gets the "ColumnSeparator" element
     */
    java.lang.String getColumnSeparator();
    
    /**
     * Gets (as xml) the "ColumnSeparator" element
     */
    ch.uzh.agglorecommender.client.inputbeans.File.ColumnSeparator xgetColumnSeparator();
    
    /**
     * Sets the "ColumnSeparator" element
     */
    void setColumnSeparator(java.lang.String columnSeparator);
    
    /**
     * Sets (as xml) the "ColumnSeparator" element
     */
    void xsetColumnSeparator(ch.uzh.agglorecommender.client.inputbeans.File.ColumnSeparator columnSeparator);
    
    /**
     * Gets the "Location" element
     */
    java.lang.String getLocation();
    
    /**
     * Gets (as xml) the "Location" element
     */
    org.apache.xmlbeans.XmlString xgetLocation();
    
    /**
     * Sets the "Location" element
     */
    void setLocation(java.lang.String location);
    
    /**
     * Sets (as xml) the "Location" element
     */
    void xsetLocation(org.apache.xmlbeans.XmlString location);
    
    /**
     * Gets the "ValueColumnNumber" element
     */
    java.math.BigInteger getValueColumnNumber();
    
    /**
     * Gets (as xml) the "ValueColumnNumber" element
     */
    org.apache.xmlbeans.XmlPositiveInteger xgetValueColumnNumber();
    
    /**
     * Sets the "ValueColumnNumber" element
     */
    void setValueColumnNumber(java.math.BigInteger valueColumnNumber);
    
    /**
     * Sets (as xml) the "ValueColumnNumber" element
     */
    void xsetValueColumnNumber(org.apache.xmlbeans.XmlPositiveInteger valueColumnNumber);
    
    /**
     * Gets the "StartLine" element
     */
    java.math.BigInteger getStartLine();
    
    /**
     * Gets (as xml) the "StartLine" element
     */
    org.apache.xmlbeans.XmlPositiveInteger xgetStartLine();
    
    /**
     * True if has "StartLine" element
     */
    boolean isSetStartLine();
    
    /**
     * Sets the "StartLine" element
     */
    void setStartLine(java.math.BigInteger startLine);
    
    /**
     * Sets (as xml) the "StartLine" element
     */
    void xsetStartLine(org.apache.xmlbeans.XmlPositiveInteger startLine);
    
    /**
     * Unsets the "StartLine" element
     */
    void unsetStartLine();
    
    /**
     * Gets the "EndLine" element
     */
    java.math.BigInteger getEndLine();
    
    /**
     * Gets (as xml) the "EndLine" element
     */
    org.apache.xmlbeans.XmlPositiveInteger xgetEndLine();
    
    /**
     * True if has "EndLine" element
     */
    boolean isSetEndLine();
    
    /**
     * Sets the "EndLine" element
     */
    void setEndLine(java.math.BigInteger endLine);
    
    /**
     * Sets (as xml) the "EndLine" element
     */
    void xsetEndLine(org.apache.xmlbeans.XmlPositiveInteger endLine);
    
    /**
     * Unsets the "EndLine" element
     */
    void unsetEndLine();
    
    /**
     * An XML ColumnSeparator(@ch/uzh/agglorecommender/client/inputbeans).
     *
     * This is an atomic type that is a restriction of ch.uzh.agglorecommender.client.inputbeans.File$ColumnSeparator.
     */
    public interface ColumnSeparator extends org.apache.xmlbeans.XmlString
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(ColumnSeparator.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sB60B15B0AF9AA2D067FC4DD65B1CBDC2").resolveHandle("columnseparatorda13elemtype");
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static ch.uzh.agglorecommender.client.inputbeans.File.ColumnSeparator newValue(java.lang.Object obj) {
              return (ch.uzh.agglorecommender.client.inputbeans.File.ColumnSeparator) type.newValue( obj ); }
            
            public static ch.uzh.agglorecommender.client.inputbeans.File.ColumnSeparator newInstance() {
              return (ch.uzh.agglorecommender.client.inputbeans.File.ColumnSeparator) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static ch.uzh.agglorecommender.client.inputbeans.File.ColumnSeparator newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (ch.uzh.agglorecommender.client.inputbeans.File.ColumnSeparator) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static ch.uzh.agglorecommender.client.inputbeans.File newInstance() {
          return (ch.uzh.agglorecommender.client.inputbeans.File) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.File newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (ch.uzh.agglorecommender.client.inputbeans.File) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static ch.uzh.agglorecommender.client.inputbeans.File parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (ch.uzh.agglorecommender.client.inputbeans.File) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.File parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ch.uzh.agglorecommender.client.inputbeans.File) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static ch.uzh.agglorecommender.client.inputbeans.File parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.File) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.File parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.File) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.File parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.File) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.File parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.File) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.File parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.File) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.File parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.File) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.File parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.File) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.File parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.File) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.File parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (ch.uzh.agglorecommender.client.inputbeans.File) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.File parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ch.uzh.agglorecommender.client.inputbeans.File) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.File parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (ch.uzh.agglorecommender.client.inputbeans.File) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.File parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ch.uzh.agglorecommender.client.inputbeans.File) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static ch.uzh.agglorecommender.client.inputbeans.File parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (ch.uzh.agglorecommender.client.inputbeans.File) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static ch.uzh.agglorecommender.client.inputbeans.File parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (ch.uzh.agglorecommender.client.inputbeans.File) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
