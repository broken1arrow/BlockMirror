package org.brokenarrow.blockmirror;

import org.broken.lib.rbg.TextTranslator;
import org.brokenarrow.blockmirror.api.BlockMirrorAPI;
import org.brokenarrow.blockmirror.api.BlockMirrorUtillity;
import org.brokenarrow.blockmirror.api.blockpattern.PatternData;
import org.brokenarrow.blockmirror.api.blockpattern.PatternSettingsWrapperApi;
import org.brokenarrow.blockmirror.api.builders.ItemWrapper;
import org.brokenarrow.blockmirror.api.builders.SettingsData;
import org.brokenarrow.blockmirror.api.builders.language.Language;
import org.brokenarrow.blockmirror.api.builders.language.PluginMessages;
import org.brokenarrow.blockmirror.api.builders.patterns.PatternWrapperApi;
import org.brokenarrow.blockmirror.api.commands.CommandBuilder;
import org.brokenarrow.blockmirror.blockpatterns.CirclePattern;
import org.brokenarrow.blockmirror.blockpatterns.cache.PatternCache;
import org.brokenarrow.blockmirror.blockpatterns.utily.ChangeFacingToPattern;
import org.brokenarrow.blockmirror.blockpatterns.utily.PatternWrapper;
import org.brokenarrow.blockmirror.blockpatterns.utily.SetFillBlocksInPattern;
import org.brokenarrow.blockmirror.commandhandler.CommandRegister;
import org.brokenarrow.blockmirror.commands.ClassicMirror;
import org.brokenarrow.blockmirror.commands.CustomMirror;
import org.brokenarrow.blockmirror.commands.PatternsCommand;
import org.brokenarrow.blockmirror.commands.Reload;
import org.brokenarrow.blockmirror.listeners.CheckPlayerInventory;
import org.brokenarrow.blockmirror.listeners.ClassicPlacement;
import org.brokenarrow.blockmirror.listeners.CustomPlacements;
import org.brokenarrow.blockmirror.listeners.ItemRemove;
import org.brokenarrow.blockmirror.listeners.PatternPlacements;
import org.brokenarrow.blockmirror.settings.LanguageCache;
import org.brokenarrow.blockmirror.settings.MenusCache;
import org.brokenarrow.blockmirror.settings.Settings;
import org.brokenarrow.blockmirror.tasks.RunTasks;
import org.brokenarrow.blockmirror.utily.TextConvertPlaceholders;
import org.brokenarrow.menu.library.RegisterMenuAPI;
import org.brokenarrow.menu.library.dependencies.nbt.nbtapi.metodes.RegisterNbtAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.logging.Level;

public class BlockMirror extends BlockMirrorUtillity implements BlockMirrorAPI {

	private PlayerCache playerCache;

	private CommandRegister commandRegister;
	private MenusCache menusCache;
	private RunTasks runTask;
	private LanguageCache languageCache;
	private PatternCache patternCache;
	private Settings settings;

	@Override
	public void onLoad() {
		plugin = this;

	}

	@Override
	public void onEnable() {
		plugin = this;
		new RegisterMenuAPI(this);
		this.playerCache = new PlayerCache();
		this.menusCache = new MenusCache(this);
		this.languageCache = new LanguageCache(this);

		this.menusCache.reload();
		this.languageCache.reload();

		this.runTask = new RunTasks(true);
		this.patternCache = new PatternCache();

		this.settings = new Settings(this);
		this.settings.reload();

		registerListeners();

		this.patternCache.addPattern(new CirclePattern());
		registerCommands();
	}

	@Override
	public void onDisable() {
	}

	public void registerListeners() {
		BlockMirrorListeners blockMirrorListeners = new BlockMirrorListeners();
		this.getServer().getPluginManager().registerEvents(blockMirrorListeners, this);


		blockMirrorListeners.addLissner(new ClassicPlacement());
		blockMirrorListeners.addLissner(new CustomPlacements());
		blockMirrorListeners.addLissner(new ItemRemove());
		blockMirrorListeners.addLissner(new PatternPlacements());
		blockMirrorListeners.addLissner(new CheckPlayerInventory());
	}

	@Override
	public PlayerCache getPlayerCache() {
		return playerCache;
	}

	public static BlockMirror getPlugin() {
		return (BlockMirror) plugin;
	}

	public RegisterNbtAPI getNbt() {
		return RegisterMenuAPI.getNbtApi();
	}

	public MenusCache getMenusCache() {
		return menusCache;
	}

	@Override
	public LanguageCache getLanguageCache() {
		return languageCache;
	}

	public PatternCache getPatternCache() {
		return patternCache;
	}

	@Override
	public Settings getSettings() {
		return settings;
	}

	@Override
	public PatternWrapperApi createPatternWrapperApi(@Nonnull ItemWrapper circlePassive, ItemWrapper circleActive) {
		return new PatternWrapper(circlePassive, circleActive);
	}

	@Override
	public PatternSettingsWrapperApi createSetFillBlocksInPattern(@Nonnull final ItemWrapper passive, final ItemWrapper active) {
		return new SetFillBlocksInPattern(passive, active);
	}

	@Override
	public PatternSettingsWrapperApi createChangeFacingToPattern(@Nonnull final ItemWrapper passive, final ItemWrapper active) {
		return new ChangeFacingToPattern(passive, active);
	}

