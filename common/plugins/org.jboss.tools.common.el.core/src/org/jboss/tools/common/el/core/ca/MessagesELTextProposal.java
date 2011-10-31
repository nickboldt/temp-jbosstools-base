/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.common.el.core.ca;

import java.util.List;

import org.jboss.tools.common.text.TextProposal;

/**
 * Class to store proposals generated by ELResolver implementers
 *  
 * @author Victor Rubezhny
 */
public class MessagesELTextProposal extends TextProposal {
	private Object allObjects;
	private String baseName;
	private String propertyName;
	

	/**
	 * Adds a Object for the proposal
	 * 
	 * @param objects
	 */
	public void setObjects(List objects) {
		this.allObjects = objects;
	}

	/**
	 * returns all the Object for the proposal
	 * @return
	 */
	public Object getAllObjects() {
		return allObjects;
	}

	public String getBaseName() {
		return baseName;
	}

	public void setBaseName(String baseName) {
		this.baseName = baseName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
}
