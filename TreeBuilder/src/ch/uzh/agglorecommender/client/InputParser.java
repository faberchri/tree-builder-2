package ch.uzh.agglorecommender.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.Trim;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.Unique;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import ch.uzh.agglorecommender.client.inputbeans.Attribute;
import ch.uzh.agglorecommender.client.inputbeans.Category;
import ch.uzh.agglorecommender.client.inputbeans.InputDocument;
import ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input;
import ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalAttribute;
import ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalMultivaluedAttribute;
import ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNumericalAttribute;
import ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.Rating;
import ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalAttribute;
import ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalMultivaluedAttribute;
import ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNumericalAttribute;
import ch.uzh.agglorecommender.client.inputbeans.MetaFile;
import ch.uzh.agglorecommender.client.inputbeans.NominalAttribute;
import ch.uzh.agglorecommender.client.inputbeans.NominalMultivaluedAttribute;
import ch.uzh.agglorecommender.client.inputbeans.NumericalAttribute;
import ch.uzh.agglorecommender.client.inputbeans.RatingFile;
import ch.uzh.agglorecommender.util.TBLogger;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;

public class InputParser {

	private static final String USER_ID = "user id";
	private static final String CONTENT_ID = "content id";
	private static final String META_ID = "meta id";
	private static final String VALUE = "value";

	private static final double SCALLING_FACTOR = 10.0;

	private List<IDatasetItem<Double>> trainingItems = new ArrayList<>();
	private List<IDatasetItem<Double>> testItems = new ArrayList<>();

	private final IDataset<Double> trainigsDataset;
	private final IDataset<Double> testDataset;


	private Map<String,ListMultimap<String, Object>> nominalUserMetaAttributes = new HashMap<>();
	private Map<String,ListMultimap<String, Object>> nominalContentMetaAttributes = new HashMap<>();

	private Map<String,ListMultimap<String, Double>> numericalUserMetaAttributes = new HashMap<>();
	private Map<String,ListMultimap<String, Double>> numericalContentMetaAttributes = new HashMap<>();

	private Map<String, Boolean> useForClustering = new HashMap<>();
	
	private Map<String, NumericalAttribute> numericalAttributes = new HashMap<>();

	public InputParser(File propertiesXmlFile) {


		InputDocument inputDoc = null;
		try {
			inputDoc = InputDocument.Factory.parse(propertiesXmlFile);
		} catch (XmlException | IOException e) {
			System.err.println("Error while parsing the data set property file: " + propertiesXmlFile.getPath());
			e.printStackTrace();
		}
		validateXML(inputDoc);

		Input input = inputDoc.getInput();

		parseRatings(input.getRatingArray());

		for (int i = 0; i < input.sizeOfContentNominalAttributeArray(); i++) {
			parseMetaAttribute(input.getContentNominalAttributeArray(i));
		}
		for (int i = 0; i < input.sizeOfContentNominalMultivaluedAttributeArray(); i++) {
			parseMetaAttribute(input.getContentNominalMultivaluedAttributeArray(i));
		}
		for (int i = 0; i < input.sizeOfContentNumericalAttributeArray(); i++) {
			ContentNumericalAttribute cna = input.getContentNumericalAttributeArray(i);
			parseMetaAttribute(cna);
			numericalAttributes.put(cna.getAttribute().getTag(), cna.getAttribute());
		}
		for (int i = 0; i < input.sizeOfUserNominalAttributeArray(); i++) {
			parseMetaAttribute(input.getUserNominalAttributeArray(i));
		}	
		for (int i = 0; i < input.sizeOfUserNominalMultivaluedAttributeArray(); i++) {
			parseMetaAttribute(input.getUserNominalMultivaluedAttributeArray(i));
		}
		for (int i = 0; i < input.sizeOfUserNumericalAttributeArray(); i++) {
			UserNumericalAttribute una = input.getUserNumericalAttributeArray(i);
			parseMetaAttribute(una);
			numericalAttributes.put(una.getAttribute().getTag(), una.getAttribute());
		}

		trainigsDataset = new Dataset(trainingItems);
		testDataset = new Dataset(testItems);
		
		useForClustering.remove(input.getRatingArray()[0].getAttribute().getTag());
	}

	public IDataset<Double> getTestDataset() {
		return testDataset;
	}

	public IDataset<Double> getTrainigsDataset() {
		return trainigsDataset;
	}

