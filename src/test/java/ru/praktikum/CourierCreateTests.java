package ru.praktikum;


import static org.hamcrest.CoreMatchers.is;

import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.model.Courier;
import ru.praktikum.steps.CourierSteps;

public class CourierCreateTests extends AbstractTest{
  private final CourierSteps courierSteps = new CourierSteps();
  private Courier courier1;

  @Before
  public void setUp() {
    courier1 = new Courier();
    courier1.setLogin(RandomStringUtils.randomAlphabetic(5));
    courier1.setPassword(RandomStringUtils.randomAlphabetic(5));
  }

  @Test
  @DisplayName("Проверка happy-path с передачей всех полей в запросе")
  public void shouldReturnOkTrue() {
    courier1.setFirstName(RandomStringUtils.randomAlphabetic(5));

    courierSteps
      .createCourier(courier1)
      .statusCode(201)
      .body("ok", is(true));

    Integer id = courierSteps.loginCourier(courier1)
      .extract().body().path("id");
    courier1.setId(id);
    courierSteps.deleteCourier(courier1);
  }

  @Test
  @DisplayName("Проверка happy-path без заполнения поля firstName")
  public void shouldReturnOkTrueWithoutFirstName() {
    courierSteps
      .createCourier(courier1)
      .statusCode(201)
      .body("ok", is(true));

    Integer id = courierSteps.loginCourier(courier1)
      .extract().body().path("id");
    courier1.setId(id);
    courierSteps.deleteCourier(courier1);
  }

  @Test
  @DisplayName("Проверка статус-кода 409 при попытке создать идентичного курьера")
  public void shouldReturn409() {
    courierSteps
      .createCourier(courier1);

    courierSteps
      .createCourier(courier1)
      .statusCode(409)
      .body("message", is("Этот логин уже используется. Попробуйте другой."));

    Integer id = courierSteps.loginCourier(courier1)
      .extract().body().path("id");
    courier1.setId(id);
    courierSteps.deleteCourier(courier1);
  }

  @Test
  @DisplayName("Проверка статус-кода 409 при попытке создать курьера с тем же логином")
  public void shouldReturn409SameLogin() {
    Courier courier2 = new Courier();
    courier2.setLogin(courier1.getLogin());
    courier2.setPassword(RandomStringUtils.randomAlphabetic(5));

    courierSteps
      .createCourier(courier1);

    courierSteps
      .createCourier(courier2)
      .statusCode(409)
      .body("message", is("Этот логин уже используется. Попробуйте другой."));

    Integer id = courierSteps.loginCourier(courier1)
      .extract().body().path("id");
    courier1.setId(id);
    courierSteps.deleteCourier(courier1);
  }

  @Test
  @DisplayName("Проверка статус-кода 400, если не передавать в запросе поле password")
  public void shouldReturn400WithoutPassword() {
    courier1.setPassword(null);

    courierSteps
      .createCourier(courier1)
      .statusCode(400)
      .body("message", is("Недостаточно данных для создания учетной записи"));
  }

  @Test
  @DisplayName("Проверка статус-кода 400, если не передавать в запросе поле login")
  public void shouldReturn400WithoutLogin() {
    courier1.setLogin(null);

    courierSteps
      .createCourier(courier1)
      .statusCode(400)
      .body("message", is("Недостаточно данных для создания учетной записи"));
  }
}
