package strimy.bukkit.plugins.serverbrowser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class ServerConfiguration 
{
	private Document doc;
	private Element root;
	private String pluginConfigPath;
	
	ServerBrowser plugin;
	
	private String dnsServer;
	private String serverName;
	private boolean isPremiumOnly;
	private String token;
	private String password;
	private String serverBrowserUrl;
	private boolean isVerbose = false;
	private Integer notifDelay = 60*5*1000;
	
	public String getServerBrowserUrl() {
		return serverBrowserUrl;
	}

	public void setServerBrowserUrl(String serverBrowserUrl) {
		this.serverBrowserUrl = serverBrowserUrl;
	}

	public ServerConfiguration(ServerBrowser plugin) 
	{
		this.plugin = plugin;
		pluginConfigPath = plugin.getDataFolder().getParent() + "/serverbrowser.xml";
		
		setPluginConfiguration();
	}
	
	public boolean isVerbose() {
		return isVerbose;
	}

	public void setVerbose(boolean isVerbose) {
		this.isVerbose = isVerbose;
	}

	private void setPluginConfiguration()
	{
		if(new File(pluginConfigPath).exists())
		{
			readPluginConfiguration();
		}
		else
		{
			createPluginConfiguration();
		}
	}
	
	public void readPluginConfiguration()
	{
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		DocumentBuilder db;
		try 
		{
			db = dbf.newDocumentBuilder();
			doc = db.parse(pluginConfigPath);
			root = doc.getDocumentElement();
			
			Node serverNameNode = root.getElementsByTagName("ServerName").item(0);
			setServerName(serverNameNode.getTextContent());
			
			Node serverDnsNode = root.getElementsByTagName("ServerDNS").item(0);
			setDnsServer(serverDnsNode.getTextContent());
			
			Node tokenNode = root.getElementsByTagName("Token").item(0);
			setToken(tokenNode.getTextContent());
			
			Node tokenPasswordNode = root.getElementsByTagName("TokenPassword").item(0);
			setPassword(tokenPasswordNode.getTextContent());
			
			Node serverBrowserUrlNode = root.getElementsByTagName("ServerBrowserUrl").item(0);
			setServerBrowserUrl(serverBrowserUrlNode.getTextContent());
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void createPluginConfiguration()
	{
		DocumentBuilderFactory factory   = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try 
		{
			builder = factory.newDocumentBuilder();
			DOMImplementation impl = builder.getDOMImplementation();
			
			doc = impl.createDocument(null, null, null);
			root = doc.createElement("ServerConfig");
			doc.appendChild(root);
			
			Element serverNameElem = doc.createElement("ServerName");
			serverNameElem.setTextContent("Your Server Name");
			root.appendChild(serverNameElem);
			
			Element serverBrowserUrlElem = doc.createElement("ServerBrowserUrl");
			serverBrowserUrlElem.setTextContent("The URL to the Web Server Browser");
			root.appendChild(serverBrowserUrlElem);
			
			Element serverDnsElem = doc.createElement("ServerDNS");
			serverDnsElem.setTextContent("Your DNS Name");
			root.appendChild(serverDnsElem);
			
			// Create a token to identify the server
			Element tokenElem = doc.createElement("Token");
			UUID newToken = UUID.randomUUID();
			tokenElem.setTextContent(newToken.toString());
			root.appendChild(tokenElem);
			
			// Generate a password for this token
			String password = getMD5(getServerName() + newToken + UUID.randomUUID().toString());
			
			Element passwordElem = doc.createElement("TokenPassword");
			passwordElem.setTextContent(password);
			root.appendChild(passwordElem);
						
			saveXml();
			
			readPluginConfiguration();
			
		} 
		catch (ParserConfigurationException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	private void saveXml()
	{
		 try {
		        // Prepare the DOM document for writing
		        Source source = new DOMSource(doc);

		        // Prepare the output file
		        File file = new File(pluginConfigPath);
		        Result result = new StreamResult(file);

		        // Write the DOM document to the file
		        Transformer xformer = TransformerFactory.newInstance().newTransformer();
		        xformer.transform(source, result);
		    } catch (TransformerConfigurationException e) {
		    } catch (TransformerException e) {
		    }
	}
	
	public void readServerConfiguration()
	{
		File serverConf = new File("server.properties");
		if(serverConf.exists())
		{
			try 
			{
				BufferedReader br = new BufferedReader(new FileReader(serverConf));
				
				String line;
				while((line = br.readLine()) != null)
				{
					if(line.startsWith("#"))
					{
						continue;
					}
					
					String[] conf = line.split("=");
					if(conf.length != 2)
						continue;
					
					if(conf[0].equals("online-mode"))
					{
						setPremiumOnly(conf[1].equals("true"));
					}
				}
			} 
			catch (IOException e) 
			{
					e.printStackTrace();
			}
		}
	}

	public void refreshServerInfos()
	{
		// to delete...
	}
	
	public String getDnsServer() {
		return dnsServer;
	}

	public void setDnsServer(String dnsServer) {
		this.dnsServer = dnsServer;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public boolean isPremiumOnly() {
		return isPremiumOnly;
	}

	public void setPremiumOnly(boolean isPremiumOnly) {
		this.isPremiumOnly = isPremiumOnly;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String string) {
		this.token = string;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	private String bytes2Hex(byte[] data)
	{
		StringBuffer sb = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
          sb.append(Integer.toString((data[i] & 0xff) + 0x100, 16).substring(1));
        }
        
        return sb.toString();
	}
	
	private String getMD5(String str)
	{
		try 
		{
			return bytes2Hex(MessageDigest.getInstance("MD5").digest(str.getBytes()));
		} 
		catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
		}
		return null;
	}

	public void setNotifDelay(Integer notifDelay) {
		this.notifDelay = notifDelay;
	}

	public Integer getNotifDelay() {
		return notifDelay;
	}
}
