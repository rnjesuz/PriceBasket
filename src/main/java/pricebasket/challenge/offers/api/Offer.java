package pricebasket.challenge.offers.api;

import pricebasket.challenge.Basket;
import pricebasket.challenge.offers.func.OfferType;

import java.time.Instant;

public abstract class Offer {

	protected final Instant validUntil;
	protected final String promotedItemID;
	protected final String discountMessage;
	protected final String offerItemID;
	protected final OfferType type;

	protected Offer(
		Instant validUntil,
		String promotedItemID,
		String discountMessage,
		String offerItemID,
		OfferType type)
	{
		this.validUntil = validUntil;
		this.promotedItemID = promotedItemID;
		this.discountMessage = discountMessage;
		this.offerItemID = offerItemID;
		this.type = type;
	}

	public abstract Basket apply(Basket basket);
}
