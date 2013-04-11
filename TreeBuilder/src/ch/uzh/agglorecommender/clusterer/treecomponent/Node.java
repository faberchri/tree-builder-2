package ch.uzh.agglorecommender.clusterer.treecomponent;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import ch.uzh.agglorecommender.client.IDataset;
import ch.uzh.agglorecommender.clusterer.treesearch.ClassitMaxCategoryUtilitySearcher;

import com.google.common.collect.ImmutableMap;


public class Node implements INode, Comparable<Node>, Serializable {

	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Node id counter.
	 */
	private static long idCounter = 0;

	/**
	 * The unique id of this node.
	 */
	private long id = idCounter++;

	/**
	 * The node type.
	 */
	private final ENodeType nodeType;

	/**
	 * The numerical background attributes of the node (e.g. gender, age, genre).
	 * Stores for each node that is an
	 * attribute of this node a mapping
	 * to an IAttribute object.
	 */
	private Map<String, IAttribute> numericalMetaAttributes = new HashMap<String, IAttribute>();

	/**
	 * The nominal background attributes of the node (e.g. gender, age, genre).
	 * Stores for each node that is an
	 * attribute of this node a mapping
	 * to an IAttribute object.
	 */
	private Map<String, IAttribute> nominalMetaAttributes = new HashMap<String, IAttribute>();

	/**
	 * The rating attributes of the node 
	 * Stores for each node that is an
	 * attribute of this node a mapping
	 * to an IAttribute object.
	 */
	private Map<INode, IAttribute> ratingAttributes = new HashMap<INode, IAttribute>();
	
	/**
	 * Reference to the data set.
	 */
	private final IDataset dataset;

	/**
	 * The children of this node.
	 */
	private Set<INode> children = new HashSet<INode>();

	/**
	 * The parent of this node.
	 * Is null s long as the node was not merged.
	 */
	private INode parent = null;

	/**
	 * The id of the represented data item in the data set.
	 * Is null if the size of this cluster is > 1.
	 */
	private final String dataSetId;

	/**
	 * The category utility of the merge of this nodes children or 1 if node is leaf
	 */
	private double categoryUtility;

	/**
	 * All nodes with recently changed attributes.
	 */
	private static Set<INode> dirtySet = new HashSet<INode>();

	/**
	 * Node constructor for a leaf node.
	 * 
	 * @param nodeType type of the node
	 * @param dataSetId id of the corresponding item in the data set
	 * @param dataset the data set to cluster
	 */
	public Node(ENodeType nodeType,
			String dataSetId,
			IDataset dataset) {
		this.nodeType = nodeType;
		this.dataSetId = dataSetId;
		categoryUtility = 1.0;
		this.dataset = dataset;
	}

	/**
	 * Node constructor for an internal node.
	 * 
	 * @param nodeType Type of the node
	 * @param children List of children
	 * @param ratingAttributes the ratings attributes map of this node
	 * @param numericalMetaAttributes the numerical meta attributes for this node
	 * @param nominalMetaAttributes the nominal meta attributes map for the node
	 */
	public Node(ENodeType nodeType,
			Collection<INode> children,
			Map<INode, IAttribute> ratingAttributes,
			Map<String, IAttribute> numericalMetaAttributes, 
			Map<String, IAttribute> nominalMetaAttributes,
			double categoryUtility) {
		this.nodeType = nodeType;
		if (children != null) {
			for (INode child : children) {
				this.children.add(child);
				child.setParent(this);
			}	
		}
		this.numericalMetaAttributes = numericalMetaAttributes;
		this.nominalMetaAttributes = nominalMetaAttributes;
		this.ratingAttributes = ratingAttributes;
		this.dataSetId = null;
		this.categoryUtility = categoryUtility;
		this.dataset = children.iterator().next().getDataset();
	}

	@Override
	public String getNumericalAttributesString() {

		List<INode> keyList = new ArrayList<INode>(ratingAttributes.keySet());
		// sort attributes by node id
		Collections.sort(keyList, new Comparator<INode>() {
			@Override
			public int compare(INode o1, INode o2) {
				return Long.compare(o1.getId(), o2.getId());
			}
		});

		List<String> keyList2 = new ArrayList<String>(numericalMetaAttributes.keySet());
		Collections.sort(keyList2);

		return "ratings: "
				.concat(getAttributesString(keyList, ratingAttributes))
				.concat(" | numerical meta attributes: ")
				.concat(getAttributesString(keyList2, numericalMetaAttributes));
	}