	private void validateXML(InputDocument inputDoc) {
		ArrayList validationErrors = new ArrayList();
		XmlOptions validationOptions = new XmlOptions();
		validationOptions.setErrorListener(validationErrors);
		boolean isValid = inputDoc.validate(validationOptions);
		if (!isValid) {
			Iterator iter = validationErrors.iterator();
			System.err.println("The following errors occured while validating the data set property file:");
			while (iter.hasNext())
			{
				System.err.println(">> " + iter.next() + "\n");
			}
			System.exit(-1);
		}
	}

	private void parseMetaAttribute(ContentNominalAttribute cNAttribute) {
		IWrappedAttribute wAttribute = new NominalAttributeWrapper(cNAttribute.getAttribute(), nominalContentMetaAttributes);
		ListMultimap<String, String> rawData = getRawData(cNAttribute.getFileArray(), wAttribute);
		wAttribute.collectData(rawData);
	}

	private void parseMetaAttribute(ContentNominalMultivaluedAttribute cNMAttribute) {
		IWrappedAttribute wAttribute = new NominalMultivaluedAttributeWrapper(cNMAttribute.getAttribute(), nominalContentMetaAttributes);
		ListMultimap<String, String> rawData = getRawData(cNMAttribute.getFileArray(), wAttribute);
		wAttribute.collectData(rawData);
	}

	private void parseMetaAttribute(ContentNumericalAttribute cNumAttribute) {
		IWrappedAttribute wAttribute = new NumericalAttributeWrapper(cNumAttribute.getAttribute(), numericalContentMetaAttributes);
		ListMultimap<String, String> rawData = getRawData(cNumAttribute.getFileArray(), wAttribute);
		wAttribute.collectData(rawData);
	}

	private void parseMetaAttribute(UserNominalAttribute uNAttribute) {
		IWrappedAttribute wAttribute = new NominalAttributeWrapper(uNAttribute.getAttribute(), nominalUserMetaAttributes);
		ListMultimap<String, String> rawData = getRawData(uNAttribute.getFileArray(), wAttribute);
		wAttribute.collectData(rawData);
	}

	private void parseMetaAttribute(UserNominalMultivaluedAttribute uNMAttribute) {
		IWrappedAttribute wAttribute = new NominalMultivaluedAttributeWrapper(uNMAttribute.getAttribute(), nominalUserMetaAttributes);
		ListMultimap<String, String> rawData = getRawData(uNMAttribute.getFileArray(), wAttribute);
		wAttribute.collectData(rawData);
	}

	private void parseMetaAttribute(UserNumericalAttribute uNumAttribute) {
		IWrappedAttribute wAttribute = new NumericalAttributeWrapper(uNumAttribute.getAttribute(), numericalUserMetaAttributes);
		ListMultimap<String, String> rawData = getRawData(uNumAttribute.getFileArray(), wAttribute);
		wAttribute.collectData(rawData);
	}

