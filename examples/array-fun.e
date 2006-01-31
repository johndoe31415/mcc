int[5 + 3][20][3] a;

int main() {
	int i;
	int j;
	int k;
	
	i := 0;
	while (i < 8) {
		j := 0;
		while (j < 20) {
			k := 0;
			while (k < 3) {
				a[i][j][k] := 0;
				k := k + 1;
			}
			j := j + 1;
		}
		i := i + 1;
	}
	
	a[0][0][0] := 2;
	a[i][j][k] := a[k][j][i];

	return 0;
}
