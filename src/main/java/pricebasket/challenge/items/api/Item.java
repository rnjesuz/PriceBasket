package pricebasket.challenge.items.api;

import java.math.BigDecimal;

public record Item(
	String ID,
	String description,
	BigDecimal cost)
{ }
