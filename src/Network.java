import java.io.*;

public class Network {
    private Layer layer;

    public Network(int layerSize, double[] input) {
        layer = new Layer(layerSize, input);
    }

    public double[] output() {
        return layer.output();
    }

    public int getWinner() {
        double[] out = output();
        double min = out[0];
        int minIndex = 0;
        for (int i = 1; i < out.length; ++i) {
            if (out[i] < min) {
                min = out[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    public Layer getLayer() {
        return layer;
    }

    public void saveWeights() {
        String res = "";
        BufferedWriter output = null;
        try {
            File file = new File("src/weights.txt");
            output = new BufferedWriter(new FileWriter(file));
            for (int i = 0; i < layer.getNeurons().length; ++i) {
                for (int j = 0; j < layer.getNeurons()[i].getWeights().length; ++j) {
                    res += layer.getNeurons()[i].getWeights()[j] + "\n";
                }
            }
            output.write(res);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static double[][] loadWeights(double [][]weights) {
        BufferedReader buffreader = null;
        try {
            FileReader fileReader = new FileReader("src/weights.txt");
            buffreader = new BufferedReader(fileReader);
            for (int i = 0; i < weights.length; ++i) {
                for (int j = 0; j < weights[0].length; ++j) {
                    weights[i][j] = Double.parseDouble(buffreader.readLine());
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        } finally {
            try {
                assert buffreader != null;
                buffreader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return weights;
    }
}
