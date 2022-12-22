package io.perfwise.cb.config;

import java.io.Serializable;
import java.time.Duration;
import java.util.List;

import com.couchbase.client.java.*;
import io.perfwise.cb.utils.VariableSettings;
import org.apache.jmeter.config.ConfigElement;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testbeans.TestBeanHelper;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.core.error.CouchbaseException;

import static com.couchbase.client.java.ClusterOptions.clusterOptions;

public class BucketConfig extends ConfigTestElement
		implements ConfigElement, TestBean, TestStateListener, Serializable {

	private static final long serialVersionUID = -313826245020004586L;

	private static Logger LOGGER = LoggerFactory.getLogger(BucketConfig.class);

	private Cluster cluster;
	private Bucket bucket;
	private String username;
	private String password;
	private String server;
	private String bucketName;
	private String bucketWaitUntilReadyTime;
	private String bucketObject;
	private List<VariableSettings> extraConfigs;

	public BucketConfig() {
	}
	@Override
	public void addConfigElement(ConfigElement config) {
	}

	@Override
	public void testStarted() {
		this.setRunningVersion(true);
		TestBeanHelper.prepare(this);
		JMeterVariables variables = getThreadContext().getVariables();

		LOGGER.debug("Additional Cofig Size::: " + getExtraConfigs().size());
		if (getExtraConfigs().size() >= 1) {
			LOGGER.info("Setting up Additional properties");
			for (int i = 0; i < getExtraConfigs().size(); i++) {
//				props.put(getExtraConfigs().get(i).getConfigKey(),
//						getExtraConfigs().get(i).getConfigValue());
				LOGGER.debug(
						String.format("Adding property : %s", getExtraConfigs().get(i).getConfigKey()));
			}
		}

		if (variables.getObject(bucketObject) != null) {
			LOGGER.error("Found an active Couchbase connection !!");
		} else {
			synchronized (this) {
				try {
					cluster = Cluster.connect("couchbases://"+ getServer(), getClusterOptions(getUsername(), getPassword()));
					LOGGER.info("Connection to couchbase cluster has been established");
					try {
						bucket = cluster.bucket(bucketName);
						bucket.waitUntilReady(Duration.ofSeconds(Integer.parseInt(getBucketWaitUntilReadyTime())));

//						//// Get a user defined collection reference
//						Scope scope = bucket.scope("tenant_agent_00");
//						Collection collection = scope.collection("users");
//						// Get Document
//						GetResult getResult = collection.get("0");
//						String name = getResult.contentAsObject().getString("name");
//						LOGGER.info("GET ::::::" + name); // name == "mike"
//
//						// Call the query() method on the scope object and store the result.
//						Scope inventoryScope = bucket.scope("inventory");
//						QueryResult result = inventoryScope.query("SELECT * FROM airline WHERE id = 10;");
//						List<JsonObject> test = result.rowsAsObject();
//						LOGGER.info("RESULT ::::::" + test.get(0));

						variables.putObject(bucketObject, bucket);
					} catch (CouchbaseException e) {
						LOGGER.info("Exception while creating connection to bucket:" + e);
					}
				} catch (Exception e) {
					LOGGER.info("Failed to create couchbase cluster", e);
					e.printStackTrace();
				}
			}
		}
	}

	public ClusterOptions getClusterOptions(String username, String password){
		return clusterOptions(username, password).environment(env -> {
			env.applyProfile("wan-development");
		});
	}

	@Override
	public void testStarted(String host) {
		testStarted();
	}

	@Override
	public void testEnded() {
		try {
			cluster.disconnect();
		} catch (Exception e) {
			LOGGER.info("Exception occurred while closing the connection with couchbase server");
		}
	}

	@Override
	public void testEnded(String host) {
		testEnded();
	}

// ====== Getters and Setters

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public Bucket getBucket() {
		return bucket;
	}

	public void setBucket(Bucket bucket) {
		this.bucket = bucket;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getBucketWaitUntilReadyTime() {
		return bucketWaitUntilReadyTime;
	}

	public void setBucketWaitUntilReadyTime(String bucketWaitUntilReadyTime) {
		this.bucketWaitUntilReadyTime = bucketWaitUntilReadyTime;
	}

	public List<VariableSettings> getExtraConfigs() {
		return extraConfigs;
	}

	public void setExtraConfigs(List<VariableSettings> extraConfigs) {
		this.extraConfigs = extraConfigs;
	}

	public String getBucketObject() {
		return bucketObject;
	}

	public void setBucketObject(String bucketObject) {
		this.bucketObject = bucketObject;
	}

}
