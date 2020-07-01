package io.perfwise.cb.config;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jmeter.testbeans.gui.TypeEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BucketConfigBeanInfo extends BeanInfoSupport {

	private static Logger LOGGER = LoggerFactory.getLogger(BucketConfigBeanInfo.class);

	public BucketConfigBeanInfo() {
		super(BucketConfig.class);

		createPropertyGroup("connection",
				new String[] { "server", "username", "password", "bucketName", "bucketObject" });

		PropertyDescriptor propertyDescriptor = property("server");
		propertyDescriptor.setValue(NOT_UNDEFINED, Boolean.TRUE);
		propertyDescriptor.setValue(DEFAULT, "localhost");
		propertyDescriptor.setDisplayName("Server");
		propertyDescriptor.setShortDescription("List of servers");

		propertyDescriptor = property("username");
		propertyDescriptor.setValue(NOT_UNDEFINED, Boolean.TRUE);
		propertyDescriptor.setValue(DEFAULT, "username");
		propertyDescriptor.setDisplayName("UserName");
		propertyDescriptor.setShortDescription("UserName for the Bucket");

		propertyDescriptor = property("password", TypeEditor.PasswordEditor);
		propertyDescriptor.setValue(NOT_UNDEFINED, Boolean.TRUE);
		propertyDescriptor.setValue(DEFAULT, "password");
		propertyDescriptor.setDisplayName("Password");
		propertyDescriptor.setShortDescription("Password for the User");

		propertyDescriptor = property("bucketName");
		propertyDescriptor.setValue(NOT_UNDEFINED, Boolean.TRUE);
		propertyDescriptor.setValue(DEFAULT, "bucketName");
		propertyDescriptor.setDisplayName("BucketName");
		propertyDescriptor.setShortDescription("Name of the bucket to connect");

		propertyDescriptor = property("bucketObject");
		propertyDescriptor.setValue(NOT_UNDEFINED, Boolean.TRUE);
		propertyDescriptor.setValue(DEFAULT, "bucket1");
		propertyDescriptor.setDisplayName("BucketObject");
		propertyDescriptor.setShortDescription("Provide a name to export the connection object to sampler");

		if (LOGGER.isDebugEnabled()) {
			String pubDescriptorsAsString = Arrays.stream(getPropertyDescriptors())
					.map(pd -> pd.getName() + "=" + pd.getDisplayName()).collect(Collectors.joining(" ,"));
			LOGGER.debug(pubDescriptorsAsString);
		}

	}

}
