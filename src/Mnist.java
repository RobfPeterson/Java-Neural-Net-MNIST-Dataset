import java.io.File;
import java.io.IOException;
import java.io.*;

public class Mnist {
    public static void main(String[] args) {
        long startTime = System.nanoTime();
        NeuralNet network = new NeuralNet(new int[]{784,105,70, 35, 10},-0.5,0.7,-1,1);
        TrainSet set = createTrainSet(0,4999);
        trainData(network, set, 100, 50, 100, 0.7);
        TrainSet testSet = createTrainSet(5000,9999);
        testTrainSet(network, testSet, 10);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println(duration/1000000);
        System.out.println(duration/(1000000*1000));
    }
    public static TrainSet createTrainSet(int start, int end) {

        TrainSet set = new TrainSet(28 * 28, 10);

        try {

            String path = new File("").getAbsolutePath();

            MnistMatrix[] mnistMatrix = new MnistDataReader().readData("data/train-images.idx3-ubyte", "data/train-labels.idx1-ubyte");

            for(int i = start; i <= end; i++) {
                if(i % 100 ==  0){
                    System.out.println("prepared: " + i);
                }

                double[] input = new double[28 * 28];
                double[] output = new double[10];

                output[mnistMatrix[i].getLabel()] = 1d;
                for(int j = 0; j < 28; j++){
                    for (int x=0;x<28;x++) {
                        input[j*28+x]=mnistMatrix[i].getValue(j,x) / (double) 256;
                    }
                }
                set.addData(input, output);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return set;
    }
    public static void trainData(NeuralNet net,TrainSet set, int epochs, int loops, int batch_size, double learningRate) {
        for(int e = 0; e < epochs;e++) {
            net.train(set, loops, batch_size,learningRate);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>   "+ e+ "   <<<<<<<<<<<<<<<<<<<<<<<<<<");
        }
    }

    public static void testTrainSet(NeuralNet net, TrainSet set, int printSteps) {
        int correct = 0;
        for(int i = 0; i < set.size(); i++) {

            double highest = indexOfHighestValue(net.feedForward(set.getInput(i)));
            double actualHighest = indexOfHighestValue(set.getOutput(i));
            if(highest == actualHighest) {

                correct ++ ;
            }
            if(i % printSteps == 0) {
                System.out.println(i + ": " + (double)correct / (double) (i + 1));
            }
        }
        System.out.println("Testing finished, RESULT: " + correct + " / " + set.size()+ "  -> " + (double)correct / (double)set.size() +" %");
    }

    public static int indexOfHighestValue(double[] values){
        int index = 0;
        for(int i = 1; i < values.length; i++){
            if(values[i] > values[index]){
                index = i;
            }
        }
        return index;
    }
}
