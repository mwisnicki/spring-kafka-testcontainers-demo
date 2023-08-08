package com.example.springkafkatestcontainersdemo;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.junit.jupiter.api.extension.*;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Resolve {@link Consumer} parameter to per-test instance of consumer suitable for use with {@link org.springframework.kafka.test.utils.KafkaTestUtils}.
 */
public class TestConsumerParameterResolver implements ParameterResolver, TestInstancePreDestroyCallback {
    private final List<Consumer<?, ?>> consumers = new ArrayList<>();
    private final Pattern invalidIdPattern = Pattern.compile("[^a-zA-Z0-9._-]|(\\bConsumer\\b)+");

    private String normalizeGroupId(String testName) {
        return invalidIdPattern.matcher(testName).replaceAll("");
    }

    @Override
    public void preDestroyTestInstance(ExtensionContext context) {
        TestInstancePreDestroyCallback.preDestroyTestInstances(context, testInstance -> {
            // custom logic that processes testInstance
            consumers.forEach(Consumer::close);
            consumers.clear();
        });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == Consumer.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        var appContext = SpringExtension.getApplicationContext(extensionContext);
        //ObjectProvider<ConsumerFactory<?, ?>> consumerFactoryProvider = appContext.getBeanProvider(ResolvableType.forType(parameterContext.getParameter().getParameterizedType()));
        var consumerFactoryProvider = appContext.getBeanProvider(ConsumerFactory.class);
        var consumerFactory = consumerFactoryProvider.getObject();

        var props = new Properties();
        // Consumer may be started before topics even exist in which case it will attach after message is sent and creates topic
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        var groupId = "test-" + normalizeGroupId(extensionContext.getDisplayName());
        var consumer = consumerFactory.createConsumer(groupId, null, null, props);

        consumers.add(consumer);
        return consumer;
    }
}
