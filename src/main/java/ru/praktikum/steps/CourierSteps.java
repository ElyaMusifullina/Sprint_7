package ru.praktikum.steps;

import static io.restassured.RestAssured.given;
import static ru.praktikum.EndPoints.COURIER_CREATE;
import static ru.praktikum.EndPoints.COURIER_DELETE;
import static ru.praktikum.EndPoints.COURIER_LOGIN;

import io.restassured.response.ValidatableResponse;
import ru.praktikum.model.Courier;

public class CourierSteps {
  public ValidatableResponse createCourier(Courier courier) {
    return given()
      .body(courier)
      .when()
      .post(COURIER_CREATE)
      .then();
  }

  public ValidatableResponse loginCourier(Courier courier) {
    return given()
      .body(courier)
      .when()
      .post(COURIER_LOGIN)
      .then();
  }

  public ValidatableResponse deleteCourier(Courier courier) {
    return given()
      .body(courier)
      .when()
      .post(COURIER_DELETE)
      .then();
  }
}
