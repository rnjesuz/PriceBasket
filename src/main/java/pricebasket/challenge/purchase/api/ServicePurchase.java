package pricebasket.challenge.purchase.api;

import pricebasket.challenge.Basket;
import pricebasket.challenge.PriceBasketError;
import io.vavr.control.Either;

public interface ServicePurchase {

	Either<PriceBasketError, Purchase> checkOut(Basket basket);
}
