package pricebasket.challenge.offers.func;

import pricebasket.challenge.Basket;
import pricebasket.challenge.offers.api.Offer;

import java.time.Instant;

class PercentageDiscount extends Offer {

	PercentageDiscount(
		Instant validUntil,
		String promotedItemID,
		String discountMessage,
		String offerItemID)
	{
		super(validUntil, promotedItemID, discountMessage, offerItemID, OfferType.PERCENTAGE_DISCOUNT);
	}

	@Override
	public Basket apply(Basket basket) {
		return basket.getItemQuantity(promotedItemID)
			.map(quantity -> basket.addDiscount(offerItemID, quantity, discountMessage))
			.orElse(basket);
	}
}