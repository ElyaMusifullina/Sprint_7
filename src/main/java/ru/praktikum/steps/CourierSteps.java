package ru.praktikum.steps;

import static io.restassured.RestAssured.given;
import static ru.praktikum.EndPoints.COURIER_CREATE;
import static ru.praktikum.EndPoints.COURIER_DELETE;
import static ru.praktikum.EndPoints.COURIER_LOGIN;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.praktikum.model.Courier;

public class CourierSteps {
  @Step("Создаем курьера")
  public ValidatableResponse createCourier(Courier courier) {
    return given()
      .body(courier)
      .when()
      .post(COURIER_CREATE)
      .then();
  }
  @Step("Курьер логинится")
  public ValidatableResponse loginCourier(Courier courier) {
    return given()
      .body(courier)
      .when()
      .post(COURIER_LOGIN)
      .then();
  }

  @Step("Удаляем курьера")
  public void deleteCourier(Courier courier) {
    given()
      .pathParam("id", courier.getId())
      .when()
      .delete(COURIER_DELETE)
      .then();
  }
}
