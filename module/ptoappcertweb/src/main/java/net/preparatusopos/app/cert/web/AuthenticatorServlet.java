package net.preparatusopos.app.cert.web;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class AuthenticatorServlet
extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	
	private static final String PARAMETER_REFERER = "ref";
	private static final String PARAMETER_REFERER_PATH = "refpath";
	private static final String HEADER_LOCATION = "Location";
	private static final String HEADER_REFERER = "Referer";
	private static final String DEFAULT_REFERER_PATH = "/";
	private static final String DEFAULT_TITLE = "Redirect...";
	
	public AuthenticatorServlet()
	{
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException
	{
		URI refererURI = getRefererURI(req);
		setLocationHeader(resp, refererURI);
		writeRefreshHTML(resp.getOutputStream(), refererURI);
	}
	
	private static void setLocationHeader(HttpServletResponse resp,
			URI refererURI)
	{
		resp.setHeader(HEADER_LOCATION, refererURI.toString());
	}
	
	private static void writeRefreshHTML(OutputStream out, URI refererURI)
	throws IOException
	{
		try
		{
			XMLOutputFactory fact = XMLOutputFactory.newFactory();
			XMLStreamWriter writer = fact.createXMLStreamWriter(out);
			
			Charset encoding = Charset.defaultCharset();
			writer.writeStartDocument(encoding.name(), "1.0");
			writer.writeDTD("html");
			writer.writeStartElement("html");
			
			writer.writeStartElement("head");
			
			writer.writeStartElement("title");
			writer.writeCharacters(DEFAULT_TITLE);
			writer.writeEndElement();
			
			writer.writeStartElement("meta");
			writer.writeAttribute("charset", encoding.name());
			writer.writeEndElement();
			
			writer.writeStartElement("meta");
			writer.writeAttribute("http-equiv", "Refresh");
			writer.writeAttribute("content", getRefreshContent(refererURI));
			writer.writeEndElement();
			
			writer.writeEndElement();
			
			writer.writeStartElement("body");
			writer.writeEndElement();
			
			writer.writeEndElement();
			writer.writeEndDocument();
			writer.flush();
		}
		catch (XMLStreamException exception)
		{
			throw new IOException(exception);
		}
	}
	
	private static URI getRefererURI(HttpServletRequest req)
	throws ServletException
	{
		try
		{
			String referer = req.getParameter(PARAMETER_REFERER);
			if (referer != null)
				return new URI(decodeURL(referer));
			
			referer = req.getHeader(HEADER_REFERER);
			if (referer != null)
				return new URI(decodeURL(referer));
			
			String path = req.getHeader(PARAMETER_REFERER_PATH);
			if (path == null)
				path = DEFAULT_REFERER_PATH;
			
			String scheme = req.getScheme();
			String host = req.getRemoteHost();
			return new URI(scheme, host, decodeURL(path), null);
		}
		catch (URISyntaxException exception)
		{
			throw new ServletException(exception);
		}
	}
	
	private static String getRefreshContent(URI refererURI)
	throws IOException
	{
		return String.format("0; url=", encodeURL(refererURI.toString()));
	}
	
	private static String encodeURL(String str)
	throws IOException
	{
		try
		{
			Charset charset = Charset.defaultCharset();
			return URLEncoder.encode(str, charset.name());
		}
		catch (UnsupportedEncodingException exception)
		{
			throw new IOException(exception);
		}
	}
	
	private static String decodeURL(String str)
	throws ServletException
	{
		try
		{
			Charset charset = Charset.defaultCharset();
			return URLDecoder.decode(str, charset.name());
		}
		catch (UnsupportedEncodingException exception)
		{
			throw new ServletException(exception);
		}
	}
}
