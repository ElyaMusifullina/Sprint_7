package ru.praktikum;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.model.Courier;
import ru.praktikum.steps.CourierSteps;

public class CourierLoginTests extends AbstractTest{
  private final CourierSteps courierSteps = new CourierSteps();
  private Courier firstCourier;
  private Courier secondCourier;

  @Before
  public void setUp() {
    firstCourier = new Courier();
    firstCourier.setLogin(RandomStringUtils.randomAlphabetic(5));
    firstCourier.setPassword(RandomStringUtils.randomAlphabetic(5));
  }

  @Test
  @DisplayName("Проверка happy-path")
  public void shouldReturnId() {
    courierSteps
      .createCourier(firstCourier);

    courierSteps
      .loginCourier(firstCourier)
      .statusCode(200)
      .body("id", notNullValue());
  }

  @Test
  @DisplayName("Проверка статус-кода 404 при некорректном логине")
  public void shouldReturn404IncorrectLogin() {

    secondCourier = new Courier();
    secondCourier.setLogin(RandomStringUtils.randomAlphabetic(5));

    courierSteps
      .createCourier(firstCourier);

    secondCourier.setPassword(firstCourier.getPassword());

    courierSteps
      .loginCourier(secondCourier)
      .statusCode(404)
      .body("message", is("Учетная запись не найдена"));
  }

  @Test
  @DisplayName("Проверка статус-кода 404 при некорректном пароле")
  public void shouldReturn404IncorrectPassword() {
    secondCourier = new Courier();
    secondCourier.setPassword(RandomStringUtils.randomAlphabetic(5));

    courierSteps
      .createCourier(firstCourier);

    secondCourier.setLogin(firstCourier.getLogin());

    courierSteps
      .loginCourier(secondCourier)
      .statusCode(404)
      .body("message", is("Учетная запись не найдена"));
  }

  @Test
  @DisplayName("Проверка статус-кода 400 без передачи поля login")
  public void shouldReturn400WithoutLogin() {
    secondCourier = new Courier();

    courierSteps
      .createCourier(firstCourier);

    secondCourier.setLogin("");
    secondCourier.setPassword(firstCourier.getPassword());

    courierSteps
      .loginCourier(secondCourier)
      .statusCode(400)
      .body("message", is("Недостаточно данных для входа"));
  }

  @Test
  @DisplayName("Проверка статус-кода 400 без передачи поля password")
  public void shouldReturn504WithoutPassword() {
    secondCourier = new Courier();

    courierSteps
      .createCourier(firstCourier);

    secondCourier.setLogin(firstCourier.getLogin());
    secondCourier.setPassword("");

    courierSteps
      .loginCourier(secondCourier)
      .statusCode(400)
      .body("message", is("Недостаточно данных для входа"));
  }

  @Test
  @DisplayName("Проверка статус-кода 404 по незаведенному ранее курьеру")
  public void shouldReturn404() {
    RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    courierSteps
      .loginCourier(firstCourier)
      .statusCode(404)
      .body("message", is("Учетная запись не найдена"));
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
