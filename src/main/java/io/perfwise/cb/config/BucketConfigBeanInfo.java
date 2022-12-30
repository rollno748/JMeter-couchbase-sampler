package io.perfwise.cb.config;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import io.perfwise.cb.utils.VariableSettings;
import org.apache.jmeter.testbeans.BeanInfoSupport;
import org.apache.jmeter.testbeans.gui.TableEditor;
import org.apache.jmeter.testbeans.gui.TypeEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BucketConfigBeanInfo extends BeanInfoSupport {

	private static Logger LOGGER = LoggerFactory.getLogger(BucketConfigBeanInfo.class);
	private static final String SERVER="server";
	private static final String USERNAME="username";
	private static final String PASSWORD="password";
	private static final String BUCKET="bucketName";
	private static final String BUCKET_WAITUNTILREADYTIME= "bucketWaitUntilReadyTime";
	private static final String BUCKET_OBJ= "bucketObject";

	public BucketConfigBeanInfo() {
		super(BucketConfig.class);

		createPropertyGroup("Couchbase client config",
				new String[] { SERVER, USERNAME, PASSWORD, BUCKET, BUCKET_WAITUNTILREADYTIME, BUCKET_OBJ });
		// Extra configs Table
		createPropertyGroup("Additional Configs related to bucket ", new String[] { "extraConfigs" });

		PropertyDescriptor propertyDescriptor = property(SERVER);
		propertyDescriptor.setValue(NOT_UNDEFINED, Boolean.TRUE);
		propertyDescriptor.setValue(DEFAULT, "localhost");
		propertyDescriptor.setDisplayName("Server Hostname");
		propertyDescriptor.setShortDescription("Server IP/hostname");

		propertyDescriptor = property(USERNAME);
		propertyDescriptor.setValue(NOT_UNDEFINED, Boolean.TRUE);
		propertyDescriptor.setValue(DEFAULT, "username");
		propertyDescriptor.setDisplayName("Username");
		propertyDescriptor.setShortDescription("UserName for the Bucket");

		propertyDescriptor = property(PASSWORD, TypeEditor.PasswordEditor);
		propertyDescriptor.setValue(NOT_UNDEFINED, Boolean.TRUE);
		propertyDescriptor.setValue(DEFAULT, "password");
		propertyDescriptor.setDisplayName("Password");
		propertyDescriptor.setShortDescription("Password for the User");

		propertyDescriptor = property(BUCKET);
		propertyDescriptor.setValue(NOT_UNDEFINED, Boolean.TRUE);
		propertyDescriptor.setValue(DEFAULT, "travel-sample");
		propertyDescriptor.setDisplayName("Bucket Name");
		propertyDescriptor.setShortDescription("Name of the bucket to connect");

		propertyDescriptor = property(BUCKET_WAITUNTILREADYTIME);
		propertyDescriptor.setValue(NOT_UNDEFINED, Boolean.TRUE);
		propertyDescriptor.setValue(DEFAULT, "120");
		propertyDescriptor.setDisplayName("Bucket WaitTime");
		propertyDescriptor.setShortDescription("Bucket WaitUntilReady Time in secs");

		propertyDescriptor = property(BUCKET_OBJ);
		propertyDescriptor.setValue(NOT_UNDEFINED, Boolean.TRUE);
		propertyDescriptor.setValue(DEFAULT, "travel-sample-bucket-object");
		propertyDescriptor.setDisplayName("Bucket Object");
		propertyDescriptor.setShortDescription("Name to export the connection object which is used in sampler");

		PropertyDescriptor configProps = property("extraConfigs", TypeEditor.TableEditor);
		configProps.setValue(TableEditor.CLASSNAME, VariableSettings.class.getName());
		configProps.setValue(TableEditor.HEADERS, new String[] { "Config Key", "Config Value" });
		configProps.setValue(TableEditor.OBJECT_PROPERTIES,
				new String[] { VariableSettings.CONFIG_KEY, VariableSettings.CONFIG_VALUE });
		configProps.setValue(DEFAULT, new ArrayList<>());
		configProps.setValue(NOT_UNDEFINED, Boolean.TRUE);
		configProps.setDisplayName("Couchbase Additional Configs (Optional)");

		if (LOGGER.isDebugEnabled()) {
			String pubDescriptorsAsString = Arrays.stream(getPropertyDescriptors())
					.map(pd -> pd.getName() + "=" + pd.getDisplayName()).collect(Collectors.joining(" ,"));
			LOGGER.debug(pubDescriptorsAsString);
		}

	}

}