	/**
	 * Gets a String representation of the passed attributes map for the passed attribute keys.
	 * @param keyList the attributes to look up in the passed map
	 * @param atts map of attributes
	 * @return String representation of attributes
	 */
	private String getAttributesString(List<? extends Object> keyList, Map<? extends Object,? extends Object> atts) {	
		StringBuilder sb = new StringBuilder();
		for (Object o : keyList) {
			sb.append(o.toString());
			sb.append(": ");
			sb.append(atts.get(o).toString());
			sb.append(";\t");
		}
		String s = sb.toString();

		if (s.length() == 0) {
			return "no_attributes";
		} else {
			return s.substring(0, s.length()-1);
		}
	}

	@Override
	public String getNominalAttributesString() {
		List<Object> keyList = new ArrayList<Object>(nominalMetaAttributes.keySet());
		Collections.sort(keyList, new ObjectComparator());
		return "nominalMetaAttributes: ".concat(getAttributesString(keyList, nominalMetaAttributes));
	}

	@Override
	public int compareTo(Node o) {
		return ((Long)this.getId()).compareTo((Long)o.getId());
	}

	@Override
	public Iterator<INode> getChildren() {
		return children.iterator();
	}

	@Override
	public boolean isChild(INode possibleChild) {
		return children.contains(possibleChild);
	}

	@Override
	public INode getParent() {
		return parent;
	}

	@Override
	public void addChild(INode child) {
		this.children.add(child);
		child.setParent(this);
	}

	@Override
	public boolean removeChild(INode child) {
		if (children.remove(child)) {
			child.setParent(null);
			return true;
		}
		return false;
	}

	@Override
	public INode setParent(INode parent) {
		INode prevP = this.parent;
		this.parent = parent;
		return prevP;
	}

	@Override
	public boolean isLeaf() {
		if (this.children.isEmpty()) return true;
		return false;
	}

	@Override
	public boolean isRoot() {
		if (this.parent == null) return true;
		return false;
	}

	@Override
	public void setNumericalMetaAttributes(Map<String, IAttribute> attributes) {
		dirtySet.add(this);
		this.numericalMetaAttributes = attributes;
	}

	@Override
	public void setRatingAttributes(Map<INode, IAttribute> attributes) {
		dirtySet.add(this);
		this.ratingAttributes = attributes;
	}

	@Override
	public void setNominalMetaAttributes(Map<String, IAttribute> attributes) {
		dirtySet.add(this);
		this.nominalMetaAttributes = attributes;
	}

	@Override
	public Set<INode> getRatingAttributeKeys() {
		return Collections.unmodifiableSet(ratingAttributes.keySet());
	}

	@Override
	public Set<String> getNumericalMetaAttributeKeys() {
		return Collections.unmodifiableSet(numericalMetaAttributes.keySet());
	}

	@Override
	public Set<String> getNominalMetaAttributeKeys() {
		return Collections.unmodifiableSet(nominalMetaAttributes.keySet());
	}

	@Override
	public IAttribute getNumericalAttributeValue(Object att) {
		IAttribute res = ratingAttributes.get(att);
		if (res != null) return res;
		return numericalMetaAttributes.get(att);
	}

	@Override
	public IAttribute getNominalAttributeValue(Object att) {
		return nominalMetaAttributes.get(att);
	}

	@Override
	public boolean useAttributeForClustering(Object attribute) {
		ImmutableMap<String, Boolean> useForClustering = dataset.getAttributeClusteringConfig();
		if (useForClustering == null) return true;
		Boolean r = useForClustering.get(attribute);
		if (r != null) {
			return r;
		}
		return true;
	}

