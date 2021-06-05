package org.core;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InsertLog{
	private static Logger logger = LoggerFactory.getLogger(InsertLog.class);
	/*
	 * add print log into class unit
	 * It works. However, some cases like inner class do not work.
	 */
	@SuppressWarnings("unchecked")
	public void fileAddprints(CompilationUnit cunit,Document document,File file) throws MalformedTreeException, BadLocationException, CoreException, IOException{
		AST ast = cunit.getAST(); // create a ASTRewrite, cunit is a java file
		ASTRewrite rewriter = ASTRewrite.create(ast);
		HashSet<String> classset = new HashSet<String>();  //hashset store the class name

		String init_filepath = file.getPath(); //get the relative path
		System.out.println(init_filepath);
		String filepath = "";
		if (init_filepath.length() > 48){
//			String filepath2 = init_filepath.replace("/Users/jinfu/Documents/workspace/Git/openmrs-core/","");
//			filepath = filepath2.replace("org/apache/openmrs", "--");
			filepath = init_filepath.replace("/Users/jinfu/Documents/workspace/Git/openmrs-core/","");
		}
			
		for (int classi = 0; classi < cunit.types().size(); classi++) { //one class may have multiple sub-classes
			if(!(cunit.types().get(classi) instanceof TypeDeclaration))//if the type is not TypeDeclaration
				continue;
			TypeDeclaration typedeclaration = (TypeDeclaration) cunit.types().get(classi);
			logger.info("--------classname: {} --------",typedeclaration.getName());
			if(typedeclaration.isInterface()) //interface, return
				return ;
			classset.add(typedeclaration.getName().getIdentifier());
			// travel every method of this class, rewrite the block inside the method
			for (MethodDeclaration methodDecl : typedeclaration.getMethods()) {
				logger.info("Method name: {}",methodDecl.getName().getIdentifier());
				Block block = methodDecl.getBody();
				// if the method is abstract, no body
				if (block == null || block.statements().size()==0)
					continue;
				// create new statements for insertion log
				MethodInvocation methodInv = ast.newMethodInvocation();
				SimpleName nameSystem = ast.newSimpleName("System");
				SimpleName nameOut = ast.newSimpleName("out");
				SimpleName namePrintln = ast.newSimpleName("println");
				// connect "System" and "out"
				QualifiedName nameSystemOut = ast.newQualifiedName(nameSystem, nameOut);
				// connect "System.out" and "println" to MethodInvocation
				methodInv.setExpression(nameSystemOut);
				methodInv.setName(namePrintln);
				StringLiteral printArgument = ast.newStringLiteral();
				// setup argument, double quotation
				String methodprint = "\"" +filepath+":"+ methodDecl.getName().getFullyQualifiedName() + "\"";
				printArgument.setEscapedValue(methodprint);
				methodInv.arguments().add(printArgument);
				// add MethodInvocation node to ExpressionStatement
				ExpressionStatement printstatement = ast.newExpressionStatement(methodInv);
				// there is no statements in block
				ListRewrite listRewrite = rewriter.getListRewrite(block, Block.STATEMENTS_PROPERTY);
				
				if(block.statements().get(0).toString().length()>4)
				{
					if(block.statements().get(0).toString().substring(0, 4).equals("this"))
						continue;
					if(block.statements().get(0).toString().substring(0, 5).equals("throw"))
						continue;
				}
				//if constructor, insert last and continue
				if(methodDecl.isConstructor() || classset.contains(methodDecl.getName().getIdentifier()) )
				{
					listRewrite.insertLast(printstatement, null);
					continue;
				}
//				listRewrite.insertFirst(printstatement, null);
				System.out.print(block.statements().get(1).toString());
				listRewrite.insertAfter((ASTNode)block.statements().get(1), printstatement, null);
			}
		}
		// apply the text edits to the compilation unit
		TextEdit edits = rewriter.rewriteAST(document,null);
		edits.apply(document);
		// this is the code for adding all the statements
		FileUtils.write(file, document.get());
	}
	
	/*
	 * add print log into class unit
	 * It works. 
	 */
	@SuppressWarnings("unchecked")
	public void fileAddprints2(CompilationUnit cunit,Document document,File file){
		AST ast = cunit.getAST(); // create a ASTRewrite, cunit is a java file
		ASTRewrite rewriter = ASTRewrite.create(ast);

		List<MethodDeclaration> methodDeclarations = MethodDeclarationFinder.perform(cunit);
		
		String init_filepath = file.getPath(); //get the relative path
		String filepath = "";
		if (init_filepath.length() > 48){
//			String filepath2 = init_filepath.replace("/Users/jinfu/Documents/workspace/Git/openmrs/","");
//			filepath = filepath2.replace("org/apache/openmrs", "--");
			filepath = init_filepath.replace("/Users/jinfu/Documents/workspace/Git/openmrs-core/","");
		}
		
		for (MethodDeclaration methodDeclaration : methodDeclarations) {
		    MethodInvocation methodInvocation = ast.newMethodInvocation();
		    // System.out.println("Hello, World")
		    QualifiedName qName = ast.newQualifiedName(ast.newSimpleName("System"), ast.newSimpleName("out"));
		    methodInvocation.setExpression(qName);
		    methodInvocation.setName(ast.newSimpleName("println"));

		    StringLiteral printArgument = ast.newStringLiteral();
			// setup argument, double quotation
			String methodprint = "\"" +filepath+":"+ methodDeclaration.getName().getFullyQualifiedName() + "\"";
			printArgument.setEscapedValue(methodprint);
		    methodInvocation.arguments().add(printArgument);
		    ExpressionStatement printstatement = ast.newExpressionStatement(methodInvocation);
		 

		    // no body, interface
		    Block block = methodDeclaration.getBody();
		    if (block == null  || block.statements().size()==0)
		    	continue;
		    ListRewrite listRewrite = rewriter.getListRewrite(block, Block.STATEMENTS_PROPERTY);
			if(block.statements().get(0).toString().substring(0, 4).equals("this") || block.statements().get(0).toString().substring(0, 5).equals("throw"))
				continue;
			//if constructor, insert last and continue
			if(methodDeclaration.isConstructor())
			{
				listRewrite.insertLast(printstatement, null);
				continue;
			}
		    
//		    methodDeclaration.getBody().statements().add(ast.newExpressionStatement(methodInvocation));   // do not work
//		    listRewrite.insertFirst(printstatement, null);
		    System.out.print(block.statements().get(1).toString());
			listRewrite.insertAt(printstatement, 3, null);
		}
		TextEdit edits = rewriter.rewriteAST(document,null);
		try {
			edits.apply(document);
			FileUtils.write(file, document.get());
			} catch (MalformedTreeException | BadLocationException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	/*
	 * add print log into method unit
	 * Changes can be inserted into the function. However, the location is wrong.
	 */
	@SuppressWarnings("unchecked")
	public void methodAddprints(MethodDeclaration methodUnit,Document document,File file) throws MalformedTreeException, BadLocationException, CoreException, IOException {
		AST ast = methodUnit.getAST(); // create a ASTRewrite
		ASTRewrite rewriter = ASTRewrite.create(ast);
		//System.out.println("method name:"+methodUnit.getName().toString());
		// for getting insertion position
		Block block = methodUnit.getBody();
		// create new statements for insertion
		MethodInvocation methodInv = ast.newMethodInvocation();  
        SimpleName nameSystem = ast.newSimpleName("System");  
        SimpleName nameOut = ast.newSimpleName("out");  
        SimpleName namePrintln = ast.newSimpleName("println");  
        // connect "System" and "out"
        QualifiedName nameSystemOut = ast.newQualifiedName(nameSystem, nameOut);  
        // connect "System.out" and "println" to MethodInvocation
        methodInv.setExpression(nameSystemOut);  
        methodInv.setName(namePrintln);  
        //"Done!"  
        StringLiteral printArgument = ast.newStringLiteral();  
        String methodprint = "\"" + methodUnit.getName().getFullyQualifiedName() + "\"";
        logger.info("method name: {}", methodprint);
		printArgument.setEscapedValue(methodprint);
        methodInv.arguments().add(printArgument);  
        // add MethodInvocation node to ExpressionStatement
        ExpressionStatement es = ast.newExpressionStatement(methodInv); 		
		//create ListRewrite
		ListRewrite listRewrite = rewriter.getListRewrite(block,Block.STATEMENTS_PROPERTY);
		listRewrite.insertFirst(es, null);
 
		TextEdit edits = rewriter.rewriteAST(document,null);
		edits.apply(document);
		// this is the code for adding statements
		FileUtils.write(file, document.get());
	}
	
	/*
	 * It does not work
	 */
	@SuppressWarnings("unchecked")
	public void methodAddprints2(MethodDeclaration node){
		AST ast = node.getAST();
        MethodInvocation methodInvocation = ast.newMethodInvocation();
        // System.out.println("Hello, World")
        QualifiedName qName =  ast.newQualifiedName(ast.newSimpleName("System"), ast.newSimpleName("out"));
        methodInvocation.setExpression(qName);
        methodInvocation.setName(ast.newSimpleName("println"));

        StringLiteral literal = ast.newStringLiteral();
        literal.setLiteralValue("Hello, World");
        methodInvocation.arguments().add(literal);
        // Append the statement
        node.getBody().statements().add(ast.newExpressionStatement(methodInvocation));
        
	}
	

}