package stream.scotty.demo.beam.windowFunctions;

import stream.scotty.core.windowFunction.InvertibleReduceAggregateFunction;
import org.apache.beam.sdk.values.KV;

import java.io.Serializable;

public class Count implements InvertibleReduceAggregateFunction<KV<Integer, Integer>>, Serializable {

    @Override
    public KV<Integer, Integer> lift(KV<Integer, Integer> inputTuple) {
        return KV.of(inputTuple.getKey(), 1);
    }

    @Override
    public KV<Integer, Integer> combine(KV<Integer, Integer> partialAggregate1, KV<Integer, Integer> partialAggregate2) {
        return KV.of(partialAggregate1.getKey(), partialAggregate1.getValue() + partialAggregate2.getValue());
    }

    @Override
    public KV<Integer, Integer> invert(KV<Integer, Integer> currentAggregate, KV<Integer, Integer> toRemove) {
        return KV.of(currentAggregate.getKey(), currentAggregate.getValue() - toRemove.getValue());
    }
}