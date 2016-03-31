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

    public double learn() {
        if (learningRadius == 0) {
            return updateWeights(getWinner(), 1);
        }
        int winner = getWinner();
        updateWeights(winner, 1);
        return updateOnRadiusWeights(winner % layerHeight, winner / layerHeight, learningRadius);
    }

    private double updateWeights(int index, double merge) {
        double err = 0.0;
        for (int i = 0; i < getLayer().getNeurons()[index].getWeights().length; ++i) {
            err += getLayer().getNeurons()[index].getInput()[i] -
                    getLayer().getNeurons()[index].getWeights()[i];
            getLayer().getNeurons()[index].getWeights()[i] +=
                    learningRate * merge * (
                    getLayer().getNeurons()[index].getInput()[i] -
                    getLayer().getNeurons()[index].getWeights()[i]
                    );
        }
        return err;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public void setLearningRadius(int learningRadius) {
        this.learningRadius = learningRadius;
        this.squaredRadius = learningRadius * learningRadius;
    }

    private double updateOnRadiusWeights(int x, int y, int radius) {
        double err = 0.0;
        int n = 1;
        while (n <= radius) {
            if (x - n >= 0) {
                int w = (0 > y - n) ? 0 : y - n;
                int h = (y + n < layerWidth) ? y + n : layerWidth - 1;
                for (int j = w; j <= h; ++j) {
                    double distance = Math.sqrt(n * n + (j - y) * (j - y));
                    if (distance <= radius) {
                        err += updateWeights((x - n) + layerHeight * j, Math.exp(-distance / 2 * squaredRadius));
                    }
                }
            }
            if (x + n < layerHeight) {
                int w = (0 > y - n) ? 0 : y - n;
                int h = (y + n < layerWidth) ? y + n : layerWidth - 1;
                for (int j = w; j <= h; ++j) {
                    double distance = Math.sqrt(n * n + (j - y) * (j - y));
                    if (distance <= radius) {
                        err += updateWeights((x + n) + layerHeight * j, Math.exp(-distance / 2 * squaredRadius));
                    }
                }
            }
            if (y - n >= 0) {
                int w = (0 > x - n + 1) ? 0 : x - n + 1;
                int h = (x + n - 1 < layerHeight) ? x + n - 1 : layerHeight - 1;
                for (int j = w; j <= h; ++j) {
                    double distance = Math.sqrt((j - x) * (j - x) + n * n);
                    if (distance <= radius) {
                        err += updateWeights(j + layerHeight * (y - n), Math.exp(-distance / 2 * squaredRadius));
                    }
                }
            }
            if (y + n < layerWidth) {
                int w = (0 > x - n + 1) ? 0 : x - n + 1;
                int h = (x + n - 1 < layerHeight) ? x + n - 1 : layerHeight - 1;
                for (int j = w; j <= h; ++j) {
                    double distance = Math.sqrt((j - x) * (j - x) + n * n);
                    if (distance <= radius) {
                        err += updateWeights(j + layerHeight * (y + n), Math.exp(-distance / 2 * squaredRadius));
                    }
                }
            }
            ++n;
        }
        return err;
    }
}