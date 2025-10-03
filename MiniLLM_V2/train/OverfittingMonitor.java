package MiniLLM_V2.train;

import java.util.ArrayList;

import java.util.List;

import MiniLLM_V2.configuration.Configuration;

import	MiniLLM_V2.utilities.beep_generator.Beep;

//********************************************************************************************************************************//

public class OverfittingMonitor
{
    private final int patience;
    private final double minDelta;
    private final List<Double> lossHistory;
    private final List<Double> validationLossHistory;
    private double bestLoss;
    private int waitCounter;
    private boolean overfittingEminentWarning;
    private boolean overfittingAlertWarning;
    private boolean overfittingWarning;

//********************************************************************************************************************************//

    public OverfittingMonitor(int patience, double minDelta)
    {
        this.patience = patience;
        this.minDelta = minDelta;
        this.lossHistory = new ArrayList<>();
        this.validationLossHistory = new ArrayList<>();
        this.bestLoss = Double.MAX_VALUE;
        this.waitCounter = 0;
        this.overfittingEminentWarning = false;
        this.overfittingAlertWarning = false;
        this.overfittingWarning = false;
    }

//********************************************************************************************************************************//

	//
	// Monitor Training Loss Only
	//
    public void monitorTraining(double currentLoss)
    {
        lossHistory.add(currentLoss);
		//
        // Check if we have improvement
        //
        if (currentLoss < bestLoss - minDelta) {
            bestLoss = currentLoss;
            waitCounter = 0;
            resetWarnings(); 								// Reset warnings if we see improvement
            return;
        }
		//
        // No significant improvement
        //
        waitCounter++;

        checkOverfittingConditions();
    }

//********************************************************************************************************************************//

    /**
     * Monitor with both training and validation loss (recommended for overfitting detection)
     */
    public void monitorWithValidation(double trainingLoss, double validationLoss)
    {
        lossHistory.add(trainingLoss);
        validationLossHistory.add(validationLoss);

        // Use validation loss for determining improvement
        if (validationLoss < bestLoss - minDelta) {
            bestLoss = validationLoss;
            waitCounter = 0;
            resetWarnings();
            return;
        }

        // No significant improvement in validation loss
        waitCounter++;
        checkOverfittingConditions();

        // Additional overfitting detection: training loss decreasing but validation loss increasing
        if (lossHistory.size() >= 3 && validationLossHistory.size() >= 3) {
            double recentTrainImprovement = lossHistory.get(lossHistory.size() - 3) - trainingLoss;
            double recentValDegradation = validationLoss - validationLossHistory.get(validationLossHistory.size() - 3);

            if (recentTrainImprovement > minDelta && recentValDegradation > minDelta) {
                System.out.println("  OVERFITTING: Training loss decreasing but validation loss increasing!");
                overfittingWarning = true;
            }
        }
    }

//********************************************************************************************************************************//

    private void checkOverfittingConditions()
    {
        int patienceThreshold1 = patience / 3;

        int patienceThreshold2 = patience * 2 / 3;

        if (!overfittingEminentWarning && waitCounter >= patienceThreshold1)
        {
            System.out.println("  OVERFITTING EMINENT: No improvement for " + waitCounter + " epochs");

            overfittingEminentWarning = true;

            if(Configuration.overfittingBeepFlag) {Beep.playBeep(48000, 500, 0.2f);}
        }
        else if (!overfittingAlertWarning && waitCounter >= patienceThreshold2)
        {
            System.out.println("  OVERFITTING ALERT: Significant period without improvement - " + waitCounter + " epochs");

            overfittingAlertWarning = true;

			if(Configuration.overfittingBeepFlag) {Beep.playBeep(48000, 500, 0.2f);}
}
        else if (!overfittingWarning && waitCounter >= patience)
        {
            System.out.println("  OVERFITTING: Extended period without improvement - " + waitCounter + " epochs");

            overfittingWarning = true;

			if(Configuration.overfittingBeepFlag) {Beep.playBeep(48000, 500, 0.2f);}
        }
    }

//********************************************************************************************************************************//

    /**
     * Check for Loss Divergence between Training and Validation
     */
    public void checkLossDivergence()
    {
        if (lossHistory.size() < 5 || validationLossHistory.size() < 5) {
            return;
        }
		//
        // Calculate Moving Averages to Smooth Out Noise
        //
        double trainAvg = calculateMovingAverage(lossHistory, 5);
        double valAvg = calculateMovingAverage(validationLossHistory, 5);

        double divergence = valAvg - trainAvg;

        if (divergence > minDelta * 5) { // Significant divergence
            System.out.printf("  LOSS DIVERGENCE: Validation loss (%.4f) significantly higher than training loss (%.4f)%n",
                            valAvg, trainAvg);
        }
    }

//********************************************************************************************************************************//

	//
	// Check if Loss has Plateaued
	//
    public void checkPlateau(int windowSize)
    {
        if (lossHistory.size() < windowSize) {
            return;
        }

        List<Double> recentLosses = getRecentValues(lossHistory, windowSize);
        double improvement = recentLosses.get(0) - recentLosses.get(recentLosses.size() - 1);

        if (Math.abs(improvement) < minDelta) {
            System.out.println("  LOSS PLATEAU: No significant improvement in last " + windowSize + " epochs");
        }
    }

//********************************************************************************************************************************//

	//
	// Comprehensive Monitoring with All Checks
	//
    public void comprehensiveMonitor(double trainingLoss, Double validationLoss)
    {
        if (validationLoss != null) {
            monitorWithValidation(trainingLoss, validationLoss);
            checkLossDivergence();
        } else {
            monitorTraining(trainingLoss);
        }

        checkPlateau(5); 														// Check for plateau over last 5 epochs
    }

//********************************************************************************************************************************//

    private double calculateMovingAverage(List<Double> values, int window)
    {
        int start = Math.max(0, values.size() - window);
        List<Double> recent = values.subList(start, values.size());
        return recent.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

//********************************************************************************************************************************//

    private List<Double> getRecentValues(List<Double> values, int count)
    {
        int start = Math.max(0, values.size() - count);
        return new ArrayList<>(values.subList(start, values.size()));
    }

//********************************************************************************************************************************//

    private void resetWarnings()
    {
        if (overfittingEminentWarning || overfittingAlertWarning || overfittingWarning) {
            System.out.println("  Improvement detected - overfitting warnings reset");
            overfittingEminentWarning = false;
            overfittingAlertWarning = false;
            overfittingWarning = false;
        }
    }

//********************************************************************************************************************************//

	//
    // Getters
    //
    public double getBestLoss() { return bestLoss; }

    public int getWaitCounter() { return waitCounter; }

    public boolean isOverfittingEminent() { return overfittingEminentWarning; }

    public boolean isOverfittingAlert() { return overfittingAlertWarning; }

    public boolean isOverfitting() { return overfittingWarning; }

//********************************************************************************************************************************//

    public void reset()
    {
        lossHistory.clear();
        validationLossHistory.clear();
        bestLoss = Double.MAX_VALUE;
        waitCounter = 0;
        resetWarnings();
    }

//********************************************************************************************************************************//

}
