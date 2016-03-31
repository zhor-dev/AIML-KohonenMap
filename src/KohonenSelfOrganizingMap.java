public class KohonenSelfOrganizingMap extends Network {

    private double learningRate = 0.1;
    private int learningRadius = 7;
    private int squaredRadius = 2 * 7 * 7;
    private int layerWidth;
    private int layerHeight;

    public KohonenSelfOrganizingMap(int layerSize, double[] input) {
        super(layerSize, input);
        this.layerHeight = this.layerWidth = (int)Math.sqrt(layerSize);
        if (layerWidth * layerWidth != layerSize) {
            throw new IllegalArgumentException("Layer size must be some number square!");
        }
    }

    public KohonenSelfOrganizingMap(int layerSize, double[] input, int width, int height) {
        super(layerSize, input);
        if (width * height != layerSize) {
            throw new IllegalArgumentException("Layer size must be some number square!");
        }
        this.layerHeight = height;
        this.layerWidth = width;
    }

    @Override
    public double[] output() {
        int outLength = getLayer().getNeurons().length;
        double[] out = new double[outLength];
        for (int i = 0; i < outLength; ++i) {
            out[i] = 0.0;
        }
        out[getWinner()] = 1.0;
        return out;
    }

    public void learn() {
        if (learningRadius == 0) {
            updateWeights(getWinner(), 1);
        } else {
            int winner = getWinner();
            updateWeights(winner, 1);
            updateOnRadiusWeights(winner % layerHeight, winner / layerHeight, learningRadius);
        }
    }

    public void batchLearning() {

    }

    private void updateWeights(int index, double merge) {
        for (int i = 0; i < getLayer().getNeurons()[index].getWeights().length; ++i) {
            getLayer().getNeurons()[index].getWeights()[i] +=
                    learningRate * merge * (
                    getLayer().getNeurons()[index].getInput()[i] -
                    getLayer().getNeurons()[index].getWeights()[i]
                    );
        }
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public void setLearningRadius(int learningRadius) {
        this.learningRadius = learningRadius;
        this.squaredRadius = learningRadius * learningRadius;
    }

    private void updateOnRadiusWeights(int x, int y, int radius) {
        int n = 1;
        while (n <= radius) {
            if (x - n >= 0) {
                int w = (0 > y - n) ? 0 : y - n;
                int h = (y + n < layerWidth) ? y + n : layerWidth - 1;
                for (int j = w; j <= h; ++j) {
                    double distance = n * n + (j - y) * (j - y);
                    if (distance <= radius) {
                        updateWeights((x - n) + layerHeight * j, Math.exp(distance / 2 * squaredRadius));
                    }
                }
            }
            if (x + n < layerHeight) {
                int w = (0 > y - n) ? 0 : y - n;
                int h = (y + n < layerWidth) ? y + n : layerWidth - 1;
                for (int j = w; j <= h; ++j) {
                    double distance = n * n + (j - y) * (j - y);
                    if (distance <= radius) {
                        updateWeights((x + n) + layerHeight * j, Math.exp(distance / 2 * squaredRadius));
                    }
                }
            }
            if (y - n >= 0) {
                int w = (0 > x - n + 1) ? 0 : x - n + 1;
                int h = (x + n - 1 < layerHeight) ? x + n - 1 : layerHeight - 1;
                for (int j = w; j <= h; ++j) {
                    double distance = (j - x) * (j - x) + n * n;
                    if (distance <= radius) {
                        updateWeights(j + layerHeight * (y - n), Math.exp(distance / 2 * squaredRadius));
                    }
                }
            }
            if (y + n < layerWidth) {
                int w = (0 > x - n + 1) ? 0 : x - n + 1;
                int h = (x + n - 1 < layerHeight) ? x + n - 1 : layerHeight - 1;
                for (int j = w; j <= h; ++j) {
                    double distance = (j - x) * (j - x) + n * n;
                    if (distance <= radius) {
                        updateWeights(j + layerHeight * (y + n), Math.exp(distance / 2 * squaredRadius));
                    }
                }
            }
            ++n;
        }
    }
}