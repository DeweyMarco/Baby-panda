
public class Jaco {

	public static void main(String[] args) {
		System.out.println(Networking.getIpAddress());
		if(Networking.connect("192.168.56.1", true) == 1) {
		
		} else if (Networking.connect("192.168.56.1", true) == 0) {
	
		}
		else {
			System.out.println("No One Loves You");
		}
		String a = Networking.read();
		
	}
	
}
//My IP Address = 192.168.56.1