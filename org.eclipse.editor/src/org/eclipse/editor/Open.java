
package org.eclipse.editor;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class Open {
	@Execute
	public void execute(Shell pShell, EModelService modelService, MWindow window, EPartService partService) {
		FileDialog fd = new FileDialog(pShell);
		String file = fd.open();
		if (file != null) {
			MPart part = MBasicFactory.INSTANCE.createPart();
			part.setContributionURI("bundleclass://org.eclipse.editor/org.eclipse.editor.TextEditor");
			part.setElementId(file);
			part.setCloseable(true);
			part.setContainerData(file);
			MPartStack pPartStack = (MPartStack) modelService.find("org.eclipse.editor.partstack.0", window);
			pPartStack.getChildren().add(part);
			partService.showPart(part, PartState.ACTIVATE);
		}
	}
}