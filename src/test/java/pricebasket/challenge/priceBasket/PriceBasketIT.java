package pricebasket.challenge.priceBasket;

import pricebasket.challenge.Main;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

class PriceBasketIT {

	@Test
	void successWithDiscount() throws IOException {
		String input = "PriceBasket Apples Milk Bread";
		InputStream inputStream = new ByteArrayInputStream(input.getBytes("UTF-8") );
		System.setIn(inputStream);
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));

		Main.main(null);

		String expectedResult = "Subtotal: £3.10\nApples 10% off: -10p\nTotal: £3.00\n";
		Assertions.assertEquals(expectedResult, outContent.toString());
	}


	@Test
	void invalidItem() throws IOException {
		String invalidItem = "InvalidItem";
		String input = "PriceBasket Milk " + invalidItem;
		InputStream inputStream = new ByteArrayInputStream(input.getBytes("UTF-8") );
		System.setIn(inputStream);
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));

		Main.main(null);

		Assertions.assertTrue(outContent.toString().contains(invalidItem));
	}

}