	@Override
	public String toString() {
		return getNodeType().toString().concat(" Node").concat(" ").concat(String.valueOf(id));
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void addNominalMetaAttribute(String key, IAttribute value) {
		dirtySet.add(this);
		nominalMetaAttributes.put(key, value);		
	}

	@Override
	public void addNumericalMetaAttribute(String key, IAttribute value) {
		dirtySet.add(this);
		numericalMetaAttributes.put(key, value);		
	}

	@Override
	public void addRatingAttribute(INode node, IAttribute attribute) {
		dirtySet.add(this);
		ratingAttributes.put(node, attribute);		
	}

	@Override
	public ENodeType getNodeType() {
		return nodeType;
	}

	@Override
	public boolean hasAttribute(Object attribute) {
		if (ratingAttributes.containsKey(attribute)) {
			return true;
		}
		if (numericalMetaAttributes.containsKey(attribute)) {
			return true;
		}
		if (nominalMetaAttributes.containsKey(attribute)) {
			return true;
		}
		return false;
	}

	@Override
	public void removeAttribute(Object attribute) {
		dirtySet.add(this);
		ratingAttributes.remove(attribute);
		numericalMetaAttributes.remove(attribute);
		nominalMetaAttributes.remove(attribute);
	}

	@Override
	public int getChildrenCount() {
		return children.size();

	}

	@Override
	public int getNumberOfNodesInSubtree() {
		int res = children.size();
		for (INode child : children) {
			res += child.getNumberOfNodesInSubtree();
		}
		return res;
	}

	@Override
	public int getNumberOfLeafNodes() {
		if (isLeaf()) return 1;
		int sum = 0;
		for (INode child : children) {
			sum += child.getNumberOfLeafNodes();
		}
		return sum;
	}

	@Override
	public JTree getJTreeOfSubtree() {
		DecimalFormat formatter = new DecimalFormat("#.####");
		return new JTree(getJTreeNode(formatter));
	}

	/**
	 * Creates the JTree of this nodes subtree recursively.
	 * 
	 * @param formatter number formatter to apply.
	 * @return the root of the JTree
	 */
	private DefaultMutableTreeNode getJTreeNode(final DecimalFormat formatter) {
		DefaultMutableTreeNode jNode = new DefaultMutableTreeNode(this) {
			
			/**
			 * Determines if a de-serialized file is compatible with this class.
			 * <br>
			 * <br>
			 * Maintainers must change this value if and only if the new version
			 * of this class is not compatible with old versions.
			 */
			private static final long serialVersionUID = 1L;
			
			@Override
			public String toString() {
				Node n = (Node)userObject;
				if (n.isLeaf()) {
					return n.getJTreeLeafString(formatter);
				} else {
					return "id: " + n.getId() + ", cluster size: " + n.getNumberOfLeafNodes() + ", merge category utility: " + formatter.format(n.getCategoryUtility());
				}
			}
		};
		Iterator<INode> it = getChildren();
		while (it.hasNext()) {
			jNode.add(((Node)it.next()).getJTreeNode(formatter));
		}
		return jNode;
	}

	/**
	 * Gets the string representation of the MutableTreeNode for this cluster.
	 * @param formatter the number formatter to apply
	 * @return the string to appear in the JTree
	 */
	private String getJTreeLeafString(final DecimalFormat formatter) {
		Pattern linkPattern = Pattern.compile("<a href.*>(.*)</a>");
		StringBuilder sb = new StringBuilder();
		sb.append("id: ");
		sb.append(getId());
		sb.append(" | ");

		Collection<IAttribute> atts = ratingAttributes.values();
		sb.append("num. of ratings: ");
		sb.append(atts.size());
		sb.append(" | ");
		sb.append("avg. of ratings: ");
		sb.append(formatter.format(ClassitMaxCategoryUtilitySearcher.calcSumOfRatingsOfAttribute(atts) / (double) atts.size()));	
		sb.append(" | ");
		sb.append("std. of ratings: ");
		sb.append(formatter.format(ClassitMaxCategoryUtilitySearcher.calcStdDevOfAttribute(atts)));
		sb.append(" | ");
		for (Map.Entry<String, IAttribute> entry : nominalMetaAttributes.entrySet()) {
			sb.append(entry.getKey());
			sb.append(": ");
			Iterator<Entry<Object,Double>> it = entry.getValue().getProbabilities();
			while (it.hasNext()) {
				Map.Entry<Object, Double> entry2 = it.next();
				Matcher matcher = linkPattern.matcher(entry2.getKey().toString());
				if (matcher.matches()) {
					sb.append(matcher.group(1));
				} else {
					sb.append(entry2.getKey());	
				}
				sb.append(", ");
			}
			sb.setLength(sb.length() - 2);
			sb.append(" | ");
		}
		for (Map.Entry<String, IAttribute> entry : numericalMetaAttributes.entrySet()) {
			sb.append(entry.getKey());
			sb.append(": ");			
			sb.append(formatter.format(dataset.denormalize(entry.getValue().getSumOfRatings(), entry.getKey())));
			sb.append(" | ");
		}
		sb.setLength(sb.length() - 2);
		return sb.toString();
	}

	@Override
	public String getAttributeHTMLLabelString() {
		DecimalFormat formatter = new DecimalFormat("#.####");
		StringBuilder description = new StringBuilder("<html><head><style>td { width:40px; border:1px solid black; text-align: center; }</style></head>");
		description.append("<body> Node: ");
		description.append(getId());
		description.append("<br>data set id: ");
		description.append(getSimpleDataSetIdString(this));
		description.append("<br>Category Utility: ");
		description.append(formatter.format(getCategoryUtility()));
		description.append("<br>Cluster size: ");
		description.append(getNumberOfLeafNodes());
		description.append("<br>");

		description.append("<h2>Rating Attributes</h2>");		
		if (ratingAttributes.size() > 0) {
			List<Node> atKeyLi = new ArrayList(ratingAttributes.keySet());
			Collections.sort(atKeyLi);	
			description.append(createRatingsAttributesHTMLTable(atKeyLi, formatter));
		}
		description.append("<h2>Numerical Meta Attributes</h2>");
		if (numericalMetaAttributes.size() > 0) {
			List<String> atKeyLi = new ArrayList<>(numericalMetaAttributes.keySet());
			Collections.sort(atKeyLi);	
			description.append(createNumericalMetaAttributesHTMLTable(atKeyLi, formatter));
		}
		description.append("<h2>Nominal Meta Attributes</h2>");
		if (nominalMetaAttributes.size() > 0) {
			List<String> atKeyLi = new ArrayList<>(nominalMetaAttributes.keySet());
			Collections.sort(atKeyLi, new ObjectComparator());
			description.append(createNominalMetaAttributesHTMLTable(atKeyLi, formatter));
		}
		return description.append("</body></html>").toString();
	}

	/**
	 * Gets a HTML-String of this nodes numerical meta attributes for the passed attribute keys.
	 * @param attributes the attributes to include in the table
	 * @param formatter formats the value
	 * @return table as HTML-String
	 */
	private String createNumericalMetaAttributesHTMLTable(List<String> attributes, DecimalFormat formatter) {
		// Header
		StringBuilder description = new StringBuilder("<table><tr><td>tag</td><td>mean</td><td>std</td><td>support</td></tr>");
		// Data
		for(String attributeKey : attributes) {

			IAttribute attributeValue = getNumericalAttributeValue(attributeKey);
			description.append( "<tr><td>").append(attributeKey.toString()).append("</td>");
			if (useAttributeForClustering(attributeKey)) {

				description.append("<td>")
				.append(formatter.format(attributeValue.getSumOfRatings()/attributeValue.getSupport()))
				.append("</td>")
				.append("<td>")
				.append(formatter.format(ClassitMaxCategoryUtilitySearcher.calcStdDevOfAttribute(ratingAttributes.values())))
				.append("</td>")
				.append("<td>")
				.append(attributeValue.getSupport())
				.append("</td>").append("</tr>");
			} else {
				description.append("<td>- not used for clustering -</td></tr>");
			}
		}
		return description.append("</table>").toString();
	}

	/**
	 * Gets a HTML-String of this nodes nominal meta attributes for the passed attribute keys.
	 * @param attributes the attributes to include in the table
	 * @param formatter formats the value
	 * @return table as HTML-String
	 */
	private String createNominalMetaAttributesHTMLTable(List<String> attributes, DecimalFormat formatter) {
		// Header
		StringBuilder description = new StringBuilder("<table><tr><td>tag</td><td width='150'>value -> probability</td></tr>");

		// Data
		for(String attributeKey : attributes) {

			IAttribute attributeValue = getNominalAttributeValue(attributeKey);
			description.append("<tr><td>").append(attributeKey.toString()).append("</td>");

			Iterator<Entry<Object,Double>> values = attributeValue.getProbabilities();
			if (! useAttributeForClustering(attributeKey)) {
				description.append("<td>- not used for clustering -<br>");
			} else {
				description.append("<td>");
			}

			while ( values.hasNext() ){
				Entry<Object,Double> tempEntry = values.next();
				description.append(tempEntry.getKey().toString());
				if (useAttributeForClustering(attributeKey)) {
					description.append(" -> ").append(formatter.format(tempEntry.getValue().doubleValue())); 
				}
				description.append("<br>");
			}
			description.append("</td></tr>");
		}
		return description.append("</table>").toString();
	}
	
	/**
	 * Gets a HTML-String of this nodes ratings attributes for the passed attribute keys.
	 * @param attributes the attributes to include in the table
	 * @param formatter formats the value
	 * @return table as HTML-String
	 */
	private String createRatingsAttributesHTMLTable(List<? extends INode> attributes, DecimalFormat formatter) {
		// Header
		StringBuilder description = new StringBuilder("<table><tr><td>cluster id</td><td>data set id</td><td>type</td><td>mean</td><td>std</td><td>support</td></tr>");
		// Data
		for(INode attributeKey : attributes) {

			IAttribute attributeValue = getNumericalAttributeValue(attributeKey);
			description.append("<tr><td>").append(attributeKey.getId()).append("</td>")
			.append("<td>").append(getSimpleDataSetIdString(attributeKey)).append("</td>")
			.append("<td>").append(attributeKey.getNodeType()).append("</td>")
			.append("<td>").append(formatter.format(attributeValue.getSumOfRatings()/attributeValue.getSupport())).append("</td>")
			.append("<td>").append(formatter.format(ClassitMaxCategoryUtilitySearcher.calcStdDevOfAttribute(ratingAttributes.values()))).append("</td>")
			.append("<td>").append(attributeValue.getSupport()).append("</td>")
			.append("</tr>");
		}
		return description.append("</table>").toString();
	}

	/**
	 * Gets a string representation of the data set id's contained in the cluster.
	 * @param node the cluster to query
	 * @return a string representation of the data set items contained in the cluster.
	 * The representation is of the form "data set item" for leaves, "data set item 1, data set item 2" 
	 * for clusters of size two and "data set item 1, data set item 2, ..." for 
	 * cluster with size > 2.
	 */
	private String getSimpleDataSetIdString(INode node) {
		String dataSetIdsString = "";
		List<String> ids = node.getDataSetIds();
		if (ids.size() > 1) {
			dataSetIdsString = ids.get(0) + ", " + ids.get(1) + " ...";
		}
		if (node.getDataSetIds().size() == 1) {
			dataSetIdsString = ids.get(0).toString();
		}
		return dataSetIdsString;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public List<String> getDataSetIds() {
		List<String> li = new ArrayList<String>();
		return getDataSetIds(li);
	}

	/**
	 * Gets recursively a list with all data set id's of
	 * data set items contained in this cluster. 
	 * 
	 * @param li the list to expand
	 * @return a list with all data set id's contained in the cluster
	 */
	private List<String> getDataSetIds(List<String> li) {
		if (isLeaf()) {
			li.add(dataSetId);
		} else {
			for (INode child : children) {
				((Node)child).getDataSetIds(li);
			}
		}
		return li;
	}

	@Override
	public double getCategoryUtility() {
		return categoryUtility;
	}

	@Override
	public boolean isDirty() {
		return dirtySet.contains(this);
	}

	@Override
	public void setClean() {
		dirtySet.remove(this);

	}

	/**
	 * Gets the set of dirty nodes.
	 * @return all nodes with recently changed attributes.
	 */
	public static Set<INode> getAllDirtyNodes() {
		return dirtySet;
	}

	@Override
	public String getDatasetId() {
		return dataSetId;
	}

	private class ObjectComparator implements Comparator<Object> {
		@Override
		public int compare(Object o1, Object o2) {			
			if (o1 instanceof Comparable && o2 instanceof Comparable) {
				if (o1.getClass().equals(o2.getClass())) {
					Comparable c1 = (Comparable) o1;
					Comparable c2 = (Comparable) o2;
					return c1.compareTo(c2);
				}
			}
			return 0;
		};
	}

	@Override
	public ImmutableMap<String, Boolean> getClusteringControlMap() {
		return dataset.getAttributeClusteringConfig();
	}

	@Override
	public IDataset getDataset() {
		return dataset;
	}

}