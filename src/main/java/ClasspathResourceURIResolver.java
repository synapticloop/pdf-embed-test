import javax.xml.transform.Source;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;

public class ClasspathResourceURIResolver implements URIResolver {
	@Override
	public Source resolve(String href, String base) {
		InputStream resourceAsStream = ClasspathResourceURIResolver.class.getResourceAsStream(href);
		return new StreamSource(resourceAsStream);
	}
}
