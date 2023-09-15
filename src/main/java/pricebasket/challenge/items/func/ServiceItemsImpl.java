package pricebasket.challenge.items.func;

import pricebasket.challenge.items.api.Item;
import pricebasket.challenge.items.api.ServiceItems;
import io.vavr.collection.Seq;
import io.vavr.collection.Set;

import javax.inject.Inject;

class ServiceItemsImpl implements ServiceItems {

	private final DAOItems daoItems;

	@Inject
	public ServiceItemsImpl (
		DAOItems daoItems
	) {
		this.daoItems = daoItems;
	}

	@Override
	public Seq<Item> getItems(Set<String> managedIDs) {
		return daoItems.getItems(managedIDs);
	}
}
