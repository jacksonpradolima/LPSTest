package util.readfiles;

public class ReadProdMutantsFile_Main {

    public static void main(String[] args) {
        ReadProdMutantsFile read = new ReadProdMutantsFile("in/mt/rui/weatherstation/PROD_MUTANTS_");
        read.read();
    }
}
