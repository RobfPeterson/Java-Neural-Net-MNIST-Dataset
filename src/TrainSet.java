import java.util.ArrayList;
import java.util.*;
public class TrainSet {

    int inputSize;
    int outputSize;

    ArrayList<double[][]> data = new ArrayList<>();

    public TrainSet(int inputSize, int outputSize) {
        this.inputSize = inputSize;
        this.outputSize = outputSize;
    }

    public void addData(double[] in, double[] expected) {
        if (in.length != inputSize || expected.length != outputSize) return;
        data.add(new double[][]{in, expected});
    }

    public TrainSet extractBatch(int size) {
        if (size > 0 && size <= data.size()) {
            TrainSet set = new TrainSet(inputSize, outputSize);
            Integer[] ids = randomValues(0, data.size() - 1, size);
            for (Integer i : ids) {
                set.addData(this.getInput(i), this.getOutput(i));
            }
            return set;
        } else return this;
    }

    public static Integer[] randomValues(int lowerBound, int upperBound, int amount) {

        lowerBound --;

        if(amount > (upperBound-lowerBound)){
            return null;
        }

        Integer[] values = new Integer[amount];
        for(int i = 0; i< amount; i++){
            int n = (int)(Math.random() * (upperBound-lowerBound+1) + lowerBound);
            while(containsValue(values, n)){
                n = (int)(Math.random() * (upperBound-lowerBound+1) + lowerBound);
            }
            values[i] = n;
        }
        return values;
    }

    public double[] getInput(int index) {
        if (index >= 0 && index < data.size())
            return data.get(index)[0];
        else return null;
    }

    public double[] getOutput(int index) {
        if (index >= 0 && index < data.size())
            return data.get(index)[1];
        else return null;
    }

    public static void main(String[] args) {
        TrainSet set = new TrainSet(3, 2);

        for (int i = 0; i < 8; i++) {
            double[] a = new double[3];
            double[] b = new double[2];
            for (int k = 0; k < 3; k++) {
                a[k] = (double) ((int) (Math.random() * 10)) / (double) 10;
                if (k < 2) {
                    b[k] = (double) ((int) (Math.random() * 10)) / (double) 10;
                }
            }
            set.addData(a, b);
        }

        System.out.println(set);
        System.out.println(set.extractBatch(3));
    }
    public static <T extends Comparable<T>> boolean containsValue(T[] ar, T value){
        for(int i = 0; i < ar.length; i++){
            if(ar[i] != null){
                if(value.compareTo(ar[i]) == 0){
                    return true;
                }
            }

        }
        return false;
    }
    public String toString() {
        String s = "TrainSet ["+inputSize+ " ; "+outputSize+"]\n";
        int index = 0;
        for(double[][] r:data) {
            s += index +":   "+Arrays.toString(r[0]) +"  >-||-<  "+Arrays.toString(r[1]) +"\n";
            index++;
        }
        return s;
    }
}
