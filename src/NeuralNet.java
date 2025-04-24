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
    double[][] error_signal;
    double[][] output_derivative;

    public NeuralNet(int [] Network_Sizes_By_Layer, double BiasLower, double BiasUpper, double WeightLower, double WeightUpper){
        this.Network_Sizes_By_Layer=Network_Sizes_By_Layer;
        inputSize=Network_Sizes_By_Layer[0];
        LayerNum=Network_Sizes_By_Layer.length;
        outputSize=Network_Sizes_By_Layer[LayerNum-1];

        output=new double[LayerNum][];
        weights=new double[LayerNum][][];
        bias=new double[LayerNum][];

        error_signal=new double[LayerNum][outputSize];
        output_derivative=new double[LayerNum][outputSize];
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

    public void train(TrainSet set, int loops, int batch_size, double learningRate) {
        if(set.inputSize != inputSize || set.outputSize != outputSize) return;
        for(int i = 0; i < loops; i++) {
            TrainSet batch = set.extractBatch(batch_size);
            for(int b = 0; b < batch_size; b++) {
                RunNeuralNet(batch.getInput(b), batch.getOutput(b),learningRate);
            }
        }
    }
    public void trainDifferent(TrainSet set, int loops, int batch_size, double learningRate) {
        if(set.inputSize != inputSize || set.outputSize != outputSize) return;
        for(int i = 0; i < loops; i++) {
            TrainSet batch = set.extractBatch(batch_size);
            for(int b = 0; b < batch_size; b++) {
                TrainHelper(batch.getInput(b), batch.getOutput(b));;
            }
            updateWeights(learningRate);
        }
    }


    public double MSE(double[] input, double[] target) {
        if(input.length != inputSize || target.length != outputSize) return 0;
        feedForward(input);
        double v = 0;
        for(int i = 0; i < target.length; i++) {
            v += (target[i] - output[LayerNum-1][i]) * (target[i] - output[LayerNum-1][i]);
        }
        return v / (2 * target.length);
    }

    public double MSE(TrainSet set) {
        double v = 0;
        for(int i = 0; i< set.size(); i++) {
            v += MSE(set.getInput(i), set.getOutput(i));
        }
        return v / set.size();
    }

    private double sigmoid(double x){
        return(1/(1+Math.exp(-x)));
    }
    public double[] feedForward(double[] input) {
        if(input.length != this.inputSize) return null;
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
    public void TrainHelper(double [] input, double [] target){
        if(input.length!=inputSize||target.length!=outputSize){
            return;
        }
        feedForward(input);
        backprop_error(target);
    }
    public void RunNeuralNet(double [] input, double [] target, double learningRate){
        if(input.length!=inputSize||target.length!=outputSize){
            return;
        }
        feedForward(input);
        backprop_error(target);
        updateWeights(learningRate);

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
                this.error_signal[layer][node] = sum * output_derivative[layer][node];
            }
        }
    }
    public void updateWeights(double learningRate){
        for (int layer = 1; layer < LayerNum; layer++) {
            for (int node = 0; node < Network_Sizes_By_Layer[layer]; node++) {
                bias[layer][node] += -learningRate * error_signal[layer][node];
                for (int preNode = 0; preNode < Network_Sizes_By_Layer[layer - 1]; preNode++) {
                    weights[layer][node][preNode] += -learningRate * output[layer - 1][preNode] * error_signal[layer][node];
                }
            }
        }
    }

    public static void main(String[] args){

        NeuralNet net = new NeuralNet(new int[]{4,3,3,2},-0.5,0.7,-1,1);

        TrainSet set = new TrainSet(4, 2);
        set.addData(new double[]{0.1,0.2,0.3,0.4}, new double[]{0.9,0.1});
        set.addData(new double[]{0.9,0.8,0.7,0.6}, new double[]{0.1,0.9});
        set.addData(new double[]{0.3,0.8,0.1,0.4}, new double[]{0.3,0.7});
        set.addData(new double[]{0.9,0.8,0.1,0.2}, new double[]{0.7,0.3});

        net.train(set, 100000, 4,0.3);

        for(int i = 0; i < 4; i++) {
            System.out.println(Arrays.toString(net.feedForward(set.getInput(i))));
        }
        System.out.println(net.MSE(set));

    }
}
