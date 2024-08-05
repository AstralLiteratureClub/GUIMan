package bet.astral.signman;

import java.util.List;

@FunctionalInterface
public interface SignHandler {
	List<SignAction> handle();
}
