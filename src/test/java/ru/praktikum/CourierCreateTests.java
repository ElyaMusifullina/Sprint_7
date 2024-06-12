package ru.praktikum;


import static org.hamcrest.CoreMatchers.is;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.parsing.Parser;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.model.Courier;
import ru.praktikum.steps.CourierSteps;

public class CourierCreateTests extends AbstractTest{
  private final CourierSteps courierSteps = new CourierSteps();
  private Courier firstCourier;

  @Before
  public void setUp() {
    firstCourier = new Courier();
    firstCourier.setLogin(RandomStringUtils.randomAlphabetic(5));
    firstCourier.setPassword(RandomStringUtils.randomAlphabetic(5));
  }

  @Test
  @DisplayName("Проверка happy-path с передачей всех полей в запросе")
  public void shouldReturnOkTrue() {
    firstCourier.setFirstName(RandomStringUtils.randomAlphabetic(5));
    courierSteps
      .createCourier(firstCourier)
      .statusCode(201)
      .body("ok", is(true));
  }

  @Test
  @DisplayName("Проверка happy-path без заполнения поля firstName")
  public void shouldReturnOkTrueWithoutFirstName() {
    courierSteps
      .createCourier(firstCourier)
      .statusCode(201)
      .body("ok", is(true));
  }

  @Test
  @DisplayName("Проверка статус-кода 409 при попытке создать идентичного курьера")
  public void shouldReturn409() {
    courierSteps
      .createCourier(firstCourier);

    courierSteps
      .createCourier(firstCourier)
      .statusCode(409)
      .body("message", is("Этот логин уже используется. Попробуйте другой."));
  }

  @Test
  @DisplayName("Проверка статус-кода 409 при попытке создать курьера с тем же логином")
  public void shouldReturn409SameLogin() {
    RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    Courier secondCourier = new Courier();
    secondCourier.setLogin(firstCourier.getLogin());
    secondCourier.setPassword(RandomStringUtils.randomAlphabetic(5));

    courierSteps
      .createCourier(firstCourier);

    courierSteps
      .createCourier(secondCourier)
      .statusCode(409)
      .body("message", is("Этот логин уже используется. Попробуйте другой."));
  }

  @Test
  @DisplayName("Проверка статус-кода 400, если не передавать в запросе поле password")
  public void shouldReturn400WithoutPassword() {
    RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    firstCourier.setPassword("");

    courierSteps
      .createCourier(firstCourier)
      .statusCode(400)
      .body("message", is("Недостаточно данных для создания учетной записи"));
  }

  @Test
  @DisplayName("Проверка статус-кода 400, если не передавать в запросе поле login")
  public void shouldReturn400WithoutLogin() {
    firstCourier.setLogin("");

    courierSteps
      .createCourier(firstCourier)
      .statusCode(400)
      .body("message", is("Недостаточно данных для создания учетной записи"));
  }
  @After
  public void tearDown() {
    Integer id = courierSteps.loginCourier(firstCourier)
      .extract().body().path("id");
    if (id != null) {
      firstCourier.setId(id);
      courierSteps.deleteCourier(firstCourier);
    }
  }
}
