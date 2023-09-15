package pricebasket.challenge.items.api;

import io.vavr.collection.Seq;
import io.vavr.collection.Set;

public interface ServiceItems {
	Seq<Item> getItems(Set<String> managedIDs);
}
