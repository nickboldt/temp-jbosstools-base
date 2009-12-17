/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.common.model.loaders;

/**
 * 
 * Interface is used for cases when file name is sufficient to define entity of file model object.
 * @author Viacheslav Kabanovich
 *
 */
public interface EntityRecognizerExtension extends EntityRecognizer {

	public String getEntityName(String fileName, String ext, String body);

}
