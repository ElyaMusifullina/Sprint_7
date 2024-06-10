package ru.praktikum.steps;

import static io.restassured.RestAssured.given;
import static ru.praktikum.EndPoints.ORDER;


import io.restassured.response.ValidatableResponse;

import ru.praktikum.model.Order;

public class OrderSteps {
  public ValidatableResponse createOrder(Order order) {
    return given()
      .body(order)
      .when()
      .post(ORDER)
      .then();
  }

  public ValidatableResponse getOrder() {
    return given()
      .when()
      .get(ORDER)
      .then();
  }
}
