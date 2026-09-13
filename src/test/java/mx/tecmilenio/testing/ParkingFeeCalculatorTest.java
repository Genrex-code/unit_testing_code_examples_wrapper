package mx.tecmilenio.testing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ParkingFeeCalculatorTest {

    @Test
    void fifteenMinutesShouldBeFree() {
        // Arrange
        ParkingFeeCalculator calculator = new ParkingFeeCalculator();

        // Act
        int result = calculator.calculateFee(15, false);

        // Assert
        assertEquals(0, result);
    }

    @Test
    void sixtyOneMinutesShouldChargeOneStartedAdditionalHour() {
        ParkingFeeCalculator calculator = new ParkingFeeCalculator();

        int result = calculator.calculateFee(61, false);

        assertEquals(35, result);
    }

    @Test
    void normalFeeShouldNotBeGreaterThanEighty() {
        ParkingFeeCalculator calculator = new ParkingFeeCalculator();

        int result = calculator.calculateFee(600, false);

        assertEquals(80, result);
    }

    @Test
    void lostTicketShouldCostOneHundredFifty() {
        ParkingFeeCalculator calculator = new ParkingFeeCalculator();

        int result = calculator.calculateFee(10, true);

        assertEquals(150, result);
    }

    @Test
    void negativeMinutesShouldBeRejected() {
        ParkingFeeCalculator calculator = new ParkingFeeCalculator();

        assertThrows(
                IllegalArgumentException.class,
                () -> calculator.calculateFee(-1, false)
        );
    }

    @Test
    void thirtyMinutesShouldChargeBaseFee() {
        // Arrange
        ParkingFeeCalculator calculator = new ParkingFeeCalculator();

        // Act
        int result = calculator.calculateFee(30, false);

        // Assert
        assertEquals(20, result);
    }

    @Test
    void zeroMinutesShouldBeFree() {
        // Arrange
        ParkingFeeCalculator calculator = new ParkingFeeCalculator();

        // Act
        int result = calculator.calculateFee(0, false);

        // Assert
        assertEquals(0, result);
    }

    @Test
    void sixteenMinutesShouldChargeBaseFee() {
        // Arrange
        ParkingFeeCalculator calculator = new ParkingFeeCalculator();

        // Act
        int result = calculator.calculateFee(16, false);

        // Assert
        assertEquals(20, result);
    }

    @Test
    void sixtyMinutesShouldStillChargeBaseFee() {
        // Arrange
        ParkingFeeCalculator calculator = new ParkingFeeCalculator();

        // Act
        int result = calculator.calculateFee(60, false);

        // Assert
        assertEquals(20, result);
    }

    @Test
    void oneHundredTwentyMinutesShouldChargeExactlyOneAdditionalHour() {
        // Arrange
        ParkingFeeCalculator calculator = new ParkingFeeCalculator();

        // Act
        int result = calculator.calculateFee(120, false);

        // Assert
        assertEquals(35, result);
    }

    @Test
    void oneHundredTwentyOneMinutesShouldChargeTwoStartedAdditionalHours() {
        // Arrange
        ParkingFeeCalculator calculator = new ParkingFeeCalculator();

        // Act
        int result = calculator.calculateFee(121, false);

        // Assert
        assertEquals(50, result);
    }

    @Test
    void twoHundredFortyMinutesShouldRemainBelowMaximumFee() {
        // Arrange
        ParkingFeeCalculator calculator = new ParkingFeeCalculator();

        // Act
        int result = calculator.calculateFee(240, false);

        // Assert
        assertEquals(65, result);
    }

    @Test
    void twoHundredFortyOneMinutesShouldReachMaximumFee() {
        // Arrange
        ParkingFeeCalculator calculator = new ParkingFeeCalculator();

        // Act
        int result = calculator.calculateFee(241, false);

        // Assert
        assertEquals(80, result);
    }

    @Test
    void lostTicketShouldCostOneHundredFiftyEvenForLongParkingTime() {
        // Arrange
        ParkingFeeCalculator calculator = new ParkingFeeCalculator();

        // Act
        int result = calculator.calculateFee(600, true);

        // Assert
        assertEquals(150, result);
    }
}
