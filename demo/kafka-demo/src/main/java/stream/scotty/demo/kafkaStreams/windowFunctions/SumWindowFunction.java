package stream.scotty.demo.kafkaStreams.windowFunctions;
import stream.scotty.core.windowFunction.InvertibleReduceAggregateFunction;

public class SumWindowFunction implements InvertibleReduceAggregateFunction<Integer> {
    @Override
    public Integer invert(Integer currentAggregate, Integer toRemove) {
        return currentAggregate-toRemove;
    }

    @Override
    public Integer combine(Integer partialAggregate1, Integer partialAggregate2) {
        return partialAggregate1+partialAggregate2;
    }
}
