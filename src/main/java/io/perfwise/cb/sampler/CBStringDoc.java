package io.perfwise.cb.sampler;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.java.Collection;
import com.couchbase.client.java.kv.GetOptions;
import com.couchbase.client.java.kv.GetResult;
import com.couchbase.client.java.kv.InsertOptions;
import com.couchbase.client.java.kv.MutationResult;
import com.couchbase.client.java.kv.RemoveOptions;
import com.couchbase.client.java.kv.UpsertOptions;

public class CBStringDoc extends AbstractJavaSamplerClient implements Serializable {

	private static Logger LOGGER = LoggerFactory.getLogger(CBStringDoc.class);
	private static final long serialVersionUID = -1423424536752659921L;

	private Collection collection = null;

	// set up default arguments for the JMeter GUI
	@Override
	public Arguments getDefaultParameters() {
		Arguments defaultParameters = new Arguments();
		defaultParameters.addArgument("bucket", "bucketObject");
		defaultParameters.addArgument("method", "GET");
		defaultParameters.addArgument("key", "key");
		defaultParameters.addArgument("value", "val");
		defaultParameters.addArgument("expiry", "0");
		return defaultParameters;
	}

	@Override
	public void setupTest(JavaSamplerContext context) {
		JMeterVariables vars = JMeterContextService.getContext().getVariables();

		try {
			collection = (Collection) vars.getObject(context.getParameter("bucket"));
			LOGGER.info(String.format("The %s Bucket has been initialised successfully !!", collection.bucketName()));
		} catch (Exception ex) {
			LOGGER.error("Exception occured while getting the initialised bucket:" + ex);
		}

	}

	
	@Override
	public SampleResult runTest(JavaSamplerContext context) {

		// StringDocument cbDocument = null;
		String method = context.getParameter("method");
		int timeout = context.getIntParameter("expiry"); 
		
		GetResult queryRes = null;
		MutationResult queryResult  = null;
		SampleResult result = new SampleResult();
		result.sampleStart();

		if (collection != null) {
			result.setRequestHeaders(String.format("Key: %s; Value: %s", context.getParameter("key"), context.getParameter("value")));
			
			try {
				
				 switch(method.toLowerCase()){
				 case "upsert":
					 if (timeout > 0) {
						 queryResult = collection.upsert(context.getParameter("key"),
									context.getParameter("value"), UpsertOptions.upsertOptions().expiry(Duration.ofSeconds(timeout)));
					 }
					 queryResult = collection.upsert(context.getParameter("key"),
								context.getParameter("value"));
					 result.setResponseData(queryResult.toString(), StandardCharsets.UTF_8.name());
				   break;
				 case "insert":
					 if (timeout > 0) {
						 queryResult = collection.insert(context.getParameter("key"),
									context.getParameter("value"), InsertOptions.insertOptions().expiry(Duration.ofSeconds(timeout)));
					 }
					 queryResult = collection.insert(context.getParameter("key"),
								context.getParameter("value"));
					 result.setResponseData(queryResult.toString(), StandardCharsets.UTF_8.name());
					 
				   break;
				 case "get":
					 if (timeout > 0) {
						 queryRes = collection.get(context.getParameter("key"), GetOptions.getOptions().withExpiry(true));
					 }
					 	queryRes = collection.get(context.getParameter("key"));
					 result.setResponseData(queryRes.toString(), StandardCharsets.UTF_8.name());
				   break;
				 case "remove":
					 if (timeout > 0) {
						 queryResult = collection.remove(context.getParameter("key"), RemoveOptions.removeOptions().timeout(Duration.ofSeconds(timeout)));
					 }
					 queryResult = collection.remove(context.getParameter("key"));
					 result.setResponseData(queryResult.toString(), StandardCharsets.UTF_8.name());
			           break;
				 default:
					 LOGGER.info("Method type invalid");
			      }
				 
				 result.sampleEnd();
				 result.setResponseOK();
				 
			} catch(Exception e) {
				LOGGER.info("Exception Occurred");
				result.setSuccessful(false);
				result.setResponseCode("500");
				result.setResponseMessage("Exception: " + e);
				java.io.StringWriter stringWriter = new java.io.StringWriter();
				e.printStackTrace(new java.io.PrintWriter(stringWriter));
				result.setResponseData(stringWriter.toString().getBytes());
				result.setDataType(org.apache.jmeter.samplers.SampleResult.TEXT);	
				
			}
		} else {
			LOGGER.info("Couchbase Client not initialised");
			result.setResponseCode("400");
		}
			
		return result;
	}

	@Override
	public void teardownTest(JavaSamplerContext context) {

	}

}
