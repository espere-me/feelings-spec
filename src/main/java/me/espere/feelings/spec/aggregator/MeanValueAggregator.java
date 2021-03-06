package me.espere.feelings.spec.aggregator;

import me.espere.feelings.spec.analyzer.WordAnalysis;
import me.espere.feelings.spec.VadValue;

import java.math.BigDecimal;
import java.util.Collection;

public class MeanValueAggregator implements Aggregator {
    private static final VadValue INITIAL_VALUE = new VadValue(
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            BigDecimal.ZERO
    );

    @Override
    public VadValue aggregate(String text, Collection<WordAnalysis> wordAnalyses) {
        if (wordAnalyses.isEmpty()) {
            return new VadValue(
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO
            );
        }

        VadValue accumulatedVadValue = wordAnalyses
                .stream()
                .map(WordAnalysis::getVadValue)
                .reduce(INITIAL_VALUE, (a, b) -> new VadValue(
                        a.getValence().add(b.getValence()),
                        a.getArousal().add(b.getArousal()),
                        a.getDominance().add(b.getDominance())
                ));

        BigDecimal numberOfVadValues = BigDecimal.valueOf(wordAnalyses.size());

        return new VadValue(
                calculateMeanValue(accumulatedVadValue.getValence(), numberOfVadValues),
                calculateMeanValue(accumulatedVadValue.getArousal(), numberOfVadValues),
                calculateMeanValue(accumulatedVadValue.getDominance(), numberOfVadValues)
        );
    }

    private BigDecimal calculateMeanValue(BigDecimal accumulatedValue, BigDecimal numberOfValues) {
        return accumulatedValue.divide(numberOfValues, BigDecimal.ROUND_HALF_UP);
    }
}
