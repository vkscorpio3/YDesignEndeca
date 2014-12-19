<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" 
	xmlns:endeca="http://endeca.com/schema/content/2008">

	<!-- Return nested hierarchy of items in adjacency list -->
  <xsl:function name="endeca:get-hierarchy" as="document-node()">
		<xsl:param name="input-file" as="xs:string"/>
		<xsl:param name="delimiter" as="xs:string"/>
    <xsl:param name="parent-id" as="xs:string"/>
	
		<!-- Read text file and split by line into nodeset -->
		<xsl:variable name="input-text" select="unparsed-text($input-file, 'utf-8')"/>
		<!-- <xsl:variable name="input-text" select="unparsed-text($input-file, 'ISO-10646-UCS-2')"/> -->
		<!-- <xsl:variable name="input-text" select="unparsed-text($input-file, 'UTF-16LE')"/> -->
		<xsl:variable name="input-lines" select="tokenize($input-text, '\r?\n')"/>
		
		<!-- Store header values -->
		<xsl:variable name="headers" select="tokenize($input-lines[1], $delimiter)"/>

		<!-- Create variable for all text lines transformed to XML -->
		<xsl:variable name="items">
			<xsl:element name="items">
			
				<xsl:for-each select="$input-lines[position()&gt;1]">
					<xsl:element name="item">
					
						<xsl:call-template name="split-line">
							<xsl:with-param name="text" select="."/>
							<xsl:with-param name="delimiter" select="$delimiter"/>
							<xsl:with-param name="headers" select="$headers"/>
						</xsl:call-template>
											
					</xsl:element>
					
				</xsl:for-each>
			</xsl:element>
		</xsl:variable>
		
		<!-- Create nested items variable -->
		<xsl:variable name="nested-items">
			<xsl:element name="items">

				<!-- Write children -->
				<xsl:call-template name="nest-items">
					<xsl:with-param name="items" select="$items"/>
					<xsl:with-param name="parent-id" select="$parent-id"/>
				</xsl:call-template>
			
			</xsl:element>
		</xsl:variable>

		<xsl:sequence select="$nested-items"/>
			
  </xsl:function>

	
	<!-- Return flat list of items in adjacency list -->
  <xsl:function name="endeca:get-flat-list" as="document-node()">
		<xsl:param name="input-file" as="xs:string"/>
		<xsl:param name="delimiter" as="xs:string"/>
	
		<!-- Read text file and split by line into nodeset -->
		<xsl:variable name="input-text" select="unparsed-text($input-file, 'utf-8')"/>
		<xsl:variable name="input-lines" select="tokenize($input-text, '\r?\n')"/>
		
		<!-- Store header values -->
		<xsl:variable name="headers" select="tokenize($input-lines[1], $delimiter)"/>

		<!-- Create variable for all text lines transformed to XML -->
		<xsl:variable name="items">
			<xsl:element name="items">

				<xsl:for-each select="$input-lines[position()&gt;1]">

					<xsl:element name="item">
					
						<xsl:call-template name="split-line">
							<xsl:with-param name="text" select="."/>
							<xsl:with-param name="delimiter" select="$delimiter"/>
							<xsl:with-param name="headers" select="$headers"/>
						</xsl:call-template>
											
					</xsl:element>
					
				</xsl:for-each>
			</xsl:element>
		</xsl:variable>

		<xsl:sequence select="$items"/>

  </xsl:function>
	
	<!-- Split each line on the provided delimiter -->
	<xsl:template name="split-line">
		<xsl:param name="text"/>
		<xsl:param name="delimiter"/>
		<xsl:param name="headers"/>
		
		<xsl:variable name="values" select="tokenize($text, $delimiter)"/>

		<xsl:for-each select="$values">
			<xsl:call-template name="make-attribute">
				<xsl:with-param name="value" select="."/>
				<xsl:with-param name="index" select="position()"/>
				<xsl:with-param name="headers" select="$headers"/>
			</xsl:call-template>
		</xsl:for-each>		
		
	</xsl:template>

	<!-- Create element name based on header position with current value -->
	<xsl:template name="make-element">
		<xsl:param name="value"/>
		<xsl:param name="index"/>
		<xsl:param name="headers"/>
		
		<xsl:element name="{$headers[$index]}">
			<xsl:value-of select="$value"/>
		</xsl:element>		
	</xsl:template>

	<!-- Create attribute name based on header position with current value -->
	<xsl:template name="make-attribute">
		<xsl:param name="value"/>
		<xsl:param name="index"/>
		<xsl:param name="headers"/>

		<xsl:attribute name="{$headers[$index]}">
			<xsl:value-of select="$value"/>
		</xsl:attribute>		

	</xsl:template>
	
	<!-- Create nested items -->
	<xsl:template name="nest-items">
		<xsl:param name="items"/>
		<xsl:param name="parent-id" select="0"/>
	
		<!-- Select all "parent" nodes grouped by parent-id -->
		<xsl:for-each-group select="$items//item[@parentId=$parent-id]" group-by="@id">

			<xsl:for-each select="current-group()">
      
				<xsl:element name="item">
					<xsl:copy-of select="./node()|@*"/>

					<!--
					<xsl:element name="parentId">
						<xsl:value-of select="$parent-id"/>
					</xsl:element>		
					-->
								
					<!-- Write children -->
					<xsl:call-template name="nest-items">
						<xsl:with-param name="items" select="$items"/>
						<xsl:with-param name="parent-id" select="current-grouping-key()"/>
					</xsl:call-template>
			
				</xsl:element>					
			</xsl:for-each>
			
		</xsl:for-each-group>
					
	</xsl:template>
	
</xsl:stylesheet>
