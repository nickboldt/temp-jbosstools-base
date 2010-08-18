/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.usage.reporting;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.jboss.tools.usage.reporting.messages"; //$NON-NLS-1$
	public static String UsageReport_Checkbox_Text;
	public static String UsageReport_DialogMessage;
	public static String UsageReport_DialogTitle;
	
	public static String UsageReport_Error_SavePreferences;
	public static String UsageReport_GoogleAnalyticsAccount;
	public static String UsageReport_HostName;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
