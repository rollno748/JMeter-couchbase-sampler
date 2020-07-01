package io.perfwise.cb.config;

import java.io.Serializable;

import org.apache.jmeter.config.ConfigElement;
import org.apache.jmeter.config.ConfigTestElement;
import org.apache.jmeter.testbeans.TestBean;
import org.apache.jmeter.testbeans.TestBeanHelper;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jmeter.threads.JMeterContextService;
import org.apache.jmeter.threads.JMeterVariables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.core.error.CouchbaseException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;

public class BucketConfig extends ConfigTestElement
		implements ConfigElement, TestBean, TestStateListener, Serializable {

	private static final long serialVersionUID = -313826245020004586L;

	private static Logger LOGGER = LoggerFactory.getLogger(BucketConfig.class);

	private Cluster cluster;
	private Bucket bucket;
	private Collection collection;

	private String username;
	private String password;
	private String server;
	private String bucketName;
	private String bucketObject;

	public BucketConfig() {
	}

	@Override
	public void testStarted() {
		this.setRunningVersion(true);
		TestBeanHelper.prepare(this);
		JMeterVariables variables = JMeterContextService.getContext().getVariables();

		if (variables.getObject(bucketObject) != null) {
			LOGGER.error("Couchbase connection is already established and active !!");
		} else {
			synchronized (this) {
				try {
					cluster = Cluster.connect(server, username, password);
					LOGGER.info("CouchbaseCluster Created");
				} catch (Exception e) {
					LOGGER.info("Failed to create couchbase cluster", e);
					e.printStackTrace();
				}
			}
		}

		try {
			bucket = cluster.bucket(bucketName);
			collection = bucket.defaultCollection();
			variables.putObject(bucketObject, collection);
		} catch (CouchbaseException e) {
			LOGGER.info("Exception while creating bucket :" + e);

		}
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
			LOGGER.info("Exception occured while closeing the connection with couchbase server");
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

	public String getBucketObject() {
		return bucketObject;
	}

	public void setBucketObject(String bucketObject) {
		this.bucketObject = bucketObject;
	}

}
