package stream.scotty.demo.storm;

import stream.scotty.core.windowType.SessionWindow;
import stream.scotty.core.windowType.SlidingWindow;
import stream.scotty.core.windowType.TumblingWindow;
import stream.scotty.core.windowType.WindowMeasure;
import stream.scotty.stormconnector.KeyedScottyWindowOperator;
import stream.scotty.demo.storm.windowFunctions.Sum;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

/*
 * @author Batuhan Tüter
 * Runner class for Scotty on Storm
 *
 *
 * Input data should be in the following format:
 * <key,value,timeStamp>, timeStamp is type Long
 * */

public class ScottyDemoTopology {

    public static void main(String[] args) throws Exception {
        LocalCluster cluster = new LocalCluster();
        TopologyBuilder builder = new TopologyBuilder();

        Config conf = new Config();
        conf.setDebug(false);
        conf.setNumWorkers(1);
        conf.setMaxTaskParallelism(1);
        //Disable Acking
        conf.setNumAckers(0);

        KeyedScottyWindowOperator scottyBolt = new KeyedScottyWindowOperator<Integer, Integer>(new Sum(), 0);
        scottyBolt.addWindow(new TumblingWindow(WindowMeasure.Time, 1000));
        scottyBolt.addWindow(new SlidingWindow(WindowMeasure.Time, 1000, 250));
        scottyBolt.addWindow(new SessionWindow(WindowMeasure.Time, 1000));

        builder.setSpout("spout", new DataGeneratorSpout());
        builder.setBolt("scottyWindow", scottyBolt).fieldsGrouping("spout", new Fields("key"));
        builder.setBolt("printer", new PrinterBolt()).shuffleGrouping("scottyWindow");

        cluster.submitTopology("testTopology", conf, builder.createTopology());
        //cluster.killTopology("testTopology");
        //cluster.shutdown();
    }
}