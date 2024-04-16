import com.asierso.astraconnector.AstraConnector;
import com.asierso.astraconnector.actions.GetModels;

public class Main {
	public static void main(String[] args) {
		try {
			AstraConnector conn = new AstraConnector("127.0.0.1", 26700, "atk_yzbk3txxis32a49utxm9sgta2");
			System.out.println(conn.fetch(new GetModels()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}