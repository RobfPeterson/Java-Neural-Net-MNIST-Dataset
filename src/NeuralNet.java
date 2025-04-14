import java.math.*;
import java.util.*;
public class NeuralNet {

    double [] input;
    double[][] weights_input_hidden;
    double[] hidden_layer_output;
    double[][] weights_hidden_output;
    double[] hidden_bias;
    double[] output_bias;
    double [] output;
    double learningRate;

    public NeuralNet(int inputSize, int hiddenSize, int outputSize, double learningRate){
        input=new double[inputSize];
        weights_input_hidden=new double[hiddenSize][inputSize];
        hidden_layer_output=new double[hiddenSize];
        weights_hidden_output=new double[outputSize][hiddenSize];
        hidden_bias=new double[hiddenSize];
        output_bias=new double[outputSize];
        output=new double[outputSize];
        this.learningRate=learningRate;
        initializeWeights();
        initializeBiases();
    }
    private void initializeWeights(){
        for (int i = 0; i < weights_input_hidden.length; i++) {
            for (int j = 0; j < weights_input_hidden[i].length; j++) {
                weights_input_hidden[i][j] = Math.random();
            }
        }
        for (int i = 0; i < weights_hidden_output.length; i++) {
            for (int j = 0; j < weights_hidden_output[i].length; j++) {
                weights_hidden_output[i][j] = Math.random();
            }
        }
    }
    private void initializeBiases() {
        for (int i = 0; i < hidden_bias.length; i++) {
            hidden_bias[i] = 0;
        }
        for (int i=0;i<output_bias.length;i++){
            output_bias[i]=0;
        }
    }
    private double sigmoid(double x){
        return(1/(1+Math.exp(-x)));
    }
    public double[] feedForward(double[] input) {
        for (int i = 0; i < hidden_layer_output.length; i++) {
            double sum = 0;
            for (int j = 0; j < input.length; j++) {
                sum += input[j] * weights_input_hidden[i][j];
            }
            hidden_layer_output[i] = sigmoid(sum+hidden_bias[i]);
        }
        for (int i = 0; i < output.length; i++) {
            double sum = 0;
            for (int j = 0; j < hidden_layer_output.length; j++) {
                sum += output[j] * weights_hidden_output[i][j];
            }
            output[i] = sigmoid(sum+output_bias[i]);
        }
        return output;
    }
    public void backprop_error(double[] expected_output){
        for (int i = 0; i < expected_output.length; i++) {
            double d = expected_output[i];
            if (d < 0 || d > 1) {
                if (d < 0) {
                    expected_output[i] = 0 + learningRate;
                }
                else {
                    expected_output[i] = 1 - learningRate;
                }
            }
        }
        for (int i=0;i<output.length;i++){
            double error=0;
            for (int j=0;j<hidden_layer_output.length;j++){
                
            }
        }
    }
}
