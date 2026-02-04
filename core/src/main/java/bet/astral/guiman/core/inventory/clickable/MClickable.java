package bet.astral.guiman.core.inventory.clickable;

import bet.astral.guiman.api.Permission;
import bet.astral.guiman.api.messenger.PlaceholderGenerator;
import bet.astral.messenger.v2.translation.TranslationKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class MClickable implements bet.astral.guiman.api.inventory.clickable.MClickable {
	@Override
	public @Nullable TranslationKey getNameTranslation() {
		return null;
	}

	@Override
	public boolean hasNameTranslation() {
		return false;
	}

	@Override
	public @Nullable TranslationKey getLoreTranslation() {
		return null;
	}

	@Override
	public boolean hasLoreTranslation() {
		return false;
	}

	@Override
	public @Nullable TranslationKey getPermissionMessageTranslation() {
		return null;
	}

	@Override
	public boolean hasPermissionMessageTranslation() {
		return false;
	}

	@Override
	public @Nullable PlaceholderGenerator getPlaceholderGenerator() {
		return null;
	}

	@Override
	public boolean hasPlaceholderGenerator() {
		return false;
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public @NotNull ItemStack getItem() {
		return null;
	}

	@Override
	public @NotNull Permission getPermission() {
		return null;
	}

	@Override
	public boolean hasPermission() {
		return false;
	}

	@Override
	public void sendPermissionMessage(Player player) {

	}

	@Override
	public boolean isAsync() {
		return false;
	}

	@Override
	public void setData(@NotNull String key, Object value) {

	}

	@Override
	public @Nullable Object getData(String key) {
		return null;
	}

	@Override
	public @NotNull Map<String, Object> getData() {
		return Map.of();
	}

	@Override
	public void clearData() {

	}

	@Override
	public @NotNull ItemStack generate(@NotNull Player player) {
		return null;
	}
}
