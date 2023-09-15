package pricebasket.challenge.offers.func;

import pricebasket.challenge.Basket;
import pricebasket.challenge.offers.api.Offer;

import java.time.Instant;
import java.util.Optional;

class ItemForItem extends Offer {

	private final int necessaryQuantity;
	private final int offerQuantity;

	ItemForItem(
		Instant validUntil,
		String promotedItemID,
		int necessaryQuantity,
		String discountMessage,
		String offerItemID,
		int offerQuantity)
	{
		super(validUntil, promotedItemID, discountMessage, offerItemID, OfferType.ITEM_FOR_ITEM);
		this.necessaryQuantity = necessaryQuantity;
		this.offerQuantity = offerQuantity;
	}

	@Override
	public Basket apply(Basket basket) {
		return this.validateOfferInvariants(basket)
			.map(validBasket -> basket.addDiscount(offerItemID, offerQuantity, discountMessage))
			.orElse(basket);
	}

	private Optional<Basket> validateOfferInvariants(Basket basket) {
		return basket.getItemQuantity(promotedItemID)
			.filter(quantity -> quantity >= necessaryQuantity)
			.map(quantity -> basket);
	}
}
