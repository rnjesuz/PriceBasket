package pricebasket.challenge.offers.api;

import pricebasket.challenge.Basket;
import io.vavr.collection.Seq;

public interface ServiceOffers {
	Basket applyDiscount(Basket basket, Offer offer);

	Seq<Offer> getAvailableOffers();
}
