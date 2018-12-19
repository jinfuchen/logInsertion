package org.core;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;


public class JavaToAst {

	private ASTParser astParser = ASTParser.newParser(AST.JLS8); 
    /**
     * Function: convert java file to ASTNode compilation unit
     * input: java file path (String)
     * output:ASTNode compilationUnit
     */
    public CompilationUnit getCompilationUnit(String javaFilePath)
            throws CoreException, Exception {
        BufferedInputStream bufferedInputStream;
        byte[] input;
		bufferedInputStream = new BufferedInputStream(new FileInputStream(javaFilePath));
        input = new byte[bufferedInputStream.available()];
        bufferedInputStream.read(input);
        bufferedInputStream.close();

        this.astParser.setResolveBindings(true);//
        this.astParser.setKind(ASTParser.K_COMPILATION_UNIT);//
        this.astParser.setBindingsRecovery(true);//
        Map<String, String> options = JavaCore.getOptions();
        this.astParser.setCompilerOptions(options);
		
        this.astParser.setSource(new String(input).toCharArray());
		/**/
        CompilationUnit result = (CompilationUnit) (this.astParser.createAST(null)); 
        result.recordModifications();
        return result;

    } 
    /**
     * Function: convert java file to ASTNode compilation unit
     * input: java file path (File)
     * output:ASTNode compilationUnit
     */
    public CompilationUnit getCompilationUnit2(File javaFile)
            throws CoreException, Exception {
    	
    	//File javaSRC = new File(javaFilePath);
    	final String source = FileUtils.readFileToString(javaFile);
    	Document document = new Document(source);
    	// Parse the source code and generate an AST.
        astParser.setResolveBindings(true);//
        astParser.setSource(document.get().toCharArray());
        astParser.setKind(ASTParser.K_COMPILATION_UNIT);
        astParser.setBindingsRecovery(true);//

        CompilationUnit cu = (CompilationUnit) astParser.createAST(null);
        
        CompilationUnitVisitor compilationUnitvisitor = new CompilationUnitVisitor(document,javaFile);
        cu.accept(compilationUnitvisitor);
        
        return cu;
    } 
 
}
