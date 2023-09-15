package pricebasket.challenge.offers.func;

import pricebasket.challenge.offers.api.ServiceOffers;
import com.google.inject.Binder;
import com.google.inject.Module;

import javax.inject.Singleton;

public class OffersModule implements Module {

	@Override
	public void configure(Binder binder) {
		binder.bind(DAOOffers.class).in(Singleton.class);
		binder.bind(ServiceOffers.class).to(ServiceOffersImpl.class).in(Singleton.class);
	}

}
