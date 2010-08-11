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
package org.jboss.tools.usage.jgoogleanalytics;

/**
 * Interface for logging adapter. You can hook up log4j, System.out or any other loggers you want.
 *
 * @author : Siddique Hameed
 * @version : 0.1
 */

public interface ILoggingAdapter {

  public void logError(String errorMessage);

  public void logMessage(String message);

}