package pricebasket.challenge;

import pricebasket.challenge.items.func.ItemsModule;
import pricebasket.challenge.offers.func.OffersModule;
import pricebasket.challenge.purchase.api.Purchase;
import pricebasket.challenge.purchase.api.ServicePurchase;
import pricebasket.challenge.purchase.func.PurchaseModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import static java.lang.System.exit;

public class Main {
	public static void main(String[] args) {

		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(System.in));
			StringTokenizer st = new StringTokenizer(br.readLine());
			String firstToken = st.nextToken();
			switch(firstToken) {
				case "PriceBasket":
					purchaseBasketCommand(st);
					break;
				default:
					System.out.println("Invalid Command");
					exit(-1);
			}
		}
		catch (Exception e) {
			System.out.println("InternalError");
			exit(-1);
		}
	}

	private static void purchaseBasketCommand(StringTokenizer tokenizer) {
		Basket basket = new Basket();
		while (tokenizer != null && tokenizer.hasMoreElements()) {
			basket.addItem(tokenizer.nextToken());
		}

		Injector injector = Guice.createInjector(
			new ItemsModule(),
			new PurchaseModule(),
			new OffersModule());

		ServicePurchase servicePurchase = injector.getInstance(ServicePurchase.class);
		servicePurchase.checkOut(basket)
			.fold(
				Main::mapErrors,
				Main::printPurchase
			);
	}

	private static String mapErrors(PriceBasketError error) {
		return error.visit(new PriceBasketError.PriceBasketErrorVisitor<>() {
			@Override
			public String visit(PriceBasketError.InternalError error) {
				String message = "Internal Error";
				System.out.println(message);
				return message;
			}

			@Override
			public String visit(PriceBasketError.InvalidBasketItem error) {
				String message = error.getMessage();
				System.out.println(message);
				return message;
			}
		});
	}

	private static String printPurchase(Purchase purchase) {
		System.out.println("Subtotal: £" + purchase.subtotal());
		if (purchase.offerDescriptions().isEmpty()) {
			System.out.println("(no offers available)");
		} else {
			for (String discount: purchase.offerDescriptions()) {
				System.out.println(discount);
			}
		}
		System.out.println("Total: £" + purchase.total());

		return "OK";
	}

}