/******************************************************************************* 
 * Copyright (c) 2012 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 *     Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/

package org.jboss.tools.tests.installation;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.eclipse.swtbot.eclipse.finder.SWTBotEclipseTestCase;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.waits.ICondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This is a bot scenario which performs install through p2 UI.
 * It takes as input a p2 repository URL configured in the UPDATE_SITE
 * system property. By default all features will be installed. 
 * If IUs system property is specified - only selected IUs 
 * will be installed. IUs system property is a comma separeted string
 * of IU names. For example: "Abridged JBoss Tools 4.0,Hibernate Tools"
 * 
 * @author Mickael Istria
 * @author Pavol Srna
 */
@RunWith(SWTBotJunit4ClassRunner.class)
public class InstallTest extends SWTBotEclipseTestCase {
	
	/**
	 * System property expected to receive URL of a p2 repo to install
	 * It the input of the scenario.
	 */
	public static final String UPDATE_SITE_PROPERTY = "UPDATE_SITE";
	
	@Test
	public void testInstall() throws Exception {
		String site = System.getProperty("UPDATE_SITE");
		Assert.assertNotNull("No site specified, set UPDATE_SITE system property first", site);
		
		String IUs = System.getProperty("IUs");//optional property to install only selected IUs
		
		installFromSite(site, IUs);
	}

	
	private void installFromSite(String site, String selectedIUs) {
		this.bot.menu("Help").menu("Install New Software...").click();
		this.bot.shell("Install").bot().button("Add...").click();
		this.bot.shell("Add Repository").activate().setFocus();
		this.bot.text(1).setText(site);
		this.bot.button("OK").click();
		this.bot.shell("Install").activate().setFocus();
		this.bot.waitWhile(new ICondition() {
			
			@Override
			public boolean test() throws Exception {
				return bot.tree().getAllItems()[0].getText().startsWith("Pending...");
			}
			
			@Override
			public void init(SWTBot bot) {
			}
			
			@Override
			public String getFailureMessage() {
				return "Could not see categories in tree";
			}
		});
		
		if(selectedIUs != null){
			//select IUs to install
			for(String iu : selectedIUs.split(",")){
				assertFalse("IU: \"" + iu + "\" NOT FOUND!", !checkIU(iu));	
			}
			
		} else {
			this.bot.button("Select All").click();			
		}
		
		this.bot.button("Next >").click();
		this.bot.waitUntil(new ICondition() {
			@Override
			public boolean test() throws Exception {
				return bot.button("Cancel").isEnabled();
			}
			
			@Override
			public void init(SWTBot bot) {
			}
			
			@Override
			public String getFailureMessage() {
				return "Blocking while calculating deps";
			}
		}, 10 * 60000); // 5 minutes timeout
		try {
			continueInstall(bot);
		} catch (InstallFailureException ex) {
			StringBuilder message = new StringBuilder();
			message.append("Could not install from: " + site);
			message.append("\n");
			message.append(ex.getMessage());
			
			fail(message.toString());
		}
		
	}

	public static void continueInstall(final SWTWorkbenchBot bot) throws InstallFailureException {
		try {
			bot.button("Next >").click();
			bot.radio(0).click();
			bot.button("Finish").click();
			// wait for Security pop-up, or install finished.
			final SWTBotShell shell = bot.shell("Installing Software");
			bot.waitWhile(new ICondition() {
				
				@Override
				public boolean test() throws Exception {
					return shell.isActive();
				}
				
				@Override
				public void init(SWTBot bot) {
				}
				
				@Override
				public String getFailureMessage() {
					return null;
				}
			}, 20 * 60000); // 20 minutes_tino
			if (bot.activeShell().getText().equals("Security Warning")) {
				bot.button("OK").click();
				System.err.println("OK clicked");
				bot.waitUntil(new ICondition() {
					@Override
					public boolean test() throws Exception {
						try {
							boolean stillOpen = bot.shell("Installing Software").isOpen();
							System.err.println("still open? " + stillOpen);
							return !stillOpen;
						} catch (WidgetNotFoundException ex) {
							System.err.println("no shell");
							// Shell already closed
							return true;
						}
					}
				
					@Override
					public void init(SWTBot bot) {
					}
				
					@Override
					public String getFailureMessage() {
						return null;
					}
				}, 15 * 60000); // 15 more minutes
			}
			SWTBot restartShellBot = bot.shell("Software Updates").bot();
			try {
				// Eclipse 3.7.x => "Not now"
				restartShellBot.button("Not Now").click(); // Don't restart in test, test executor will do it.
			} catch (WidgetNotFoundException ex) {
				// Eclipse 4.2 => "No"
				restartShellBot.button("No").click();
			}
		} catch (Exception ex) {
			
			String installDesc = bot.text().getText();
			if (installDesc == null || installDesc.isEmpty()) {
				throw new RuntimeException("Internal error", ex);
			}
			throw new InstallFailureException(installDesc);
		}
	}

	/**
	 * Checks IU (Category, or Feature) in a tree
	 * @param iu to be checked 
	 * @return true if checked
	 * 
	 * @author Pavol Srna
	 */
	private boolean checkIU(String iu){

		boolean checked = false;
		for(SWTBotTreeItem node : bot.tree().getAllItems()){
			//traverse through all categories
			if(node.getText().equals(iu)){
				node.check();
				checked = true;
				break;
			}else{
				//expand category
				node.expand();
				for(SWTBotTreeItem i : node.getItems()){
					//traverse through category features
					if(i.getText().equals(iu)){
						i.check();
						checked = true;
						break;
					}
				}
			}
		}
		return checked;
	}

}
