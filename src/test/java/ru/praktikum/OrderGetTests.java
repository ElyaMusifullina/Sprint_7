package ru.praktikum;

import static java.util.Optional.empty;
import static org.hamcrest.CoreMatchers.not;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;
import ru.praktikum.steps.OrderSteps;

public class OrderGetTests extends AbstractTest {

  private final OrderSteps orderSteps = new OrderSteps();

  @Test
  @DisplayName("Проверка happy-path")
  public void shouldReturn200() {
    orderSteps
      .getOrder()
      .statusCode(200)
      .body("orders[]", not(empty()));
  }
}
