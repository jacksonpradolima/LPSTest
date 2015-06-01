package util.readfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import util.InstanceReader;

public class ReadProdMutantsFile {

    protected List<String[]> products;

    protected String filename;

    protected InstanceReader reader;

    public ReadProdMutantsFile(String filename) {
        this.filename = filename;
        this.reader = new InstanceReader(filename);
        this.products = new ArrayList<String[]>();
    }

    public void read() {
        reader.open();

        String line = null;

        List<Integer> mutants = new ArrayList<Integer>();

        while ((line = reader.readLine()) != null) {
            String[] part = line.split("Mutants:");
            part[0] = part[0].trim();
            part[1] = part[1].trim();

            String[] mut = part[1].split(", ");

            for (String s : mut) {
                s = s.replace("[", "").replace("]", "").trim();

                if (!mutants.contains(Integer.valueOf(s))) {
                    mutants.add(Integer.valueOf(s));
                }
            }

            // Process product
            part[0] = part[0].replaceAll("Product", "").trim();
            part[0] = part[0].replaceAll(":", "").trim();

            products.add(Integer.valueOf(part[0]), mut);
        }

        System.out.println("Number of Mutants:" + mutants.size());
        Collections.sort(mutants);

        HashMap<Integer, Integer> hash = new HashMap<Integer, Integer>();
        int index = 0;

        for (Integer mutantId : mutants) {
            if (!hash.containsKey(mutantId)) {
                hash.put(mutantId, index++);
            }
        }

        System.out.println(hash);
        System.out.println("=============================");

        int[][] coverage = new int[mutants.size()][products.size()];

        for (int i = 0; i < products.size(); i++) {
            String[] mut = products.get(i);
            for (String s : mut) {
                s = s.replace("[", "").replace("]", "").trim();
                int id = hash.get(Integer.valueOf(s));
                coverage[id][i] = 1;
            }
        }

        System.out.println(products.size());
        System.out.println(coverage.length);

        printMatrix(coverage, products.size());
		//System.out.println(hash);

		//System.out.println(mutants);
        reader.close();
    }

    public void printMatrix(int[][] matrix, int columns) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(matrix[i][j]);
                if (j + 1 != columns) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
}
