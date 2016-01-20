package cc.isotopestudio.SubtleRPG.subtlerpg;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SubtleRPGCommand implements CommandExecutor {
	private final SubtleRPG plugin;

	public SubtleRPGCommand(SubtleRPG plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("SubtleRPG"))
			if (args.length > 0 && !args[0].equals("help")) {

				if (args[0].equals("info")) {
					if ((sender instanceof Player && (args.length == 1 || args.length == 2))
							|| (!(sender instanceof Player) && args.length == 2)) {
						if (args.length == 2) {
							Player player = (Bukkit.getServer().getPlayer(args[1]));
							if (player == null) {
								sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED).append("玩家")
										.append(args[1]).append("不存在").toString());
								return true;
							} else {// Core
								sendInfo(sender, args, player);
								return true;
							}
						} else {// Core
							sendInfo(sender, args, (Player) sender);
							return true;
						}

					} else {
						sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED)
								.append("/subtleRPG info <玩家名字>").toString());
					}
				}

				if (args[0].equals("join")) {
					if (sender.hasPermission("subtleRPG.join")) {
						if (args.length == 3) {
							Player player = (Bukkit.getServer().getPlayer(args[2]));
							if (player == null) {
								sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED).append("玩家")
										.append(args[2]).append("不存在").toString());
								return true;
							} else { // Core
								List<String> list = plugin.getConfig().getStringList("Groups");
								String newJob = null, newJobName = null;
								for (int i = 0; i < list.size(); i++) {
									if (list.get(i).equals(args[1])) {
										newJob = list.get(i);
										newJobName = plugin.getConfig().getString(newJob + ".name");
										break;
									}
									if (i == list.size() - 1) {
										sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED)
												.append("职业").append(args[1]).append("不存在").toString());
										return true;
									}
								}
								if (newJob.equals(
										plugin.getPlayersData().getString("Players." + player.getName() + ".group"))) {
									sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED)
											.append("你已经是").append(newJobName).append("了").toString());
									return true;
								}

								// Delete Subgroups
								String temp = "";
								int count = 0;
								while (temp != null) {
									count++;
									temp = plugin.getPlayersData()
											.getString("Players." + player.getName() + ".subGroup" + count);
									if (temp != null) {
										plugin.getPlayersData().set("Players." + player.getName() + ".subGroup" + count,
												null);
									}
								}

								plugin.getPlayersData().set("Players." + player.getName() + ".group", newJob);
								plugin.savePlayersData();

								sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.AQUA)
										.append(args[2]).append("加入了").append(newJobName).append("！").toString());
								return true;
							}
						} else {
							sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED)
									.append("/subtleRPG join <职业> <玩家名字>").toString());
							return true;
						}
					} else {
						sender.sendMessage(
								(new StringBuilder(plugin.prefix)).append(ChatColor.RED).append("你没有权限").toString());
						return true;
					}
				}

				if (args[0].equals("joinsub")) {
					if (sender.hasPermission("subtleRPG.join")) {
						if (args.length == 3) {
							Player player = (Bukkit.getServer().getPlayer(args[2]));
							if (player == null) {
								sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED).append("玩家")
										.append(args[2]).append("不存在").toString());
								return true;
							} else { // Core
								int count = 0;
								String group = plugin.getPlayersData()
										.getString("Players." + player.getName() + ".group");
								String temp = "";
								while (temp != null) {
									count++;
									temp = plugin.getPlayersData()
											.getString("Players." + player.getName() + ".subGroup" + count);
								}
								if (count != 1) {
									group = plugin.getPlayersData()
											.getString("Players." + player.getName() + ".subGroup" + (count - 1));
								}
								List<String> list = plugin.getConfig().getStringList(group + ".Children");
								if (list.size() == 0) {
									sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED)
											.append(plugin.getConfig().getString(group + ".name")).append("没有子职业")
											.toString());
									return true;
								}
								String newSubGroup = null;
								for (int i = 0; i < list.size(); i++) {
									if (list.get(i).equals(args[1])) {
										newSubGroup = list.get(i);
										break;
									}
									if (i == list.size() - 1) {
										sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED)
												.append(plugin.getConfig().getString(group + ".name")).append("的子职业")
												.append(args[1]).append("不存在").toString());
										return true;
									}
								}
								String newJobName = plugin.getConfig().getString(newSubGroup + ".name");

								plugin.getPlayersData().set("Players." + player.getName() + ".subGroup" + count,
										newSubGroup);
								plugin.savePlayersData();

								sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.AQUA)
										.append(args[2]).append("加入了子职业").append(newJobName).append("！").toString());
								return true;
							}
						} else {
							sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.RED)
									.append("/subtleRPG joinsub <子职业> <玩家名字>").toString());
							return true;
						}
					} else {
						sender.sendMessage(
								(new StringBuilder(plugin.prefix)).append(ChatColor.RED).append("你没有权限").toString());
						return true;
					}
				} else {
					sender.sendMessage(
							(new StringBuilder(plugin.prefix)).append(ChatColor.RED).append("未知命令").toString());
					return true;
				}

			} else { // Help Menu
				sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.AQUA).append("帮助菜单").toString());
				sender.sendMessage((new StringBuilder()).append(ChatColor.GOLD).append("/subtleRPG info [玩家名字]")
						.append(ChatColor.GRAY).append(" - ").append(ChatColor.LIGHT_PURPLE).append("查看玩家职业信息")
						.toString());
				sender.sendMessage((new StringBuilder()).append(ChatColor.GOLD).append("/subtleRPG join <职业> <玩家名字>")
						.append(ChatColor.GRAY).append(" - ").append(ChatColor.LIGHT_PURPLE).append("加入一个职业")
						.toString());
				sender.sendMessage((new StringBuilder()).append(ChatColor.GOLD)
						.append("/subtleRPG joinsub <子职业> <玩家名字>").append(ChatColor.GRAY).append(" - ")
						.append(ChatColor.LIGHT_PURPLE).append("加入一个子职业").toString());
				return true;
			}

		return false;

	}

	public void sendInfo(CommandSender sender, String[] args, Player player) {
		sender.sendMessage((new StringBuilder(plugin.prefix)).append(ChatColor.AQUA).append("玩家")
				.append(player.getName()).append("信息").toString());

		boolean isOp = player.isOp();
		if (player.isOp()) {
			player.setOp(false);
		}
		double addDamage = 0, Defence = 0;

		String group = plugin.getPlayersData().getString("Players." + player.getName() + ".group");
		if (group == null) {
			sender.sendMessage((new StringBuilder()).append(ChatColor.DARK_GREEN).append("ta没有职业").toString());
			return;
		}
		addDamage = plugin.getConfig().getDouble(group + ".Attack.default")
				+ plugin.getConfig().getDouble(group + ".Attack.increasePerPeriod")
						* (int) (player.getLevel() / plugin.getConfig().getDouble(group + ".Attack.levPeriod"));
		Defence = plugin.getConfig().getDouble(group + ".Defence.default")
				+ plugin.getConfig().getDouble(group + ".Defence.increasePerPeriod")
						* (int) (player.getLevel() / plugin.getConfig().getDouble(group + ".Defence.levPeriod"));
		sender.sendMessage((new StringBuilder()).append(ChatColor.DARK_GREEN).append("职业: ")
				.append(plugin.getConfig().getString(group + ".name")).toString());

		String temp = "";
		int count = 0;
		while (temp != null) {
			count++;
			temp = plugin.getPlayersData().getString("Players." + player.getName() + ".subGroup" + count);
			if (temp != null && count == 1) {
				sender.sendMessage((new StringBuilder()).append(ChatColor.GREEN).append("子职业: ").toString());
			}
			if (temp != null) {
				addDamage += plugin.getConfig().getDouble(temp + ".Attack.default")
						+ plugin.getConfig().getDouble(temp + ".Attack.increasePerPeriod")
								* (int) (player.getLevel() / plugin.getConfig().getDouble(temp + ".Attack.levPeriod"));
				Defence += plugin.getConfig().getDouble(temp + ".Defence.default")
						+ plugin.getConfig().getDouble(temp + ".Defence.increasePerPeriod")
								* (int) (player.getLevel() / plugin.getConfig().getDouble(temp + ".Defence.levPeriod"));
				sender.sendMessage((new StringBuilder()).append(ChatColor.GREEN).append("- ")
						.append(plugin.getConfig().getString(temp + ".name")).toString());
			}
		}
		sender.sendMessage((new StringBuilder()).append(ChatColor.DARK_RED).append("HP: ").append(player.getHealth())
				.append(" / ").append(player.getMaxHealth()).toString());
		sender.sendMessage(
				(new StringBuilder()).append(ChatColor.DARK_GREEN).append("等级: ").append(player.getLevel()).toString());
		sender.sendMessage(
				(new StringBuilder()).append(ChatColor.YELLOW).append("攻击加成: ").append(addDamage).toString());
		sender.sendMessage((new StringBuilder()).append(ChatColor.YELLOW).append("防御: ").append(Defence).toString());

		player.setOp(isOp);

	}
}