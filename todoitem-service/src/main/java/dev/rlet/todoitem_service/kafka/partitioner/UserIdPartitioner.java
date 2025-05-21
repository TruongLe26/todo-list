package dev.rlet.todoitem_service.kafka.partitioner;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class UserIdPartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] bytes,
                         Object value, byte[] bytes1, Cluster cluster) {
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        int numberOfPartitions = partitions.size();

        if (key == null) {
            return ThreadLocalRandom.current().nextInt(numberOfPartitions);
        }

        String normalizedKey = key.toString().trim().toLowerCase();
        return Math.abs(normalizedKey.hashCode()) % numberOfPartitions;
    }

    @Override
    public void close() {}
    @Override
    public void configure(Map<String, ?> map) {}
}
