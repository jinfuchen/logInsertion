package org.core;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;


import java.io.File;


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
//	@SuppressWarnings("unchecked")
//	@Override
//	public boolean visit(MethodDeclaration node) {
//		try {
////			insertLog.methodAddprints(methodDec, document, file);
//			insertLog.methodAddprints2(node);
//		} //catch (MalformedTreeException | BadLocationException | CoreException | IOException e) {
//			catch(MalformedTreeException e){
//			e.printStackTrace();
//		}
//		return true;		
//	}

	@Override
	public boolean visit(CompilationUnit node) {
		// TODO Auto-generated method stub
		insertLog.fileAddprints2(node, document, file);
		return super.visit(node);
		
	}
	
}
