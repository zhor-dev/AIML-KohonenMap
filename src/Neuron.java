
public class Neuron {
    private double[] input;
    private double[] weights;
    private boolean enableNormalization = false;

    public Neuron(double[] i) {
        if (enableNormalization) {
            normalizeInputs(i);
        } else {
            this.input = i;
        }
        weights = new double[input.length];
        setRandRange(0, 1);
    }

    public void setRandRange(double randMin, double randMax) {
        for (int i = 0; i < weights.length; ++i) {
            weights[i] = randMin + (randMax - randMin) * Math.random();
        }
    }

    public double summingBlock() {
        double sum = 0.0;
        for (int i = 0; i < input.length; ++i) {
            sum += Math.abs(input[i] - weights[i]);
        }
        return sum;
    }

    public void setWeights(double[] weights) {
        this.weights = weights;
    }

    public void setInput(double[] input) {
        if (enableNormalization) {
            normalizeInputs(input);
        } else {
            this.input = input;
        }
    }

    public double[] getInput() {
        return input;
    }

    public double[] getWeights() {
        return weights;
    }

    public void setNormalization() {
        enableNormalization = true;
    }

    private void normalizeInputs(double[] i) {
        this.input = new double[i.length];
        double div = 0.0;
        for (double anI : i) {
            div += anI * anI;
        }
        div = Math.sqrt(div);
        for (int j = 0; j < i.length; ++j) {
            input[j] = i[j] / div;
        }
    }
}
