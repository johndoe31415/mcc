import java.util.*;

public class VariableFactory {
	private static int id = 0;

	public static Variable getTempVariable(Type type) {
		return new Variable("__tmp" + id++, type, -1, new Vector<Integer>());
	}
}
