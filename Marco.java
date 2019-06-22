
public class Marco {

	public static void main(String[] args) {
		System.out.println(Networking.getIpAddress());
		Networking.connect("192.168.56.1");
		String a = Networking.read();
		
	}
	
}