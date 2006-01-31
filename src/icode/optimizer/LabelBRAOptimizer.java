import java.util.*;

class LabelBRAOptimizer {
	protected FunctionContainer functions;
	
	public LabelBRAOptimizer(FunctionContainer functions) {
		this.functions = functions;
	}

	protected boolean removeIndirectJumps() {
		boolean changed = false;
		Vector<CLabel> label_src = new Vector<CLabel>();
		Vector<CLabel> label_dst = new Vector<CLabel>();
		
		for (Iterator i = functions.iterator(); i.hasNext(); ) {
			ICodeContainer c = (ICodeContainer)i.next();
			for (int j = 0; j < c.size() - 1; j++) {
				ICode this_ic, next_ic;
				this_ic = c.get(j);
				next_ic = c.get(j + 1);
				if ((this_ic instanceof CLabel) && (next_ic instanceof CBRA)) {
					CLabel a = (CLabel)this_ic;
					CBRA b = (CBRA)next_ic;
					label_src.add(a);
					label_dst.add(b.getDestination());
				}
			}
		}

		// Resolve double jumps here
		for (int i = 0; i<label_src.size(); i++) {
			int idx = label_src.indexOf(label_dst.get(i));
			if (idx != -1) {
				label_dst.set(i, label_dst.get(idx));
			}
		}
	
		/*
		for (int i = 0; i < label_src.size(); i++) {
			System.out.println("Replacing label src=" + label_src.get(i) + " by dst=" + label_dst.get(i));
		}
		*/

		for (Iterator i = functions.iterator(); i.hasNext(); ) {
			ICodeContainer c = (ICodeContainer)i.next();
			for (Iterator j = c.iterator(); j.hasNext(); ) {
				ICode this_ic = (ICode)j.next();
				if (this_ic instanceof CBranchCode) {
					CBranchCode a = (CBranchCode)this_ic;
					int idx = label_src.indexOf(a.getDestination());
					if (idx != -1) {
						a.setDestination(label_dst.get(idx));
						changed = true;
					}
				}
				if (this_ic instanceof CLabel) {
					CLabel a = (CLabel)this_ic;
					if (label_src.contains(a.getName())) {
						/* Remove Label ICode */
						j.remove();
						changed = true;
					}
				}
			}
		}
		return changed;
	}

	protected boolean removeDoubleJumps() {
		for (Iterator i = functions.iterator(); i.hasNext(); ) {
			ICodeContainer c = (ICodeContainer)i.next();
			for (int j = 0; j < c.size() - 1; j++) {
				ICode this_ic, next_ic;
				this_ic = c.get(j);
				next_ic = c.get(j + 1);
				if ((this_ic instanceof CBRA) && (next_ic instanceof CBRA)) {
					c.remove(j + 1);
					j--;
				}
			}
		}

		return false;
	}

	public void optimize() {
		boolean changed;
		int runs = 0;
		do {
			runs++;
			changed = false;
			changed = (changed || removeIndirectJumps());
			changed = (changed || removeDoubleJumps());
		} while (changed);
		//System.err.println("Immediate code optimization took " + runs + " runs.");
	}
}
