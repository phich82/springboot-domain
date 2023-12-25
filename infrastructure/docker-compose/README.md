### Start Zookeeper
    docker-compose -f common.yml -f zookeeper.yml up
    docker-compose -f common.yml -f zookeeper.yml down

### Start Kafka
    docker-compose -f common.yml -f kafka_cluster.yml up
    docker-compose -f common.yml -f kafka_cluster.yml down

### Initiate Kafka Topics
    docker-compose -f common.yml -f init_kafka.yml up
    docker-compose -f common.yml -f init_kafka.yml down



