package drone;

import guiModel.ConsoleModel;


public class Controller {
	private Connection _connection, _connectionFake;
	private ConsoleModel _consoleModel;
	private String _addr;
	
	public int getSeq() {
	//	return _connectionFake.getSeq();
		return _connection.getSeq();
	}
	
	public String getAddr() {
		return _addr;
	}

	public Controller(String addr, 
					int port, 
					String addrFake, 
					int portFake, 
					ConsoleModel consoleModel) throws InterruptedException {
		_addr = addr;
		_connection = new Connection(addr, port, "\r");
		_connectionFake = new Connection(addrFake, portFake, "\n");
		_consoleModel = consoleModel;
		this.initialize();
	}

	public void sendMessage(String message) {
		_connection.sendMessage(message + "\r");
		_connectionFake.sendMessage(message + "\n");
	}

	public void run() {
		try {
			initialize();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// sequence d'initialisation a executer avant tout autre commande a envoyer
	public void initialize() throws InterruptedException {
		System.out.println("initialisation du drone : " + _addr +" \n");
		_consoleModel.writeInFile("UAV is inializing. Please wait...");
		sendMessage(Commands.configIDS(_connection.getSeq()));
		sendMessage(Commands.config("custom:session_id", "-all",
				_connection.getSeq()));
		Thread.sleep(250);

		sendMessage(Commands.configIDS(_connection.getSeq()));
		sendMessage(Commands.config("custom:profile_id",
				"-" + Convert.convertCRC32(Commands.PROFILE),
				_connection.getSeq()));
		;
		Thread.sleep(250);

		sendMessage(Commands.configIDS(_connection.getSeq()));
		sendMessage(Commands.config("custom:application_id",
				"-" + Convert.convertCRC32(Commands.APPLI),
				_connection.getSeq()));
		Thread.sleep(250);

		sendMessage(Commands.configIDS(_connection.getSeq()));
		sendMessage(Commands.config("custom:session_id",
				Convert.convertCRC32(Commands.SESSION), _connection.getSeq()));
		Thread.sleep(250);

		sendMessage(Commands.configIDS(_connection.getSeq()));
		sendMessage(Commands.config("custom:application_id",
				Convert.convertCRC32(Commands.APPLI), _connection.getSeq()));
		Thread.sleep(250);

		sendMessage(Commands.configIDS(_connection.getSeq()));
		sendMessage(Commands.config("custom:profile_id",
				Convert.convertCRC32(Commands.PROFILE), _connection.getSeq()));
		Thread.sleep(250);

		sendMessage(Commands.configIDS(_connection.getSeq()));
		sendMessage(Commands.config("custom:application_desc", Commands.APPLI,
				_connection.getSeq()));
		Thread.sleep(250);

		sendMessage(Commands.configIDS(_connection.getSeq()));
		sendMessage(Commands.config("custom:profile_desc", Commands.PROFILE,
				_connection.getSeq()));
		Thread.sleep(250);

		sendMessage(Commands.configIDS(_connection.getSeq()));
		sendMessage(Commands.config("custom:session_desc", Commands.SESSION,
				_connection.getSeq()));
		Thread.sleep(250);
		sendMessage(Commands.check(_connection.getSeq()));
		_consoleModel.writeInFile("[OK] UAV is now initialized.");
	}
}
