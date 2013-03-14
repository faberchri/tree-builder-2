/*
 * XML Type:  Attribute
 * Namespace: ch/uzh/agglorecommender/client/inputbeans
 * Java type: ch.uzh.agglorecommender.client.inputbeans.Attribute
 *
 * Automatically generated - do not modify.
 */
package ch.uzh.agglorecommender.client.inputbeans;


/**
 * An XML Attribute(@ch/uzh/agglorecommender/client/inputbeans).
 *
 * This is a complex type.
 */
public interface Attribute extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(Attribute.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.sB60B15B0AF9AA2D067FC4DD65B1CBDC2").resolveHandle("attribute9a5ctype");
    
    /**
     * Gets the "Tag" element
     */
    java.lang.String getTag();
    
    /**
     * Gets (as xml) the "Tag" element
     */
    org.apache.xmlbeans.XmlString xgetTag();
    
    /**
     * Sets the "Tag" element
     */
    void setTag(java.lang.String tag);
    
    /**
     * Sets (as xml) the "Tag" element
     */
    void xsetTag(org.apache.xmlbeans.XmlString tag);
    
    /**
     * Gets the "useForClustering" element
     */
    boolean getUseForClustering();
    
    /**
     * Gets (as xml) the "useForClustering" element
     */
    org.apache.xmlbeans.XmlBoolean xgetUseForClustering();
    
    /**
     * Sets the "useForClustering" element
     */
    void setUseForClustering(boolean useForClustering);
    
    /**
     * Sets (as xml) the "useForClustering" element
     */
    void xsetUseForClustering(org.apache.xmlbeans.XmlBoolean useForClustering);
    
    /**
     * Gets the "PreProcessingRegex" element
     */
    java.lang.String getPreProcessingRegex();
    
    /**
     * Gets (as xml) the "PreProcessingRegex" element
     */
    org.apache.xmlbeans.XmlString xgetPreProcessingRegex();
    
    /**
     * True if has "PreProcessingRegex" element
     */
    boolean isSetPreProcessingRegex();
    
    /**
     * Sets the "PreProcessingRegex" element
     */
    void setPreProcessingRegex(java.lang.String preProcessingRegex);
    
    /**
     * Sets (as xml) the "PreProcessingRegex" element
     */
    void xsetPreProcessingRegex(org.apache.xmlbeans.XmlString preProcessingRegex);
    
    /**
     * Unsets the "PreProcessingRegex" element
     */
    void unsetPreProcessingRegex();
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        /** @deprecated No need to be able to create instances of abstract types */
        public static ch.uzh.agglorecommender.client.inputbeans.Attribute newInstance() {
          return (ch.uzh.agglorecommender.client.inputbeans.Attribute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        /** @deprecated No need to be able to create instances of abstract types */
        public static ch.uzh.agglorecommender.client.inputbeans.Attribute newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (ch.uzh.agglorecommender.client.inputbeans.Attribute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static ch.uzh.agglorecommender.client.inputbeans.Attribute parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (ch.uzh.agglorecommender.client.inputbeans.Attribute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.Attribute parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ch.uzh.agglorecommender.client.inputbeans.Attribute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static ch.uzh.agglorecommender.client.inputbeans.Attribute parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.Attribute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.Attribute parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.Attribute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.Attribute parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.Attribute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.Attribute parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.Attribute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.Attribute parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.Attribute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.Attribute parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.Attribute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.Attribute parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.Attribute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.Attribute parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ch.uzh.agglorecommender.client.inputbeans.Attribute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.Attribute parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (ch.uzh.agglorecommender.client.inputbeans.Attribute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.Attribute parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ch.uzh.agglorecommender.client.inputbeans.Attribute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.Attribute parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (ch.uzh.agglorecommender.client.inputbeans.Attribute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static ch.uzh.agglorecommender.client.inputbeans.Attribute parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ch.uzh.agglorecommender.client.inputbeans.Attribute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static ch.uzh.agglorecommender.client.inputbeans.Attribute parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (ch.uzh.agglorecommender.client.inputbeans.Attribute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static ch.uzh.agglorecommender.client.inputbeans.Attribute parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (ch.uzh.agglorecommender.client.inputbeans.Attribute) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}
