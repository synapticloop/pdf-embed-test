# How To Embed everything you need in a `.jar` file for FOP PDF Generation

The code included in this repository has placed lots of things in the `src/main/resources` directory to test out the inclusion (rather than FOP sneakily using the filesystem).

This started as a search to use font files in Apache FOP, which took a long time, went down a lot of bad paths, and finally had to step through the code and make a few educated guesses.

see the `embeded-test.pdf` file for the completed file.

to run the code

*NIX / MacOS 

```shell
./gradlew build shadowJar
cd build/libs/ && java -jar pdf-embed-test-1.0-all.jar && cd ../..
```

Windows
```shell
gradlew.bat build shadowJar
cd build\libs
java -jar pdf-embed-test-1.0-all.jar
cd ..\..
```

the output pdf location: `build/libs/embed-test.pdf`

## What this means

It means that you can create a jar file with all the resources embedded within it and it will run from the command line, or where it is being included in another programme.

# Using xsl includes:

e.g.: `<xsl:include href="/xsl/includes/page-templates.xsl" />`

**Note:** the resource resolver (`ClasspathResourceURIResolver`) only uses the classpath and does not look at the file system

All resources are from the root of the `src/main/resources` directory and **should** include a beginning forward slash (`/`)

# Using image includes:

e.g.:  `<fo:external-graphic src="classpath:///images/puppy.jpg" content-width="7.02cm" />`

The resource resolver (`ClasspathResourceResolver`) will load both from the file system and from the classpath. (although I only use the `classpath://` designator)

**Note**: the **triple** slash `///` after `classpath:`.

---

## Very Important

You have to be super careful with your dependencies or this will not work - see the `build.gradle` file - the lines of interest will are:

```groovy
dependencies {
    implementation('org.apache.xmlgraphics:fop:2.9') {
        exclude group:'org.apache.xmlgraphics', module: 'xmlgraphics-commons'
    }
    implementation 'org.apache.xmlgraphics:xmlgraphics-commons:2.9'
}
```

If you don't do this - you will get a stacktrace error something along the lines of:

```java
SEVERE: Image not available. URI: classpath:///images/puppy.jpg. Reason: org.apache.xmlgraphics.image.loader.ImageException: The file format is not supported. No ImagePreloader found for classpath:///images/puppy.jpg (No context info available)
org.apache.xmlgraphics.image.loader.ImageException: The file format is not supported. No ImagePreloader found for classpath:///images/puppy.jpg
        at org.apache.xmlgraphics.image.loader.ImageManager.preloadImage(ImageManager.java:181)
        at org.apache.xmlgraphics.image.loader.cache.ImageCache.needImageInfo(ImageCache.java:127)
        at org.apache.xmlgraphics.image.loader.ImageManager.getImageInfo(ImageManager.java:123)
        at org.apache.fop.fo.flow.ExternalGraphic.bind(ExternalGraphic.java:81)
        at org.apache.fop.fo.FObj.processNode(FObj.java:131)
        at org.apache.fop.fo.FOTreeBuilder$MainFOHandler.startElement(FOTreeBuilder.java:321)
        at org.apache.fop.fo.FOTreeBuilder$2.run(FOTreeBuilder.java:185)
        at org.apache.fop.fo.FOTreeBuilder$2.run(FOTreeBuilder.java:182)
        at java.base/java.security.AccessController.doPrivileged(AccessController.java:318)
        at org.apache.fop.fo.FOTreeBuilder.startElement(FOTreeBuilder.java:181)
        at java.xml/com.sun.org.apache.xml.internal.serializer.ToXMLSAXHandler.closeStartTag(ToXMLSAXHandler.java:206)
        at java.xml/com.sun.org.apache.xml.internal.serializer.ToSAXHandler.flushPending(ToSAXHandler.java:250)
        at java.xml/com.sun.org.apache.xml.internal.serializer.ToXMLSAXHandler.endElement(ToXMLSAXHandler.java:245)
        at java.xml/com.sun.org.apache.xml.internal.serializer.ToXMLSAXHandler.endElement(ToXMLSAXHandler.java:557)
        at jdk.translet/die.verwandlung.GregorSamsa.template$dot$3()
        at jdk.translet/die.verwandlung.GregorSamsa.applyTemplates()
        at jdk.translet/die.verwandlung.GregorSamsa.transform()
        at java.xml/com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet.transform(AbstractTranslet.java:627)
        at java.xml/com.sun.org.apache.xalan.internal.xsltc.trax.TransformerImpl.transform(TransformerImpl.java:782)
        at java.xml/com.sun.org.apache.xalan.internal.xsltc.trax.TransformerImpl.transform(TransformerImpl.java:395)
        at Main.main(Main.java:57)

```

---

# Using font includes

e.g.: `<font kerning="yes" embed-url="classpath:///fonts/Poppins-LightItalic.ttf" embedding-mode="subset">`

This is a little bit more tricky, and you need to get everything in order.

It uses the resource resolver (`ClasspathResourceResolver`) will load both from the file system and from the classpath. (although I only use the `classpath://` designator)

**Note**: the **triple** slash `///` after `classpath:`.

## Example - including the Yarndings 20 Chartered Font

### 1. Download the font
(named `Yarndings20Charted-Regular.ttf`) and place it in the `src/main/resources/fonts/` directory.

This is an open source font from google fonts - [Google Yarndings 20 Font](https://fonts.google.com/specimen/Yarndings+20)

### 2. Create the FOP configuration file 

`src/main/resources/config/fopconfig.xml`

which should look like the following:

```xml
<fop version="1.0">
  <strict-validation>false</strict-validation>

  <source-resolution>72</source-resolution>
  <target-resolution>72</target-resolution>
  <renderers>
    <renderer mime="application/pdf">
      <fonts>
        <font kerning="yes" embed-url="classpath:///fonts/Yarndings20Charted-Regular.ttf" embedding-mode="subset">
          <font-triplet name="Yarndings 20 Charted" style="normal" weight="normal"/>
        </font>
      </fonts>
    </renderer>
  </renderers>
</fop>
```
**Note:**

 - The `embed-url` attribute of `classpath:///fonts/Yarndings20Charted-Regular.ttf` 
 - The `classpath:///` designator with the triple slash (`///`)
 - The `name` attribute of `Yarndings 20 Charted` - which will need to be referenced within your xsl file


If you have other files with other weights and styles - include a new `<font />` snippet for each font file. 

### 3. Reference the font name in your xsl

```xml
<fo:block 
    margin-bottom="8pt" 
    font-family="Yarndings 20 Charted" 
    font-size="16pt">
  Welcome to Yarndings Font
</fo:block>
```

