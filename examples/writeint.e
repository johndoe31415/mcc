int main() {
	int a;
	int b;
	int d;

	a := 10;
	b := 123456;

	d:=writeInt(a);
	d:=writeChar(10);
	d:=writeInt(b);
	d:=writeChar(10);

	a := 0-10;
	b := 0-123456;
	d:=writeInt(a);
	d:=writeChar(10);
	d:=writeInt(b);
	d:=writeChar(10);
	
	return 0;
}

