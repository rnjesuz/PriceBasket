package pricebasket.challenge;

public abstract class PriceBasketError {

	private final String message;

	protected PriceBasketError(final String message) {
		this.message = message;
	}


	public String getMessage() {
		return message;
	}

	public abstract <T> T visit(PriceBasketErrorVisitor<T> visitor);


	public static PriceBasketError.InternalError internalError(final String message) {
		return new InternalError(message);
	}

	public static PriceBasketError.InvalidBasketItem invalidBasketItem(final String message) {
		return new InvalidBasketItem(message);
	}

	public static class InternalError extends PriceBasketError {
		private InternalError(final String message) {
			super(message);
		}

		@Override
		public <T> T visit(PriceBasketErrorVisitor<T> visitor) {
				return visitor.visit(this);
		}
	}

	public static class InvalidBasketItem extends PriceBasketError {
		private InvalidBasketItem(final String message) {
			super(message);
		}

		@Override
		public <T> T visit(PriceBasketErrorVisitor<T> visitor) {
				return visitor.visit(this);
		}
	}

	public interface PriceBasketErrorVisitor<T> {
		T visit(InternalError error);
		T visit(InvalidBasketItem error);
	}
}
