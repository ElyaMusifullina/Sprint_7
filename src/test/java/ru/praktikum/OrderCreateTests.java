package ru.praktikum;

import static org.hamcrest.CoreMatchers.notNullValue;

import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum.model.Order;

import ru.praktikum.steps.OrderSteps;

@RunWith(Parameterized.class)
public class OrderCreateTests extends AbstractTest {

  private final OrderSteps orderSteps = new OrderSteps();
  private Order order;
  private final String firstColor;
  private final String secondColor;

  @Parameterized.Parameters
  public static Object[][] color() {
    return new Object[][]{
      {null, null},
      {"BLACK", null},
      {null, "GREY"},
      {"BLACK", "GREY"}
    };
  }

  public OrderCreateTests(String firstColor, String secondColor) {
    this.firstColor = firstColor;
    this.secondColor = secondColor;
  }

  @Before
  public void setUp() {
    order = new Order();
    order.setDeliveryDate("2021-02-02");
    order.setFirstName(RandomStringUtils.randomAlphabetic(10));
    order.setLastName(RandomStringUtils.randomAlphabetic(10));
    order.setAddress(RandomStringUtils.randomAlphabetic(10));
    order.setMetroStation(RandomStringUtils.randomAlphabetic(10));
    order.setPhone(RandomStringUtils.randomAlphabetic(10));
    order.setRentTime(RandomUtils.nextInt());
    order.setComment(RandomStringUtils.randomAlphabetic(10));
  }
  @Test
  @DisplayName("Проверка happy-path")
  public void shouldReturn201TrackNotNull() {
    String[] testColor = {firstColor, secondColor};
    order.setColor(testColor);

    orderSteps
      .createOrder(order)
      .statusCode(201)
      .body("track", notNullValue());
  }
}
