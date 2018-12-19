package org.core;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;

public class CompilationUnitVisitor extends ASTVisitor{
	private Document document;
	private File file;
	private InsertLog insertLog;
	
	public CompilationUnitVisitor(Document document, File file) {
		// TODO Auto-generated constructor stub
		this.document = document;
		this.file = file;
		insertLog = new InsertLog();
	}
	
	//visit each class file, insert log 
	public boolean visit(CompilationUnit node) {
		try {
			insertLog.fileAddprints(node,document,file);
		} catch (MalformedTreeException | BadLocationException | CoreException | IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}
