import org.apache.fop.apps.*;
import org.apache.fop.apps.io.ResourceResolverFactory;
import org.apache.fop.configuration.DefaultConfiguration;
import org.apache.fop.configuration.DefaultConfigurationBuilder;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URI;

public class Main {

	public static final URI DEFAULT_BASE_URI = new File(".").toURI();

	public static void main(String[] args) throws Exception {
		StreamSource xmlSource = new StreamSource(Main.class.getResourceAsStream("/xml/blank.xml"));

		DefaultConfigurationBuilder cfgBuilder = new DefaultConfigurationBuilder();
		DefaultConfiguration cfg = cfgBuilder.build(Main.class.getResourceAsStream("/config/fopconfig.xml"));

		// this will allow you to reference external graphics from within a jar file
		// i.e. <fo:external-graphic src="classpath:///images/puppy.jpg" content-width="7.02cm" />
		FopFactoryBuilder fopFactoryBuilder = new FopFactoryBuilder(
				DEFAULT_BASE_URI,
				new ClasspathResourceResolver())
				.setConfiguration(cfg);

		FopFactory fopFactory = fopFactoryBuilder.build();

		// This will allow you to load fonts from the fopconfig.xml i.e.
		// <font kerning="yes" embed-url="classpath:///fonts/Poppins-LightItalic.ttf" embedding-mode="subset">
		fopFactory.getFontManager().setResourceResolver(
				ResourceResolverFactory.createInternalResourceResolver(
						DEFAULT_BASE_URI,
						new ClasspathResourceResolver()));


		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

		// Setup output
		try (OutputStream out = new FileOutputStream("./embed-test.pdf")) {

			Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

			TransformerFactory factory = TransformerFactory.newInstance();

			// This allows you to reference includes within the xsl file
			// <xsl:include href="/xsl/includes/page-templates.xsl" />
			factory.setURIResolver(new ClasspathResourceURIResolver());

			InputStream resourceAsStream = Main.class.getResourceAsStream("/xsl/embed-test.xsl");
			Transformer transformer = factory.newTransformer(new StreamSource(resourceAsStream));

			SAXResult res = new SAXResult(fop.getDefaultHandler());
			transformer.transform(xmlSource, res);
		}
	}
}
