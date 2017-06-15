package jdtcore;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
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

public class DemoVisitor extends ASTVisitor {

	private Document document;
	private File file;
	//constructor
	public DemoVisitor(Document document,File file) {
		// TODO Auto-generated constructor stub
		this.document = document;
		this.file = file;
	}
	@SuppressWarnings("unchecked")
	@Override
	//visit the whole java file
	public boolean visit(CompilationUnit node) {
		// TODO Auto-generated method stub
		try {
			fileAddprints(node,document,file);
		} catch (MalformedTreeException | BadLocationException | CoreException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
//	public boolean visit(MethodDeclaration methodDec){
//		try {
//			methodAddprints(methodDec, document, file);
//		} catch (MalformedTreeException | BadLocationException | CoreException | IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return true;
//	}
	
	
//	public void endVisit(CompilationUnit node) {
//		// TODO Auto-generated method stub
//		try {
//			endModifyFile(node,document, file);
//		} catch (MalformedTreeException | BadLocationException | IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	private void fileAddprints(CompilationUnit cunit,Document document,File file) throws MalformedTreeException, BadLocationException, CoreException, IOException{
		// create a ASTRewrite, cunit is a java file
		AST ast = cunit.getAST();
		ASTRewrite rewriter = ASTRewrite.create(ast);
		//hashset store the classname
		HashSet<String> classset = new HashSet<String>();
		//get the relative path
		String filepath = file.getPath();
		for (int classi = 0; classi < cunit.types().size(); classi++) {
			//if the type is not TypeDeclaration, continue
			if(!(cunit.types().get(classi) instanceof TypeDeclaration))
				continue;
			TypeDeclaration typedeclaration = (TypeDeclaration) cunit.types().get(classi);
			System.out.println("--------------------------classname:\t"+typedeclaration.getName());
			
			if(typedeclaration.isInterface())
				return ;
			classset.add(typedeclaration.getName().getIdentifier());
			// travel every method of this class, rewrite the block inside the method
			for (MethodDeclaration methodDecl : typedeclaration.getMethods()) {
				System.out.println("Method name:\t" + methodDecl.getName().getIdentifier());
				
				Block block = methodDecl.getBody();
				
				// if the method is abstract, no body
				if (block == null || block.statements().size()==0)
					continue;
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
				// "Done!"
				StringLiteral printArgument = ast.newStringLiteral();
				// 需要加两个引号
				String methodprint = "\"" +filepath+":"+ methodDecl.getName().getFullyQualifiedName() + "\"";
				printArgument.setEscapedValue(methodprint);
				methodInv.arguments().add(printArgument);
				// 将方法调用节点MethodInvocation连接为表达式语句ExpressionStatement的子节点
				ExpressionStatement printstatement = ast.newExpressionStatement(methodInv);
				// there is no block in interface
				ListRewrite listRewrite = rewriter.getListRewrite(block, Block.STATEMENTS_PROPERTY);
				
				//if constructor, insert last and continue
				if(methodDecl.isConstructor() || classset.contains(methodDecl.getName().getIdentifier()) )
				{
					listRewrite.insertLast(printstatement, null);
					continue;
				}
				//the first some expressions are this statements
				if(block.statements().get(0).toString().substring(0, 4).equals("this"))
				{
//					int size = block.statements().size();
//					int thislast = 0;
//					//how many this statements in the block
//					for(int i=0;i<size;i++){
//						if(!block.statements().get(i).toString().substring(0, 4).equals("this")){
//							thislast = i;
//							break;
//						}
//					}
//					//if final statement is return, insert size-2 location
//					if((block.statements().get(size-1).toString().length() >6) && block.statements().get(size-1).toString().substring(0, 6).equals("return"))
//						thislast=(size-2);
//					listRewrite.insertAfter(printstatement, (ASTNode) block.statements().get(thislast),null);
					continue;
				}
				
				listRewrite.insertFirst(printstatement, null);
			}
		}
		// apply the text edits to the compilation unit
		TextEdit edits = rewriter.rewriteAST(document,null);
		edits.apply(document);
		// this is the code for adding all the statements
		FileUtils.write(file, document.get());
	}

	private void methodAddprints(MethodDeclaration methodUnit,Document document,File file) throws MalformedTreeException, BadLocationException, CoreException, IOException {
		// create a ASTRewrite
		AST ast = methodUnit.getAST();
		ASTRewrite rewriter = ASTRewrite.create(ast);
		System.out.println("method name:"+methodUnit.getName().toString());
		// for getting insertion position
		Block block = methodUnit.getBody();
		// create new statements for insertion
		MethodInvocation methodInv = ast.newMethodInvocation();  
        SimpleName nameSystem = ast.newSimpleName("System");  
        SimpleName nameOut = ast.newSimpleName("out");  
        SimpleName namePrintln = ast.newSimpleName("println");  
        //连接‘System’和‘out’  
        QualifiedName nameSystemOut = ast.newQualifiedName(nameSystem, nameOut);  
        //连接‘System.out’和‘println’到MethodInvocation节点  
        methodInv.setExpression(nameSystemOut);  
        methodInv.setName(namePrintln);  
        //"Done!"  
        StringLiteral sDone = ast.newStringLiteral();  
        sDone.setEscapedValue("\"Done!\"");  
        //System.out.println("Done!")  
        methodInv.arguments().add(sDone);  
        //将方法调用节点MethodInvocation连接为表达式语句ExpressionStatement的子节点  
        ExpressionStatement es = ast.newExpressionStatement(methodInv); 		
		//create ListRewrite
		ListRewrite listRewrite = rewriter.getListRewrite(block,Block.STATEMENTS_PROPERTY);
		listRewrite.insertFirst(es, null);
 
		TextEdit edits = rewriter.rewriteAST(document,null);
		edits.apply(document);
		// this is the code for adding statements
		//FileUtils.write(file, document.get());
	}
	
	private void endModifyFile(CompilationUnit node,Document document,File file) throws MalformedTreeException, BadLocationException, IOException{
		System.out.println("end of class");
		AST ast = node.getAST();
		ASTRewrite rewriter = ASTRewrite.create(ast);
		TextEdit edits = rewriter.rewriteAST(document,null);
		// apply the text edits to the compilation unit
		edits.apply(document);
		// this is the code for adding statements
		FileUtils.write(file, document.get());
	}
}