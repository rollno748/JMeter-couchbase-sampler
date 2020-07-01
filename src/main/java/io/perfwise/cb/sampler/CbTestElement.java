package io.perfwise.cb.sampler;

import java.io.Serializable;

import org.apache.jmeter.gui.Searchable;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestStateListener;

public abstract class CbTestElement extends AbstractTestElement implements TestStateListener, TestElement, Serializable, Searchable {

	private static final long serialVersionUID = -4337462275829335043L;

}
