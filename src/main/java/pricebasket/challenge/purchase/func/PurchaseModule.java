package pricebasket.challenge.purchase.func;

import pricebasket.challenge.purchase.api.ServicePurchase;
import com.google.inject.Binder;
import com.google.inject.Module;

import javax.inject.Singleton;

public class PurchaseModule implements Module {

	@Override
	public void configure(Binder binder) {
		binder.bind(ServicePurchase.class).to(ServicePurchaseImpl.class).in(Singleton.class);
	}

}
