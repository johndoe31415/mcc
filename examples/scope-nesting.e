int main() {
	int a;
	a := 3;
	{
		int b;
		b := 5;
		{
			int c;
			c := 20;
			{
				int d;
				d := 4;
				{
					int e;
					e := ((a * b) + (c * d)) * 2;
					a := writeInt(e);
					a := writeChar(10);
				}
			}
		}
	}

	return a;
}
