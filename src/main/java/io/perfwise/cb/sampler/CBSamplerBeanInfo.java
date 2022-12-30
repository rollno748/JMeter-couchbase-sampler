package io.perfwise.cb.sampler;

import io.perfwise.cb.config.BucketConfigBeanInfo;
import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jmeter.testbeans.gui.TypeEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.stream.Collectors;


public class CBSamplerBeanInfo extends BeanInfoSupport {

	private static Logger LOGGER = LoggerFactory.getLogger(BucketConfigBeanInfo.class);
	private static final String BUCKET_OBJECT="bucketObject";
	private static final String SCOPE="scope";
	private static final String COLLECTION="collection";
	private static final String QUERYTYPE_VALUE= "queryTypeValue";
	private static final String QUERY="query";
	private static final String PARAMS= "parameters";
	private static final String[] QUERYTYPEVALUE_TAGS = new String[5];
	static final int N1QL = 0;
	static final int INSERT = 1;
	static final int GET = 2;
	static final int UPSERT = 3;
	static final int REMOVE = 4;


	static {
		QUERYTYPEVALUE_TAGS[N1QL] = "queryTypeValue.n1ql";
		QUERYTYPEVALUE_TAGS[INSERT] = "queryTypeValue.insert";
		QUERYTYPEVALUE_TAGS[GET] = "queryTypeValue.get";
		QUERYTYPEVALUE_TAGS[UPSERT] = "queryTypeValue.upsert";
		QUERYTYPEVALUE_TAGS[REMOVE] = "queryTypeValue.remove";
		//collection.insert();		collection.get();		collection.upsert();		collection.remove();
	}

	public CBSamplerBeanInfo() {
		super(CBSampler.class);

		createPropertyGroup("Bucket Configurations", new String[] { BUCKET_OBJECT, SCOPE, COLLECTION });
		createPropertyGroup("Query Arena", new String[] { QUERYTYPE_VALUE, QUERY, PARAMS });

		PropertyDescriptor bucketPropDescriptor = property(BUCKET_OBJECT);
		bucketPropDescriptor.setValue(NOT_UNDEFINED, Boolean.TRUE);
		bucketPropDescriptor.setValue(DEFAULT, "travel-sample-bucket-object");
		bucketPropDescriptor.setDisplayName("Bucket Object");
		bucketPropDescriptor.setShortDescription("Bucket Object reference name");

		bucketPropDescriptor = property(SCOPE);
		bucketPropDescriptor.setValue(NOT_UNDEFINED, Boolean.TRUE);
		bucketPropDescriptor.setValue(DEFAULT, "scope");
		bucketPropDescriptor.setDisplayName("Scope");
		bucketPropDescriptor.setShortDescription("Bucket scope");

		bucketPropDescriptor = property(COLLECTION);
		bucketPropDescriptor.setValue(NOT_UNDEFINED, Boolean.TRUE);
		bucketPropDescriptor.setValue(DEFAULT, "collection");
		bucketPropDescriptor.setDisplayName("Collection");
		bucketPropDescriptor.setShortDescription("Bucket collection");
		
		PropertyDescriptor queryPropDescriptor =  property(QUERYTYPE_VALUE, TypeEditor.ComboStringEditor);
		queryPropDescriptor.setValue(RESOURCE_BUNDLE, getBeanDescriptor().getValue(RESOURCE_BUNDLE));
		queryPropDescriptor.setValue(NOT_UNDEFINED, Boolean.TRUE);
		queryPropDescriptor.setValue(DEFAULT, QUERYTYPEVALUE_TAGS[N1QL]);
		queryPropDescriptor.setValue(NOT_OTHER, Boolean.FALSE);
		queryPropDescriptor.setValue(NOT_EXPRESSION, Boolean.FALSE);
		queryPropDescriptor.setValue(TAGS, QUERYTYPEVALUE_TAGS);
		queryPropDescriptor.setDisplayName("Query Type");
		queryPropDescriptor.setShortDescription("Select the query type - kv Pair / N1QL");

//		queryPropDescriptor =  property(QUERY);
		queryPropDescriptor = property(QUERY, TypeEditor.TextAreaEditor);
		queryPropDescriptor.setValue(NOT_UNDEFINED, Boolean.TRUE);
		queryPropDescriptor.setValue(TEXT_LANGUAGE, "sql");
		queryPropDescriptor.setValue(DEFAULT, "query");
		queryPropDescriptor.setDisplayName("Query");
		queryPropDescriptor.setShortDescription("Select the query type - kv Pair / N1QL");

		queryPropDescriptor = property(PARAMS);
		queryPropDescriptor.setValue(NOT_UNDEFINED, Boolean.TRUE);
		queryPropDescriptor.setValue(DEFAULT, "");
		queryPropDescriptor.setDisplayName("Parameters");
		queryPropDescriptor.setShortDescription("Parameters for N1ql query - with , delimiter");

		if (LOGGER.isDebugEnabled()) {
			String pubDescriptorsAsString = Arrays.stream(getPropertyDescriptors())
					.map(pd -> pd.getName() + "=" + pd.getDisplayName()).collect(Collectors.joining(" ,"));
			LOGGER.debug(pubDescriptorsAsString);
		}
	}

	public static int getQueryTypeValueAsInt(String mode) {
		if (mode == null || mode.length() == 0) {
			return N1QL;
		}
		for (int i = 0; i < QUERYTYPEVALUE_TAGS.length; i++) {
			if (QUERYTYPEVALUE_TAGS[i].equals(mode)) {
				return i;
			}
		}
		return -1;
	}

	public static String[] getQueryTypeValueTags() {
		String[] copy = new String[QUERYTYPEVALUE_TAGS.length];
		System.arraycopy(QUERYTYPEVALUE_TAGS, 0, copy, 0, QUERYTYPEVALUE_TAGS.length);
		return copy;
	}
}
