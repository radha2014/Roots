package com.tools.xslxml;

import java.io.IOException;

public class XSLCreator {
	private String xslfile;

	public XSLCreator(String xslfile) {
		super();
		this.xslfile = xslfile;
	}

	public void generateXSL() throws IOException {

		FileOperation fo = new FileOperation(xslfile);

		fo.writeToFile("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
		fo.writeToFile("<!-- Edited by XMLSpy? -->");
		fo
				.writeToFile("<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">");

		fo.writeToFile("<xsl:template match=\"/\">");
		fo.writeToFile("  <html>");
		fo.writeToFile("  <body>");
		fo.writeToFile("  <h3><xsl:value-of select=\"member/name\"/></h3>");
		fo.writeToFile("    <table border=\"1\" width=\"70%\" >");
		fo.writeToFile("      <tr>");
		fo.writeToFile("        <td>ID</td>");
		fo.writeToFile("        <td>:</td>");
		fo.writeToFile("        <td><xsl:value-of select=\"member/id\"/></td>");
		fo.writeToFile("      </tr>");
		fo.writeToFile("      <tr>");
		fo.writeToFile("        <td>Age</td>");
		fo.writeToFile("        <td>:</td>");
		fo
				.writeToFile("        <td><xsl:value-of select=\"member/age\"/></td>");
		fo.writeToFile("      </tr>");
		fo.writeToFile("      <tr>");
		fo.writeToFile("        <td>Date of birth</td>");
		fo.writeToFile("        <td>:</td>");
		fo
				.writeToFile("        <td><xsl:value-of select=\"member/dateofbirth\"/></td>");
		fo.writeToFile("      </tr>");
		fo.writeToFile("      <tr>");
		fo.writeToFile("        <td>Gender</td>");
		fo.writeToFile("        <td>:</td>");
		fo
				.writeToFile("        <td><xsl:value-of select=\"member/gender\"/></td>");
		fo.writeToFile("      </tr>");
		fo.writeToFile("      <tr>");
		fo.writeToFile("        <td>Father's Name</td>");
		fo.writeToFile("        <td>:</td>");
		fo.writeToFile("        <td><a href=\"{member/fathername/@url}\">"
				+ "<xsl:value-of select=\"member/fathername\"/></a></td>");
		fo.writeToFile("      </tr>");
		fo.writeToFile("      <tr>");
		fo.writeToFile("        <td>Mother's Name</td>");
		fo.writeToFile("        <td>:</td>");
		fo.writeToFile("        <td><a href=\"{member/mothername/@url}\">"
				+ "<xsl:value-of select=\"member/mothername\"/></a></td>");
		fo.writeToFile("      </tr>");

		fo.writeToFile("      <tr><td colspan=\"3\">Siblings :</td></tr>");
		fo.writeToFile("      <xsl:for-each select=\"member/sibling\">");
		fo.writeToFile("      <tr>");
		fo
				.writeToFile("	<td colspan=\"3\"><xsl:value-of select=\"sno\"/>. "
						+ "<a href=\"{name/@url}\"><xsl:value-of select=\"name\"/></a> "
						+ "(<xsl:value-of select=\"tag\"/>)</td>");
		fo.writeToFile("      </tr>");
		fo.writeToFile("      </xsl:for-each>");

		fo.writeToFile("      <tr><td colspan=\"3\">Kids :</td></tr>");
		fo.writeToFile("      <xsl:for-each select=\"member/kid\">");
		fo.writeToFile("      <tr>");
		fo
				.writeToFile("        <td colspan=\"3\"><xsl:value-of select=\"sno\"/>. "
						+ "<a href=\"{name/@url}\"><xsl:value-of select=\"name\"/></a> "
						+ "(<xsl:value-of select=\"tag\"/>)</td>");
		fo.writeToFile("      </tr>");
		fo.writeToFile("      </xsl:for-each>");
		fo.writeToFile("      ");
		fo.writeToFile("      <tr>");
		fo.writeToFile("      <td>");
		fo.writeToFile("	<xsl:for-each select=\"member/picture\">");
		fo
				.writeToFile("	    <a href=\"{url}\" ><img src=\"{src}\" alt=\"{alt}\" width=\"200\" /></a>");
		fo.writeToFile("	</xsl:for-each>");
		fo.writeToFile("      </td>");
		fo.writeToFile("      </tr>");

		fo.writeToFile("    </table>");
		fo.writeToFile("  </body>");
		fo.writeToFile("  </html>");
		fo.writeToFile("</xsl:template>");
		fo.writeToFile("</xsl:stylesheet>");

	}
}