	private ListMultimap<String, String> getRawData(MetaFile[] files, IWrappedAttribute wAttribute) {
		ListMultimap<String, String> rawMetaAttribute = ArrayListMultimap.create();
		for (MetaFile metaFile : files) {
			ICsvMapReader reader = getMapReader(metaFile);
			try {
				reader.getHeader(true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(-1);
			}
			String[] header = createMetaHeader(metaFile, reader.length());
			printArray(header);
			reader = getMapReader(metaFile);
			CellProcessor[] processors = wAttribute.getProcessors(header);
			printArray(processors);
			putData(metaFile, rawMetaAttribute, reader, header, processors);	
		}
		checkForColumnLengthEquality(rawMetaAttribute);
		return rawMetaAttribute;
	}

	private void parseRatings(Rating[] ratings) {
		for (Rating rating : ratings) {
			RatingFile[] files = rating.getFileArray();
			IWrappedAttribute wAttribute = new NumericalAttributeWrapper(rating.getAttribute(), null);
			ListMultimap<String, String> rawTrainingRatings = ArrayListMultimap.create();
			ListMultimap<String, String> rawTestRatings = ArrayListMultimap.create();
			for (RatingFile ratingFile : files) {
				Multimap<String, String> rawRatings;
				if (ratingFile.getUseForTestOnly()) {
					rawRatings = rawTestRatings;
				} else {
					rawRatings = rawTrainingRatings;
				}
				ICsvMapReader reader = getMapReader(ratingFile);
				try {
					reader.getHeader(true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(-1);
				}			
				String[] header = createRatingsHeader(ratingFile, reader.length());
				printArray(header);
				reader = getMapReader(ratingFile);
				CellProcessor[] processors = wAttribute.getProcessors(header);
				printArray(processors);
				putData(ratingFile, rawRatings, reader, header, processors);
			}
			checkForColumnLengthEquality(rawTestRatings);
			checkForColumnLengthEquality(rawTrainingRatings);
			mapRatings(rating.getAttribute(), rawTestRatings, testItems);
			mapRatings(rating.getAttribute(), rawTrainingRatings, trainingItems);
		}
	}

	private void printArray(Object [] array) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (Object object : array) {
			if (object == null) {
				sb.append("null");
			} else {
				sb.append(object.toString());
			}
			sb.append(", ");
		}
//		System.out.println(sb.substring(0, sb.length() - 2).concat("]"));
	}

	private String[] createMetaHeader(MetaFile metaFile, int headerLength) {
		Map<Integer, String> headerMap = new HashMap<Integer, String>();
		headerMap.put(metaFile.getIdColumnNumber().intValue() - 1, META_ID);
		headerMap.put(metaFile.getValueColumnNumber().intValue() - 1, VALUE);
		return createHeader(headerMap, headerLength);
	}

	private String[] createRatingsHeader(RatingFile ratingFile, int headerLength) {
		Map<Integer, String> headerMap = new HashMap<Integer, String>();
		headerMap.put(ratingFile.getUserIdColumnNumber().intValue() - 1, USER_ID);
		headerMap.put(ratingFile.getContentIdColumnNumber().intValue() - 1, CONTENT_ID);
		headerMap.put(ratingFile.getValueColumnNumber().intValue() - 1, VALUE);
		return createHeader(headerMap, headerLength);
	}

	private String[] createHeader(Map<Integer, String> headerMap, int length) {
		String[] header = new String[length];
		for (int i = 0; i < header.length; i++) {
			header[i] = headerMap.get(i);
		}
		return header;
	}

	private ICsvMapReader getMapReader(ch.uzh.agglorecommender.client.inputbeans.File file) {
		CsvPreference csvPreference = new CsvPreference.Builder('"', file.getColumnSeparator().charAt(0), "\n").build();
		ICsvMapReader mapReader = null;
		try {
			mapReader = new CsvMapReader(new FileReader(file.getLocation()), csvPreference);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
		return mapReader;
	}

	private Multimap<String, String> putData(ch.uzh.agglorecommender.client.inputbeans.File file, Multimap<String, String> rawData, ICsvMapReader reader, String[] header, CellProcessor[] processors) {
		try {
			Map<String, Object> tmpMap;
			while((tmpMap = reader.read(header, processors)) != null) {
				boolean addRec = true;
				if (file.isSetStartLine()) {
					if (reader.getLineNumber() < file.getStartLine().intValue())
						addRec = false;
				}
				if (file.isSetEndLine()) {
					if (reader.getLineNumber() > file.getEndLine().intValue())
						addRec = false;
				}
				if (addRec) {
					for (Map.Entry<String, Object> entry : tmpMap.entrySet()) {
						rawData.put(entry.getKey(), (String)entry.getValue());
					}					
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
		return rawData;
	}

	private void checkForColumnLengthEquality(ListMultimap<String, String> rawData) {
		List<Integer> lengths = new ArrayList<>();
		for (Collection<String> collection : rawData.asMap().values()) {
			lengths.add(collection.size());
		}
		for (int l : lengths) {
			if (l != lengths.get(0)) {
				throw new Error();
			}
		}
	}

	private void mapRatings(NumericalAttribute attribute, ListMultimap<String, String> rawRatings, List<IDatasetItem<Double>> items) {

		List<String> values = rawRatings.get(VALUE);
		List<String> uIds = rawRatings.get(USER_ID);
		List<String> cIds = rawRatings.get(CONTENT_ID);

		for (int i = 0; i < values.size(); i++) {
			String valueString = values.get(i);
			if (valueString == null) continue;
			String cleanedString = applyPreprocessing(valueString, attribute.getPreProcessingRegex());
			double value = convert(cleanedString);
			value = normalize(value, attribute.getMinValue(), attribute.getMaxValue());
			items.add(new DatasetItem(value, uIds.get(i), cIds.get(i)));
		}
	}

	private static double normalize(double value, double min, double max) {
		double diffToMin = value - min;
		double range = max - min;
		return (diffToMin / range) * SCALLING_FACTOR;
	}

	private static String applyPreprocessing(String o, String regex) {
		//TODO
		if (o == null || regex == null) {
			return o;
		}
		return o.replaceAll(regex, "");
	}

	private static Double convert(String value) {
		try {
			return Double.valueOf(value);
		} catch (NumberFormatException e) {
			TBLogger.getLogger(InputParser.class.getName()).severe("The string \"" + value + "\" is not convertable to Double!");
			System.exit(-1);
		}
		return null;
	}

	private interface IWrappedAttribute {
		public Attribute getAttribute();

		public CellProcessor[] getProcessors(String[] header);

		public void collectData(ListMultimap<String, String> rawData);

	}


	private class NominalAttributeWrapper implements IWrappedAttribute {

		protected final NominalAttribute attribute;
		protected Map<String,ListMultimap<String, Object>> mappedAttributes;

		public NominalAttributeWrapper(NominalAttribute attribute,
				Map<String,ListMultimap<String, Object>> mappedAttributes) {
			this.attribute = attribute;		
			this.mappedAttributes = mappedAttributes;

			useForClustering.put(attribute.getTag(), attribute.getUseForClustering());
		}

		@Override
		public Attribute getAttribute() {
			return attribute;
		}

		@Override
		public void collectData(ListMultimap<String, String> rawData) {
			List<String> idColumn = rawData.get(META_ID);
			List<String> valueColumn = rawData.get(VALUE);
			for (int i = 0; i < idColumn.size(); i++) {
				if (valueColumn.get(i) == null) continue;
				collectData(idColumn.get(i), valueColumn.get(i));
			}			
		}

		protected void collectData(String id, String value) {
			ListMultimap<String, Object> idLmm = mappedAttributes.get(id);
			if (idLmm == null) {
				idLmm = ArrayListMultimap.create();
			}
			String cleanedValue = applyPreprocessing(value, attribute.getPreProcessingRegex());
			String catSplitResult = applyCategorySplit(cleanedValue, attribute.getCategoryArray());
			idLmm.put(attribute.getTag(), catSplitResult);
			mappedAttributes.put(id, idLmm);
		}

		private String applyCategorySplit(String value, Category[] categories) {
			if (categories == null) return value;
			for (Category category : categories) {
				if (value.matches(category.getIdentificationRegex())) {
					return category.getCategoryTag();
				}
			}
			return value;
		}


		@Override
		public CellProcessor[] getProcessors(String[] header) {
			CellProcessor[] processors = new CellProcessor[header.length];
			for (int i = 0; i < header.length; i++) {
				if (header[i] == USER_ID || header[i] == CONTENT_ID) {
					processors[i] = new NotNull(new Trim());
				}
				if (header[i] == META_ID) {
					processors[i] = new Unique(new Trim());
				}
				if (header[i] == VALUE) {
					processors[i] = new Optional(new Trim());
				}
			}
			return processors;
		}

	}

	private class NominalMultivaluedAttributeWrapper extends NominalAttributeWrapper implements IWrappedAttribute {

		protected final NominalMultivaluedAttribute attribute;

		public NominalMultivaluedAttributeWrapper(NominalMultivaluedAttribute attribute,
				Map<String,ListMultimap<String, Object>> mappedAttributes) {
			super(attribute, mappedAttributes);
			this.attribute = attribute;
		}

		@Override
		public void collectData(ListMultimap<String, String> rawData) {
			List<String> idColumn = rawData.get(META_ID);
			List<String> valueColumn = rawData.get(VALUE);
			for (int i = 0; i < idColumn.size(); i++) {
				if (valueColumn.get(i) == null) continue;
				List<String> values = createValueTokens(valueColumn.get(i));
				for (String value : values) {
					collectData(idColumn.get(i), value);
				}				
			}			
		}

		private List<String> createValueTokens(String value) {
			List<String> res = new ArrayList<>();
			String[] tokens = value.split(attribute.getValueSeparator());
			for (String string : tokens) {
				string = string.trim();
				if (string != "") {
					res.add(string);
				}
			}
			return res;
		}
	}

	private class NumericalAttributeWrapper implements IWrappedAttribute {

		protected final NumericalAttribute attribute;
		protected Map<String,ListMultimap<String, Double>> mappedAttributes;

		public NumericalAttributeWrapper(NumericalAttribute attribute,
				Map<String,ListMultimap<String, Double>> mappedAttributes) {
			this.attribute = attribute;			
			this.mappedAttributes = mappedAttributes;

			useForClustering.put(attribute.getTag(), attribute.getUseForClustering());
		}

		@Override
		public Attribute getAttribute() {
			return attribute;
		}


		@Override
		public CellProcessor[] getProcessors(String[] header) {
			CellProcessor[] processors = new CellProcessor[header.length];
			for (int i = 0; i < header.length; i++) {
				if (header[i] == USER_ID || header[i] == CONTENT_ID) {
					processors[i] = new NotNull(new Trim());
				}
				if (header[i] == META_ID) {
					processors[i] = new Unique(new Trim());
				}
				if (header[i] == VALUE) {
					processors[i] = new Optional(new Trim());
				}
			}
			return processors;
		}

		@Override
		public void collectData(ListMultimap<String, String> rawData) {
			List<String> idColumn = rawData.get(META_ID);
			List<String> valueColumn = rawData.get(VALUE);
			for (int i = 0; i < idColumn.size(); i++) {
				String value = valueColumn.get(i);
				if (value == null) continue;
				String id = idColumn.get(i);
				ListMultimap<String, Double> idLmm = mappedAttributes.get(id);
				if (idLmm == null) {
					idLmm = ArrayListMultimap.create();
				}
				String cleanedValue = applyPreprocessing(value, attribute.getPreProcessingRegex());
				double v = convert(cleanedValue);
				v = normalize(v, attribute.getMinValue(), attribute.getMaxValue());

				idLmm.put(attribute.getTag(), v);
				mappedAttributes.put(id, idLmm);			
			}

		}

	}

	private class Dataset implements IDataset<Double> {

		private final List<IDatasetItem<Double>> items;

		private final INormalizer<Double> normalizer;

		public Dataset(List<IDatasetItem<Double>> items) {
			this.items = items;
			this.normalizer = new INormalizer<Double>() {

				@Override
				public double normalizeRating(Double rating) {
					// Nothing to do
					return rating;
				}
			};
		}

		@Override
		public Iterator<IDatasetItem<Double>> iterateOverDatasetItems() {
			return items.iterator();
		}

		@Override
		public INormalizer<Double> getNormalizer() {
			return normalizer;
		}

		@Override
		public ImmutableMap<String, Boolean> getAttributeClusteringConfig() {
			return ImmutableMap.copyOf(useForClustering);
		}

		@Override
		public double denormalize(double value, String attributeTag) {
			NumericalAttribute att = numericalAttributes.get(attributeTag);
			if (att == null) {
				return value;
			}
			double r = value / SCALLING_FACTOR;
			double range = att.getMaxValue() - att.getMinValue();
			r = r * range;
			r += att.getMinValue();
			return r;
		}

	}

	private class DatasetItem implements IDatasetItem<Double> {

		/**
		 * The rating.
		 */
		private final double value;

		/**
		 * The user id.
		 */
		private final String userId;

		/**
		 * The content id.
		 */
		private final String contentId;

		/**
		 * Instantiates a new {@code SimpleDataSetItem} which
		 * represents a user-content-rating combination.
		 * 
		 * @param value the raw rating.
		 * @param userId the id of the user of the rating.
		 * @param contentId the id of the rated content.
		 */
		public DatasetItem(double value,
				String userId,
				String contentId)  {
			this.value = value;
			this.userId = userId;
			this.contentId = contentId;
		}

		@Override
		public String getUserId() {
			return userId;
		}

		@Override
		public String getContentId() {
			return contentId;
		}

		@Override
		public Double getRating() {
			return value;
		}

		@Override
		public Multimap<String, Object> getNominalUserMetaMap() {
			return nominalUserMetaAttributes.get(userId);
		}

		@Override
		public Multimap<String, Object> getNominalContentMetaMap() {
			return nominalContentMetaAttributes.get(contentId);
		}

		@Override
		public Multimap<String, Double> getNumericalUserMetaMap() {
			return numericalUserMetaAttributes.get(userId);
		}

		@Override
		public Multimap<String, Double> getNumericalContentMetaMap() {
			return numericalContentMetaAttributes.get(contentId);
		}
		
		@Override
		public void addContentMetaData(String attribute, Object value) {
			// nothing to do 
		}

		@Override
		public void addUserMetaData(String attribute, Object value) {
			// nothing to do			
		}

	}
}