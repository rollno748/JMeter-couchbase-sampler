package io.perfwise.cb.sampler;

import com.couchbase.client.core.error.CouchbaseException;
import com.couchbase.client.core.error.DocumentExistsException;
import com.couchbase.client.core.error.DocumentNotFoundException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.Scope;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.GetResult;
import com.couchbase.client.java.kv.InsertOptions;
import com.couchbase.client.java.kv.MutationResult;
import com.couchbase.client.java.kv.UpsertOptions;
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
import java.time.Duration;
import java.util.*;

import static com.couchbase.client.java.query.QueryOptions.queryOptions;


public class CBSampler extends AbstractTestElement implements Sampler, TestBean, ConfigMergabilityIndicator, TestStateListener, TestElement, Serializable, Searchable {

	private static final long serialVersionUID = 9112846706008433268L;
	private static final Logger LOGGER = LoggerFactory.getLogger(CBSampler.class);

	private static final Set<String> APPLIABLE_CONFIG_CLASSES = new HashSet<>(
			Arrays.asList("org.apache.jmeter.config.gui.SimpleConfigGui"));

	private Cluster clusterObject;
	private Bucket bucket;
	private Scope scopeObject;
	private Collection collectionObject;
	private String bucketObject;
	private String scope;
	private String collection;
	private String queryTypeValue;
	private String query;
	private String parameters;
	private int queryTypeInt = 0;
	private final long DEFAULT_TIMEOUT=300000; // 5 mins
	private HashMap<String, Object> map = new HashMap<>();

	@Override
	public SampleResult sample(Entry e) {
		this.queryTypeInt = CBSamplerBeanInfo.getQueryTypeValueAsInt(getQueryTypeValue());
		SampleResult result = new SampleResult();

		if(map.size()<2){
			this.map = (HashMap<String, Object>) JMeterContextService.getContext().getVariables().getObject(getBucketObject());
		}
		if (this.clusterObject == null && queryTypeInt == 0) {
			this.clusterObject = (Cluster) map.get("cluster");
			LOGGER.info("Cluster object ::: " + clusterObject);
		}
		if (this.bucket == null && queryTypeInt > 0) {
			this.bucket = (Bucket) map.get("bucket");
			LOGGER.info("Bucket object ::: " + bucket);
			scopeObject = bucket.scope(getScope());
			collectionObject = scopeObject.collection(getCollection());
		}

		if (this.clusterObject != null || this.bucket != null) {
			result.setSampleLabel(getName());
			result.setRequestHeaders("Method : " + getQueryTypeValue());
			result.setSamplerData(getQuery());
			result.setDataType(SampleResult.TEXT);
			result.setContentType("text/plain");
			result.setDataEncoding(StandardCharsets.UTF_8.name());

			//Starting the measurement
			result.sampleStart();
			if(queryTypeInt == 0){
				this.queryOperations(getQuery(), result);
			}else{
				this.dataOperations(queryTypeInt, getQuery(), result);
			}

		} else {
			LOGGER.info("Couchbase Client not initialised");
			result.setResponseCode("400");
		}
		result.sampleEnd(); //End timer for RT
		return result;
	}

