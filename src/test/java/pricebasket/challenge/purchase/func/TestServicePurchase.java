package pricebasket.challenge.purchase.func;

import pricebasket.challenge.Basket;
import pricebasket.challenge.PriceBasketError;
import pricebasket.challenge.items.api.Item;
import pricebasket.challenge.items.api.ServiceItems;
import pricebasket.challenge.offers.api.Offer;
import pricebasket.challenge.offers.api.ServiceOffers;
import pricebasket.challenge.purchase.api.Purchase;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

class TestServicePurchase {

	@Test
	void successfullyCheckoutNoDiscounts() {
		Basket basket = new Basket();
		String milkID = "1aa1";
		basket.addItem(milkID, 1);
		String eggsID = "2bb2";
		basket.addItem(eggsID, 2);
		String cheeseID = "3cc3";
		basket.addItem(cheeseID, 1);
		Item milk = new Item(milkID, "Fresh Milk", BigDecimal.ONE);
		Item eggs = new Item(eggsID, "Medium Eggs", BigDecimal.ONE);
		Item cheese = new Item(cheeseID, "Grated Cheese", BigDecimal.TEN);
		Seq<Item> items = List.of(milk, eggs, cheese);
		ServiceItems serviceItems = Mockito.mock(ServiceItems.class);
		Mockito.when(serviceItems.getItems(HashSet.of(milkID, eggsID, cheeseID)))
			.thenReturn(items);

		ServiceOffers serviceOffers = Mockito.mock(ServiceOffers.class);
		Mockito.when(serviceOffers.getAvailableOffers()).thenReturn(List.empty());
		Mockito.when(serviceItems.getItems(HashSet.empty())).thenReturn(List.empty());

		ServicePurchaseImpl undertest = new ServicePurchaseImpl(serviceOffers, serviceItems);

		Either<PriceBasketError, Purchase> result = undertest.checkOut(basket);

		Assertions.assertTrue(result.isRight());
		Assertions.assertTrue(result.get().offerDescriptions().isEmpty());
		Assertions.assertEquals(result.get().subtotal(), result.get().total());
		Assertions.assertEquals(new BigDecimal("13"), result.get().total());
	}

	@Test
	void successfullyCheckoutWithDiscounts() {
		Basket basket = new Basket();
		String milkID = "1aa1";
		basket.addItem(milkID, 1);
		Item milk = new Item(milkID, "Fresh Milk", BigDecimal.ONE);
		Seq<Item> items = List.of(milk);
		ServiceItems serviceItems = Mockito.mock(ServiceItems.class);
		Mockito.when(serviceItems.getItems(HashSet.of(milkID)))
			.thenReturn(items);

		ServiceOffers serviceOffers = Mockito.mock(ServiceOffers.class);
		Offer milkDiscount = Mockito.mock(Offer.class);
		String milkDiscountID = "4dd4";
		Item milkDiscountItem = new Item(milkDiscountID, "Fresh Milk", new BigDecimal("-0.5"));
		Mockito.when(serviceOffers.getAvailableOffers()).thenReturn(List.of(milkDiscount));
		basket.addDiscount(milkDiscountID, 1, "Milk 50% off");
		Mockito.when(serviceOffers.applyDiscount(basket, milkDiscount)).thenReturn(basket);
		Mockito.when(serviceItems.getItems(HashSet.of(milkDiscountID))).thenReturn(List.of(milkDiscountItem));

		ServicePurchaseImpl undertest = new ServicePurchaseImpl(serviceOffers, serviceItems);

		Either<PriceBasketError, Purchase> result = undertest.checkOut(basket);

		Assertions.assertTrue(result.isRight());
		Assertions.assertFalse(result.get().offerDescriptions().isEmpty());
		Assertions.assertNotEquals(result.get().subtotal(), result.get().total());
		Assertions.assertEquals(new BigDecimal("1"), result.get().subtotal());
		Assertions.assertEquals(new BigDecimal("0.5"), result.get().total());
	}

	@Test
	void invalidItemCheckout() {
		Basket basket = new Basket();
		String milkID = "1aa1";
		basket.addItem(milkID, 1);
		ServiceItems serviceItems = Mockito.mock(ServiceItems.class);
		Mockito.when(serviceItems.getItems(Mockito.any()))
			.thenReturn(List.empty());

		ServiceOffers serviceOffers = Mockito.mock(ServiceOffers.class);

		ServicePurchaseImpl undertest = new ServicePurchaseImpl(serviceOffers, serviceItems);

		Either<PriceBasketError, Purchase> result = undertest.checkOut(basket);

		Assertions.assertTrue(result.isLeft());
		Assertions.assertInstanceOf(PriceBasketError.InvalidBasketItem.class, result.getLeft());
	}

	@Test
	void internalErrorCheckout() {
		Basket basket = new Basket();
		String milkID = "1aa1";
		basket.addItem(milkID, 1);
		Item milk = new Item(milkID, "Fresh Milk", BigDecimal.ONE);
		Seq<Item> items = List.of(milk);
		ServiceItems serviceItems = Mockito.mock(ServiceItems.class);
		Mockito.when(serviceItems.getItems(Mockito.any()))
			.thenReturn(items);

		ServiceOffers serviceOffers = Mockito.mock(ServiceOffers.class);
		Offer milkDiscount = Mockito.mock(Offer.class);
		Mockito.when(serviceOffers.getAvailableOffers()).thenReturn(List.of(milkDiscount));

		ServicePurchaseImpl undertest = new ServicePurchaseImpl(serviceOffers, serviceItems);

		Either<PriceBasketError, Purchase> result = undertest.checkOut(basket);

		Assertions.assertTrue(result.isLeft());
		Assertions.assertInstanceOf(PriceBasketError.InternalError.class, result.getLeft());
	}
}