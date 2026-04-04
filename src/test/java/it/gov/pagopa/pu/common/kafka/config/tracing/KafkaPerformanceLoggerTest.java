package it.gov.pagopa.pu.common.kafka.config.tracing;

import io.micrometer.observation.ObservationRegistry;
import it.gov.pagopa.pu.iamsync.utils.MemoryAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.function.context.catalog.SimpleFunctionRegistry;
import org.springframework.cloud.function.observability.FunctionObservationConvention;
import org.springframework.integration.IntegrationMessageHeaderAccessor;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import java.util.Collections;
import java.util.Map;

import static it.gov.pagopa.pu.iamsync.performancelogger.PerformanceLoggerTest.assertPerformanceLogMessage;
import static it.gov.pagopa.pu.iamsync.performancelogger.PerformanceLoggerTest.buildPerformanceLoggerMemoryAppender;

@ExtendWith(MockitoExtension.class)
class KafkaPerformanceLoggerTest {

  @Mock
  private ObjectProvider<FunctionObservationConvention> functionObservationConventionProviderMock;
  @Mock
  private ObservationRegistry observationRegistryMock;

  private MemoryAppender memoryAppender;

  private KafkaPerformanceLogger kafkaPerformanceLogger;

  @BeforeEach
  void init() {
    Mockito.when(observationRegistryMock.isNoop())
      .thenReturn(true);
    Mockito.when(functionObservationConventionProviderMock.getIfAvailable(Mockito.notNull()))
      .thenReturn(null);

    this.memoryAppender = buildPerformanceLoggerMemoryAppender("INCOMING_EVENT");

    kafkaPerformanceLogger = new KafkaPerformanceLogger(observationRegistryMock, functionObservationConventionProviderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      observationRegistryMock,
      functionObservationConventionProviderMock);
  }

  @Test
  void givenNoMessageWhenDoApplyThenCallSuper() {
    // Given
    SimpleFunctionRegistry.FunctionInvocationWrapper targetFunction = Mockito.mock(SimpleFunctionRegistry.FunctionInvocationWrapper.class);

    // When
    kafkaPerformanceLogger.doApply(null, targetFunction);

    // Then
    Assertions.assertEquals(
      Collections.emptyList(),
      memoryAppender.getLoggedEvents()
    );
  }

  @Test
  void givenNoConsumerFunctionWhenDoApplyThenCallSuper() {
    // Given
    SimpleFunctionRegistry.FunctionInvocationWrapper targetFunction = Mockito.mock(SimpleFunctionRegistry.FunctionInvocationWrapper.class);

    Mockito.when(targetFunction.isConsumer())
      .thenReturn(false);

    // When
    kafkaPerformanceLogger.doApply(Mockito.mock(Message.class), targetFunction);

    // Then
    Assertions.assertEquals(
      Collections.emptyList(),
      memoryAppender.getLoggedEvents()
    );
  }

  @Test
  void givenNoHeadersWhenDoApplyThenLog() {
    // Given
    SimpleFunctionRegistry.FunctionInvocationWrapper targetFunction = Mockito.mock(SimpleFunctionRegistry.FunctionInvocationWrapper.class);
    Message<?> messageMock = new GenericMessage<>("test");

    Mockito.when(targetFunction.isConsumer())
      .thenReturn(true);

    // When
    kafkaPerformanceLogger.doApply(messageMock, targetFunction);

    // Then
    assertPerformanceLogMessage(
      "INCOMING_EVENT",
      "UNKNOWN]\\[partition: UNKNOWN offset: UNKNOWN",
      "",
      memoryAppender
    );
  }

  @Test
  void givenFirstAttemptWhenDoApplyThenLog() {
    // Given
    SimpleFunctionRegistry.FunctionInvocationWrapper targetFunction = Mockito.mock(SimpleFunctionRegistry.FunctionInvocationWrapper.class);
    Message<?> messageMock = new GenericMessage<>("test", Map.of(
      KafkaHeaders.RECEIVED_TOPIC, "test-topic",
      KafkaHeaders.RECEIVED_PARTITION, "partition-1",
      KafkaHeaders.OFFSET, "100",
      IntegrationMessageHeaderAccessor.DELIVERY_ATTEMPT, new java.util.concurrent.atomic.AtomicInteger(1)
    ));

    Mockito.when(targetFunction.isConsumer())
      .thenReturn(true);

    // When
    kafkaPerformanceLogger.doApply(messageMock, targetFunction);

    // Then
    assertPerformanceLogMessage(
      "INCOMING_EVENT",
      "test-topic]\\[partition: partition-1 offset: 100",
      "",
      memoryAppender
    );
  }

  @Test
  void whenDoApplyThenLog() {
    // Given
    SimpleFunctionRegistry.FunctionInvocationWrapper targetFunction = Mockito.mock(SimpleFunctionRegistry.FunctionInvocationWrapper.class);
    Message<?> messageMock = new GenericMessage<>("test", Map.of(
      KafkaHeaders.RECEIVED_TOPIC, "test-topic",
      KafkaHeaders.RECEIVED_PARTITION, "partition-1",
      KafkaHeaders.OFFSET, "100",
      IntegrationMessageHeaderAccessor.DELIVERY_ATTEMPT, new java.util.concurrent.atomic.AtomicInteger(3)
    ));

    Mockito.when(targetFunction.isConsumer())
      .thenReturn(true);

    // When
    kafkaPerformanceLogger.doApply(messageMock, targetFunction);

    // Then
    assertPerformanceLogMessage(
      "INCOMING_EVENT",
      "test-topic]\\[partition: partition-1 offset: 100]\\[ATTEMPT=3",
      "",
      memoryAppender
    );
  }
}
