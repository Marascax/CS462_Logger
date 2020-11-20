<?xml version="1.0" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
	<html>
		<xsl:apply-templates select="page"/>
	</html>
</xsl:template>

<xsl:template match="page">
	<head>
		<h1 style="padding-left:25px">
			<xsl:apply-templates select="user"/>
		</h1>
	</head>
	<div>
		<style>
				body {
					background-color:<xsl:apply-templates select="backgroundColor"/>;
				}
		</style>
		<body>
			<table>
				<style>
					th {
						text-align:left;
						font-size:150%;
						padding-left:25px;
						padding-bottom: 25px;
					}
				</style>
				<tr>
					<th>Log</th>
				</tr>
				<tr>
					<td style="padding-left:25px">
						<xsl:apply-templates select="log"/>
					 </td>
					 <td style="padding-left:25px">
						<xsl:apply-templates select="logInfo"/>
					 </td>
				</tr>
			</table>
		</body>
	</div>
</xsl:template>

<xsl:template match="user">
	<xsl:value-of select="text()"/>
</xsl:template>

<xsl:template match="backgroundColor">
	<xsl:value-of select="text()"/>
</xsl:template>

<xsl:template match="log">
	<xsl:value-of select="text()"/>
</xsl:template>

<xsl:template match="logInfo">
	<xsl:value-of select="text()"/>
</xsl:template>

</xsl:stylesheet>