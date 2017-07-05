
package org.eclipse.editor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

public class TextEditor implements IDocumentListener {

	@Inject
	MPart fPart;
	private int fTextHash;

	@PostConstruct
	public void postConstruct(Composite parent, MPart pPart) {
		parent.setLayout(new FillLayout());
		ITextViewer textViewer = new TextViewer(parent, SWT.V_SCROLL | SWT.H_SCROLL);
		textViewer.setDocument(loadFile(pPart));
		parent.getParent().layout();
	}

	private IDocument loadFile(MPart pPart) {

		String fileName = pPart.getContainerData();
		if (fileName == null && "new".equals(pPart.getLabel())) {
			if (Platform.getApplicationArgs().length > 0) {
				fileName = Platform.getApplicationArgs()[0];
			} else if (Platform.getCommandLineArgs().length > 0) {
				fileName = Platform.getCommandLineArgs()[0];
			}
		}

		byte[] allBytes = new byte[0];

		File file = new File(fileName);
		if (file.exists()) {
			pPart.setLabel(file.getName());
			try {
				allBytes = Files.readAllBytes(file.toPath());
			} catch (IOException e) {
				allBytes = e.getMessage().getBytes();
				e.printStackTrace();
			}
		}
		IDocument document = new Document(new String(allBytes));
		document.addDocumentListener(this);
		fTextHash = document.get().hashCode();
		return document;
	}

	@Override
	public void documentAboutToBeChanged(DocumentEvent event) {
	}

	@Override
	public void documentChanged(DocumentEvent event) {
		fPart.setDirty(event.getDocument().get().hashCode() != fTextHash);
	}
}