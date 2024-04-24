import java.util.Arrays;

import com.asierso.astracommons.requests.ClientResponse;
import com.asierso.astraconnector.AstraConnector;
import com.asierso.astraconnector.actions.AstraModel;
import com.asierso.astraconnector.actions.GetModels;
import com.asierso.astraconnector.actions.UseModel;

public class Main {
	public static void main(String[] args) {
		try {
			Thread.sleep(2000);
			AstraConnector conn = new AstraConnector("192.168.1.22", 26700, "atk_o5sd0fy2gty5xuvcrw5qlornc");
			System.out.println(conn.fetch(new GetModels()));
			
			AstraModel m = (AstraModel) conn.fetch(new UseModel("hooks-test"));
			
			ClientResponse res = (ClientResponse) m.runHook("search",Arrays.asList(new String[] {"xC"}));
			
			System.out.println(res.getOutput());
			System.out.println(res.getBody());
			
			Thread.sleep(10000);
			conn.close();
			System.out.println("Handshake and open model test sucess");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}