int[10][20] a;

int main() {
	int i;
	int j;
	int ret;
	
	i := 0;

	while (i < 10) {
		j := 0;
		while (j < 20) {
			a[i][j] := (i * 20 + j);
			j := j + 1;
		}
		i := i + 1;
	}
	
	i := 0;
	j := 0;
	
	while (i < 10) {
		j := 0;
		while (j < 20) {
			ret := writeInt(a[i][j]);
			ret := writeChar(32);
			j := j + 1;
		}
		i := i + 1;
	}
	ret := writeChar(10);
	return 0;
}
