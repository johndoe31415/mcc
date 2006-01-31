import java.util.LinkedList;

public class ICodeContainer extends LinkedList<ICode> {
	protected String name;
	
	public ICodeContainer(String name) {
		this.name = name;
	}
	
	public void acceptVisitor(CILVisitor visitor) {
		visitor.visit(this);
	}

	public final String getName() {
		return name;
	}
}
