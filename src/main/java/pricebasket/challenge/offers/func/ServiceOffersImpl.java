package pricebasket.challenge.offers.func;

import pricebasket.challenge.Basket;
import pricebasket.challenge.offers.api.Offer;
import pricebasket.challenge.offers.api.ServiceOffers;
import io.vavr.collection.Seq;

import javax.inject.Inject;

class ServiceOffersImpl implements ServiceOffers {

	private final DAOOffers daoOffers;


	@Inject
	public ServiceOffersImpl(
		final DAOOffers daoOffers)
	{
		this.daoOffers = daoOffers;
	}

	@Override
	public Basket applyDiscount(Basket basket, Offer offer) {
		return offer.apply(basket);
	}

	@Override
	public Seq<Offer> getAvailableOffers() {
		return daoOffers.getAvailableOffers();
	}
}
