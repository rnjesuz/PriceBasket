package pricebasket.challenge.items.func;

import pricebasket.challenge.items.api.ServiceItems;
import com.google.inject.Binder;
import com.google.inject.Module;

import javax.inject.Singleton;

public class ItemsModule implements Module {

	@Override
	public void configure(Binder binder) {
		binder.bind(DAOItems.class).in(Singleton.class);
		binder.bind(ServiceItems.class).to(ServiceItemsImpl.class).in(Singleton.class);
	}

}
