import java.io.*;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.*;
import javax.lang.model.SourceVersion;
import javax.tools.*;

public class App {
    private static String compileTest = "-ct";
    private static String staticTest = "-st";
    private static String dynamicTest = "-dt";
    private static String outPut = "-o";
    private static String exit = "-e";
    private static String help = "-h";

    private static String str = "";
    private static String txt = "";
    private static Boolean ctCheck;
    private static Boolean stCheck;
    private static Boolean dtCheck;

    // "C:\\Users\\joshu\\OneDrive\\Documents\\Uni\\COMP4050\\Processing-Tester\\sketch_220803a.java"
    // "C:\\Users\\joshu\\OneDrive\\Documents\\Uni\\COMP4050\\Processing-Tester\\sketch_220803a.pde"
    // "C:\\Users\\joshu\\OneDrive\\Documents\\Uni\\COMP4050\\Processing-Tester\\SampleClass.java"
    // "C:\\Users\\joshu\\OneDrive\\Documents\\Uni\\COMP4050\\example_assignment-main\\example_assignment-main\\Submissions\\s0001_Alice_Penguin\\MarchPenguin\\MarchPenguin.pde"
    // "C:\\Users\\joshu\\OneDrive\\Documents\\Uni\\COMP4050\\test1\\source\\MarchPenguin.java"
    // \w+\.java - matches any alpha numeric character + .java
    // \w+\.pde - matches any alpha numeric character + .pde
    public static void main(String[] args) throws IOException {
        // arguments need to account for folder naming convention before checking
        // contents, file type checked is java or naming convention of java file
        // need to check for correct folder/file name convnetions -
        // Submissions/SID_FN_LN/Project Name/Files
        // name of test file? -
        // Will the program also loop through the submissions folder and check each
        // students subfolder or will it only take a single project submission at a
        // time and then finish?
        // test file = Punit_tf.txt - should be exclusive tests for st and dt? or also
        // ct?
        // If they submit only a pde file that will is it assumed the directory up will
        // be the project?
        InputStreamReader in = new InputStreamReader(System.in);
        BufferedReader bf = new BufferedReader(in);
        try {
            File inputFile = new File(args[0]);
            System.out.println(args[0]);

            if (inputFile.isDirectory() && inputFile.exists()) {
                System.out.println("its a folder! - tonmoys recursive algo");
            }

            if (inputFile.isFile() && inputFile.exists()) {
                if (Pattern.matches("\\w+\\.java", inputFile.getName())) {
                    System.out.println("is java valid match: " + inputFile.getName());
                }

                if (Pattern.matches("\\w+\\.pde", inputFile.getName())) {
                    System.out.println("is pde valid match: " + inputFile.getName());
                    String path = inputFile.getParent();
                    runProcessingCommand(
                            ".\\processing-java --sketch=" + path + " --run");
                    runProcessingCommand(".\\processing-java --sketch=" + path
                            + "--output=C:\\Users\\joshu\\OneDrive\\Documents\\Uni\\COMP4050\\test1 --export");
                    // runProcessingCommand(
                    // ".\\processing-java
                    // --sketch=C:\\Users\\joshu\\OneDrive\\Documents\\Uni\\COMP4050\\example_assignment-main\\example_assignment-main\\Submissions\\s0001_Alice_Penguin\\MarchPenguin
                    // --output=C:\\Users\\joshu\\OneDrive\\Documents\\Uni\\COMP4050\\test1
                    // --export");
                }

            }

            if (args[0].length() > 0) {
                while (!str.equals(exit)) {
                    str = bf.readLine();
                    if (str.equals(compileTest)) {
                        compile(inputFile);
                    }
                    if (str.equals(staticTest)) {
                        System.out.println("Static Test Check");
                    }
                    if (str.equals(outPut)) {
                        txtOutput(inputFile);
                        resultOutput();
                        // bf.close();
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

    // for refactoring the code -todo
    public static void commandHandler(String[] args, File input) {
        try {

        } catch (Exception e) {

        }
    }

    // outputting a system log for errors - todo
    public static File logOutput() {

        return null;
    }

    // will probs put a switch case in
    public static void resultOutput() throws Exception {
        int counter = 0;
        PrintWriter result = new PrintWriter("Result.txt");

        result.println("Results for - ");

        result.println("Student ID:  " + "" + "Name: " + "\n");

        if (ctCheck == null) {
            System.out.println("Error: Compile Test has not been run, please refer to user manual or use -h for help");
            result.close();

        } else {
            eolPrint(result, '#');
            eolPrint(result, '#');
            result.println();
            result.println("COMPILE TEST\n");

            if (ctCheck) {
                counter += 20;
                result.println("Compile Test Passed: " + counter);
            } else {
                counter += 0;
                result.println("Compile Test Failed: " + counter);
            }

            eolPrint(result, '#');
            eolPrint(result, '#');
            result.println();
            result.println("Result Total = " + counter);
            result.close();
            System.out.println("Output complete, check directory for results");

        }

    }

    public static void txtOutput(File input) throws IOException {
        // scanner is better for reading text
        PrintWriter out = new PrintWriter("output.txt");
        Scanner fReader = new Scanner(new FileReader(input));
        while (fReader.hasNextLine()) {
            txt = fReader.nextLine();
            // System.out.println(txt);
            out.println(txt);
            if (txt == null) {
                break;
            }
        }
        out.close();
        fReader.close();

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
            ctCheck = true;
        } else {
            System.out.println("Gets in here");
            diagnostics.getDiagnostics().forEach(System.out::println);
            ctCheck = false;
        }

        sfm.close();
    }

    public static void runProcessingCommand(String command) throws Exception {
        Process runCmd = Runtime.getRuntime().exec(command);
        System.out.println(command);
        runCmd.waitFor();
        System.out.println(command + runCmd.exitValue());
    }

    public static void eolPrint(PrintWriter inputFile, char inputCharacter) {
        int i = 100;
        while (i-- != 0) {
            inputFile.print(inputCharacter);
        }
        inputFile.println();

    }
}
