package ru.praktikum;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.model.Courier;
import ru.praktikum.steps.CourierSteps;

public class CourierLoginTests extends AbstractTest{
  private final CourierSteps courierSteps = new CourierSteps();
  private Courier courier1;
  private Courier courier2;

  @Before
  public void setUp() {
    courier1 = new Courier();
    courier1.setLogin(RandomStringUtils.randomAlphabetic(5));
    courier1.setPassword(RandomStringUtils.randomAlphabetic(5));
  }

  @Test
  @DisplayName("Проверка happy-path")
  public void shouldReturnId() {
    courierSteps
      .createCourier(courier1);

    courierSteps
      .loginCourier(courier1)
      .statusCode(200)
      .body("id", notNullValue());

    Integer id = courierSteps.loginCourier(courier1)
      .extract().body().path("id");
    courier1.setId(id);
    courierSteps.deleteCourier(courier1);
  }

  @Test
  @DisplayName("Проверка статус-кода 404 при некорректном логине")
  public void shouldReturn404IncorrectLogin() {

    courier2 = new Courier();
    courier2.setLogin(RandomStringUtils.randomAlphabetic(5));

    courierSteps
      .createCourier(courier1);

    courier2.setPassword(courier1.getPassword());

    courierSteps
      .loginCourier(courier2)
      .statusCode(404)
      .body("message", is("Учетная запись не найдена"));

    Integer id = courierSteps.loginCourier(courier1)
      .extract().body().path("id");
    courier1.setId(id);
    courierSteps.deleteCourier(courier1);
  }

  @Test
  @DisplayName("Проверка статус-кода 404 при некорректном пароле")
  public void shouldReturn404IncorrectPassword() {
    courier2 = new Courier();
    courier2.setPassword(RandomStringUtils.randomAlphabetic(5));

    courierSteps
      .createCourier(courier1);

    courier2.setLogin(courier1.getLogin());

    courierSteps
      .loginCourier(courier2)
      .statusCode(404)
      .body("message", is("Учетная запись не найдена"));

    Integer id = courierSteps.loginCourier(courier1)
      .extract().body().path("id");
    courier1.setId(id);
    courierSteps.deleteCourier(courier1);
  }

  @Test
  @DisplayName("Проверка статус-кода 400 без передачи поля login")
  public void shouldReturn400WithoutLogin() {
    courier2 = new Courier();

    courierSteps
      .createCourier(courier1);

    courier2.setPassword(courier1.getPassword());

    courierSteps
      .loginCourier(courier2)
      .statusCode(400)
      .body("message", is("Недостаточно данных для входа"));

    Integer id = courierSteps.loginCourier(courier1)
      .extract().body().path("id");
    courier1.setId(id);
    courierSteps.deleteCourier(courier1);
  }

  @Test
  @DisplayName("Проверка статус-кода 504 без передачи поля password")
  public void shouldReturn504WithoutPassword() {
    courier2 = new Courier();

    courierSteps
      .createCourier(courier1);

    courier2.setLogin(courier1.getLogin());

    courierSteps
      .loginCourier(courier2)
      .statusCode(504);

    Integer id = courierSteps.loginCourier(courier1)
      .extract().body().path("id");
    courier1.setId(id);
    courierSteps.deleteCourier(courier1);
  }

  @Test
  @DisplayName("Проверка статус-кода 404 по незаведенному ранее курьеру")
  public void shouldReturn404() {
    courierSteps
      .loginCourier(courier1)
      .statusCode(404)
      .body("message", is("Учетная запись не найдена"));
  }
}
