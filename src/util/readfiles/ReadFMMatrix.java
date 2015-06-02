/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.readfiles;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import util.InstanceReader;

/**
 *
 * @author Prado Lima
 */
public class ReadFMMatrix {

    private List<String[]> _columns;
    private int[][] _matrix;
    private InstanceReader reader;
    private PivoType _pivoLine;
    private PivoType _pivoColumn;
    private String _path;

    public void read() {
        System.out.println("Path: " + _path);
        this.reader = new InstanceReader(_path);
        this._columns = new ArrayList<String[]>();

        reader.open();

        String line = null;

        List<Integer> lines = new ArrayList<Integer>();
        String pivo = getPivoLine().toString() + ":";

        while ((line = reader.readLine()) != null) {
            String[] part = line.split(pivo);
            part[0] = part[0].trim();
            part[1] = part[1].trim();

            String[] mut = part[1].split(", ");

            for (String s : mut) {
                s = s.replace("[", "").replace("]", "").trim();

                if (!lines.contains(Integer.valueOf(s))) {
                    lines.add(Integer.valueOf(s));
                }
            }

            // Process product
            part[0] = part[0].replaceAll(getPivoColumn().toString(), "").trim();
            part[0] = part[0].replaceAll(":", "").trim();

            getColumns().add(Integer.valueOf(part[0]), mut);
        }

        System.out.println(String.format("Number of %s: %s", getPivoColumn(), _columns.size()));
        System.out.println(String.format("Number of %s: %s", getPivoLine(), lines.size()));
        Collections.sort(lines);

        HashMap<Integer, Integer> hash = new HashMap<Integer, Integer>();
        int index = 0;

        for (Integer mutantId : lines) {
            if (!hash.containsKey(mutantId)) {
                hash.put(mutantId, index++);
            }
        }

        _matrix = new int[lines.size()][getColumns().size()];

        for (int i = 0; i < getColumns().size(); i++) {
            String[] mut = getColumns().get(i);
            for (String s : mut) {
                s = s.replace("[", "").replace("]", "").trim();
                int id = hash.get(Integer.valueOf(s));
                _matrix[id][i] = 1;
            }
        }

        reader.close();
    }

    public void generateFile() {
        // The name of the file to open.
        String path = this._path + "_BinaryMatrix.txt";
        path = path.replace("__", "_");

        try {
            // Assume default encoding.
            FileWriter fileWriter = new FileWriter(path);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(getColumns().size() + "");
            bufferedWriter.newLine();
            bufferedWriter.write(_matrix.length + "");
            bufferedWriter.newLine();

            for (int i = 0; i < _matrix.length; i++) {
                for (int j = 0; j < getColumns().size(); j++) {
                    bufferedWriter.write(_matrix[i][j] + "");
                    if (j + 1 != getColumns().size()) {
                        bufferedWriter.write(" ");
                    }
                }

                if (i + 1 < _matrix.length) {
                    bufferedWriter.newLine();
                }
            }

            // Always close files.
            bufferedWriter.close();

            System.out.println("The file was generated!");
        } catch (IOException ex) {
            System.out.println("Error writing to file '" + path + "'");
            // Or we could just do this:
            // ex.printStackTrace();
        }
    }

    public void printMatrix() {
        System.out.println("" + getColumns().size());
        System.out.println("" + _matrix.length);
        for (int i = 0; i < _matrix.length; i++) {
            for (int j = 0; j < getColumns().size(); j++) {
                System.out.print(_matrix[i][j]);
                if (j + 1 != getColumns().size()) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    /**
     * @return the _matrix
     */
    public int[][] getMatrix() {
        return this._matrix;
    }

    /**
     * @return the _columns
     */
    public List<String[]> getColumns() {
        return _columns;
    }

    /**
     * @return the _pivoLine
     */
    public PivoType getPivoLine() {
        return _pivoLine;
    }

    /**
     * @param _pivoLine the _pivoLine to set
     */
    public void setPivoLine(PivoType _pivoLine) {
        this._pivoLine = _pivoLine;
    }

    /**
     * @return the _pivoColumn
     */
    public PivoType getPivoColumn() {
        return _pivoColumn;
    }

    /**
     * @param _pivoColumn the _pivoColumn to set
     */
    public void setPivoColumn(PivoType _pivoColumn) {
        this._pivoColumn = _pivoColumn;
    }

    /**
     * @param _path the _path to set
     */
    public void setPath(String _path) {
        this._path = _path;
    }
}
