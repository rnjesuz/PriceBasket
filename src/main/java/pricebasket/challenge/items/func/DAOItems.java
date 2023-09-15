package pricebasket.challenge.items.func;

import pricebasket.challenge.items.api.Item;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.collection.Set;

import java.math.BigDecimal;
import java.util.Optional;

class DAOItems {

	Optional<Item> getItem(String managedID) {
		// This would be a DB query
		return switch (managedID) {
			case "Soup" -> Optional.of(new Item(managedID, "Soup", new BigDecimal("0.65")));
			case "Bread" -> Optional.of(new Item(managedID, "Bread", new BigDecimal("0.80")));
			case "Milk" -> Optional.of(new Item(managedID, "Milk", new BigDecimal("1.30")));
			case "Apples" -> Optional.of(new Item(managedID, "Apples", new BigDecimal("1")));
			case "HalfPricedBread" -> Optional.of(new Item(managedID, "Bread", new BigDecimal("0.40")));
			case "10%OffApples" -> Optional.of(new Item(managedID, "Apples", new BigDecimal("-0.10")));
			default -> Optional.empty();
		};
	}

	Seq<Item> getItems(Set<String> managedIDs) {
		// this would be a DB query returning a list of results
		// 1 access instead of multiple (by reusing {@link getItem()})
		return List
			.ofAll(managedIDs
				.map(this::getItem))
			.filter(Optional::isPresent)
			.map(Optional::get);
	}
}