	@Override
	public void testStarted() {
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

	public SampleResult dataOperations(int type, String data, SampleResult result){
		JsonObject content = null;
		MutationResult mutationResult;
		String key = "";
		if(type == 1 || type == 3 ){
			content = JsonObject.fromJson(data);
			Set<String> keys = content.getNames();
			for(String k : keys){
				key = k;
			}
		}

		switch (type){
			case CBSamplerBeanInfo.INSERT:
				try{
					mutationResult = collectionObject.insert(key, content.get(key), InsertOptions
							.insertOptions()
							.expiry(Duration.ofSeconds(DEFAULT_TIMEOUT)));
					result.setResponseData(mutationResult.toString(), StandardCharsets.UTF_8.name());
					result.setResponseOK();
				}catch (DocumentExistsException ex){
					LOGGER.info("The document already exists!");
					this.responseExceptionHandler(ex, result);
				}catch (CouchbaseException ex) {
					LOGGER.info("Something else happened: " + ex);
					this.responseExceptionHandler(ex, result);
				}
				break;
			case CBSamplerBeanInfo.GET:
				try{
					GetResult getResult = collectionObject.get(data);
					result.setResponseData(getResult.contentAsObject().toString(), StandardCharsets.UTF_8.name());
					result.setResponseOK();
				} catch (DocumentNotFoundException dnfe){
					LOGGER.info("Document not found!");
					this.responseExceptionHandler(dnfe, result);
				}
				break;
			case CBSamplerBeanInfo.UPSERT:
				try{
					mutationResult = collectionObject.upsert(key, content.get(key), UpsertOptions
									.upsertOptions()
									.expiry(Duration.ofSeconds(DEFAULT_TIMEOUT)));
					result.setResponseData(mutationResult.toString(), StandardCharsets.UTF_8.name());
					result.setResponseOK();
				}catch (CouchbaseException ex){
					LOGGER.info("Couchbase exception occurred!");
					this.responseExceptionHandler(ex, result);
				}
				break;
			case CBSamplerBeanInfo.REMOVE:
				try{
					mutationResult = collectionObject.remove(data);
					result.setResponseData(mutationResult.toString(), StandardCharsets.UTF_8.name());
					result.setResponseOK();
				}catch (DocumentNotFoundException dnfe){
					LOGGER.error("Document not found exception " + dnfe);
					this.responseExceptionHandler(dnfe, result);
				}
				break;
			default:
				LOGGER.info("Invalid operation Selected - Please check the sampler operation selection");
				LOGGER.info("Aborting Test..");
				testEnded();
		}

		return result;
	}

	private SampleResult responseExceptionHandler(Exception e,
												  SampleResult result) {
		LOGGER.info("Exception Occurred");
		result.setSuccessful(false);
		result.setResponseCode("500");
		result.setResponseMessage("Exception: " + e);
		java.io.StringWriter stringWriter = new java.io.StringWriter();
		e.printStackTrace(new java.io.PrintWriter(stringWriter));
		result.setResponseData(stringWriter.toString().getBytes());
		result.setDataType(org.apache.jmeter.samplers.SampleResult.TEXT);
		return result;
	}

	public SampleResult queryOperations(String data, SampleResult result){
		try{
			QueryResult queryResult = clusterObject.query(replaceParametersIfExist(data), queryOptions()
					.metrics(true)
					.readonly(true)
					.timeout(Duration.ofSeconds(DEFAULT_TIMEOUT)));
			result.setResponseData(queryResult.rowsAsObject().toString());
			result.setResponseOK();
		}catch (CouchbaseException ce){
			LOGGER.info("Couchbase exception occurred while w=executing N1ql query ");
			this.responseExceptionHandler(ce, result);
			ce.printStackTrace();
		}
		return result;
	}

	private String replaceParametersIfExist(String data) {
		if(!getParameters().isEmpty()){
			String[] parameters = getParameters().split(",");
			StringBuilder sb = new StringBuilder();
			char param = '?';
			int paramIdx = 0;

			for (int i = 0; i < data.length(); i++) {
				if (data.charAt(i) == param) {
					sb.append(parameters[paramIdx]);
					paramIdx++;
				}else {
					sb.append(data.charAt(i));
				}
			}
			return sb.toString();
		}
		return data;
	}

	//Getters & Setters
	public Cluster getClusterObject() {
		return clusterObject;
	}

	public String getBucketObject() {
		return bucketObject;
	}

	public void setBucketObject(String bucketObject) {
		this.bucketObject = bucketObject;
	}

	public Scope getScopeObject() {
		return scopeObject;
	}

	public Collection getCollectionObject() {
		return collectionObject;
	}

	public Bucket getBucket() {
		return bucket;
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
