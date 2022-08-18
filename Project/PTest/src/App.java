import java.io.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.lang.model.SourceVersion;
import javax.tools.*;

public class App {
    private static String compileTest = "-ct";
    private static String staticTest = "-st";
    private static String dynamicTest = "-dt";
    private static String outPut = "-o";
    private static String exit = "-e";
    private static String str = "";
    private static String txt = "";

    // "C:\\Users\\joshu\\OneDrive\\Documents\\Uni\\COMP4050\\Processing-Tester\\sketch_220803a.java"
    public static void main(String[] args) throws IOException {
        // arguments need to account for folder naming convention before checking
        // contents, file type checked is java or naming convention of java file
        InputStreamReader in = new InputStreamReader(System.in);
        BufferedReader bf = new BufferedReader(in);
        try {
            File javaFile = new File(args[0]);

            if (args[0].length() > 0) {
                while (!str.equals(exit)) {
                    str = bf.readLine();
                    if (str.equals(compileTest)) {
                        compile(javaFile);
                    }
                    if (str.equals(staticTest)) {
                        System.out.println("Static Test Check");
                    }
                    if (str.equals(outPut)) {
                        txtOutput(javaFile);
                        bf.close();
                    }
                }
                System.out.println("System: exiting now!");
                bf.close();
                System.exit(0);

            }

        } catch (Exception e) {
            System.out.println("Error: invalid input parameters");
            e.printStackTrace();

        }

    }

    public static void txtOutput(File input) throws IOException {
        PrintWriter out = new PrintWriter("output.txt");
        BufferedReader jfReader = new BufferedReader(new FileReader(input));
        System.out.println(jfReader.readLine());
        while (jfReader.readLine() != null) {
            txt = jfReader.readLine();
            out.println(txt);
            if (txt == null) {
                break;
            }
        }
        out.close();
        jfReader.close();

    }

    public static void compile(File Input) throws ExecutionException, InterruptedException, IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        System.out.println("Your systems javac: " + System.getProperty("java.version"));
        for (SourceVersion supportedVersion : compiler.getSourceVersions()) {
            System.out.println("This javac supports: " + supportedVersion);
        }
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager sfm = compiler.getStandardFileManager(diagnostics, null, null);
        Iterable<? extends JavaFileObject> jfo = sfm.getJavaFileObjects(Input);
        for (JavaFileObject hold : jfo) {
            System.out.println(hold.getClass());
        }
        JavaCompiler.CompilationTask task = compiler.getTask(null, sfm, diagnostics, null, null, jfo);
        // task.call();

        Future<Boolean> future = Executors.newFixedThreadPool(1).submit(task);
        Boolean result = future.get();
        if (result != null && result == true) {
            System.out.println("Compile Test Passed!");
        } else {
            System.out.println("Gets in here");
            diagnostics.getDiagnostics().forEach(System.out::println);
        }

        sfm.close();
    }
}
