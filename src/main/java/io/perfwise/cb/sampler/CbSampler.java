package io.perfwise.cb.sampler;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.client.java.Cluster;

import java.io.Serializable;

public class CbSampler extends AbstractTestElement implements Sampler, TestBean, ConfigMergabilityIndicator, TestStateListener, TestElement, Serializable, Searchable {

	private static final long serialVersionUID = 9112846706008433268L;
	private static Logger LOGGER = LoggerFactory.getLogger(CbSampler.class);
	
	private String servers;
	private String username;
	private String password;
	private Cluster cluster;
	

	@Override
	public void testStarted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void testStarted(String host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void testEnded() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void testEnded(String host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean applies(ConfigTestElement configElement) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SampleResult sample(Entry e) {
		// TODO Auto-generated method stub
		return null;
	}

}
