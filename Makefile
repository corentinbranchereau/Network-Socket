default :
	@echo compile, launchTCPClient, launchTCPServer, launchUDP

compile :
	javac -d Code-Socket/classes/ Code-Socket/src/tcp/client/ClientGUI.java Code-Socket/src/tcp/client/EchoClient.java Code-Socket/src/tcp/client/ServerListenerThread.java Code-Socket/src/tcp/server/ChatObserver.java Code-Socket/src/tcp/server/ClientThread.java Code-Socket/src/tcp/server/EchoServerMultiThreaded.java Code-Socket/src/utils/Message.java Code-Socket/src/multicast/MulticastClient.java Code-Socket/src/multicast/MulticastReceiver.java Code-Socket/src/multicast/MulticastSender.java

launchTCPClient : 
	java -cp Code-Socket/classes/ tcp.client.ClientGUI

launchTCPServer :
	java -cp Code-Socket/classes tcp.server.EchoServerMultiThreaded 1024

launchUDP :
	java -cp Code-Socket/classes multicast.MulticastClient 224.0.0.1 1024 $(name)
