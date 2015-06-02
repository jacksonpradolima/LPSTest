/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.readfiles;

/**
 *
 * @author Prado Lima
 */
public class ReadFMMatrix_Main {

    public static void main(String[] args) {
        String workingDirectory = System.getProperty("user.dir");
        String filepath = String.format("%s/%s", workingDirectory, "instances");
        String[] instances = {"cas", "eshop", "james", "weatherstation"};
        String file;
        
        ReadFMMatrix readFM = new ReadFMMatrix();

        file = "PROD_PAIRS_";
        readFM.setPivoColumn(PivoType.Product);
        readFM.setPivoLine(PivoType.Pairs);

        for (String instance : instances) {
            System.out.println("=============================");
            System.out.println("Instance: " + instance);
            System.out.println("File: " + file);
          
            readFM.setPath(String.format("%s/%s/%s", filepath, instance, file));
            readFM.read();
            readFM.generateFile();
            //readFM.printMatrix();
            System.out.println("=============================");
        }
        
        file = "PROD_MUTANTS_";
        readFM.setPivoColumn(PivoType.Product);
        readFM.setPivoLine(PivoType.Mutants);
        
        for (String instance : instances) {
            System.out.println("=============================");
            System.out.println("Instance: " + instance);
            System.out.println("File: " + file);
          
            readFM.setPath(String.format("%s/%s/%s", filepath, instance, file));
            readFM.read();
            readFM.generateFile();
            //readFM.printMatrix();
            System.out.println("=============================");
        }
    }
}
