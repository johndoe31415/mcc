public abstract class LabelFactory {
	private static int cnt=0;
	
	public static CLabel getNextLabel() {
		cnt++;
		return new CLabel("lbl" + cnt);
	}
}
