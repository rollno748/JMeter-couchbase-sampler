package io.perfwise.cb.sampler;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.Scope;
import com.couchbase.client.java.kv.MutationResult;
import com.couchbase.client.java.query.QueryResult;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.engine.util.ConfigMergabilityIndicator;
import org.apache.jmeter.gui.Searchable;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;

import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.threads.JMeterContextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class CBSampler extends AbstractTestElement implements Sampler, TestBean, ConfigMergabilityIndicator, TestStateListener, TestElement, Serializable, Searchable {

	private static final long serialVersionUID = 9112846706008433268L;
	private static Logger LOGGER = LoggerFactory.getLogger(CBSampler.class);

	private static final Set<String> APPLIABLE_CONFIG_CLASSES = new HashSet<>(
			Arrays.asList("org.apache.jmeter.config.gui.SimpleConfigGui"));

	private Cluster clusterObject;
	private Bucket bucketObject;
	private Scope scopeObject;
	private Collection collectionObject;

	private String bucket;
	private String scope;
	private String collection;
	private String queryTypeValue;
	private String query;
	private String parameters;

	private final int operationInt = CBSamplerBeanInfo.getQueryTypeValueAsInt(getQueryTypeValue());

	@Override
	public SampleResult sample(Entry e) {

		if (operationInt == 0 && this.clusterObject == null) {
			this.clusterObject = (Cluster) JMeterContextService.getContext().getVariables()
					.getObject("clusterObject");
			LOGGER.info("Cluster object ::: " + clusterObject);
		}
		if (operationInt > 0 && this.bucketObject == null) {
			this.bucketObject = (Bucket) JMeterContextService.getContext().getVariables()
					.getObject(getBucket());
			LOGGER.info("Bucket object ::: " + bucketObject);
			scopeObject = bucketObject.scope(getScope());
			collectionObject = (Collection) scopeObject.collection(getCollection());
		}

		SampleResult result = new SampleResult();
		result.setSampleLabel(getName());
		result.setSamplerData(requestBody());
		result.setDataType(SampleResult.TEXT);
		result.setContentType("text/plain");
		result.setDataEncoding(StandardCharsets.UTF_8.name());
		//Starting the measurement
		result.sampleStart();
		if(operationInt == 0){
			QueryResult res = new Operations(clusterObject).queryOperations(getQuery());
			result.setResponseData(result.toString(), StandardCharsets.UTF_8.name());
		}else{
			MutationResult res = new Operations(scopeObject, collectionObject).dataOperations(operationInt, getQuery());
			result.setResponseData(result.toString(), StandardCharsets.UTF_8.name());
		}

		result.sampleEnd();




//		if (isGzipCompression()) {
//			byteMsg = createEventCompressed(getMessage());
//		} else {
//			byteMsg = ByteString.copyFromUtf8(getMessage()).toByteArray();
//		}

		result.sampleStart();

		try {
//			template = createPubsubMessage(byteMsg, attributes);
//			publish(template, result);

		} catch (Exception ex) {
			LOGGER.info("Exception occurred while publishing message");
			result = handleException(result, ex);
		} finally {
			result.sampleEnd();
		}
		return result;
	}

	private String requestBody() {
		return null;
	}

	private SampleResult handleException(SampleResult result, Exception ex) {
		result.setResponseMessage("Message Publish Error");
		result.setResponseCode("500");
		result.setResponseData(
				String.format("Error in publishing message to PubSub topic : %s", ex.toString()).getBytes());
		result.setSuccessful(false);
		return result;
	}


	//		if (this.bucket == null) {
	//			this.bucket = (Bucket) JMeterContextService.getContext().getVariables().getObject(getBucketObject());
	//		}

	@Override
	public void testStarted() {
		int queryTypeInt = CBSamplerBeanInfo.getQueryTypeValueAsInt(getQueryTypeValue());
	}

	@Override
	public void testStarted(String host) {
	}

	@Override
	public void testEnded() {
	}

	@Override
	public void testEnded(String host) {
	}

	@Override
	public boolean applies(ConfigTestElement configElement) {
		String guiClass = configElement.getProperty(TestElement.GUI_CLASS).getStringValue();
		return APPLIABLE_CONFIG_CLASSES.contains(guiClass);
	}


	//Getters & Setters


	public Cluster getClusterObject() {
		return clusterObject;
	}

	public void setClusterObject(Cluster clusterObject) {
		this.clusterObject = clusterObject;
	}

	public Bucket getBucketObject() {
		return bucketObject;
	}

	public void setBucketObject(Bucket bucketObject) {
		this.bucketObject = bucketObject;
	}

	public Scope getScopeObject() {
		return scopeObject;
	}

	public void setScopeObject(Scope scopeObject) {
		this.scopeObject = scopeObject;
	}

	public Collection getCollectionObject() {
		return collectionObject;
	}

	public void setCollectionObject(Collection collectionObject) {
		this.collectionObject = collectionObject;
	}

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getQueryTypeValue() {
		return queryTypeValue;
	}

	public void setQueryTypeValue(String queryTypeValue) {
		this.queryTypeValue = queryTypeValue;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
}


