public class CBranchCode extends ICode {
	protected CLabel destination;

	public CBranchCode(CLabel destination) {
		setDestination(destination);
	}
	
	public CLabel getDestination() {
		return destination;
	}

	public void setDestination(CLabel destination) {
		this.destination = destination;
	}

	public Operand getFirstOpIfPossible() {
		if (this instanceof CBEQ) return ((CBEQ)this).getFirstOp();
			else if (this instanceof CBNE) return ((CBNE)this).getFirstOp();
			else if (this instanceof CBGE) return ((CBGE)this).getFirstOp();
			else if (this instanceof CBGT) return ((CBGT)this).getFirstOp();
			else if (this instanceof CBLE) return ((CBLE)this).getFirstOp();
			else if (this instanceof CBLT) return ((CBLT)this).getFirstOp();
		throw new RuntimeException("No first op with this type of branch available.");
	}
	
	public Operand getSecondOpIfPossible() {
		if (this instanceof CBEQ) return ((CBEQ)this).getSecondOp();
			else if (this instanceof CBNE) return ((CBNE)this).getSecondOp();
			else if (this instanceof CBGE) return ((CBGE)this).getSecondOp();
			else if (this instanceof CBGT) return ((CBGT)this).getSecondOp();
			else if (this instanceof CBLE) return ((CBLE)this).getSecondOp();
			else if (this instanceof CBLT) return ((CBLT)this).getSecondOp();
		throw new RuntimeException("No second op with this type of branch available.");
	}
}
