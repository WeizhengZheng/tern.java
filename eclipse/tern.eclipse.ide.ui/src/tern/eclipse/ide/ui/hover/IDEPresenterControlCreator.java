/**
 *  Copyright (c) 2013-2014 Angelo ZERR and Genuitec LLC.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 */
package tern.eclipse.ide.ui.hover;

import tern.eclipse.ide.core.IIDETernProjectProvider;
import tern.eclipse.jface.text.HoverLocationListener;
import tern.eclipse.jface.text.PresenterControlCreator;
import tern.eclipse.jface.text.TernBrowserInformationControl;

/**
 * IDE presenter control creator
 *
 */
public class IDEPresenterControlCreator extends PresenterControlCreator {

	private final IIDETernProjectProvider provider;

	public IDEPresenterControlCreator(IIDETernProjectProvider provider) {
		this.provider = provider;
	}

	@Override
	protected void addLinkListener(TernBrowserInformationControl control) {
		HoverLocationListener.addLinkListener(control,
				new IDEHoverLocationListener(control, provider));

	}
}