<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" 
	xmlns:endeca="http://endeca.com/schema/content/2008">

	<xsl:import href="hierarchy-functions.xsl"/>

	<xsl:output method="xml" doctype-system="dimensions.dtd"/>

	<xsl:param name="run-date"/>
	<xsl:param name="input-file" required="yes"/>
	<xsl:param name="delimiter" required="yes"/>
	<xsl:param name="dimension-name" required="no" select="'ProductCategory'"/>
	<xsl:param name="parent-id" required="no" select="''"/>
	<xsl:param name="id-pattern" required="yes"/>
	
	<xsl:attribute-set name="dimensions_attributes">
		<xsl:attribute name="VERSION">1.0.0</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="dimension_attributes">
		<xsl:attribute name="SRC_TYPE">INTERNAL</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="dval_attributes">
		<xsl:attribute name="TYPE">EXACT</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="synonym_attributes_classify">
		<xsl:attribute name="CLASSIFY">TRUE</xsl:attribute>
		<xsl:attribute name="DISPLAY">FALSE</xsl:attribute>
		<xsl:attribute name="SEARCH">FALSE</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="synonym_attributes_display">
		<xsl:attribute name="CLASSIFY">FALSE</xsl:attribute>
		<xsl:attribute name="DISPLAY">TRUE</xsl:attribute>
		<xsl:attribute name="SEARCH">TRUE</xsl:attribute>
	</xsl:attribute-set>

	<xsl:attribute-set name="synonym_attributes_display_classify">
		<xsl:attribute name="CLASSIFY">TRUE</xsl:attribute>
		<xsl:attribute name="DISPLAY">TRUE</xsl:attribute>
		<xsl:attribute name="SEARCH">TRUE</xsl:attribute>
	</xsl:attribute-set>

	<xsl:template match="/">
	
		<!-- Create nested items variable -->
		<xsl:variable name="nested-items" select="endeca:get-hierarchy($input-file,$delimiter,$parent-id)"/>
		
		<!--<xsl:copy-of select="$nested-items"/>-->
		
		<xsl:element name="DIMENSIONS" use-attribute-sets="dimensions_attributes">

			<xsl:element name="DIMENSION" use-attribute-sets="dimension_attributes">
			
				<xsl:attribute name="NAME">
			  	<xsl:value-of select="$dimension-name" />
			  </xsl:attribute>  
	
			  <xsl:element name="DIMENSION_ID">
					<xsl:attribute name="ID">
				  	<xsl:value-of select="$id-pattern" />
				  </xsl:attribute>  			  
				</xsl:element>	
	
				<!-- dimension node -->
			  <xsl:element name="DIMENSION_NODE">
	
					<xsl:element name="DVAL">
	
						<xsl:attribute name="TYPE">
					  	<xsl:text>EXACT</xsl:text>
					  </xsl:attribute>  
					
						<xsl:element name="DVAL_ID">						
							<xsl:attribute name="ID">
						  	<xsl:value-of select="$id-pattern" />
						  </xsl:attribute>  
						</xsl:element>			
	
						<xsl:element name="SYN" use-attribute-sets="synonym_attributes_display">
							<xsl:value-of select="$dimension-name" />
						</xsl:element>			

					</xsl:element>			

					<!-- Output grouped values as dimension nodes -->
					<xsl:call-template name="make-dimension-node">
						<xsl:with-param name="items" select="$nested-items/items"/>
					</xsl:call-template>

				</xsl:element>			
			</xsl:element>			
		</xsl:element>					
			
	</xsl:template>
		
	<!-- Create dimension nodes -->
	<xsl:template name="make-dimension-node">
		<xsl:param name="items"/>
	
		<xsl:variable name="total-items" select="count($items/item)"/>
		
		<!-- Iterate through each node in the list -->
		<xsl:for-each select="$items/item">
		
			<xsl:variable name="id" select="@category_id"/>
			<xsl:variable name="category_id" select="@category_id"/>
			<xsl:variable name="value" select="@category_name"/>
	
			<xsl:element name="DIMENSION_NODE">
			
				<xsl:element name="DVAL" use-attribute-sets="dval_attributes">
									
					<xsl:element name="DVAL_ID">
				
						<xsl:attribute name="ID">
							<xsl:value-of select="format-number(number($id-pattern)+$id,'###########')"/>						
						</xsl:attribute>		
					
					</xsl:element>

					<!-- Classify id -->
					<xsl:element name="SYN" use-attribute-sets="synonym_attributes_classify">						
						<xsl:value-of select="$category_id"/>
					</xsl:element>		

					<xsl:choose>
						<xsl:when test="child::item">
							<!-- Context node has children: Display name with CLASSIFY FALSE  -->
							<xsl:element name="SYN" use-attribute-sets="synonym_attributes_display">						
								<xsl:value-of select="$value"/>
							</xsl:element>
						</xsl:when>
						<xsl:otherwise>
							<!-- Context node does not have children: Display name with CLASSIFY TRUE -->
							<xsl:element name="SYN" use-attribute-sets="synonym_attributes_display_classify">						
								<xsl:value-of select="$value"/>
							</xsl:element>
						</xsl:otherwise>
					</xsl:choose>
						
					<!-- Add dimension value properties for all attributes -->
					<xsl:for-each select="@*[(local-name() != 'item') and (local-name() != 'ID') and (local-name() != 'category_id') and (local-name() != 'category_name') and (local-name() != 'parent_category_id') and (normalize-space(.))]">
						<xsl:element name="PROP">
							<xsl:attribute name="NAME">						
								<xsl:value-of select="local-name()"/>
							</xsl:attribute>
							<xsl:element name="PVAL">						
								<xsl:apply-templates select="current()"/>
							</xsl:element>
						</xsl:element>						
					</xsl:for-each>					
					
				</xsl:element>				

				<!-- Write dimension value children -->
				<xsl:call-template name="make-dimension-node">
					<xsl:with-param name="items" select="."/>
				</xsl:call-template>
							
			</xsl:element>		
	
		</xsl:for-each>
					
	</xsl:template>
		
</xsl:stylesheet>
