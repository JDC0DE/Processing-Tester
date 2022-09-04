import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.*;
import javax.lang.model.SourceVersion;
import javax.tools.*;

public class Punit {
    private static String compileTest = "-ct";
    private static String staticTest = "-st";
    private static String dynamicTest = "-dt";
    private static String outPut = "-o";
    private static String exit = "-e";
    private static String help = "-h";

    private static String str = "";
    private static String txt = "";
    private static String outPath = "";

    private static Boolean ctCheck;
    private static Boolean stCheck;
    private static Boolean dtCheck;

    private static Boolean conversionCheck;

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
        // Note: check for if the folder is empty or if the file in the project is
        // correct file type

        // Should probs do a check for why the runProcessingCmd == 1 - whats casuing the
        // issue? - probably because the export folder already exists
        InputStreamReader in = new InputStreamReader(System.in);
        BufferedReader bf = new BufferedReader(in);
        try {
            if (args[0].length() > 0) {
                File inputFile = new File(args[0]);
                System.out.println(args[0]);

                directoryHandler(in, bf, inputFile);
                javaProjectHandler(in, bf, inputFile);
                processingProjectHandler(in, bf, inputFile);

            }

        } catch (Exception e) {
            System.out.println("Error: invalid input parameters");
            e.printStackTrace();

        }

    }

    public static void directoryHandler(InputStreamReader in, BufferedReader bf, File inputFile) {
        try {
            if (inputFile.isDirectory() && inputFile.exists()) {
                System.out.println("its a folder! - tonmoys recursive algo");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void processingProjectHandler(InputStreamReader in, BufferedReader bf, File inputFile) {
        try {
            if (Pattern.matches("\\w+\\.pde", inputFile.getName())) {
                System.out.println("is pde valid match: " + inputFile.getName());
                String path = inputFile.getParent();
                runProcessingCommand(
                        ".\\processing-java --sketch=" + path + " --run");
                if (runProcessingCommand(".\\processing-java --sketch=" + path + " "
                        + "--output=C:\\Users\\joshu\\OneDrive\\Documents\\Uni\\COMP4050\\test2 --export") == 0) {
                    conversionCheck = true;
                    inputFile = new File(
                            "C:\\Users\\joshu\\OneDrive\\Documents\\Uni\\COMP4050\\test2\\source\\MarchPenguin.java");
                    commandHandler(in, bf, inputFile);
                } else {
                    conversionCheck = false;
                }
            }
        } catch (Exception e) {

        }

    }

    public static void javaProjectHandler(InputStreamReader in, BufferedReader bf, File inputFile) {
        try {
            if (inputFile.isFile() && inputFile.exists()) {
                if (Pattern.matches("\\w+\\.java", inputFile.getName())) {
                    System.out.println("is java valid match: " + inputFile.getName());
                    conversionCheck = false;
                    commandHandler(in, bf, inputFile);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // handles the user input commands as specified in user manual
    public static void commandHandler(InputStreamReader in, BufferedReader bf, File input) {
        try {

            while (!str.equals(exit)) {
                str = bf.readLine();
                if (str.equals(compileTest)) {
                    compile(input);
                }
                if (str.equals(staticTest)) {
                    System.out.println("Static Test Check");
                }
                if (str.equals(outPut)) {
                    txtOutput(input);
                    resultOutput();
                    // bf.close();
                }
            }
            System.out.println("System: exiting now!");
            bf.close();
            System.exit(0);

        } catch (Exception e) {
            System.out.println("Error: invalid input parameters");
            e.printStackTrace();

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

    // read the converted pde file add in the new functions for testing check
    // outputs - for dynamic testing
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

    // returns the arguments for compiling - needs to be fixed so that its adaptable
    // for each student
    public static ArrayList<String> compilerArgs() {
        ArrayList<String> args = new ArrayList<>();
        args.add("-cp");
        args.add(
                "\"C:\\Users\\joshu\\OneDrive\\Documents\\Uni\\COMP4050\\test2\\lib\\jogl-all.jar;c:\\Users\\joshu\\OneDrive\\Documents\\Uni\\COMP4050\\test2\\lib\\core.jar;c:\\Users\\joshu\\OneDrive\\Documents\\Uni\\COMP4050\\test2\\lib\\gluegen-rt.jar;c:\\Users\\joshu\\OneDrive\\Documents\\Uni\\COMP4050\\test2\\lib\\MarchPenguin.jar\"");
        return args;
        // "\"C:\\Users\\joshu\\OneDrive\\Documents\\Uni\\COMP4050\\test2\\lib\\jogl-all.jar;c:\\Users\\joshu\\OneDrive\\Documents\\Uni\\COMP4050\\test2\\lib\\core.jar;c:\\Users\\joshu\\OneDrive\\Documents\\Uni\\COMP4050\\test2\\lib\\gluegen-rt.jar;c:\\Users\\joshu\\OneDrive\\Documents\\Uni\\COMP4050\\test2\\lib\\MarchPenguin.jar\"");
        // "\"C:\\Users\\joshu\\AppData\\Roaming\\Code\\User\\workspaceStorage\\f82a7962f137588b3d5804e53ca542d1\\redhat.java\\jdt_ws\\test1_d6ad7864\\bin;c:\\Users\\joshu\\OneDrive\\Documents\\Uni\\COMP4050\\test1\\lib\\jogl-all.jar;c:\\Users\\joshu\\OneDrive\\Documents\\Uni\\COMP4050\\test1\\lib\\core.jar;c:\\Users\\joshu\\OneDrive\\Documents\\Uni\\COMP4050\\test1\\lib\\gluegen-rt.jar;c:\\Users\\joshu\\OneDrive\\Documents\\Uni\\COMP4050\\test1\\lib\\MarchPenguin.jar\"");

    }

    // needs to handle converted PDE's -cp argument
    public static void compile(File Input) throws ExecutionException, InterruptedException, IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        JavaCompiler.CompilationTask task;
        System.out.println("Your systems javac: " + System.getProperty("java.version"));
        for (SourceVersion supportedVersion : compiler.getSourceVersions()) {
            System.out.println("This javac supports: " + supportedVersion);
        }

        // compiler.run(in, out, err, arguments)

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager sfm = compiler.getStandardFileManager(diagnostics, null, null);
        Iterable<? extends JavaFileObject> jfo = sfm.getJavaFileObjects(Input);
        for (JavaFileObject hold : jfo) {
            System.out.println(hold.getClass());
        }

        if (conversionCheck) {
            Iterable<String> args = compilerArgs();
            task = compiler.getTask(null, sfm, diagnostics, args, null, jfo);
            System.out.println("Double check");

        } else {
            task = compiler.getTask(null, sfm, diagnostics, null, null, jfo);

        }

        Future<Boolean> future = Executors.newFixedThreadPool(1).submit(task);
        Boolean result = future.get();
        if (result != null && result == true) {
            System.out.println("Compile Test Passed!");
            ctCheck = true;
        } else {
            System.out.println("Compile Test Failed!");
            diagnostics.getDiagnostics().forEach(System.out::println);
            ctCheck = false;
        }

        sfm.close();
    }

    public static int runProcessingCommand(String command) throws Exception {
        Process runCmd = Runtime.getRuntime().exec(command);
        System.out.println(command);
        runCmd.waitFor();
        System.out.println(command + " " + runCmd.exitValue());
        return runCmd.exitValue();
    }

    public static void eolPrint(PrintWriter inputFile, char inputCharacter) {
        int i = 100;
        while (i-- != 0) {
            inputFile.print(inputCharacter);
        }
        inputFile.println();

    }
}
