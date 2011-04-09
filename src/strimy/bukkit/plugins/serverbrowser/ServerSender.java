package strimy.bukkit.plugins.serverbrowser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ServerSender 
{
	ServerBrowser plugin;
	

	ServerConfiguration config;


	List<JavaPlugin> pluginList = new ArrayList<JavaPlugin>();
	
	public ServerSender(ServerBrowser plugin, ServerConfiguration config)
	{
		this.plugin = plugin;
		this.config = config;
		sendInfos();
	}
	
	public void sendInfos()
	{
		DocumentBuilderFactory factory   = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try 
		{
			builder = factory.newDocumentBuilder();
			DOMImplementation impl = builder.getDOMImplementation();
			
			Document doc = impl.createDocument(null, null, null);
			Element root = doc.createElement("ServerInfos");
			doc.appendChild(root);
			
			Element serverNameElem = doc.createElement("ServerName");
			serverNameElem.setTextContent(config.getServerName());
			root.appendChild(serverNameElem);
			
			Element serverDnsElem = doc.createElement("ServerDns");
			serverDnsElem.setTextContent(config.getDnsServer());
			root.appendChild(serverDnsElem);
			
			Element serverPort = doc.createElement("ServerPort");
			serverPort.setTextContent(String.valueOf(plugin.getServer().getPort()));
			root.appendChild(serverPort);
			
			Element maxPlayersElem = doc.createElement("MaxPlayers");
			maxPlayersElem.setTextContent(String.valueOf(plugin.getServer().getMaxPlayers()));
			root.appendChild(maxPlayersElem);
			
			Element isPremiumOnlyElem = doc.createElement("IsPremiumOnlyElem");
			isPremiumOnlyElem.setTextContent(String.valueOf(config.isPremiumOnly()));
			root.appendChild(isPremiumOnlyElem);
			
			Element tokenElem = doc.createElement("Token");
			tokenElem.setTextContent(String.valueOf(config.getToken()));
			root.appendChild(tokenElem);
			
			Element passwordElem = doc.createElement("Password");
			passwordElem.setTextContent(String.valueOf(config.getPassword()));
			root.appendChild(passwordElem);
			
			Element playersElem = doc.createElement("Players");
			root.appendChild(playersElem);
			for (Player player : plugin.getServer().getOnlinePlayers()) 
			{
				Element playerElem = doc.createElement("Player");
				playerElem.setAttribute("Name", player.getDisplayName());
				playerElem.setAttribute("TimeOnline", "Unknown");
				playersElem.appendChild(playerElem);
			}
			
			DOMSource source = new DOMSource(doc);
	        StringWriter sw = new StringWriter();

	        // Write the DOM document to the file
	        Transformer xformer = TransformerFactory.newInstance().newTransformer();
	        xformer.transform(source, new StreamResult(sw));
	        
	        String data = URLEncoder.encode("data", "UTF-8") + "=" + URLEncoder.encode(sw.toString(), "UTF-8");
	        
	        // Send data
	        URL url = new URL("http://strimy2.dyndns.org/bukkit/updater.php");
	        URLConnection conn = url.openConnection();
	        conn.setDoOutput(true);
	        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
	        wr.write(data);
	        wr.flush();
	        
	        // Get the response
	        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        rd.close();
	        wr.close();

			plugin.log.info("[ServerBrowser] Notification sent");
		} 
		catch (ParserConfigurationException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(FileNotFoundException e)
		{
			plugin.log.info("[ServerBrowser] Error: URL doesn't exist");
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	
	
}
