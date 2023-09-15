package pricebasket.challenge.purchase.func;

import pricebasket.challenge.Basket;
import pricebasket.challenge.PriceBasketError;
import pricebasket.challenge.items.api.Item;
import pricebasket.challenge.items.api.ServiceItems;
import pricebasket.challenge.offers.api.ServiceOffers;
import pricebasket.challenge.purchase.api.Purchase;
import pricebasket.challenge.purchase.api.ServicePurchase;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import io.vavr.control.Either;

import javax.inject.Inject;
import java.math.BigDecimal;

public class ServicePurchaseImpl implements ServicePurchase {

	private final ServiceOffers serviceOffers;
	private final ServiceItems serviceItems;

	@Inject
	public ServicePurchaseImpl(
		ServiceOffers serviceOffers,
		ServiceItems serviceItems)
	{
		this.serviceOffers = serviceOffers;
		this.serviceItems = serviceItems;
	}


	@Override
	public Either<PriceBasketError, Purchase> checkOut(Basket basket) {
		Map<String, Integer> itemIDsAndQuantities = basket.getItems();
		return this
			.validateBasketItems(itemIDsAndQuantities, serviceItems.getItems(itemIDsAndQuantities.keySet()))
			.map(items -> items
				.map(item -> Tuple.of(item, itemIDsAndQuantities.get(item.ID()).get())))
			.map(this::calculateItemsValue)
			.flatMap(subtotal -> {
				Map<String, Integer> offerIDsAndQuantities = this
					.applyDiscounts(basket)
					.getOffers();

				return this
					.validateOfferItems(
						offerIDsAndQuantities,
						serviceItems.getItems(offerIDsAndQuantities
							.toSet()
							.map(Tuple2::_1)))
					.map(offers -> offers
						.map(offer -> Tuple.of(offer, offerIDsAndQuantities.get(offer.ID()).get())))
					.map(this::calculateItemsValue)
					.map(offersAmount -> Tuple.of(subtotal, basket, subtotal.add(offersAmount)));
			})
			.flatMap(checkoutResult -> Either.right(new Purchase(
					checkoutResult._1, checkoutResult._2.getOfferDescriptions(), checkoutResult._3)));
	}

	private Either<PriceBasketError, Seq<Item>> validateOfferItems(Map<String, Integer> offerItems, Seq<Item> fetchedOffers) {
		if (offerItems.size() != fetchedOffers.size()) {
			return Either.left(PriceBasketError.internalError(
				buildMissingItemsError(offerItems.keySet().toList(), fetchedOffers)));
		} else {
			return Either.right(fetchedOffers);
		}
	}

	private Either<PriceBasketError, Seq<Item>> validateBasketItems(Map<String, Integer> basketItems, Seq<Item> fetchedItems) {
		if (basketItems.size() != fetchedItems.size()) {
			return Either.left(PriceBasketError.invalidBasketItem(
				buildMissingItemsError(basketItems.keySet().toList(), fetchedItems)));
		} else {
			return Either.right(fetchedItems);
		}
	}

	private String buildMissingItemsError(Seq<String> basketItems, Seq<Item> fetchedItems) {
		Seq<String> missingItems = basketItems.removeAll(fetchedItems.map(Item::ID));
		StringBuilder stringBuilder = new StringBuilder("Item(s) not found [ ");
		for (String missingItem: missingItems) {
			stringBuilder.append(missingItem).append(" ");
		}
		return stringBuilder.append("]").toString();
	}

	private Basket applyDiscounts(Basket basket) {
		serviceOffers
			.getAvailableOffers()
			.map(offer -> serviceOffers.applyDiscount(basket, offer));
		return basket;
	}

	private BigDecimal calculateItemsValue(Seq<Tuple2<Item, Integer>> basketItems) {
		BigDecimal amount = BigDecimal.ZERO;
		for (Tuple2<Item, Integer> basketItem: basketItems) {
			amount = amount.add(basketItem._1.cost().multiply(BigDecimal.valueOf(basketItem._2)));
		}
		return amount;
	}
}
