import java.io.*;

public class App {
    private static String parameterTest = "-pt";
    private static String functionalityTest = "-ft";
    private static String outPut = "OUTPUT";
    private static String str = "";
    private static String txt = "";

    public static void main(String[] args) throws IOException {
        File javaFile = new File(
                "C:\\Users\\joshu\\OneDrive\\Documents\\Uni\\COMP4050\\Processing-Tester\\sketch_220803a.java");
        InputStreamReader in = new InputStreamReader(System.in);
        BufferedReader bf = new BufferedReader(in);
        try {
            if (args[0].equals(parameterTest)) {
                while (!str.equals("EXIT")) {
                    str = bf.readLine();
                    if (str.equals("OUTPUT")) {
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
