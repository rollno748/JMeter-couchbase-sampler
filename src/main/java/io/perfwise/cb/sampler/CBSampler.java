package io.perfwise.cb.sampler;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.Scope;
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
	private Bucket bucketObject;
	private Scope scope;
	private Collection collection;
	private String queryTypeValue;
	private String query;
	private String parameters;

	@Override
	public SampleResult sample(Entry e) {

		if (this.bucketObject == null){
			this.bucketObject = (Bucket) JMeterContextService.getContext().getVariables()
					.getObject(getBucketObject());
		}
		this.scope = bucketObject.scope((String) JMeterContextService.getContext().getVariables().getObject(getScope()));
		this.collection = scope.collection((String) JMeterContextService.getContext().getVariables().getObject(getCollection()));
		Operations operations = new Operations(this.scope, this.collection);


		if (JMeterContextService.getContext().getVariables().getObject(getQueryTypeValue().toLowerCase()).equals("n1ql")){


		}
		SampleResult result = new SampleResult();
		result.setSampleLabel(getName());
		result.setSamplerData(requestBody());
		result.setDataType(SampleResult.TEXT);
		result.setContentType("text/plain");
		result.setDataEncoding(StandardCharsets.UTF_8.name());


		result.sampleStart();
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
	public String getBucketObject() {
		return bucketObject;
	}

	public void setBucketObject(String bucketObject) {
		this.bucketObject = (Bucket) bucketObject;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
//		this.scope = scope;
		this.scope = bucketObject.scope((String) JMeterContextService.getContext().getVariables().getObject(getScope()));
	}


	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
//		this.collection = collection;
		this.collection = scope.collection((String) JMeterContextService.getContext().getVariables().getObject(getCollection()));
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


