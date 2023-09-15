package pricebasket.challenge.purchase.api;

import io.vavr.collection.Seq;

import java.math.BigDecimal;

public record Purchase(
	BigDecimal subtotal,
	Seq<String> offerDescriptions,
	BigDecimal total) { }
