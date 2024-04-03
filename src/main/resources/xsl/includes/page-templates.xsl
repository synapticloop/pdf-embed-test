<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">

	<xsl:output method="xml" indent="no" />

	<!--
		This should be included in every file - it sets up the page margins, header
		and footer etc.
		-->
	<xsl:template name="setup-page-sizings">
		<fo:layout-master-set>
			<fo:simple-page-master
					master-name="A4-portrait"
					page-height="29.7cm"
					page-width="21.0cm"
					margin-top="0.75cm"
					margin-left="1.0cm"
					margin-right="1.5cm"
					margin-bottom="1.5cm">
				<fo:region-body margin-top="2.5cm" margin-left="2cm"/>
				<fo:region-before margin="0" />
				<fo:region-after margin="0" />
				<fo:region-start margin="0" />
				<fo:region-end margin="0" />

			</fo:simple-page-master>
		</fo:layout-master-set>
	</xsl:template>


	<xsl:template name="heading1">
		<xsl:param name="content" />
 		<fo:block margin-bottom="8pt" font-family="Yarndings 20 Charted" font-size="16pt"><xsl:value-of select="$content" /></fo:block>
	</xsl:template>

	<xsl:template name="heading2">
		<xsl:param name="content" />
		<fo:block margin-bottom="8pt" font-family="Poppins Medium" font-size="14pt"><xsl:value-of select="$content" /></fo:block>
	</xsl:template>
</xsl:stylesheet>