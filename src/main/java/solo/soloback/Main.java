package solo.soloback;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.Player;
import cn.nukkit.plugin.PluginBase;
import solo.solobasepackage.util.Message;
import cn.nukkit.level.Position;
import cn.nukkit.event.Listener;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class Main extends PluginBase implements Listener{

  private HashMap<String, Position> posList = new HashMap<String, Position>();

  @Override
  public void onEnable() {
    this.getServer().getPluginManager().registerEvents(this, this);
  }

  @EventHandler
  public void onTeleport(PlayerTeleportEvent event){
    this.posList.put(event.getPlayer().getName().toLowerCase(), event.getFrom());
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent event){
  	String name = event.getPlayer().getName().toLowerCase();
  	if(this.posList.containsKey(name)){
  		this.posList.remove(name);
  	}
  }

  @EventHandler
  public void onDeath(PlayerDeathEvent event){
  	if(! (event.getEntity() instanceof Player)){
		return;
  	}
  	Player player = (Player) event.getEntity();
  	this.posList.put(player.getName().toLowerCase(), new Position(player.x, player.y, player.z, player.level));
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
  	if(command.getName().equals("back")){
  		if(! (sender instanceof Player)){
  			Message.alert(sender, "인게임에서만 사용 가능합니다.");
  			return true;
  		}
  		Player player = (Player) sender;
  		String name = sender.getName().toLowerCase();
  		if(this.posList.containsKey(name)){
  			player.teleport(this.posList.get(name));
  			Message.normal(sender, "텔레포트하기 전의 장소로 이동합니다.");
  		}else{
  			Message.alert(sender, "텔레포트 기록이 없습니다.");
  		}
  	}
  	return true;
  }
}
