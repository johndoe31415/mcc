import java.util.LinkedList;

public class FunctionContainer extends LinkedList<ICodeContainer> {
	public void acceptVisitor(CILVisitor visitor) {
		visitor.visit(this);
	}
}
