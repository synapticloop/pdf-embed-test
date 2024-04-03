<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">

	<xsl:output method="xml" indent="yes"/>
	<xsl:param name="data" />

	<!--
		this will invoke the ClasspathResourceURIResolver
		-->
	<xsl:include href="/xsl/includes/page-templates.xsl" />

	<xsl:template match="/">
		<fo:root>
			<xsl:call-template name="setup-page-sizings" />
			<fo:page-sequence master-reference="A4-portrait">
				<fo:flow flow-name="xsl-region-body">
					<fo:block font-family="Poppins Light" font-size="11pt" font-style="normal">This is the embedded poppins light font</fo:block>
					<fo:block font-family="Poppins Light" font-size="11pt" font-style="italic">This is the embedded poppins light italic font</fo:block>
					<fo:block font-family="Poppins Medium" font-size="11pt" font-style="normal">This is the embedded poppins medium font</fo:block>
					<fo:block font-size="11pt">Below is the yarndings 20 chartered font</fo:block>

					<xsl:call-template name="heading1"><xsl:with-param name="content">Yarndings Heading</xsl:with-param></xsl:call-template>

					<fo:block margin-left="0.57cm" margin-top="0.56cm">
						<fo:external-graphic src="classpath:///images/puppy.jpg" content-width="7.02cm" />
					</fo:block>

					<fo:block font-family="Poppins Light" font-size="11pt" font-style="normal">
						The above image was sourced from
						https://pixabay.com/photos/dog-cute-animal-pet-puppy-looking-3071334/
					</fo:block>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>