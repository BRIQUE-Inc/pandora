package org.chronotics.pandora.math;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.distribution.*;

public class MathUtil {
    public static List<Double> getGaussianDistribution(Double dbMean, Double dbSd, Long lNumOfItem) {
        List<Double> lstDist = new ArrayList<>();

        NormalDistribution objNormDist = new NormalDistribution(dbMean, dbSd);
        double[] arrSample = objNormDist.sample(lNumOfItem.intValue());

        for (double dbValue : arrSample) {
            lstDist.add(Double.valueOf(dbValue));
        }

        return lstDist;
    }

    public static List<Double> getContinuousUniformDistribution(Double dbMin, Double dbMax, Long lNumOfItem) {
        List<Double> lstDist = new ArrayList<>();

        UniformRealDistribution objUniformDist = new UniformRealDistribution(dbMin, dbMax);

        double[] arrSample = objUniformDist.sample(lNumOfItem.intValue());

        for (double dbValue : arrSample) {
            lstDist.add(Double.valueOf(dbValue));
        }

        return lstDist;
    }

    public static List<Double> getSMOTE(Double dbOver, Double dbUnder, Double dbK) {
        List<Double> lstSMOTE = new ArrayList<>();

        return lstSMOTE;
    }
}
