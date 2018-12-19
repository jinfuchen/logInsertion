package org.core;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.jface.text.BadLocationException;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.CoreException;

public class MethodVisitor extends ASTVisitor{
	private Document document;
	private File file;
	private InsertLog insertLog;
	
	public MethodVisitor(Document document, File file) {
		// TODO Auto-generated constructor stub
		this.document = document;
		this.file = file;
		insertLog = new InsertLog();
	}
	
	//visit each method, insert log 
	public boolean visit(MethodDeclaration methodDec) {
		try {
			insertLog.methodAddprints(methodDec, document, file);
		} catch (MalformedTreeException | BadLocationException | CoreException | IOException e) {
			e.printStackTrace();
		}
		
		return true;
	}
}
