package MiniLLM_V2.train;

import java.util.ArrayList;

import java.util.List;

public class TrainingStopper {
    private final int patience;
    private final double minDelta;
    private final List<Double> lossHistory;
    private double bestLoss;
    private int waitCounter;

    public TrainingStopper(int patience, double minDelta) {
        this.patience = patience;
        this.minDelta = minDelta;
        this.lossHistory = new ArrayList<>();
        this.bestLoss = Double.MAX_VALUE;
        this.waitCounter = 0;
    }

    /**
     * Check if training should stop based on current loss
     * @param currentLoss The current epoch's loss value
     * @return true if training should stop, false otherwise
     */
    public boolean shouldStop(double currentLoss) {
        lossHistory.add(currentLoss);

        // Check if we have improvement
        if (currentLoss < bestLoss - minDelta) {
            bestLoss = currentLoss;
            waitCounter = 0;
            return false;
        }

        // No significant improvement
        waitCounter++;

        // Stop if we've waited long enough without improvement
        if (waitCounter >= patience) {
            System.out.printf("Stopping training. No improvement for %d epochs. Best loss: %.4f%n",
                            patience, bestLoss);
            return true;
        }

        return false;
    }

    /**
     * Check if loss has plateaued (stopped improving significantly)
     * @param windowSize Number of recent epochs to check
     * @return true if loss has plateaued
     */
    public boolean hasPlateaued(int windowSize) {
        if (lossHistory.size() < windowSize) {
            return false;
        }

        List<Double> recentLosses = lossHistory.subList(
            lossHistory.size() - windowSize, lossHistory.size());

        double first = recentLosses.get(0);
        double last = recentLosses.get(recentLosses.size() - 1);
        double improvement = first - last;

        return Math.abs(improvement) < minDelta;
    }

    /**
     * Check if loss has become unstable (large fluctuations)
     * @param threshold Maximum allowed standard deviation for recent losses
     * @return true if loss is unstable
     */
    public boolean isUnstable(double threshold, int windowSize) {
        if (lossHistory.size() < windowSize) {
            return false;
        }

        List<Double> recentLosses = lossHistory.subList(
            lossHistory.size() - windowSize, lossHistory.size());

        double mean = recentLosses.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double variance = recentLosses.stream()
            .mapToDouble(l -> Math.pow(l - mean, 2))
            .average().orElse(0.0);
        double stdDev = Math.sqrt(variance);

        return stdDev > threshold;
    }

    /**
     * Comprehensive stopping condition combining multiple criteria
     */
    public boolean shouldStopComprehensive(double currentLoss, int plateauWindow, double instabilityThreshold) {
        return shouldStop(currentLoss) ||
               hasPlateaued(plateauWindow) ||
               isUnstable(instabilityThreshold, plateauWindow);
    }

    // Getters
    public double getBestLoss() { return bestLoss; }
    public int getWaitCounter() { return waitCounter; }
    public List<Double> getLossHistory() { return new ArrayList<>(lossHistory); }

    // Reset the stopper (useful for multiple training runs)
    public void reset() {
        lossHistory.clear();
        bestLoss = Double.MAX_VALUE;
        waitCounter = 0;
    }
}
