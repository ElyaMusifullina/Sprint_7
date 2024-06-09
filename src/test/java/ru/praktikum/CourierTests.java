package ru.praktikum;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.model.Courier;
import ru.praktikum.steps.CourierSteps;

public class CourierTests extends AbstractTest{
  private final CourierSteps courierSteps = new CourierSteps();
  private Courier courier;

  @Before
  public void setUp() {
    courier = new Courier();
    courier.setLogin(RandomStringUtils.randomAlphabetic(5));
    courier.setPassword(RandomStringUtils.randomAlphabetic(5));
  }

  @Test
  public void shouldReturnOkTrue() {
    courierSteps
      .createCourier(courier)
      .statusCode(201)
      .body("ok", is(true));
  }
  @Test
  public void shouldReturnId() {
    courierSteps
      .createCourier(courier);

    courierSteps
      .loginCourier(courier)
      .statusCode(200)
      .body("id", notNullValue());
  }

  @After
  public void tearDown() {
    Integer id = courierSteps.loginCourier(courier)
      .extract().body().path("id");
    courier.setId(id);
    courierSteps.deleteCourier(courier);
  }


}
