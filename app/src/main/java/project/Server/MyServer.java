package project.Server;
import com.sun.net.httpserver.*;

import project.Client.CreateAccountWindow.CreateAccountWindow;
import project.Client.CreateAccountWindow.CreateAccountWindowBody;
import project.Server.Client.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.*;
import java.util.*;

public class MyServer {


	// initialize server port and hostname
	private static final int SERVER_PORT = 8100;
	private static final String SERVER_HOSTNAME = "localhost";

	public static void main(String[] args) throws IOException {
		ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
		Map<String, List<String>> data = new HashMap<>();
		Map<String, Map<String,String>> data1 = new HashMap<>();
		// create a server
		HttpServer server = HttpServer.create(new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT), 0);

		server.createContext("/create" , new UserHandler(data));
		server.createContext("/login", new LoginHandler());
		server.createContext("/recipe", new RecipeHandler());
		server.createContext("/close",new CloseHandler());
		server.createContext("/share" , new ShareHandler(data1));
		server.setExecutor(threadPoolExecutor);
		server.start();
		System.out.println("Server started on port " + SERVER_PORT);
	}
}