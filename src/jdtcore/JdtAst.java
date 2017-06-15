package jdtcore;
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


public class JdtAst {

    @SuppressWarnings("deprecation")
	private ASTParser astParser = ASTParser.newParser(AST.JLS8); 
    /**
     * parser only one java file
     * 
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
        Map<String, String> compilerOptions = JavaCore.getOptions();
        this.astParser.setCompilerOptions(options);
		
        this.astParser.setSource(new String(input).toCharArray());
		/**/
        CompilationUnit result = (CompilationUnit) (this.astParser.createAST(null)); 
        result.recordModifications();
        return result;

    } 
    //javafile to CompilationUnit
    public CompilationUnit getNewCompilationUnit(File javaSRC)
            throws CoreException, Exception {
    	
    	//File javaSRC = new File(javaFilePath);
    	final String source = FileUtils.readFileToString(javaSRC);
    	Document document = new Document(source);
    	
    	// Parse the source code and generate an AST.
        astParser.setResolveBindings(true);//
        astParser.setSource(document.get().toCharArray());
        astParser.setKind(ASTParser.K_COMPILATION_UNIT);
        astParser.setBindingsRecovery(true);//

        CompilationUnit cu = (CompilationUnit) astParser.createAST(null);
        DemoVisitor visitor = new DemoVisitor(document,javaSRC);//visit AST
        cu.accept(visitor);
        return cu;

    } 
 
}
