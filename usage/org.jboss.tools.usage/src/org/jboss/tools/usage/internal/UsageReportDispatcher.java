/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.usage.internal;
import org.eclipse.ui.IStartup;

/**
 * 
 * @author Andre Dieitsheim
 */
public class UsageReportDispatcher implements IStartup {

	@Override
	public void earlyStartup() {
		new UsageReport().report();
	}
}
