/*
 * XML Type:  MetaFile
 * Namespace: ch/uzh/agglorecommender/client/inputbeans
 * Java type: ch.uzh.agglorecommender.client.inputbeans.MetaFile
 *
 * Automatically generated - do not modify.
 */
package ch.uzh.agglorecommender.client.inputbeans;


/**
 * An XML MetaFile(@ch/uzh/agglorecommender/client/inputbeans).
 *
 * This is a complex type.
 */
public interface MetaFile extends ch.uzh.agglorecommender.client.inputbeans.File
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(MetaFile.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sB60B15B0AF9AA2D067FC4DD65B1CBDC2").resolveHandle("metafile9a13type");
    
    /**
     * Gets the "IdColumnNumber" element
     */
    java.math.BigInteger getIdColumnNumber();
    
    /**
     * Gets (as xml) the "IdColumnNumber" element
     */
    org.apache.xmlbeans.XmlPositiveInteger xgetIdColumnNumber();
    
    /**
     * Sets the "IdColumnNumber" element
     */
    void setIdColumnNumber(java.math.BigInteger idColumnNumber);
    
    /**
     * Sets (as xml) the "IdColumnNumber" element
     */
    void xsetIdColumnNumber(org.apache.xmlbeans.XmlPositiveInteger idColumnNumber);
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static ch.uzh.agglorecommender.client.inputbeans.MetaFile newInstance() {
          return (ch.uzh.agglorecommender.client.inputbeans.MetaFile) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.MetaFile newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (ch.uzh.agglorecommender.client.inputbeans.MetaFile) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static ch.uzh.agglorecommender.client.inputbeans.MetaFile parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (ch.uzh.agglorecommender.client.inputbeans.MetaFile) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.MetaFile parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ch.uzh.agglorecommender.client.inputbeans.MetaFile) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static ch.uzh.agglorecommender.client.inputbeans.MetaFile parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.MetaFile) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.MetaFile parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.MetaFile) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.MetaFile parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.MetaFile) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.MetaFile parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.MetaFile) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.MetaFile parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.MetaFile) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.MetaFile parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.MetaFile) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.MetaFile parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.MetaFile) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.MetaFile parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.MetaFile) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.MetaFile parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (ch.uzh.agglorecommender.client.inputbeans.MetaFile) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.MetaFile parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ch.uzh.agglorecommender.client.inputbeans.MetaFile) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.MetaFile parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (ch.uzh.agglorecommender.client.inputbeans.MetaFile) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.MetaFile parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ch.uzh.agglorecommender.client.inputbeans.MetaFile) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static ch.uzh.agglorecommender.client.inputbeans.MetaFile parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (ch.uzh.agglorecommender.client.inputbeans.MetaFile) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static ch.uzh.agglorecommender.client.inputbeans.MetaFile parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (ch.uzh.agglorecommender.client.inputbeans.MetaFile) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
