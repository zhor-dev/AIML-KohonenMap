public class Layer {
    private Neuron[] neurons;

    public Layer(int neuronsCount, double[] i) {
        neurons = new Neuron[neuronsCount];
        for (int j = 0; j < neuronsCount; ++j) {
            neurons[j] = new Neuron(i);
        }
    }

    public Neuron[] getNeurons() {
        return this.neurons;
    }

    public double[] output() {
        double[] out = new double[neurons.length];
        for (int j = 0; j < out.length; ++j) {
            out[j] = neurons[j].summingBlock();
        }
        return out;
    }

}
