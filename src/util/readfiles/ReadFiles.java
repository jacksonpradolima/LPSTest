/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.readfiles;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Prado Lima
 */
public class ReadFiles {

    private static String[] _path
            = {
                "C:\\Users\\Prado Lima\\Documents\\Mestrado\\CI801 - Tópicos Especiais em Inteligência Artificial\\Trabalhos\\Fase 2\\nsgaii-spea2\\bisect\\NSGAII\\F2\\50_100_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30"
                ,"C:\\Users\\Prado Lima\\Documents\\Mestrado\\CI801 - Tópicos Especiais em Inteligência Artificial\\Trabalhos\\Fase 2\\nsgaii-spea2\\bisect\\SPEA2\\F2\\50_100_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30"
                ,"C:\\Users\\Prado Lima\\Documents\\Mestrado\\CI801 - Tópicos Especiais em Inteligência Artificial\\Trabalhos\\Fase 2\\nsgaii-spea2\\bub\\NSGAII\\F2\\50_100_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30"
                ,"C:\\Users\\Prado Lima\\Documents\\Mestrado\\CI801 - Tópicos Especiais em Inteligência Artificial\\Trabalhos\\Fase 2\\nsgaii-spea2\\bub\\SPEA2\\F2\\50_100_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30"
                ,"C:\\Users\\Prado Lima\\Documents\\Mestrado\\CI801 - Tópicos Especiais em Inteligência Artificial\\Trabalhos\\Fase 2\\nsgaii-spea2\\find\\NSGAII\\F2\\50_100_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30"
                ,"C:\\Users\\Prado Lima\\Documents\\Mestrado\\CI801 - Tópicos Especiais em Inteligência Artificial\\Trabalhos\\Fase 2\\nsgaii-spea2\\find\\SPEA2\\F2\\50_100_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30"
                ,"C:\\Users\\Prado Lima\\Documents\\Mestrado\\CI801 - Tópicos Especiais em Inteligência Artificial\\Trabalhos\\Fase 2\\nsgaii-spea2\\fourballs\\NSGAII\\F2\\50_100_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30"
                ,"C:\\Users\\Prado Lima\\Documents\\Mestrado\\CI801 - Tópicos Especiais em Inteligência Artificial\\Trabalhos\\Fase 2\\nsgaii-spea2\\fourballs\\SPEA2\\F2\\50_100_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30"
                ,"C:\\Users\\Prado Lima\\Documents\\Mestrado\\CI801 - Tópicos Especiais em Inteligência Artificial\\Trabalhos\\Fase 2\\nsgaii-spea2\\guizzo_cas\\NSGAII\\F2\\100_100_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30"
                ,"C:\\Users\\Prado Lima\\Documents\\Mestrado\\CI801 - Tópicos Especiais em Inteligência Artificial\\Trabalhos\\Fase 2\\nsgaii-spea2\\guizzo_cas\\SPEA2\\F2\\200_100_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30"
                ,"C:\\Users\\Prado Lima\\Documents\\Mestrado\\CI801 - Tópicos Especiais em Inteligência Artificial\\Trabalhos\\Fase 2\\nsgaii-spea2\\guizzo_james\\NSGAII\\F2\\200_100_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30"
                ,"C:\\Users\\Prado Lima\\Documents\\Mestrado\\CI801 - Tópicos Especiais em Inteligência Artificial\\Trabalhos\\Fase 2\\nsgaii-spea2\\guizzo_james\\SPEA2\\F2\\200_100_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30"
                ,"C:\\Users\\Prado Lima\\Documents\\Mestrado\\CI801 - Tópicos Especiais em Inteligência Artificial\\Trabalhos\\Fase 2\\nsgaii-spea2\\guizzo_save\\NSGAII\\F2\\200_300_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30"
                ,"C:\\Users\\Prado Lima\\Documents\\Mestrado\\CI801 - Tópicos Especiais em Inteligência Artificial\\Trabalhos\\Fase 2\\nsgaii-spea2\\guizzo_save\\SPEA2\\F2\\200_300_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30"
                ,"C:\\Users\\Prado Lima\\Documents\\Mestrado\\CI801 - Tópicos Especiais em Inteligência Artificial\\Trabalhos\\Fase 2\\nsgaii-spea2\\guizzo_weatherstation\\NSGAII\\F2\\100_100_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30" 
                ,"C:\\Users\\Prado Lima\\Documents\\Mestrado\\CI801 - Tópicos Especiais em Inteligência Artificial\\Trabalhos\\Fase 2\\nsgaii-spea2\\guizzo_weatherstation\\SPEA2\\F2\\100_100_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30"
                ,"C:\\Users\\Prado Lima\\Documents\\Mestrado\\CI801 - Tópicos Especiais em Inteligência Artificial\\Trabalhos\\Fase 2\\nsgaii-spea2\\mid\\NSGAII\\F2\\200_100_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30"
                ,"C:\\Users\\Prado Lima\\Documents\\Mestrado\\CI801 - Tópicos Especiais em Inteligência Artificial\\Trabalhos\\Fase 2\\nsgaii-spea2\\mid\\SPEA2\\F2\\200_300_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30"
                ,"C:\\Users\\Prado Lima\\Documents\\Mestrado\\CI801 - Tópicos Especiais em Inteligência Artificial\\Trabalhos\\Fase 2\\nsgaii-spea2\\trityp\\NSGAII\\F2\\200_100_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30"
                ,"C:\\Users\\Prado Lima\\Documents\\Mestrado\\CI801 - Tópicos Especiais em Inteligência Artificial\\Trabalhos\\Fase 2\\nsgaii-spea2\\trityp\\SPEA2\\F2\\200_100_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30"
            };

    final static Charset ENCODING = StandardCharsets.UTF_8;
    private static float hv;
    private static Path file;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        for (String path : _path) {
            Files.walk(Paths.get(path)).forEach(filePath -> {
                String fileName = filePath.getFileName().toString();
                file = filePath.getParent().getParent().getParent();
                if (Files.isRegularFile(filePath) && fileName.startsWith("Hypervolume_") && fileName.trim().compareTo("Hypervolume_Results") != 0) {
                    float aux = read(filePath.toAbsolutePath().toString());

                    hv = aux > hv ? aux : hv;
                }
            });

            System.out.println(String.format("Experimento: %s - Algoritmo: %s - Hypervolume: %s", file.getParent().getFileName().toString(), file.getFileName().toString(), hv));
        }
    }

    private static float read(String filePath) {
        BufferedReader br = null;
        float curValue = 0;
        float tempFloat = 0;

        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader(filePath));

            while ((sCurrentLine = br.readLine()) != null) {
                tempFloat = Float.parseFloat(sCurrentLine);
                if (tempFloat > curValue) {
                    curValue = tempFloat;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return curValue;
        }
    }
}
