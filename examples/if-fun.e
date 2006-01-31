int main() {
	int a;
	int ret;
	a := 3;
	
	if (a = 3) {
		a := 0;
	}
	else {
		a := 1;
	}
	
	if (a = 0) {
		a := 5;
	}
	ret := writeInt(a);
	ret := writeChar(10);
	return 0;
}
