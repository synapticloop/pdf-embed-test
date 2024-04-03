import org.apache.xmlgraphics.io.Resource;
import org.apache.xmlgraphics.io.ResourceResolver;

import java.io.*;
import java.net.URI;

public class ClasspathResourceResolver implements ResourceResolver {
	public static final String DESIGNATOR_CLASSPATH = "classpath://";
	public static final String DESIGNATOR_FILE = "file://";

	@Override
	public Resource getResource(URI uri) throws IOException {
		String uriString = uri.toString();

		if(uriString.startsWith(DESIGNATOR_CLASSPATH)) {
			String substring = uriString.substring(DESIGNATOR_CLASSPATH.length());
			InputStream resourceAsStream = ClasspathResourceResolver.class.getResourceAsStream(substring);
			return (new Resource(resourceAsStream));
		} else if(uriString.startsWith(DESIGNATOR_FILE)) {
			String substring = uriString.substring(DESIGNATOR_FILE.length());
			InputStream resourceAsStream = new FileInputStream(new File(substring));
			return(new Resource(resourceAsStream));
		} else {
			// just use the classpath - in case people forget...
			return new Resource(ClasspathResourceResolver.class.getResourceAsStream(uriString));
		}
	}

	@Override
	public OutputStream getOutputStream(URI uri) throws IOException {
		return new FileOutputStream(new File(uri));
	}
}