package com.blocker.blocker_server.commons.kafka;

import com.blocker.blocker_server.chat.dto.request.SendMessageRequestDto;
import com.blocker.blocker_server.chat.dto.response.SendMessageResponseDto;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@EnableKafka
@Configuration
public class ProducerConfiguration {

    @Bean
    public KafkaTemplate<String, SendMessageResponseDto> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    // Kafka ProducerFactory를 생성하는 Bean 메서드
    @Bean
    public ProducerFactory<String, SendMessageResponseDto> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigurations());
    }

    // Kafka Producer 구성을 위한 설정값들을 포함한 맵을 반환하는 메서드
    @Bean
    public Map<String, Object> producerConfigurations() {
        Map<String, Object> map = new HashMap<>();
        JsonSerializer<SendMessageResponseDto> serializer = new JsonSerializer<>();


        map.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        map.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        map.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, serializer.getClass());

        return map;
    }


}
