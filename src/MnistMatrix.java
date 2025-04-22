public class MnistMatrix {
    int [][] data;
    int nRows;
    int nCols;
    int label;

    public MnistMatrix(int nRows, int nCols){
        this.nRows=nRows;
        this.nCols=nCols;

        data = new int[nRows][nCols];
    }

    public int getLabel() {
        return label;
    }

    public int getnRows() {
        return nRows;
    }

    public int getnCols() {
        return nCols;
    }

    public void setLabel(int label) {
        this.label = label;
    }
    public int getValue(int row, int col){
        return data[row][col];
    }
    public void setData(int row, int col, int value){
        data[row][col]=value;
    }
}
