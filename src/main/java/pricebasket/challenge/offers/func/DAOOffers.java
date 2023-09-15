package pricebasket.challenge.offers.func;

import pricebasket.challenge.offers.api.Offer;
import io.vavr.collection.List;
import io.vavr.collection.Seq;

import java.time.Instant;

class DAOOffers {
	Seq<Offer> getAvailableOffers() {
		// This would be a DB query
		return List.of(
			new PercentageDiscount(
				Instant.MAX,
				"Apples",
				"Apples 10% off: -10p",
				"10%OffApples"
			),
			new ItemForItem(
				Instant.MAX,
				"Soup",
				2,
				"Discounted Bread: 40p",
				"HalfPricedBread",
				1
			));
	}
}