package pricebasket.challenge;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Basket {
	final Map<String, Integer> items = new HashMap<>();
	final Map<String, Integer> offers = new HashMap<>();
	final List<String> offerDescriptions = new ArrayList<>();

	public Basket addItem(String itemID) {
		return this.addItem(itemID, 1);
	}

	public Basket addItem(String itemID, Integer quantity) {
		Preconditions.checkArgument(quantity > 0, "Quantity must be greater than 0");

		if (items.containsKey(itemID)) {
			items.compute(itemID, (itemKey, quantityValue) -> quantityValue + quantity);
		} else {
			items.put(itemID, quantity);
		}
		return this;
	}

	public Basket addDiscount(String offerID, Integer quantity, String message) {
		offers.put(offerID, quantity);
		for (int count = 0; count < quantity; count++) {
			offerDescriptions.add(message);
		}
		return this;
	}

	public io.vavr.collection.Map<String, Integer> getItems() {
		return io.vavr.collection.HashMap.ofAll(items);
	}

	public Optional<Integer> getItemQuantity(final String itemID) {
		return Optional.ofNullable(items.get(itemID));
	}

	public io.vavr.collection.Map<String, Integer> getOffers() {
		return io.vavr.collection.HashMap.ofAll(offers);
	}

	public io.vavr.collection.Seq<String> getOfferDescriptions() {
		return io.vavr.collection.List.ofAll(offerDescriptions);
	}
}
