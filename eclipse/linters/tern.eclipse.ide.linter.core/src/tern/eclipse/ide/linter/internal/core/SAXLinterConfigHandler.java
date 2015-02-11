/**
 *  Copyright (c) 2013-2014 Angelo ZERR.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package tern.eclipse.ide.linter.internal.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import tern.eclipse.ide.linter.core.ITernLinterConfig;
import tern.eclipse.ide.linter.core.ITernLinterOption;

/**
 * SAX Module to load module with XML file.
 * 
 */
public class SAXLinterConfigHandler extends DefaultHandler {

	private StringBuilder doc = null;
	private Stack<ITernLinterOption> options;
	private TernLinterOption option;
	private ITernLinterConfig linter;

	public SAXLinterConfigHandler load(InputStream in) throws IOException {
		options = new Stack<ITernLinterOption>();
		try {
			XMLReader xmlReader = XMLReaderFactory.createXMLReader();
			xmlReader.setContentHandler(this);
			xmlReader.parse(new InputSource(in));
		} catch (SAXException e) {
			throw new IOException(e);
		}
		return this;
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		if ("linter".equals(name)) {
			String id = attributes.getValue("id");
			this.linter = new TernLinterConfig(id);
			options.push(linter);
		} else if ("option".equals(name)) {
			String id = attributes.getValue("id");
			String type = attributes.getValue("type");
			String url = attributes.getValue("url");
			option = new TernLinterOption(id, type, url);
			options.peek().addOption(option);
			options.push(option);
		} else if ("doc".equals(name)) {
			doc = new StringBuilder();
		}
		super.startElement(uri, localName, name, attributes);
	}

	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		if ("option".equals(name)) {
			options.pop();
		} else if ("doc".equals(name)) {
			if (doc != null) {
				((TernLinterOption) options.peek()).setDoc(doc.toString());
			}
			doc = null;
		}
		super.endElement(uri, localName, name);
	}

	@Override
	public void characters(char ch[], int start, int length)
			throws SAXException {
		if (doc != null) {
			doc.append(String.valueOf(ch, start, length));
		}
		super.characters(ch, start, length);
	}

	public ITernLinterConfig getLinter() {
		return linter;
	}
}