	@Override
	public void addPatter(PatternData pattern) {
		if (this.getPatternCache() == null) {
			return;
		}
		this.getPatternCache().addPattern(pattern);
	}

	public RunTasks getRunTask() {
		return runTask;
	}

	public static BukkitTask runTaskLater(long tick, Runnable task, boolean async) {
		if (async)
			return Bukkit.getScheduler().runTaskLaterAsynchronously(BlockMirror.getPlugin(), task, tick);
		else
			return Bukkit.getScheduler().runTaskLater(BlockMirror.getPlugin(), task, tick);
	}

	public static BukkitTask runTaskTimer(long tick, Runnable task, boolean async) {
		if (async)
			return Bukkit.getScheduler().runTaskTimerAsynchronously(BlockMirror.getPlugin(), task, 10, tick);
		else
			return Bukkit.getScheduler().runTaskTimer(BlockMirror.getPlugin(), task, 10, tick);
	}

	@Override
	public boolean hasPermission(@Nonnull final Player player, @Nonnull final String permission) {
		//todo Shall I add op check also?
		return /*player.isOp() ||*/ player.hasPermission("blockmirror.admin.*") || player.hasPermission(permission);
	}

	public void registerCommands() {
		CommandRegister commandRegister = CommandRegister.getInstance();
		String pluginName = getPluginName().toLowerCase();
		SettingsData settings = this.getSettings().getSettingsData();
		String mainCommand = null;
		if (settings != null)
			mainCommand = settings.getMainCommand();
		if (mainCommand == null || mainCommand.isEmpty())
			mainCommand = "blockmirror|mirror";

		commandRegister.registerMainCommand(getPluginName(), mainCommand)
				.setHelpPrefixMessage("&f---------------&8<&6Command info&8>&f-------------------", "")
				.setCommandLableMessageNoPerms("&fUse command&6 {lable}&f and required perm:&6 {perm}")
				.setCommandLableMessage("&fUsage of command:&6 {lable}")
				.setHelpSuffixMessage("", "&f---------------&8<&6Command info&8>&f-------------------");

		this.commandRegister = commandRegister;
		this.commandRegister.registerSubCommand(new CommandBuilder
				.Builder(new CustomMirror())
				.setHideLable(true)
				.setPermission(pluginName + ".command.custom_mirror")
				.setPermissionMessage("&4You don't have this permission&6 {perm} &4.")
				//.setUsageMessages("/dani about is used to get information about the plugin.")
				.build());
		this.commandRegister.registerSubCommand(new CommandBuilder
				.Builder(new ClassicMirror())
				.setHideLable(true)
				.setPermission(pluginName + ".command.classic_mirror")
				.setPermissionMessage("&4You don't have this permission&6 {perm} &4.")
				//.setUsageMessages("/dani about is used to get information about the plugin.")
				.build());
		this.commandRegister.registerSubCommand(new CommandBuilder
				.Builder(new PatternsCommand())
				.setHideLable(true)
				.setPermission(pluginName + ".command.patterns_command")
				.setPermissionMessage("&4You don't have this permission&6 {perm} &4.")
				//.setUsageMessages("/dani about is used to get information about the plugin.")
				.build());
		this.commandRegister.registerSubCommand(new CommandBuilder
				.Builder(new Reload())
				.setHideLable(true)
				.setPermission(pluginName + ".admin.command.reload")
				.setPermissionMessage("&4You don't have this permission&6 {perm} &4.")
				//.setUsageMessages("/dani about is used to get information about the plugin.")
				.build());
	}

	@Override
	public String getPluginName() {
		return plugin.getName();
	}

	@Override
	public void log(final Level level, final String message) {
		this.getLogger().log(level, message);
	}

	public void sendPlainMessage(CommandSender sender, String message) {
		if (message != null) {
			sender.sendMessage(TextTranslator.toSpigotFormat(message));
		}
	}

	public void sendPlainMessage(Player player, String message) {
		if (message != null) {
			if (!player.isConversing())
				player.sendMessage(TextTranslator.toSpigotFormat(message));
			else
				player.sendRawMessage(TextTranslator.toSpigotFormat(message));
		}
	}

	public void sendMessage(final Player player, String key, final Object... placeholders) {
		Language language = BlockMirror.getPlugin().getLanguageCache().getLanguage();
		PluginMessages pluginMessages = language.getPluginMessages();
		if (pluginMessages == null) return;
		List<String> messages = pluginMessages.getMessage(key);
		String pluginName = pluginMessages.getPluginName();
		if (pluginName == null)
			pluginName = "";
		if (!messages.isEmpty()) {
			boolean addPreSuffix = messages.size() > 1;
			if (addPreSuffix && pluginMessages.getPrefixDecor() != null) {
				String prefixMsg = TextConvertPlaceholders.translatePlaceholders(pluginMessages.getPrefixDecor(), pluginName);
				sendPlainMessage(player, prefixMsg);
			}
			for (String message : messages) {
				if (message == null) continue;
				message = TextConvertPlaceholders.translatePlaceholders((addPreSuffix ? "" : pluginName) + message, placeholders);
				sendPlainMessage(player, message);
			}
			if (addPreSuffix && pluginMessages.getSuffixDecor() != null) {
				String suffixMsg = TextConvertPlaceholders.translatePlaceholders(pluginMessages.getSuffixDecor(), pluginName);
				sendPlainMessage(player, suffixMsg);
			}
		}
	}
}
