package org.jboss.tools.runtime.as.ui.bot.test.dialog.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.waits.ICondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.jboss.tools.runtime.as.ui.bot.test.entity.Runtime;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;
import org.jboss.tools.ui.bot.ext.SWTEclipseExt;
import org.jboss.tools.ui.bot.ext.condition.TaskDuration;

public class SearchingForRuntimesDialog {
	
	public List<Runtime> getRuntimes(){
		List<Runtime> runtimes = new ArrayList<Runtime>();
		
		SWTBot bot = SWTBotFactory.getBot().shell("Searching for runtimes...").bot();
		bot.waitUntil(new RuntimeSearchedFinished(bot), TaskDuration.LONG.getTimeout());
		SWTBotTree tree = bot.tree();
		
		for (SWTBotTreeItem treeItem : SWTEclipseExt.getAllTreeItemsRecursive(SWTBotFactory.getBot(), tree, true)) {
			Runtime runtime = new Runtime();
			runtime.setName(treeItem.cell(0));
			runtime.setVersion(treeItem.cell(1));
			runtime.setType(treeItem.cell(2));
			runtime.setLocation(treeItem.cell(3));
			runtimes.add(runtime);
		}
		return runtimes;
	}
	
	public void ok(){
		SWTBotFactory.getBot().button("OK").click();
	}
	
	private static class RuntimeSearchedFinished implements ICondition {

		private SWTBot bot;
		
		public RuntimeSearchedFinished(SWTBot bot) {
			this.bot = bot;
		}
		
		@Override
		public void init(SWTBot bot) {
		}
		
		@Override
		public boolean test() throws Exception {
			try {
				bot.label("Searching runtimes is finished.");
				return true;
			} catch (WidgetNotFoundException e){
				return false;
			}
		}

		@Override
		public String getFailureMessage() {
			return "The runtime search has not finished in the specified amount of time";
		}
	}
}
