package be.lioche.compact.events;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import be.lioche.api.enums.Symboles;
import be.lioche.api.enums.Text;
import be.lioche.api.packet.Bar;
import be.lioche.api.utils.Msgs;
import be.lioche.compact.main.Main;

public class Chat implements Listener{

	public Chat(Main main){
		main.getServer().getPluginManager().registerEvents(this, Main.instance);
	}

	@EventHandler
	public void citation(AsyncPlayerChatEvent e){

		for(Player all : Bukkit.getOnlinePlayers()){
			if(all != e.getPlayer()){
				if(e.getMessage().contains(all.getName())){
					all.playSound(all.getLocation(), Sound.NOTE_PIANO, 1, 1);
					Bar.sendBar(all, "§d"+e.getPlayer().getName()+" vous a cité !");
				}
			}
		}

		if(!Main.instance.getConfig().getBoolean("Chat") && !e.getPlayer().hasPermission("lioche.compact.bypasschat")){
			e.setCancelled(true);
			e.getPlayer().sendMessage(Main.prefix+Text.CHAT_OFF.get());
		}

		String[] message = e.getMessage().toString().split(" ");
		String newmessage = "";

		for (String m : message) {
			if (m.startsWith("www.") || m.startsWith("http:") || m.endsWith(".com") || m.endsWith(".be") || m.endsWith(".eu") || m.endsWith(".fr")
					|| m.endsWith(".net") || m.endsWith(".org") || m.endsWith(".ch") && !m.equals("ts3.cubemania.fr") && !m.contains("ts3")) {
				if(!m.contains("porn") && !m.contains("sex") && !m.contains("teub") && !m.contains("xnxx") && !m.contains("tukif")
						&& !m.contains("inceste") && !m.contains("2girls1cup") && !m.contains("bite")){
					
					if(!m.contains("http://")) m = "http://"+m;
					
					String nm = m.replace("www.", "http://").replace("/", "%2F").replace(":", "%3A").replace("#", "%23").replace("?", "%3F").replace("=", "%3D");
//					String moneylink = "https://api.shorte.st/s/5a2a52d83f1d5a4bf971471f27c64412/" + m;
					String freelink = "https://api-ssl.bitly.com/v3/shorten?"
							+ "access_token=9583620e67093de4f648d9f4f3c4034380985075"
							+ "&longUrl="+ nm 
							+"&format=txt";

//					String status_txt = null;
					String shortenedUrl = null;
					try {
						BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(freelink).openStream()));
						
//						Utilisé si &format de freelink = json.
//						Moins fiable mais plus simple de passer par un format txt.
						
//						JSONParser parser = new JSONParser();
//						Object obj = parser.parse(reader);
//						JSONObject jsonObject = (JSONObject) obj;
//						status_txt = (String) jsonObject.get("status_txt");
//						shortenedUrl = (String) jsonObject.get("url");
					
						shortenedUrl = reader.readLine();
						
					} catch (Exception ex) {}

					Msgs.sendConsole("§cAncien lien: "+m);
					Msgs.sendConsole("§cRetranscription: "+nm);
					
					if (shortenedUrl != null) {
						newmessage = newmessage + " " + shortenedUrl;
					}else {
						newmessage = newmessage + " " + m;
						Bukkit.getConsoleSender().sendMessage("§cErreur: Lien normal autorisé.");
					}
				}else{
					e.setCancelled(true);
					e.getPlayer().sendMessage("§c§lLes liens pornographiques sont interdits !");
					Bukkit.getConsoleSender().sendMessage(e.getPlayer().getName()+" a mis un lien porn.");
				}
			}else {
				switch (m) {
				case "<3":
					newmessage = newmessage + " " + Symboles.HEARTH.get();
					break;

				default:
					newmessage = newmessage + " " + m;
					break;
				}
			}
		}

		e.setMessage(newmessage);
	}
}
