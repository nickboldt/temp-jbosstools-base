/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.common.meta.action;

import java.util.*;
import org.jboss.tools.common.meta.*;
import org.jboss.tools.common.model.*;
import org.jboss.tools.common.reporting.ProblemReportingHelper;

public class XActionInvoker {
    static SpecialWizard sw = SpecialWizardFactory.createSpecialWizard("org.jboss.tools.common.model.ui.action.XModelObjectActionInvoker");

    public static void invoke(String invokerEntity, String actionPath, XModelObject object, Properties runningProperties) {
    	if(object == null) {
    		report("Cannot invoke action " + actionPath + " on null object.");
    		return;
    	}
        XModelEntity entity = object.getModel().getMetaData().getEntity(invokerEntity);
        if(entity == null) {
        	report("Entity " + invokerEntity + " is not found.");
        }
        invoke(entity, actionPath, object, runningProperties);
    }

    public static void invoke(String actionPath, XModelObject object, Properties runningProperties) {
    	if(object == null) {
    		report("Cannot invoke action " + actionPath + " on null object.");
    	} else {
    		invoke(object.getModelEntity(), actionPath, object, runningProperties);
    	}
    }

    public static void invoke(XModelEntity invoker, String actionPath, XModelObject object, Properties runningProperties) {
    	if(sw == null) {
    		report("XActionInvoker could not be loaded");
    	} else {
    		XAction a = getAction(invoker, actionPath);
            if(a == null) {
            	report("Cannot find action " + actionPath + " in entity " + invoker.getName());
            } else {
            	sw.setObject(new Object[]{a, object, runningProperties});
            	sw.execute();
            }
    	}
    }

    public static void invoke(String actionPath, XModelObject object, XModelObject[] targets, Properties runningProperties) {
        invoke(object.getModelEntity(), actionPath, object, targets, runningProperties);
    }

    public static void invoke(XModelEntity invoker, String actionPath, XModelObject object, XModelObject[] targets, Properties runningProperties) {
    	if(sw == null) {
    		report("XActionInvoker could not be loaded");
    	} else {
    		XAction a = getAction(invoker, actionPath);
            if(a == null) {
            	report("Cannot find action " + actionPath + " in entity " + invoker.getName());
            } else {
            	sw.setObject(new Object[]{a, object, runningProperties, targets});
            	sw.execute();
            }
    	}
    }
    
    public static XAction getAction(String actionPath, XModelObject object) {
    	return object == null ? null : getAction(object.getModelEntity(), actionPath);
    }
    
    public static XAction getAction(XModelEntity invoker, String actionPath) {
    	return invoker.getActionList().getAction(actionPath);
    }
    
    static Set<String> reportedMessages = new HashSet<String>();
    
    private static void report(String message) {
    	if(reportedMessages.contains(message)) return;
    	reportedMessages.add(message);
    	ProblemReportingHelper.reportProblem("org.jboss.tools.common.model", new Exception(message));
    }
    
}
