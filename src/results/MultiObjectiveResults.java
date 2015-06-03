/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package results;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import util.ResultsUtil;

/**
 *
 * @author Prado Lima
 */
public class MultiObjectiveResults {

    public static void main(String[] args) throws IOException, FileNotFoundException, InterruptedException {

        List<String> instances = new ArrayList<>();
        instances.add("cas");
        instances.add("eshop");
        instances.add("james");
        instances.add("weatherstation");

        List<String> algorithms = new ArrayList<>();
        algorithms.add("NSGAII");
        algorithms.add("NSGAIII");

        int numberOfObjectives = 5;
        int numberOfExecutions = 30;

        calculatePFTrue(instances, algorithms, numberOfExecutions);
        //calculateHypervolumeResults(instances, algorithms, numberOfObjectives, numberOfExecutions);
        //calculateKruskalWallisForTuning(instances, algorithms, numberOfExecutions);
        //calculateKruskalWallisForAlgorithms(instances, algorithms, numberOfExecutions);
    }

    private static void calculateKruskalWallisForAlgorithms(List<String> instances, List<String> algorithms, int numberOfExecutions) throws IOException, InterruptedException {
        for (String instance : instances) {
            List<String> instancesKruskal = new ArrayList<>();
            instancesKruskal.add(instance);
            List<Path> paths = ResultsUtil.getPaths(instancesKruskal, algorithms);
            ResultsUtil.doKruskalWallisTest(paths, numberOfExecutions, "Hypervolume_");
        }
    }

    private static void calculateKruskalWallisForTuning(List<String> instances, List<String> algorithms, int numberOfExecutions) throws IOException, InterruptedException {
        for (String instance : instances) {
            List<String> instancesKruskal = new ArrayList<>();
            instancesKruskal.add(instance);
            for (String algorithm : algorithms) {
                List<String> algorithmsKruskal = new ArrayList<>();
                algorithmsKruskal.add(algorithm);
                List<Path> paths = ResultsUtil.getPaths(instancesKruskal, algorithmsKruskal);
                ResultsUtil.doKruskalWallisTest(paths, numberOfExecutions, "Hypervolume_");
            }
        }
    }

    private static void calculateHypervolumeResults(List<String> instances, List<String> algorithms, int numberOfObjectives, int numberOfExecutions) throws IOException {
        for (String instance : instances) {
            List<String> instanceAux = new ArrayList<>();
            instanceAux.add(instance);
            List<Path> paths = ResultsUtil.getPaths(instanceAux, algorithms);
            //write the files to be read
            System.out.println("Instância: " + instance);
            for (Path path : paths) {
                System.out.println(path);
            }
            System.out.println("--------------------------------------------------------------");
            ResultsUtil.writeHypervolume(paths, numberOfObjectives, numberOfExecutions);
        }
    }

    private static void calculatePFTrue(List<String> instances, List<String> algorithms, int numberOfExecutions) throws IOException {
        for (String instance : instances) {
            List<String> instancesKruskal = new ArrayList<>();
            instancesKruskal.add(instance);
            List<Path> paths = ResultsUtil.getPaths(instancesKruskal, algorithms);
            ResultsUtil.createPFTrue(paths);
        }
    }
}
