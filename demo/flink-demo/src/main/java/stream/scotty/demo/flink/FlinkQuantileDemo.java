package stream.scotty.demo.flink;

import stream.scotty.core.windowType.*;
import stream.scotty.flinkconnector.*;
import stream.scotty.demo.flink.windowFunctions.*;
import org.apache.flink.api.java.tuple.*;
import org.apache.flink.streaming.api.*;
import org.apache.flink.streaming.api.datastream.*;
import org.apache.flink.streaming.api.environment.*;

import java.io.*;

public class FlinkQuantileDemo implements Serializable {

    public static void main(String[] args) throws Exception {
        LocalStreamEnvironment sev = StreamExecutionEnvironment.createLocalEnvironment();
        sev.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStream<Tuple2<Integer, Integer>> stream = sev.addSource(new DemoSource());

        KeyedScottyWindowOperator<Tuple, Tuple2<Integer, Integer>, Tuple2<Integer, Integer>> windowOperator =
                new KeyedScottyWindowOperator<>(new QuantileWindowFunction(0.5));

        windowOperator.addWindow(new TumblingWindow(WindowMeasure.Time, 1000));

        stream
                .keyBy(0)
                .process(windowOperator)
                .map(x -> x.getAggValues().get(0).f1)
                .print();

        sev.execute("demo");
    }

}
