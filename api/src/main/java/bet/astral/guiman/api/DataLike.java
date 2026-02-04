package bet.astral.guiman.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Interface to support data storage
 */
public interface DataLike {
	/**
	 * Sets a data value in the clickable
	 *
	 * @param key   key
	 * @param value value
	 */
	void setData(@NotNull String key, Object value);

	/**
	 * Returns data from the data map with the given key
	 *
	 * @param key key
	 * @return value, nullable
	 */
	@Nullable
	Object getData(String key);

	/**
	 * Returns data of the clickable
	 *
	 * @return data
	 */
	@NotNull
	Map<String, Object> getData();

	/**
	 * Clears the data of the clickable
	 */
	void clearData();

}
