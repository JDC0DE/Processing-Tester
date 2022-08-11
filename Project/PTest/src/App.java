import java.io.*;

public class App {
    private static String staticTest = "-st";
    private static String dynamicTest = "-dt";
    private static String outPut = "OUTPUT";
    private static String exit = "EXIT";
    private static String str = "";
    private static String txt = "";

    // "C:\\Users\\joshu\\OneDrive\\Documents\\Uni\\COMP4050\\Processing-Tester\\sketch_220803a.java"
    public static void main(String[] args) throws IOException {
        // arguments need to account for folder naming convention before checking
        // contents, file type checked is java or naming convention of java file
        File javaFile = new File(args[0]);
        InputStreamReader in = new InputStreamReader(System.in);
        BufferedReader bf = new BufferedReader(in);
        try {
            if (args[0].length() > 0) {
                while (!str.equals(exit)) {
                    str = bf.readLine();
                    if (str.equals(staticTest)) {
                        System.out.println("Static Test Check");
                    }
                    if (str.equals(outPut)) {
                        txtOutput(javaFile);
                    }
                }
                System.out.println("System: exiting now!");
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
}
