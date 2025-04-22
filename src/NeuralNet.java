import java.math.*;
import java.util.*;
public class NeuralNet {
    int inputSize;
    int outputSize;
    int LayerNum;
    int[] Network_Sizes_By_Layer;
    double[][] output;
    double[][][]weights;
    double[][] bias;
    double learningRate;
    double[][] error_signal;
    double[][] output_derivative;

    public NeuralNet(int [] Network_Sizes_By_Layer, double learningRate, double BiasLower, double BiasUpper, double WeightLower, double WeightUpper){
        this.Network_Sizes_By_Layer=Network_Sizes_By_Layer;
        inputSize=Network_Sizes_By_Layer[0];
        LayerNum=Network_Sizes_By_Layer.length;
        outputSize=Network_Sizes_By_Layer[LayerNum-1];

        output=new double[LayerNum][];
        weights=new double[LayerNum][][];
        bias=new double[LayerNum][];

        error_signal=new double[LayerNum][outputSize];
        output_derivative=new double[LayerNum][outputSize];

        this.learningRate=learningRate;
        for (int i=0;i<LayerNum;i++){
            this.output[i]=new double[Network_Sizes_By_Layer[i]];
            this.bias[i]=new double[Network_Sizes_By_Layer[i]];
            this.bias[i]=createRandomArray(Network_Sizes_By_Layer[i],BiasLower,BiasUpper);
            error_signal[i]=new double[Network_Sizes_By_Layer[i]];
            output_derivative[i]=new double[Network_Sizes_By_Layer[i]];
            if (i>0){
                weights[i]=new double[Network_Sizes_By_Layer[i]][Network_Sizes_By_Layer[i-1]];
                weights[i]=initializeWeights(Network_Sizes_By_Layer[i],Network_Sizes_By_Layer[i-1],WeightLower,WeightUpper);
            }
        }
    }
    private static double[] createRandomArray(int x, double lowerBound, double upperBound){
        if (x < 1){
            return null;
        }
        double[] ar=new double[x];
        for (int i = 0; i < x; i++){
            ar[i] = Math.random()*(upperBound-lowerBound)+lowerBound;
        }
        return ar;
    }
    public double[][] initializeWeights(int x, int y, double lowerBound, double upperBound){
        if(x < 1 || y < 1){
            return null;
        }
        double[][] ar=new double[x][y];
        for (int i = 0; i < x; i++){
            ar[i] = createRandomArray(y, lowerBound, upperBound);
        }
        return ar;
    }
    private double sigmoid(double x){
        return(1/(1+Math.exp(-x)));
    }
    public double[] feedForward(double[] input) {
        this.output[0] = input;
        for (int layer = 1; layer < LayerNum; layer++) {
            for (int node = 0; node < Network_Sizes_By_Layer[layer]; node++){
                double sum = bias[layer][node];
                for (int preNode = 0; preNode < Network_Sizes_By_Layer[layer-1]; preNode++) {
                    sum += output[layer-1][preNode] * weights[layer][node][preNode];
                }
                output[layer][node] = sigmoid(sum);
                output_derivative[layer][node] = output[layer][node] * (1 - output[layer][node]);
            }
        }
        return output[LayerNum-1];
    }
    public void RunNeuralNet(double [] input, double [] target){
        if(input.length!=inputSize||target.length!=outputSize){
            return;
        }
        feedForward(input);
        backprop_error(target);
        updateWeights(learningRate);

    }
    public void RunSets(List<double[]> input, List<double[]> output, int batch_size, double learningRate){
        if (input.size() != output.size()) {
            throw new IllegalStateException("inputBatch and targetBatch should have the same size");
        }
        if (input.isEmpty()) {
            throw new IllegalStateException("inputBatch and targetBatch should not be empty");
        }
        double[][] weightsSum;
        double[] biasSum;
    }
    public void backprop_error(double[] expected_output) {
        for (int node = 0; node < Network_Sizes_By_Layer[LayerNum - 1]; node++) {
            error_signal[LayerNum - 1][node] = (output[LayerNum - 1][node] - expected_output[node]) * output_derivative[LayerNum-1][node];
        }
        for (int layer = LayerNum-2; layer > 0; layer --){
            for (int node=0; node < Network_Sizes_By_Layer[layer]; node++){
                double sum = 0;
                for (int nextNode = 0; nextNode < Network_Sizes_By_Layer[layer+1]; nextNode++){
                    sum += weights[layer+1][nextNode][node] * error_signal[layer+1][nextNode];
                }
            }
        }
    }
    public void updateWeights(double learningRate){
        for (int layer = 1; layer < LayerNum; layer++) {
            for (int node = 0; node < Network_Sizes_By_Layer[layer]; node++) {
                for (int preNode = 0; preNode < Network_Sizes_By_Layer[layer - 1]; preNode++) {
                    weights[layer][node][preNode] += -learningRate * output[layer - 1][preNode] * error_signal[layer][node];
                }
                bias[layer][node] += -learningRate * error_signal[layer][node];

            }
        }
    }

    public static void main(String[] args){
        NeuralNet net=new NeuralNet(new int[]{4,1,2,5,6,7,10,11,3},1, 0, 1, 0,1);
        double[] input = new double[]{0.1,1.8,0.2,0.9};
        double[] target = new double[]{0,0.2,0.5};
        for(int i=0;i<1000000;i++){
            net.RunNeuralNet(input,target);
        }
        double[] o =net.feedForward(input);
        System.out.println(Arrays.toString(o));
    }
}
